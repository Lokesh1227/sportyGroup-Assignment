package com.spg.betting_feed_manager.adapter.beta;

import com.spg.betting_feed_manager.builder.BettingEventBuilders;
import com.spg.betting_feed_manager.adapter.ProviderMessageAdapter;
import com.spg.betting_feed_manager.model.BettingEventMessages.*;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaProviderMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaSettlementMessage;

import java.util.EnumMap;


public class BetaProviderMessageAdapter implements ProviderMessageAdapter<BetaProviderMessage, BettingEventMessage> {

    public BetaProviderMessageAdapter() {}

    @Override
    public BettingEventMessage adapt(BetaProviderMessage source) {
        switch (source.type()) {
            case ODDS -> {
                return adapt((BetaOddsChangeMessage) source);
            }
            case SETTLEMENT -> {
                return adapt((BetaSettlementMessage) source);
            }
            default -> {
                throw new IllegalArgumentException("Unknown message type: " + source.type());
            }
        }
    }

    public BettingEventOddsChangeMessage adapt(BetaOddsChangeMessage source) {
        EnumMap<BetOutcome, Double> bettingOdds = new java.util.EnumMap<>(BetOutcome.class);
        source.odds().forEach((k, v) -> {
            BetOutcome key = switch (k) {
                case HOME -> BetOutcome.HOME;
                case DRAW -> BetOutcome.DRAW;
                case AWAY -> BetOutcome.AWAY;
            };
            bettingOdds.put(key, v);
        });
        return new BettingEventBuilders.OddsChangeBuilder()
                .eventId(source.eventId())
                .outcomeOdds(bettingOdds)
                .provider(MessageProvider.BETA)
                .build();
    }

    public BettingEventSettlementMessage adapt(BetaSettlementMessage source) {
        BetOutcome winning = switch (source.result()) {
            case HOME -> BetOutcome.HOME;
            case DRAW -> BetOutcome.DRAW;
            case AWAY -> BetOutcome.AWAY;
        };
        return new BettingEventBuilders.SettlementBuilder()
                .eventId(source.eventId())
                .winningOutcome(winning)
                .provider(MessageProvider.BETA)
                .build();
    }
}
