package com.banking.mock.banking.db.repo;

import com.banking.mock.banking.db.model.DepositAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface DepositAccountRepository extends JpaRepository<DepositAccountEntity, Long> {

    boolean existsByIban(String iban);
    Optional<DepositAccountEntity> findByIban(String iban);

    @Query("SELECT CASE WHEN a.accountBalance.amount >= :amount THEN true ELSE false END FROM DepositAccountEntity a WHERE a.iban = :iban")
    boolean hasSufficientBalanceByIban(@Param("iban") String accountId, @Param("amount") BigDecimal amount);

}
