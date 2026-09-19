package com.slds.api.controller;

import com.slds.api.model.Models;
import com.slds.api.service.SldsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recognition")
public class RecognitionController {
    private final SldsService service;
    public RecognitionController(SldsService service) { this.service = service; }
    @PostMapping("/text-to-sign") public Models.RecognitionResponse textToSign(@Valid @RequestBody Models.RecognitionRequest request) { return service.recognize(request.text(), "TEXT_TO_SIGN", request.dialect()); }
    @PostMapping("/voice-to-sign") public Models.RecognitionResponse voiceToSign(@Valid @RequestBody Models.RecognitionRequest request) { return service.recognize(request.text(), "VOICE_TO_SIGN", request.dialect()); }
    @PostMapping("/sign-to-text") public Models.RecognitionResponse signToText(@Valid @RequestBody Models.RecognitionRequest request) { return service.recognize(request.text(), "SIGN_TO_TEXT", request.dialect()); }
    @PostMapping("/sign-to-voice") public Models.RecognitionResponse signToVoice(@Valid @RequestBody Models.RecognitionRequest request) { return service.recognize(request.text(), "SIGN_TO_VOICE", request.dialect()); }
    @PostMapping("/train") public Models.TrainingResponse train(@Valid @RequestBody Models.TrainingRequest request) { return new Models.TrainingResponse(java.util.UUID.randomUUID(), request.datasetName(), "QUEUED", java.time.Instant.now()); }
}
