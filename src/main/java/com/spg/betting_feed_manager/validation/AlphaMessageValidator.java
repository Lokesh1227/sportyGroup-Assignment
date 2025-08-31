package com.spg.betting_feed_manager.validation;

import com.spg.betting_feed_manager.model.provider.alpha.AlphaEnums;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaEnums.AlphaOutcome;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaProviderMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaSettlementMessage;
import org.springframework.stereotype.Component;

/**
 * Validator for Alpha provider messages. Returns null when valid, or an error message when invalid.
 */
@Component
public class AlphaMessageValidator {

    public String validate(AlphaProviderMessage message) {
        if (message instanceof AlphaOddsChangeMessage a) {
            return validate(a);
        } else if (message instanceof AlphaSettlementMessage s) {
            return validate(s);
        }
        return "Unknown Alpha message payload";
    }

    public String validate(AlphaOddsChangeMessage a) {
        if (a.eventId() == null || a.eventId().isBlank()) return "Missing required field: event_id";
        if (a.values() == null) return "Missing required field: values";
        if (!a.values().keySet().containsAll(java.util.Set.of(
                AlphaOutcome.ONE,
                AlphaOutcome.X,
                AlphaOutcome.TWO)))
            return "values must contain keys '1','X','2'";
        return null;
    }

    public String validate(AlphaSettlementMessage a) {
        if (a.eventId() == null || a.eventId().isBlank()) return "Missing required field: event_id";
        if (a.outcome() == null) return "Missing required field: outcome";
        return null;
    }
}