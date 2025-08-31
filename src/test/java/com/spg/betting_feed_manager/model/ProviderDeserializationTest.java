package com.spg.betting_feed_manager.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaProviderMessage;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaOddsChangeMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaProviderMessage;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaSettlementMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProviderDeserializationTest {

    @Autowired ObjectMapper mapper;

    @Test
    void alphaOdds_msgType_notNull_afterDeserialization() throws Exception {
        String json = "{" +
                "\"msg_type\": \"odds_update\"," +
                "\"event_id\": \"ev123\"," +
                "\"values\": {\"1\": 2.0, \"X\": 3.1, \"2\": 3.8}" +
                "}";
        AlphaProviderMessage msg = mapper.readValue(json, AlphaProviderMessage.class);
        assertThat(msg).isInstanceOf(AlphaOddsChangeMessage.class);
        assertThat(msg.msgType()).isNotNull();
    }

    @Test
    void betaSettlement_type_notNull_afterDeserialization() throws Exception {
        String json = "{" +
                "\"type\": \"SETTLEMENT\"," +
                "\"event_id\": \"ev456\"," +
                "\"result\": \"away\"" +
                "}";
        BetaProviderMessage msg = mapper.readValue(json, BetaProviderMessage.class);
        assertThat(msg).isInstanceOf(BetaSettlementMessage.class);
        assertThat(msg.type()).isNotNull();
    }
}
