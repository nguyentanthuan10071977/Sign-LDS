package com.slds.api.controller;

import com.slds.api.model.Models;
import com.slds.api.service.SldsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final SldsService service;
    public AuthController(SldsService service) { this.service = service; }
    @PostMapping("/signup") public Models.AuthResponse signup(@Valid @RequestBody Models.SignUpRequest request) { return service.register(request); }
    @PostMapping("/signin") public Models.AuthResponse signin(@Valid @RequestBody Models.SignInRequest request) { return service.login(request); }
}
