package com.example.wallet.repository;

import com.example.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Wallet.
 * Предоставляет стандартные методы для работы с базой данных через Spring Data JPA.
 */
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
