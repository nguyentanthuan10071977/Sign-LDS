package com.slds.api.service;

import com.slds.api.model.Models;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SldsService {
    private final Map<UUID, Models.User> users = new ConcurrentHashMap<>();
    private final Map<String, String> passwords = new ConcurrentHashMap<>();
    private final Map<String, UUID> tokens = new ConcurrentHashMap<>();
    private final Map<UUID, Models.Conversation> conversations = new ConcurrentHashMap<>();
    private final Map<UUID, List<Models.Message>> messages = new ConcurrentHashMap<>();
    private final Map<UUID, Models.Room> rooms = new ConcurrentHashMap<>();

    public SldsService() {
        register(new Models.SignUpRequest("Demo User", "demo@slds.local", "password"));
    }
    public Models.AuthResponse register(Models.SignUpRequest request) {
        if (users.values().stream().anyMatch(u -> u.email().equalsIgnoreCase(request.email()))) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        var user = new Models.User(UUID.randomUUID(), request.name(), request.email(), "USER", null, Instant.now());
        users.put(user.id(), user); passwords.put(user.email(), request.password());
        return login(new Models.SignInRequest(request.email(), request.password()));
    }
    public Models.AuthResponse login(Models.SignInRequest request) {
        var user = users.values().stream().filter(u -> u.email().equalsIgnoreCase(request.email())).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!Objects.equals(passwords.get(user.email()), request.password())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        var token = UUID.randomUUID().toString(); tokens.put(token, user.id()); return new Models.AuthResponse(token, user);
    }
    public Models.User current(String token) { var id = tokens.get(token); if (id == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bearer token required"); return user(id); }
    public Models.User user(UUID id) { var user = users.get(id); if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"); return user; }
    public List<Models.User> users(String query) { return users.values().stream().filter(u -> query == null || (u.name()+u.email()).toLowerCase().contains(query.toLowerCase())).toList(); }
    public Models.User update(UUID id, Models.UpdateUserRequest request) { var old = user(id); var updated = new Models.User(old.id(), request.name(), request.email(), old.role(), request.profilePicture(), old.createdAt()); users.put(id, updated); return updated; }
    public void delete(UUID id) { var existing = user(id); users.remove(id); passwords.remove(existing.email()); }
    public Models.Conversation createConversation(UUID owner, List<UUID> participantIds) { var ids = new ArrayList<>(participantIds == null ? List.of() : participantIds); if (!ids.contains(owner)) ids.add(owner); ids.forEach(this::user); var c = new Models.Conversation(UUID.randomUUID(), ids, null, Instant.now()); conversations.put(c.id(), c); messages.put(c.id(), new ArrayList<>()); return c; }
    public List<Models.Conversation> conversations(UUID userId) { return conversations.values().stream().filter(c -> c.participantIds().contains(userId)).sorted(Comparator.comparing(Models.Conversation::updatedAt).reversed()).toList(); }
    public List<Models.Message> messages(UUID conversationId, UUID userId) { var c = conversation(conversationId); if (!c.participantIds().contains(userId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a participant"); return List.copyOf(messages.get(conversationId)); }
    public Models.Message message(UUID conversationId, UUID senderId, String content) { var c = conversation(conversationId); if (!c.participantIds().contains(senderId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a participant"); var m = new Models.Message(UUID.randomUUID(), conversationId, senderId, user(senderId).name(), content, Instant.now()); messages.get(conversationId).add(m); conversations.put(conversationId, new Models.Conversation(c.id(), c.participantIds(), content, m.sentAt())); return m; }
    public void deleteMessage(UUID conversationId, UUID messageId, UUID userId) { var list = messages(conversationId, userId); var m = list.stream().filter(x -> x.id().equals(messageId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found")); if (!m.senderId().equals(userId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only sender can delete message"); messages.get(conversationId).removeIf(x -> x.id().equals(messageId)); }
    private Models.Conversation conversation(UUID id) { var c = conversations.get(id); if (c == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"); return c; }
    public Models.RecognitionResponse recognize(String input, String mode, String dialect) { return new Models.RecognitionResponse(input, input == null ? "" : input.trim(), mode, dialect == null ? "ASL" : dialect, Instant.now()); }
    public Models.Room createRoom(UUID host, Models.RoomRequest r) { var room = new Models.Room(UUID.randomUUID(), UUID.randomUUID().toString().substring(0, 8), host, r.cameraEnabled(), r.microphoneEnabled(), Instant.now()); rooms.put(room.id(), room); return room; }
    public Models.Room room(String code) { return rooms.values().stream().filter(r -> r.roomCode().equals(code)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found")); }
}
