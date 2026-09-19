package com.slds.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class Models {
    private Models() {}
    public record User(UUID id, String name, String email, String role, String profilePicture, Instant createdAt) {}
    public record SignUpRequest(@NotBlank String name, @Email @NotBlank String email, @Size(min = 6) String password) {}
    public record SignInRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record AuthResponse(String token, User user) {}
    public record UpdateUserRequest(@NotBlank String name, @Email @NotBlank String email, String profilePicture) {}
    public record Conversation(UUID id, List<UUID> participantIds, String lastMessage, Instant updatedAt) {}
    public record Message(UUID id, UUID conversationId, UUID senderId, String senderName, String content, Instant sentAt) {}
    public record MessageRequest(@NotBlank String content) {}
    public record InviteRequest(@Email @NotBlank String email, String message) {}
    public record RecognitionRequest(String text, String dialect) {}
    public record RecognitionResponse(String input, String output, String mode, String dialect, Instant processedAt) {}
    public record Room(UUID id, String roomCode, UUID hostId, boolean cameraEnabled, boolean microphoneEnabled, Instant createdAt) {}
    public record RoomRequest(boolean cameraEnabled, boolean microphoneEnabled) {}
    public record TrainingRequest(@NotBlank String datasetName, @NotBlank String labels, Integer epochs) {}
    public record TrainingResponse(UUID jobId, String datasetName, String status, Instant submittedAt) {}
}
