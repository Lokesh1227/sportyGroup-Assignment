package com.spg.betting_feed_manager.model.provider.beta;

import com.fasterxml.jackson.annotation.*;
import com.spg.betting_feed_manager.model.provider.beta.BetaEnums.BetaMsgType;
import com.spg.betting_feed_manager.model.provider.beta.BetaEnums.BetaOutcome;

import java.util.Map;

public class BetaProviderMessages {

    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = BetaOddsChangeMessage.class),
            @JsonSubTypes.Type(value = BetaSettlementMessage.class)
    })

    public sealed interface BetaProviderMessage permits BetaOddsChangeMessage, BetaSettlementMessage {
        @JsonProperty("type")
        BetaMsgType type();

        @JsonProperty("event_id")
        String eventId();
    }

    public record BetaOddsChangeMessage(
            @JsonProperty("type") BetaMsgType type,
            @JsonProperty("event_id") String eventId,
            Map<BetaOutcome, Double> odds
    ) implements BetaProviderMessage {
        public BetaOddsChangeMessage(String eventId, Map<BetaOutcome, Double> odds) {
            this(BetaMsgType.ODDS, eventId, odds);
        }
    }

    public record BetaSettlementMessage(
            @JsonProperty("type") BetaMsgType type,
            @JsonProperty("event_id") String eventId,
            BetaOutcome result
    ) implements BetaProviderMessage {
        public BetaSettlementMessage(String eventId, BetaOutcome result) {
            this(BetaMsgType.SETTLEMENT, eventId, result);
        }
    }
}
