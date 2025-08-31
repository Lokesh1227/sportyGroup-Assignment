package com.spg.betting_feed_manager.controller.provider.beta;

import com.spg.betting_feed_manager.adapter.MessageAdapterFactory;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.*;
import com.spg.betting_feed_manager.service.MessageService;
import com.spg.betting_feed_manager.validation.BetaMessageValidator;
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
public class ProviderBetaController {

    private final MessageService queue;
    private final MessageAdapterFactory adapterFactory;
    private final BetaMessageValidator betaValidator;

    public ProviderBetaController(MessageService queue, MessageAdapterFactory adapterFactory, BetaMessageValidator betaValidator) {
        this.queue = queue;
        this.adapterFactory = adapterFactory;
        this.betaValidator = betaValidator;
    }

    @PostMapping(path = "/v1/provider-beta/feed", consumes = "application/json")
    public ResponseEntity<?> providerBeta(@RequestBody BetaProviderMessage message) {
        var validationError = betaValidator.validate(message);
        if (validationError != null) return bad(validationError);
        BettingEventMessage std = adapterFactory.forBeta().adapt(message);
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
