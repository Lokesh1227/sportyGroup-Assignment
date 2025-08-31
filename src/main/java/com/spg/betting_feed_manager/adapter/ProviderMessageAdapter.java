package com.spg.betting_feed_manager.adapter;

/**
 * Adapter that converts a provider-specific inbound message (S)
 * into our internal BettingEventMessage type (T).
 */
public interface ProviderMessageAdapter<S, T> {
    T adapt(S source);
}
