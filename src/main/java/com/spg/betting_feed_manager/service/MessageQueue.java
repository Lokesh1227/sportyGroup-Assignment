package com.spg.betting_feed_manager.service;

import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventOddsChangeMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventSettlementMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MessageQueue implements MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageQueue.class);

    private final ConcurrentLinkedQueue<BettingEventMessage> queue = new ConcurrentLinkedQueue<>();

    public void publish(BettingEventMessage message) {
        if (message instanceof BettingEventOddsChangeMessage oc) {
            log.info("[QUEUE] BettingEventOddsChange: eventId={}, provider={}, type={}, receivedAt={}, outcomeOdds={}",
                    oc.eventId(), oc.messageProvider(), oc.eventType(), oc.receivedAt(), oc.outcomeOdds());
        } else if (message instanceof BettingEventSettlementMessage st) {
            log.info("[QUEUE] BettingEventSettlement: eventId={}, provider={}, type={}, receivedAt={}, winningOutcome={}",
                    st.eventId(), st.messageProvider(), st.eventType(), st.receivedAt(), st.winningOutcome());
        } else {
            log.info("[QUEUE] BettingEventMessage: eventId={}, provider={}, type={}, receivedAt={}",
                    message.eventId(), message.messageProvider(), message.eventType(), message.receivedAt());
        }
        queue.add(message);
    }
    
    public Optional<Object> poll() {
        return Optional.ofNullable(queue.poll());
    }
}
