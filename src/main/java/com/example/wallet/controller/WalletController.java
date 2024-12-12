package com.example.wallet.controller;

import com.example.wallet.dto.CreateWalletRequest;
import com.example.wallet.dto.WalletOperationRequest;
import com.example.wallet.entity.Wallet;
import com.example.wallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для работы с кошельками.
 * Обрабатывает запросы на создание кошелька, выполнение операций и получение баланса.
 */
@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * Создание нового кошелька.
     *
     * @param request данные о кошельке.
     * @return статус создания кошелька.
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createWallet(@RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWallet(request.getWalletId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("status", "Wallet created successfully"));
    }

    /**
     * Выполнение операции с кошельком (депозит или снятие).
     *
     * @param request данные об операции.
     * @return статус операции и актуальный баланс кошелька.
     */
    @PostMapping("/operations")
    public ResponseEntity<Map<String, Object>> processOperation(@RequestBody WalletOperationRequest request) {
        Wallet updatedWallet = walletService.processOperation(request.getWalletId(), request.getOperationType(), request.getAmount());
        return ResponseEntity.ok(Map.of("status", "Success", "balance", updatedWallet.getBalance()));
    }

    /**
     * Получение баланса кошелька.
     *
     * @param walletId идентификатор кошелька.
     * @return баланс кошелька.
     */
    @GetMapping("/{walletId}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable UUID walletId) {
        Wallet wallet = walletService.findWalletById(walletId);
        if (wallet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Wallet not found"));
        }
        return ResponseEntity.ok(Map.of("balance", wallet.getBalance()));
    }
}
