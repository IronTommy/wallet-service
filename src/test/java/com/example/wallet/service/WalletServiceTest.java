package com.example.wallet.service;

import com.example.wallet.entity.Wallet;
import com.example.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для WalletService.
 * Проверяет логику создания кошелька, выполнения операций и обработки ошибок.
 */
class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    public WalletServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Тест на создание кошелька, если кошелек не существует.
     */
    @Test
    void createWallet_shouldCreateWalletSuccessfully() {
        UUID walletId = UUID.randomUUID();
        when(walletRepository.existsById(walletId)).thenReturn(false);

        Wallet createdWallet = new Wallet();
        createdWallet.setId(walletId);
        createdWallet.setBalance(BigDecimal.ZERO);

        when(walletRepository.save(any(Wallet.class))).thenReturn(createdWallet);

        Wallet result = walletService.createWallet(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.getId());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    /**
     * Тест на создание кошелька, если кошелек уже существует.
     */
    @Test
    void createWallet_shouldThrowExceptionWhenWalletExists() {
        UUID walletId = UUID.randomUUID();
        when(walletRepository.existsById(walletId)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> walletService.createWallet(walletId));

        assertEquals("Wallet already exists", exception.getMessage());
    }

    /**
     * Тест на успешное выполнение операции депозита.
     */
    @Test
    void processOperation_shouldDepositSuccessfully() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.ZERO);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet updatedWallet = walletService.processOperation(walletId, "DEPOSIT", BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), updatedWallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    /**
     * Тест на успешное выполнение операции снятия.
     */
    @Test
    void processOperation_shouldWithdrawSuccessfully() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(200));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet updatedWallet = walletService.processOperation(walletId, "WITHDRAW", BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), updatedWallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    /**
     * Тест на снятие средств с недостаточным балансом.
     */
    @Test
    void processOperation_shouldThrowExceptionWhenInsufficientFunds() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(50));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> walletService.processOperation(walletId, "WITHDRAW", BigDecimal.valueOf(100)));

        assertEquals("Insufficient funds", exception.getMessage());
    }

    /**
     * Тест на неверный тип операции.
     */
    @Test
    void processOperation_shouldThrowExceptionForInvalidOperationType() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> walletService.processOperation(walletId, "INVALID", BigDecimal.valueOf(100)));

        assertEquals("Invalid operation type", exception.getMessage());
    }
}
