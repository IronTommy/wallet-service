package com.example.wallet.controller;

import com.example.wallet.dto.CreateWalletRequest;
import com.example.wallet.entity.Wallet;
import com.example.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для WalletController.
 * Проверяет создание кошельков, операции с ними и получения баланса.
 */
@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    /**
     * Тест на создание кошелька.
     * Проверяет успешное создание кошелька с возвратом статуса 201.
     */
    @Test
    void createWallet_shouldReturn201() throws Exception {
        UUID walletId = UUID.randomUUID();
        CreateWalletRequest request = new CreateWalletRequest();
        request.setWalletId(walletId);

        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.ZERO);

        when(walletService.createWallet(walletId)).thenReturn(wallet);

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("Wallet created successfully"));
    }

    /**
     * Тест на получение баланса.
     * Проверяет успешное получение баланса кошелька.
     */
    @Test
    void getBalance_shouldReturn200() throws Exception {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));

        when(walletService.findWalletById(walletId)).thenReturn(wallet);

        mockMvc.perform(get("/api/v1/wallets/" + walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100));
    }

    /**
     * Тест на получение баланса при отсутствии кошелька.
     * Проверяет возврат ошибки 404, если кошелек не найден.
     */
    @Test
    void getBalance_shouldReturn404WhenWalletNotFound() throws Exception {
        UUID walletId = UUID.randomUUID();

        when(walletService.findWalletById(walletId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/wallets/" + walletId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Wallet not found"));
    }
}
