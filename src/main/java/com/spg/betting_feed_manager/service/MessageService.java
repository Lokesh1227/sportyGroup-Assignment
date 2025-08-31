package com.spg.betting_feed_manager.service;

import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;

import java.util.Optional;

/**
 * Abstraction over the message queue so that it can be swapped with a real
 * queue implementation (e.g., Kafka, RabbitMQ) without changing callers.
 */
public interface MessageService {
    void publish(BettingEventMessage oddsChange);
    Optional<Object> poll();
}
