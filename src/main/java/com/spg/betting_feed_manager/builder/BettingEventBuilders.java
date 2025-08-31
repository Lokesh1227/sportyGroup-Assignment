package com.spg.betting_feed_manager.builder;

import com.spg.betting_feed_manager.model.BettingEventMessages.*;

import java.time.Instant;
import java.util.Map;

/**
 * Builders for BettingEvent messages, enabling step-wise construction
 * and making controller code cleaner. Keeps old convenience constructors intact.
 */
public final class BettingEventBuilders {
    private BettingEventBuilders() {}

    public static class OddsChangeBuilder {
        private String eventId;
        private Map<BetOutcome, Double> outcomeOdds;
        private MessageProvider messageProvider;
        private Instant receivedAt = Instant.now();

        public OddsChangeBuilder eventId(String id) { this.eventId = id; return this; }
        public OddsChangeBuilder outcomeOdds(Map<BetOutcome, Double> map) { this.outcomeOdds = map; return this; }
        public OddsChangeBuilder provider(MessageProvider p) { this.messageProvider = p; return this; }
        public OddsChangeBuilder receivedAt(Instant ts) { this.receivedAt = ts; return this; }

        public BettingEventOddsChangeMessage build() {
            return new BettingEventOddsChangeMessage(eventId, messageProvider, outcomeOdds, receivedAt);
        }
    }

    public static class SettlementBuilder {
        private String eventId;
        private BetOutcome winningOutcome;
        private MessageProvider messageProvider;
        private Instant receivedAt = Instant.now();

        public SettlementBuilder eventId(String id) { this.eventId = id; return this; }
        public SettlementBuilder winningOutcome(BetOutcome o) { this.winningOutcome = o; return this; }
        public SettlementBuilder provider(MessageProvider p) { this.messageProvider = p; return this; }
        public SettlementBuilder receivedAt(Instant ts) { this.receivedAt = ts; return this; }

        public BettingEventSettlementMessage build() {
            return new BettingEventSettlementMessage(eventId, messageProvider, winningOutcome, receivedAt);
        }
    }
}
