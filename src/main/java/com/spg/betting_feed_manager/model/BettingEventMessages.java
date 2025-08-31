package com.spg.betting_feed_manager.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.Instant;
import java.util.Map;

public class BettingEventMessages {

    public enum MessageProvider { ALPHA, BETA }
    public enum EventType { ODDS_CHANGE, BET_SETTLEMENT }

    public enum BetOutcome {
        HOME("home"), DRAW("draw"), AWAY("away");

        private final String json;

        BetOutcome(String json) { this.json = json; }
        @JsonValue public String getJson() { return json; }

        @JsonCreator
        public static BetOutcome from(String v) {
            if (v == null) return null;
            String s = v.trim();
            for (BetOutcome o : values()) {
                if (o.json.equalsIgnoreCase(s)) return o;
            }
            throw new IllegalArgumentException("Invalid standard outcome: " + v);
        }
    }

    // Common base type for all betting event messages
    public sealed abstract static class BettingEventMessage permits BettingEventOddsChangeMessage, BettingEventSettlementMessage {
        public String eventId;
        public MessageProvider messageProvider;
        public Instant receivedAt;

        public BettingEventMessage(String eventId, MessageProvider messageProvider, Instant receivedAt) {
            this.eventId = eventId;
            this.messageProvider = messageProvider;
            this.receivedAt = receivedAt;
        }

        public abstract EventType eventType();
        public String eventId() { return eventId; }
        public MessageProvider messageProvider() { return messageProvider; }
        public Instant receivedAt() { return receivedAt; }

    }

    public static final class BettingEventOddsChangeMessage extends BettingEventMessage {
        private final Map<BetOutcome, Double> outcomeOdds;

        public BettingEventOddsChangeMessage(String eventId, MessageProvider provider, Map<BetOutcome, Double> outcomeOdds) {
            this(eventId, provider, outcomeOdds, Instant.now());
        }

        public BettingEventOddsChangeMessage(String eventId, MessageProvider provider, Map<BetOutcome, Double> outcomeOdds, Instant receivedAt) {
            super(eventId, provider, receivedAt);
            this.outcomeOdds = outcomeOdds;
        }

        public Map<BetOutcome, Double> outcomeOdds() { return outcomeOdds; }
        public EventType eventType() { return EventType.ODDS_CHANGE; }
    }

    public static final class BettingEventSettlementMessage extends BettingEventMessage {
        private final BetOutcome winningOutcome;

        public BettingEventSettlementMessage(String eventId, MessageProvider provider, BetOutcome winningOutcome) {
            this(eventId, provider, winningOutcome, Instant.now());
        }
        public BettingEventSettlementMessage(String eventId, MessageProvider provider, BetOutcome winningOutcome, Instant receivedAt) {
            super(eventId, provider, receivedAt);
            this.winningOutcome = winningOutcome;
        }

        public BetOutcome winningOutcome() { return winningOutcome; }
        @Override public EventType eventType() { return EventType.BET_SETTLEMENT; }
    }
}