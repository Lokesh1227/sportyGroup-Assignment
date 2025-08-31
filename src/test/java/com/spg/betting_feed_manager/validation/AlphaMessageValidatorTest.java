package com.spg.betting_feed_manager.validation;

import com.spg.betting_feed_manager.model.provider.alpha.AlphaEnums;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaSettlementMessage;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AlphaMessageValidatorTest {

    private final AlphaMessageValidator validator = new AlphaMessageValidator();

    @Test
    void validateOdds_missingEventId_returnsError() {
        Map<AlphaEnums.AlphaOutcome, Double> values = new EnumMap<>(AlphaEnums.AlphaOutcome.class);
        values.put(AlphaEnums.AlphaOutcome.ONE, 2.0);
        values.put(AlphaEnums.AlphaOutcome.X, 3.0);
        values.put(AlphaEnums.AlphaOutcome.TWO, 4.0);
        var msg = new AlphaOddsChangeMessage(null, values);
        assertThat(validator.validate(msg)).isEqualTo("Missing required field: event_id");
    }

    @Test
    void validateSettlement_nullOutcome_returnsError() {
        var msg = new AlphaSettlementMessage("ev", null);
        assertThat(validator.validate(msg)).isEqualTo("Missing required field: outcome");
    }

    @Test
    void validateOdds_valid_returnsNull() {
        Map<AlphaEnums.AlphaOutcome, Double> values = new EnumMap<>(AlphaEnums.AlphaOutcome.class);
        values.put(AlphaEnums.AlphaOutcome.ONE, 2.0);
        values.put(AlphaEnums.AlphaOutcome.X, 3.0);
        values.put(AlphaEnums.AlphaOutcome.TWO, 4.0);
        var msg = new AlphaOddsChangeMessage("ev1", values);
        assertThat(validator.validate(msg)).isNull();
    }
}
