package com.spg.betting_feed_manager.adapter;

import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventOddsChangeMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventSettlementMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BetOutcome;
import com.spg.betting_feed_manager.model.BettingEventMessages.EventType;
import com.spg.betting_feed_manager.model.BettingEventMessages.MessageProvider;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaEnums;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaProviderMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaEnums;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaProviderMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaSettlementMessage;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageAdapterFactoryTest {

    private final MessageAdapterFactory factory = new MessageAdapterFactory();

    @Test
    void forAlpha_returnsOddsAdapter_andAdapts() {
        Map<AlphaEnums.AlphaOutcome, Double> values = new EnumMap<>(AlphaEnums.AlphaOutcome.class);
        values.put(AlphaEnums.AlphaOutcome.ONE, 2.2);
        values.put(AlphaEnums.AlphaOutcome.X, 3.3);
        values.put(AlphaEnums.AlphaOutcome.TWO, 4.4);
        var msg = new AlphaOddsChangeMessage("e1", values);

        ProviderMessageAdapter<AlphaProviderMessage, BettingEventMessage> adapter = factory.forAlpha();
        BettingEventMessage out = adapter.adapt(msg);
        assertThat(out).isInstanceOf(BettingEventOddsChangeMessage.class);
        var oc = (BettingEventOddsChangeMessage) out;
        assertThat(oc.eventId()).isEqualTo("e1");
        assertThat(oc.messageProvider()).isEqualTo(MessageProvider.ALPHA);
        assertThat(oc.eventType()).isEqualTo(EventType.ODDS_CHANGE);
        assertThat(oc.outcomeOdds()).containsOnlyKeys(BetOutcome.HOME, BetOutcome.DRAW, BetOutcome.AWAY);
    }

    @Test
    void forBeta_returnsSettlementAdapter_andAdapts() {
        var msg = new BetaSettlementMessage("e2", BetaEnums.BetaOutcome.HOME);

        ProviderMessageAdapter<BetaProviderMessage, BettingEventMessage> adapter = factory.forBeta();
        BettingEventMessage out = adapter.adapt(msg);
        assertThat(out).isInstanceOf(BettingEventSettlementMessage.class);
        var st = (BettingEventSettlementMessage) out;
        assertThat(st.eventId()).isEqualTo("e2");
        assertThat(st.messageProvider()).isEqualTo(MessageProvider.BETA);
        assertThat(st.winningOutcome()).isEqualTo(BetOutcome.HOME);
    }
}
