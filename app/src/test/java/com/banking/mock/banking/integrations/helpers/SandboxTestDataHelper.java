package com.banking.mock.banking.integrations.helpers;

import com.banking.mock.banking.api.model.account.SandboxAccount;
import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.api.model.payment.SandboxPaymentRequest;
import com.banking.mock.banking.db.model.AccountBalanceEntity;
import com.banking.mock.banking.db.model.DepositAccountEntity;
import com.banking.utils.ResourceLoaderUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SandboxTestDataHelper {

    private static final String ACCOUNTS_CLASS_PATH = "classpath:/sandbox/bank-accounts.json";
    private static final String PAYMENT_CURRENCY = "EUR";

    public static List<SandboxAccount> getAccounts() {
        List<DepositAccountEntity> depositAccountEntities = getSandboxAccounts();
        List<SandboxAccount> accounts = new ArrayList<>(depositAccountEntities.size());
        depositAccountEntities.forEach(depositAccount -> {
            SandboxAccount account = SandboxAccount.builder()
                    .id(depositAccount.getId())
                    .active(depositAccount.isActive())
                    .iban(depositAccount.getIban())
                    .currency(Currency.getInstance(depositAccount.getCurrency()))
                    .balance(buildBalanceAmount(depositAccount.getAccountBalance()))
                    .build();
            accounts.add(account);
        });
        return accounts;
    }

    public static SandboxPaymentRequest createPaymentRequest(String debtorIban, String creditorIban, BigDecimal amount) {
        return SandboxPaymentRequest.builder()
                .creditorName("creditorName")
                .creditorAccount(SandboxPaymentRequest.AccountReference.builder()
                        .iban(creditorIban)
                        .currency(Currency.getInstance(PAYMENT_CURRENCY))
                        .build())
                .debtorAccount(SandboxPaymentRequest.AccountReference.builder()
                        .iban(debtorIban)
                        .currency(Currency.getInstance(PAYMENT_CURRENCY))
                        .build())
                .instructedAmount(SandboxPaymentRequest.InstructedAmount.builder()
                        .amount(amount)
                        .currency(Currency.getInstance(PAYMENT_CURRENCY))
                        .build())
                .build();
    }

    private static List<DepositAccountEntity> getSandboxAccounts() {
        return ResourceLoaderUtil.readResource(ACCOUNTS_CLASS_PATH, new TypeReference<>() {
        });
    }

    private static BaseAmount buildBalanceAmount(AccountBalanceEntity accountBalanceEntity) {
        return BaseAmount.builder()
                .amount(accountBalanceEntity.getAmount())
                .currency(Currency.getInstance(accountBalanceEntity.getCurrency()))
                .build();
    }

}
