package com.spg.betting_feed_manager.validation;

import com.spg.betting_feed_manager.model.provider.beta.BetaEnums;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaProviderMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaSettlementMessage;
import org.springframework.stereotype.Component;

/**
 * Validator for Beta provider messages. Returns null when valid, or an error message when invalid.
 */
@Component
public class BetaMessageValidator {

    public String validate(BetaProviderMessage message) {
        if (message instanceof BetaOddsChangeMessage b) {
            return validate(b);
        } else if (message instanceof BetaSettlementMessage s) {
            return validate(s);
        }
        return "Unknown Beta message payload";
    }

    public String validate(BetaOddsChangeMessage b) {
        if (b.eventId() == null || b.eventId().isBlank()) return "Missing required field: event_id";
        if (b.odds() == null) return "Missing required field: odds";
        if (!b.odds().keySet().containsAll(java.util.Set.of(
                BetaEnums.BetaOutcome.HOME,
                BetaEnums.BetaOutcome.DRAW,
                BetaEnums.BetaOutcome.AWAY)))
            return "odds must contain keys 'home','draw','away'";
        return null;
    }

    public String validate(BetaSettlementMessage b) {
        if (b.eventId() == null || b.eventId().isBlank()) return "Missing required field: event_id";
        if (b.result() == null) return "Missing required field: result";
        return null;
    }
}