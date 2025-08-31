package com.spg.betting_feed_manager.controller.provider.alpha;

import com.spg.betting_feed_manager.adapter.MessageAdapterFactory;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaProviderMessage;
import com.spg.betting_feed_manager.service.MessageService;
import com.spg.betting_feed_manager.validation.AlphaMessageValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class ProviderAlphaController {

    private final MessageService queue;
    private final MessageAdapterFactory adapterFactory;
    private final AlphaMessageValidator alphaValidator;

    public ProviderAlphaController(MessageService queue, MessageAdapterFactory adapterFactory, AlphaMessageValidator alphaValidator) {
        this.queue = queue;
        this.adapterFactory = adapterFactory;
        this.alphaValidator = alphaValidator;
    }

    @PostMapping(path = "/v1/provider-alpha/feed", consumes = "application/json")
    public ResponseEntity<?> providerAlpha(@RequestBody AlphaProviderMessage message) {
            var validationError = alphaValidator.validate(message);
            if (validationError != null) return bad(validationError);
            BettingEventMessage std = adapterFactory.forAlpha().adapt(message);
            queue.publish(std);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(String.format("Message accepted : {%s}", std.eventId()));
    }

    private ResponseEntity<Map<String, Object>> bad(String message) {
        return ResponseEntity.badRequest().body(error(message));
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> m = new HashMap<>();
        m.put("error", message);
        return m;
    }
}
