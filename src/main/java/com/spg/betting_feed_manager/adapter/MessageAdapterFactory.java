package com.spg.betting_feed_manager.adapter;

import com.spg.betting_feed_manager.adapter.alpha.AlphaProviderMessageAdapter;
import com.spg.betting_feed_manager.adapter.beta.BetaProviderMessageAdapter;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaProviderMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaProviderMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageAdapterFactory {

    private static final AlphaProviderMessageAdapter alphaProviderMessageAdapter = new AlphaProviderMessageAdapter();
    private static final BetaProviderMessageAdapter betaProviderMessageAdapter = new BetaProviderMessageAdapter();

    public ProviderMessageAdapter<AlphaProviderMessage, BettingEventMessage> forAlpha() {
        return alphaProviderMessageAdapter;
    }

    public ProviderMessageAdapter<BetaProviderMessage, BettingEventMessage> forBeta() {
        return betaProviderMessageAdapter;
    }
}
