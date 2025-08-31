package com.spg.betting_feed_manager.model.provider.alpha;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public final class AlphaEnums {
    private AlphaEnums() {}

    public enum AlphaMsgType {
        odds_update, settlement;
    }

    public enum AlphaOutcome {
        ONE("1"), X("X"), TWO("2");

        private final String json;
        AlphaOutcome(String json) { this.json = json; }
        @JsonValue
        public String getJson() { return json; }

        @JsonCreator
        public static AlphaOutcome from(String v) {
            if (v == null) return null;
            String s = v.trim();
            for (AlphaOutcome o : values()) {
                if (o.json.equals(s)) return o;
            }
            throw new IllegalArgumentException("Invalid Alpha outcome: " + v);
        }
    }
}
