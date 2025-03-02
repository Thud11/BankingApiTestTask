package com.banking.mock.banking.mappers.helpers;

import com.banking.mock.banking.api.model.account.SandboxAccount;
import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.db.model.AccountBalanceEntity;
import com.banking.mock.banking.db.model.DepositAccountEntity;
import com.banking.utils.ResourceLoaderUtil;

public class DepositAccountFixtures {

    private static final String ACCOUNT_ENTITY = "classpath:/fixtures/mappers/deposit-account-mapper/sandbox-account-entity.json";
    private static final String ACCOUNT_MODEL = "classpath:/fixtures/mappers/deposit-account-mapper/sandbox-account-model.json";
    private static final String ACCOUNT_BALANCE_ENTITY = "classpath:/fixtures/mappers/deposit-account-mapper/sandbox-balance-amount-entity.json";
    private static final String ACCOUNT_BALANCE_MODEL = "classpath:/fixtures/mappers/deposit-account-mapper/sandbox-balance-amount-model.json";

    public static SandboxAccount getSandboxAccount() {
        return ResourceLoaderUtil.readResource(ACCOUNT_MODEL, SandboxAccount.class);
    }

    public static DepositAccountEntity getDepositAccountEntity() {
        return ResourceLoaderUtil.readResource(ACCOUNT_ENTITY, DepositAccountEntity.class);
    }

    public static AccountBalanceEntity getDepositAccountBalanceEntity() {
        return ResourceLoaderUtil.readResource(ACCOUNT_BALANCE_ENTITY, AccountBalanceEntity.class);
    }

    public static BaseAmount getSandboxBalanceAmount() {
        return ResourceLoaderUtil.readResource(ACCOUNT_BALANCE_MODEL, BaseAmount.class);
    }
}
