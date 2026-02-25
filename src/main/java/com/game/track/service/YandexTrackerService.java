package com.game.track.service;

import com.game.track.entity.Task;
import com.game.track.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class YandexTrackerService {

    private static final Logger logger = LoggerFactory.getLogger(YandexTrackerService.class);

    private final RestTemplate restTemplate;

    @Value("${yandex.tracker.oauth.token:#{null}}")
    private String oauthToken;

    @Value("${yandex.tracker.org.id:#{null}}")
    private String orgId;

    @Value("${yandex.tracker.queue.id:#{null}}")
    private String queueId;

    @Value("${yandex.tracker.queue.key:#{null}}")
    private String queueKey;

    public YandexTrackerService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean syncTaskToYandexTracker(Task task) {
        // Validate required configuration
        if (oauthToken == null || orgId == null || queueKey == null) {
            throw new ValidationException(
                "Yandex Tracker configuration is incomplete. Please set yandex.tracker.oauth.token, " +
                "yandex.tracker.org.id, yandex.tracker.queue.id, and yandex.tracker.queue.key in application.properties"
            );
        }
        logger.info("Syncing task to Yandex Tracker: {}", orgId);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "OAuth " + oauthToken);
            headers.set("X-Cloud-Org-ID", orgId);
            headers.set("X-Org-ID", orgId);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("summary", task.getTitle());

            Map<String, Object> queue = new HashMap<>();
            queue.put("id", queueId);
            queue.put("key", queueKey);
            requestBody.put("queue", queue);

            if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                requestBody.put("description", task.getDescription());
            }

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            String url = "https://api.tracker.yandex.net/v3/issues/";
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            logger.info("Yandex Tracker sync response: {} - {}", response.getStatusCode(), response.getBody());
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.error("Failed to sync task to Yandex Tracker: {}", e.getMessage(), e);
            throw new ValidationException("Failed to sync task to Yandex Tracker: " + e.getMessage());
        }
    }
}