package com.banking.mock.banking.db.repo;

import com.banking.mock.banking.db.model.AccountTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransactionEntity, Long> {

}