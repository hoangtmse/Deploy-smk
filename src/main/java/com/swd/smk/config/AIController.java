package com.swd.smk.config;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/public/ai")
public class AIController {
    private final QnAService qnAService;

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody Map<String, String> payload){
        String question = payload.get("question");
        String answer = qnAService.getAnswer(question);
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/ask/schema")
    public ResponseEntity<Map<String, Object>> askSchema(@RequestBody Map<String, String> payload){
        String question = payload.get("question");
        Map<String, Object> jsonSchema = qnAService.getAnswerAsJson(question);
        return ResponseEntity.ok(jsonSchema);
    }
}
