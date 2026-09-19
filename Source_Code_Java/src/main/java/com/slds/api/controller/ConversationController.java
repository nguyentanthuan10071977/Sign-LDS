package com.slds.api.controller;

import com.slds.api.model.Models;
import com.slds.api.service.SldsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final SldsService service;
    public ConversationController(SldsService service) { this.service = service; }
    @GetMapping public List<Models.Conversation> list(@RequestHeader("Authorization") String auth) { return service.conversations(service.current(UserController.token(auth)).id()); }
    @PostMapping public Models.Conversation create(@RequestHeader("Authorization") String auth, @RequestBody(required = false) List<UUID> participantIds) { return service.createConversation(service.current(UserController.token(auth)).id(), participantIds); }
    @GetMapping("/{id}/messages") public List<Models.Message> messages(@RequestHeader("Authorization") String auth, @PathVariable UUID id) { return service.messages(id, service.current(UserController.token(auth)).id()); }
    @PostMapping("/{id}/messages") public Models.Message send(@RequestHeader("Authorization") String auth, @PathVariable UUID id, @Valid @RequestBody Models.MessageRequest request) { return service.message(id, service.current(UserController.token(auth)).id(), request.content()); }
    @DeleteMapping("/{conversationId}/messages/{messageId}") public void delete(@RequestHeader("Authorization") String auth, @PathVariable UUID conversationId, @PathVariable UUID messageId) { service.deleteMessage(conversationId, messageId, service.current(UserController.token(auth)).id()); }
}
