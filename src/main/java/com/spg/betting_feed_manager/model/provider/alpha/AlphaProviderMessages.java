package com.spg.betting_feed_manager.model.provider.alpha;

import com.fasterxml.jackson.annotation.*;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaEnums.AlphaMsgType;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaEnums.AlphaOutcome;

import java.util.Map;

/**
 * Common parent for Alpha provider request messages.
 * Implemented by AlphaOddsChangeMessage and AlphaSettlementMessage.
 */
public class AlphaProviderMessages {

    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = AlphaOddsChangeMessage.class),
            @JsonSubTypes.Type(value = AlphaSettlementMessage.class)
    })
    public sealed interface AlphaProviderMessage permits AlphaOddsChangeMessage, AlphaSettlementMessage {
        @JsonProperty("msg_type")
        AlphaMsgType msgType();
        @JsonProperty("event_id")
        String eventId();
    }

    /**
     * Common parent for Alpha provider messages containing shared fields.
     */
    public record AlphaBaseMessage(
            @JsonProperty("msg_type") AlphaMsgType msgType,
            @JsonProperty("event_id") String eventId
    ) {}

    public record AlphaOddsChangeMessage(
            @JsonProperty("msg_type") AlphaMsgType msgType,
            @JsonProperty("event_id") String eventId,
            Map<AlphaOutcome, Double> values
    ) implements AlphaProviderMessage {
        public AlphaOddsChangeMessage(String eventId, Map<AlphaOutcome, Double> values) {
            this(AlphaMsgType.odds_update, eventId, values);
        }
    }

    public record AlphaSettlementMessage(
            @JsonProperty("msg_type") AlphaMsgType msgType,
            @JsonProperty("event_id") String eventId,
            AlphaOutcome outcome
    ) implements AlphaProviderMessage {
        public AlphaSettlementMessage(String eventId, AlphaOutcome outcome) {
            this(AlphaMsgType.settlement, eventId, outcome);
        }
    }
}