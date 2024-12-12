package com.example.wallet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сущность кошелька.
 * Содержит данные о кошельке, такие как баланс и версия.
 */
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @Column(name = "wallet_id", nullable = false)
    private UUID id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /**
     * Возвращает идентификатор кошелька.
     *
     * @return идентификатор кошелька.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор кошелька.
     *
     * @param id идентификатор кошелька.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает баланс кошелька.
     *
     * @return баланс кошелька.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Устанавливает баланс кошелька.
     *
     * @param balance баланс кошелька.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Возвращает версию кошелька.
     *
     * @return версия кошелька.
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Устанавливает версию кошелька.
     *
     * @param version версия кошелька.
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
