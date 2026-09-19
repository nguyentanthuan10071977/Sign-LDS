package com.slds.api.controller;

import com.slds.api.model.Models;
import com.slds.api.service.SldsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final SldsService service;
    public RoomController(SldsService service) { this.service = service; }
    @PostMapping public Models.Room create(@RequestHeader("Authorization") String auth, @RequestBody(required = false) Models.RoomRequest request) { var host = service.current(UserController.token(auth)); return service.createRoom(host.id(), request == null ? new Models.RoomRequest(true, true) : request); }
    @GetMapping("/{code}") public Models.Room get(@PathVariable String code) { return service.room(code); }
    @PostMapping("/{code}/join") public Models.Room join(@PathVariable String code) { return service.room(code); }
    @PostMapping("/{code}/leave") public String leave(@PathVariable String code) { service.room(code); return "Left room " + code; }
}
