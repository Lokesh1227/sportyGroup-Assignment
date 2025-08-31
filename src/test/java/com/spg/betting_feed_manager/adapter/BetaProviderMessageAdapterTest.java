package com.spg.betting_feed_manager.adapter;

import com.spg.betting_feed_manager.adapter.beta.BetaProviderMessageAdapter;
import com.spg.betting_feed_manager.model.BettingEventMessages.*;
import com.spg.betting_feed_manager.model.provider.beta.BetaEnums;
import com.spg.betting_feed_manager.model.provider.beta.BetaEnums.BetaOutcome;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaSettlementMessage;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BetaProviderMessageAdapterTest {

    private final BetaProviderMessageAdapter adapter = new BetaProviderMessageAdapter();

    @Test
    void adaptOddsChange_mapsOutcomesAndMetadata() {
        Map<BetaOutcome, Double> odds = new EnumMap<>(BetaOutcome.class);
        odds.put(BetaOutcome.HOME, 1.95);
        odds.put(BetaOutcome.DRAW, 3.2);
        odds.put(BetaOutcome.AWAY, 4.0);
        var msg = new BetaOddsChangeMessage("ev456", odds);

        BettingEventMessage out = adapter.adapt(msg);
        assertThat(out).isInstanceOf(BettingEventOddsChangeMessage.class);
        var oc = (BettingEventOddsChangeMessage) out;

        assertThat(oc.eventId()).isEqualTo("ev456");
        assertThat(oc.messageProvider()).isEqualTo(MessageProvider.BETA);
        assertThat(oc.eventType()).isEqualTo(EventType.ODDS_CHANGE);
        assertThat(oc.receivedAt()).isNotNull().isBeforeOrEqualTo(Instant.now());

        assertThat(oc.outcomeOdds()).containsOnlyKeys(BetOutcome.HOME, BetOutcome.DRAW, BetOutcome.AWAY);
        assertThat(oc.outcomeOdds().get(BetOutcome.HOME)).isEqualTo(1.95);
        assertThat(oc.outcomeOdds().get(BetOutcome.DRAW)).isEqualTo(3.2);
        assertThat(oc.outcomeOdds().get(BetOutcome.AWAY)).isEqualTo(4.0);
    }

    @Test
    void adaptSettlement_mapsOutcomeAndMetadata() {
        var msg = new BetaSettlementMessage("ev789", BetaOutcome.AWAY);
        BettingEventMessage out = adapter.adapt(msg);
        assertThat(out).isInstanceOf(BettingEventSettlementMessage.class);
        var st = (BettingEventSettlementMessage) out;

        assertThat(st.eventId()).isEqualTo("ev789");
        assertThat(st.messageProvider()).isEqualTo(MessageProvider.BETA);
        assertThat(st.eventType()).isEqualTo(EventType.BET_SETTLEMENT);
        assertThat(st.receivedAt()).isNotNull();
        assertThat(st.winningOutcome()).isEqualTo(BetOutcome.AWAY);
    }
}
