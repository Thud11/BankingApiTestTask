package com.banking.mock.banking.db.services;

import com.banking.exception.AccountNotFoundException;
import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.db.converters.DepositAccountMapper;
import com.banking.mock.banking.db.model.DepositAccountEntity;
import com.banking.mock.banking.db.repo.DepositAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositAccountRepoService {

    private final DepositAccountRepository depositAccountRepository;
    private final DepositAccountMapper depositAccountMapper;

    public boolean accountExistsByIban(String iban) {
        return depositAccountRepository.existsByIban(iban);
    }

    public BaseAmount getBalanceByIban(String iban) {
        DepositAccountEntity depositAccountEntity = depositAccountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return depositAccountMapper.map(depositAccountEntity.getAccountBalance());
    }

    @Transactional
    public void changeBalance(String iban, BigDecimal balanceChange) {
        DepositAccountEntity depositAccountEntity = depositAccountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        BigDecimal currentBalance = depositAccountEntity.getAccountBalance().getAmount();
        depositAccountEntity.getAccountBalance().setAmount(currentBalance.add(balanceChange));
    }


    @Transactional
    public boolean checkSufficientFunds(String iban, BigDecimal amount) {
        return depositAccountRepository.hasSufficientBalanceByIban(iban, amount);
    }
}
