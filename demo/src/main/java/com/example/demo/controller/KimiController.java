package com.example.demo.controller;

import com.example.demo.config.KimiConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kimi")
public class KimiController {

    @Autowired
    private KimiConfig kimiConfig;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, Object> request) {
        try {
            String userMessage = (String) request.get("message");
            @SuppressWarnings("unchecked")
            List<Map<String, String>> history = (List<Map<String, String>>) request.getOrDefault("history", List.of());

            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", kimiConfig.getModel());

            ArrayNode messages = requestBody.putArray("messages");

            ObjectNode systemMsg = messages.addObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是校园猫咪小助手，专门帮助同学们了解校园里的猫咪信息、志愿任务等。请用友好、亲切的语气回答。");

            for (Map<String, String> msg : history) {
                ObjectNode historyMsg = messages.addObject();
                historyMsg.put("role", msg.get("role"));
                historyMsg.put("content", msg.get("content"));
            }

            ObjectNode userMsg = messages.addObject();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(kimiConfig.getApiKey());

            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    kimiConfig.getBaseUrl() + "/chat/completions",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String reply = responseBody.path("choices").get(0).path("message").path("content").asText();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "reply", reply
            ));

        } catch (HttpClientErrorException e) {
            // ✅ 统一捕获所有 HTTP 4xx 错误，根据状态码判断
            e.printStackTrace();

            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(Map.of(
                                "success", false,
                                "message", "AI 服务暂时不可用：账户余额不足或请求过于频繁，请联系管理员充值。",
                                "errorCode", "INSUFFICIENT_BALANCE"
                        ));
            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "success", false,
                                "message", "AI 服务配置错误，请联系管理员检查 API Key。",
                                "errorCode", "INVALID_API_KEY"
                        ));
            } else {
                return ResponseEntity.status(e.getStatusCode())
                        .body(Map.of(
                                "success", false,
                                "message", "AI 服务错误: " + e.getStatusText(),
                                "errorCode", "API_ERROR"
                        ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "AI 服务暂时不可用，请稍后重试。",
                            "errorCode", "UNKNOWN_ERROR"
                    ));
        }
    }
}