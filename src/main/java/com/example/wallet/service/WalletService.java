package com.example.wallet.service;

import com.example.wallet.entity.Wallet;
import com.example.wallet.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для работы с кошельками.
 * Выполняет логику создания кошелька, обработки операций и получения информации о кошельке.
 */
@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Создание кошелька по идентификатору.
     *
     * @param walletId идентификатор кошелька.
     * @return созданный кошелек.
     */
    @Transactional
    public Wallet createWallet(UUID walletId) {
        if (walletRepository.existsById(walletId)) {
            throw new IllegalStateException("Wallet already exists");
        }

        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }

    /**
     * Поиск кошелька по идентификатору.
     *
     * @param walletId идентификатор кошелька.
     * @return найденный кошелек или null, если не найден.
     */
    @Transactional
    public Wallet findWalletById(UUID walletId) {
        return walletRepository.findById(walletId).orElse(null);
    }

    /**
     * Обработка операции с кошельком (депозит или снятие).
     *
     * @param walletId     идентификатор кошелька.
     * @param operationType тип операции (DEPOSIT/ WITHDRAW).
     * @param amount       сумма операции.
     * @return обновленный кошелек.
     */
    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public Wallet processOperation(UUID walletId, String operationType, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        if ("WITHDRAW".equalsIgnoreCase(operationType)) {
            if (wallet.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance().subtract(amount));
        } else if ("DEPOSIT".equalsIgnoreCase(operationType)) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            throw new IllegalArgumentException("Invalid operation type");
        }

        return walletRepository.save(wallet);
    }
}
