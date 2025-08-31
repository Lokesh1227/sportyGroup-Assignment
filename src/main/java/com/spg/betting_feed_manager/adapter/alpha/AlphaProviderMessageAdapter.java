package com.spg.betting_feed_manager.adapter.alpha;

import com.spg.betting_feed_manager.builder.BettingEventBuilders;
import com.spg.betting_feed_manager.adapter.ProviderMessageAdapter;
import com.spg.betting_feed_manager.model.BettingEventMessages.*;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaProviderMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaSettlementMessage;

import java.util.EnumMap;


public class AlphaProviderMessageAdapter implements ProviderMessageAdapter<AlphaProviderMessage, BettingEventMessage> {

    public AlphaProviderMessageAdapter() {}

    @Override
    public BettingEventMessage adapt(AlphaProviderMessage source) {
        switch (source.msgType()) {
            case odds_update -> {
                return adapt((AlphaOddsChangeMessage) source);
            }
            case settlement -> {
                return adapt((AlphaSettlementMessage) source);
            }
            default -> {
                throw new IllegalArgumentException("Unknown message type: " + source.msgType());
            }
        }
    }

    public BettingEventOddsChangeMessage adapt(AlphaOddsChangeMessage source) {
        EnumMap<BetOutcome, Double> bettingOdds = new java.util.EnumMap<>(BetOutcome.class);
        source.values().forEach((k, v) -> {
            BetOutcome key = switch (k) {
                case ONE -> BetOutcome.HOME;
                case X -> BetOutcome.DRAW;
                case TWO -> BetOutcome.AWAY;
            };
            bettingOdds.put(key, v);
        });
        return new BettingEventBuilders.OddsChangeBuilder()
                .eventId(source.eventId())
                .outcomeOdds(bettingOdds)
                .provider(MessageProvider.ALPHA)
                .build();
    }

    public BettingEventSettlementMessage adapt(AlphaSettlementMessage source) {
        BetOutcome outcome = switch (source.outcome()) {
            case ONE -> BetOutcome.HOME;
            case X -> BetOutcome.DRAW;
            case TWO -> BetOutcome.AWAY;
        };
        return new BettingEventBuilders.SettlementBuilder()
                .eventId(source.eventId())
                .winningOutcome(outcome)
                .provider(MessageProvider.ALPHA)
                .build();
    }
}
