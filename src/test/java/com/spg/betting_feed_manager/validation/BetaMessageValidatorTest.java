package com.spg.betting_feed_manager.validation;

import com.spg.betting_feed_manager.model.provider.beta.BetaEnums;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaSettlementMessage;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BetaMessageValidatorTest {

    private final BetaMessageValidator validator = new BetaMessageValidator();

    @Test
    void validateOdds_missingEventId_returnsError() {
        Map<BetaEnums.BetaOutcome, Double> odds = new EnumMap<>(BetaEnums.BetaOutcome.class);
        odds.put(BetaEnums.BetaOutcome.HOME, 1.95);
        odds.put(BetaEnums.BetaOutcome.DRAW, 3.2);
        odds.put(BetaEnums.BetaOutcome.AWAY, 4.0);
        var msg = new BetaOddsChangeMessage(null, odds);
        assertThat(validator.validate(msg)).isEqualTo("Missing required field: event_id");
    }

    @Test
    void validateSettlement_nullResult_returnsError() {
        var msg = new BetaSettlementMessage("ev", null);
        assertThat(validator.validate(msg)).isEqualTo("Missing required field: result");
    }

    @Test
    void validateOdds_valid_returnsNull() {
        Map<BetaEnums.BetaOutcome, Double> odds = new EnumMap<>(BetaEnums.BetaOutcome.class);
        odds.put(BetaEnums.BetaOutcome.HOME, 1.95);
        odds.put(BetaEnums.BetaOutcome.DRAW, 3.2);
        odds.put(BetaEnums.BetaOutcome.AWAY, 4.0);
        var msg = new BetaOddsChangeMessage("ev456", odds);
        assertThat(validator.validate(msg)).isNull();
    }
}
