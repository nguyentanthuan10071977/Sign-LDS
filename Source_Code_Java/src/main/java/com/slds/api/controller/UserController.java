package com.slds.api.controller;

import com.slds.api.model.Models;
import com.slds.api.service.SldsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final SldsService service;
    public UserController(SldsService service) { this.service = service; }
    @GetMapping public List<Models.User> list(@RequestParam(required = false) String search) { return service.users(search); }
    @GetMapping("/me") public Models.User me(@RequestHeader("Authorization") String authorization) { return service.current(token(authorization)); }
    @GetMapping("/{id}") public Models.User profile(@PathVariable UUID id) { return service.user(id); }
    @PutMapping("/{id}") public Models.User update(@PathVariable UUID id, @Valid @RequestBody Models.UpdateUserRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") public void delete(@PathVariable UUID id) { service.delete(id); }
    @PostMapping("/invite") public String invite(@Valid @RequestBody Models.InviteRequest request) { return "Invitation queued for " + request.email(); }
    static String token(String authorization) { return authorization.replaceFirst("(?i)^Bearer\\s+", "").trim(); }
}
