package com.spg.betting_feed_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spg.betting_feed_manager.adapter.MessageAdapterFactory;
import com.spg.betting_feed_manager.adapter.ProviderMessageAdapter;
import com.spg.betting_feed_manager.controller.provider.alpha.ProviderAlphaController;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventSettlementMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BetOutcome;
import com.spg.betting_feed_manager.model.BettingEventMessages.MessageProvider;
import com.spg.betting_feed_manager.model.provider.alpha.AlphaProviderMessages.AlphaProviderMessage;
import com.spg.betting_feed_manager.service.MessageService;
import com.spg.betting_feed_manager.validation.AlphaMessageValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProviderAlphaController.class)
class ProviderAlphaControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    MessageService queue;
    @MockitoBean MessageAdapterFactory adapterFactory;
    @MockitoBean AlphaMessageValidator alphaValidator;

    @Test
    void alphaOddsUpdate_valid_returnsAccepted_andPublishes() throws Exception {
        String json = "{" +
                "\"msg_type\": \"odds_update\"," +
                "\"event_id\": \"ev123\"," +
                "\"values\": {\"1\": 2.0, \"X\": 3.1, \"2\": 3.8}" +
                "}";

        Mockito.when(alphaValidator.validate(any(AlphaProviderMessage.class))).thenReturn(null);
        @SuppressWarnings("unchecked")
        ProviderMessageAdapter<AlphaProviderMessage, BettingEventMessage> adapter = (ProviderMessageAdapter<AlphaProviderMessage, BettingEventMessage>) (ProviderMessageAdapter<?, ?>) (msg ->
                new BettingEventSettlementMessage("ev123", MessageProvider.ALPHA, BetOutcome.HOME));
        Mockito.when(adapterFactory.forAlpha()).thenReturn(adapter);

        mockMvc.perform(post("/v1/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted());

        verify(queue, times(1)).publish(any(BettingEventMessage.class));
    }

    @Test
    void alphaSettlement_valid_returnsAccepted() throws Exception {
        String json = "{" +
                "\"msg_type\": \"settlement\"," +
                "\"event_id\": \"ev123\"," +
                "\"outcome\": \"1\"" +
                "}";

        Mockito.when(alphaValidator.validate(any(AlphaProviderMessage.class))).thenReturn(null);
        @SuppressWarnings("unchecked")
        ProviderMessageAdapter<AlphaProviderMessage, BettingEventMessage> adapter = (ProviderMessageAdapter<AlphaProviderMessage, BettingEventMessage>) (ProviderMessageAdapter<?, ?>) (msg ->
                new BettingEventSettlementMessage("ev123", MessageProvider.ALPHA, BetOutcome.HOME));
        Mockito.when(adapterFactory.forAlpha()).thenReturn(adapter);

        mockMvc.perform(post("/v1/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted());

        verify(queue, times(1)).publish(any(BettingEventMessage.class));
    }

    @Test
    void alpha_invalid_returnsBadRequest() throws Exception {
        // Missing event_id
        String json = "{" +
                "\"msg_type\": \"odds_update\"," +
                "\"values\": {\"1\": 2.0, \"X\": 3.1, \"2\": 3.8}" +
                "}";

        Mockito.when(alphaValidator.validate(any(AlphaProviderMessage.class))).thenReturn("Missing required field: event_id");

        mockMvc.perform(post("/v1/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
