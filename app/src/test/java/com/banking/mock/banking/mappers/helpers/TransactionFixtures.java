package com.banking.mock.banking.mappers.helpers;

import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.utils.ResourceLoaderUtil;

public class TransactionFixtures {

    private static final String TRANSACTION_MODEL = "classpath:/fixtures/mappers/account-transaction-mapper/sandbox-account-transaction.json";
    private static final String TRANSACTION_ENTITY = "classpath:/fixtures/mappers/account-transaction-mapper/account-transaction-entity.json";

    public static SandboxAccountTransaction getTransactionModel() {
        return ResourceLoaderUtil.readResource(TRANSACTION_MODEL, SandboxAccountTransaction.class);
    }

    public static com.banking.mock.banking.db.model.AccountTransactionEntity getTransactionEntity() {
        return ResourceLoaderUtil.readResource(TRANSACTION_ENTITY, com.banking.mock.banking.db.model.AccountTransactionEntity.class);
    }
}
