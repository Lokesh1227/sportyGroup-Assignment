package com.spg.betting_feed_manager.adapter;

import com.spg.betting_feed_manager.adapter.alpha.AlphaProviderMessageAdapter;
import com.spg.betting_feed_manager.model.BettingEventMessages.BetOutcome;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventOddsChangeMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventSettlementMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.EventType;
import com.spg.betting_feed_manager.model.BettingEventMessages.MessageProvider;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaEnums;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaSettlementMessage;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AlphaProviderMessageAdapterTest {

    private final AlphaProviderMessageAdapter adapter = new AlphaProviderMessageAdapter();

    @Test
    void adaptOddsChange_mapsOutcomesAndMetadata() {
        Map<AlphaEnums.AlphaOutcome, Double> values = new EnumMap<>(AlphaEnums.AlphaOutcome.class);
        values.put(AlphaEnums.AlphaOutcome.ONE, 2.0);
        values.put(AlphaEnums.AlphaOutcome.X, 3.1);
        values.put(AlphaEnums.AlphaOutcome.TWO, 3.8);
        var msg = new AlphaOddsChangeMessage("ev123", values);

        BettingEventMessage out = adapter.adapt(msg);
        assertThat(out).isInstanceOf(BettingEventOddsChangeMessage.class);
        var oc = (BettingEventOddsChangeMessage) out;

        assertThat(oc.eventId()).isEqualTo("ev123");
        assertThat(oc.messageProvider()).isEqualTo(MessageProvider.ALPHA);
        assertThat(oc.eventType()).isEqualTo(EventType.ODDS_CHANGE);
        assertThat(oc.receivedAt()).isNotNull().isBeforeOrEqualTo(Instant.now());

        assertThat(oc.outcomeOdds()).containsOnlyKeys(BetOutcome.HOME, BetOutcome.DRAW, BetOutcome.AWAY);
        assertThat(oc.outcomeOdds().get(BetOutcome.HOME)).isEqualTo(2.0);
        assertThat(oc.outcomeOdds().get(BetOutcome.DRAW)).isEqualTo(3.1);
        assertThat(oc.outcomeOdds().get(BetOutcome.AWAY)).isEqualTo(3.8);
    }

    @Test
    void adaptSettlement_mapsOutcomeAndMetadata() {
        var msg = new AlphaSettlementMessage( "ev999", AlphaEnums.AlphaOutcome.X);
        BettingEventMessage out = adapter.adapt(msg);
        assertThat(out).isInstanceOf(BettingEventSettlementMessage.class);
        var st = (BettingEventSettlementMessage) out;

        assertThat(st.eventId()).isEqualTo("ev999");
        assertThat(st.messageProvider()).isEqualTo(MessageProvider.ALPHA);
        assertThat(st.eventType()).isEqualTo(EventType.BET_SETTLEMENT);
        assertThat(st.receivedAt()).isNotNull();
        assertThat(st.winningOutcome()).isEqualTo(BetOutcome.DRAW);
    }
}
