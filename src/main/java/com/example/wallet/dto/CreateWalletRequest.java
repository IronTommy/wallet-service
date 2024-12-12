package com.example.wallet.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO для создания нового кошелька.
 */
public class CreateWalletRequest {

    @NotNull(message = "Wallet ID cannot be null")
    private UUID walletId;

    /**
     * Возвращает идентификатор кошелька.
     *
     * @return идентификатор кошелька.
     */
    public UUID getWalletId() {
        return walletId;
    }

    /**
     * Устанавливает идентификатор кошелька.
     *
     * @param walletId идентификатор кошелька.
     */
    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }
}
