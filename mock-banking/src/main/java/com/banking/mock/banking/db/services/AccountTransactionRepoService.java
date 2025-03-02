package com.banking.mock.banking.db.services;

import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.mock.banking.db.converters.AccountTransactionMapper;
import com.banking.mock.banking.db.model.AccountTransactionEntity;
import com.banking.mock.banking.db.repo.AccountTransactionRepository;
import com.banking.mock.banking.db.services.helpers.TransactionsQueryBuilder;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTransactionRepoService {

    private final AccountTransactionRepository accountTransactionRepository;
    private final AccountTransactionMapper accountTransactionMapper;
    private final TransactionsQueryBuilder queryBuilder;


    @Transactional
    public List<SandboxAccountTransaction> findTransactions(String accountId, LocalDateTime fromDate, LocalDateTime toDate, int limit) {
        Query query = queryBuilder.prepeareSearchTransactionQuery(accountId, fromDate, toDate, limit);
        List<AccountTransactionEntity> transactions = ((List<?>) query.getResultList())
                .stream()
                .map(AccountTransactionEntity.class::cast)
                .toList();
        return accountTransactionMapper.toModel(transactions);
    }

    @Transactional
    public void saveTransactions(List<SandboxAccountTransaction> transactions) {
        accountTransactionRepository.saveAll(accountTransactionMapper.toEntity(transactions));
    }
}
