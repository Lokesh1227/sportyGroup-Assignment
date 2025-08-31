package com.spg.betting_feed_manager.model.provider.beta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public final class BetaEnums {
    private BetaEnums() {}

    public enum BetaMsgType {
        ODDS, SETTLEMENT;
    }

    public enum BetaOutcome {
        HOME("home"), DRAW("draw"), AWAY("away");

        private final String json;
        BetaOutcome(String json) { this.json = json; }

        @JsonValue
        public String getJson() { return json; }

        @JsonCreator
        public static BetaOutcome from(String v) {
            if (v == null) return null;
            String s = v.trim().toLowerCase();
            for (BetaOutcome r : values()) {
                if (r.json.equals(s)) return r;
            }
            throw new IllegalArgumentException("Invalid Beta result: " + v);
        }
    }
}
