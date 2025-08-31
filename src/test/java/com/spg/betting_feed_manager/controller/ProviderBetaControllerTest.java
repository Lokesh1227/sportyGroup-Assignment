package com.spg.betting_feed_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spg.betting_feed_manager.adapter.MessageAdapterFactory;
import com.spg.betting_feed_manager.adapter.ProviderMessageAdapter;
import com.spg.betting_feed_manager.controller.provider.beta.ProviderBetaController;
import com.spg.betting_feed_manager.model.BettingEventMessages.BetOutcome;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.BettingEventSettlementMessage;
import com.spg.betting_feed_manager.model.BettingEventMessages.MessageProvider;
import com.spg.betting_feed_manager.model.provider.beta.BetaProviderMessages.BetaProviderMessage;
import com.spg.betting_feed_manager.service.MessageService;
import com.spg.betting_feed_manager.validation.BetaMessageValidator;
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

@WebMvcTest(controllers = ProviderBetaController.class)
class ProviderBetaControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean MessageService queue;
    @MockitoBean MessageAdapterFactory adapterFactory;
    @MockitoBean BetaMessageValidator betaValidator;

    @Test
    void betaOdds_valid_returnsAccepted_andPublishes() throws Exception {
        String json = "{" +
                "\"type\": \"ODDS\"," +
                "\"event_id\": \"ev456\"," +
                "\"odds\": {\"home\": 1.95, \"draw\": 3.2, \"away\": 4.0}" +
                "}";

        Mockito.when(betaValidator.validate(any(BetaProviderMessage.class))).thenReturn(null);
        @SuppressWarnings("unchecked")
        ProviderMessageAdapter<BetaProviderMessage, BettingEventMessage> adapter = (ProviderMessageAdapter<BetaProviderMessage, BettingEventMessage>) (ProviderMessageAdapter<?, ?>) (msg ->
                new BettingEventSettlementMessage("ev456", MessageProvider.BETA, BetOutcome.HOME));
        Mockito.when(adapterFactory.forBeta()).thenReturn(adapter);

        mockMvc.perform(post("/v1/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted());

        verify(queue, times(1)).publish(any(BettingEventMessage.class));
    }

    @Test
    void betaSettlement_valid_returnsAccepted() throws Exception {
        String json = "{" +
                "\"type\": \"SETTLEMENT\"," +
                "\"event_id\": \"ev456\"," +
                "\"result\": \"away\"" +
                "}";

        Mockito.when(betaValidator.validate(any(BetaProviderMessage.class))).thenReturn(null);
        @SuppressWarnings("unchecked")
        ProviderMessageAdapter<BetaProviderMessage, BettingEventMessage> adapter = (ProviderMessageAdapter<BetaProviderMessage, BettingEventMessage>) (ProviderMessageAdapter<?, ?>) (msg ->
                new BettingEventSettlementMessage("ev456", MessageProvider.BETA, BetOutcome.AWAY));
        Mockito.when(adapterFactory.forBeta()).thenReturn(adapter);

        mockMvc.perform(post("/v1/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted());

        verify(queue, times(1)).publish(any(BettingEventMessage.class));
    }

    @Test
    void beta_invalid_returnsBadRequest() throws Exception {
        String json = "{" +
                "\"type\": \"ODDS\"," +
                "\"event_id\": \"\"," +
                "\"odds\": {\"home\": 1.95, \"draw\": 3.2, \"away\": 4.0}" +
                "}";

        Mockito.when(betaValidator.validate(any(BetaProviderMessage.class))).thenReturn("Missing required field: event_id");

        mockMvc.perform(post("/v1/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
