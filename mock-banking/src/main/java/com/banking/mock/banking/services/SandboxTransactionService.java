package com.banking.mock.banking.services;

import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.mock.banking.db.services.AccountTransactionRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SandboxTransactionService {

    private final AccountTransactionRepoService accountTransactionRepoService;


    public List<SandboxAccountTransaction> getTransactions(String accountId, LocalDateTime fromDate, LocalDateTime toDate, int limit) {
        return accountTransactionRepoService.findTransactions(accountId, fromDate, toDate, limit);

    }

    public void saveTransactions(List<SandboxAccountTransaction> senderTransaction) {
        accountTransactionRepoService.saveTransactions(senderTransaction);
    }
}
