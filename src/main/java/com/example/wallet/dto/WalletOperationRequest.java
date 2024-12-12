package com.example.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO для выполнения операций с кошельком.
 */
public class WalletOperationRequest {

    @NotNull(message = "Wallet ID cannot be null")
    private UUID walletId;

    @NotNull(message = "Operation type cannot be null")
    @Pattern(regexp = "DEPOSIT|WITHDRAW", message = "OperationType must be DEPOSIT or WITHDRAW")
    private String operationType;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

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

    /**
     * Возвращает тип операции.
     *
     * @return тип операции.
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * Устанавливает тип операции.
     *
     * @param operationType тип операции.
     */
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    /**
     * Возвращает сумму операции.
     *
     * @return сумма операции.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Устанавливает сумму операции.
     *
     * @param amount сумма операции.
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
