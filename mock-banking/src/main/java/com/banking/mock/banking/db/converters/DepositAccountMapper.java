package com.banking.mock.banking.db.converters;

import com.banking.mock.banking.api.model.account.SandboxAccount;
import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.db.model.AccountBalanceEntity;
import com.banking.mock.banking.db.model.DepositAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DepositAccountMapper {

    @Mapping(target = "balance", source = "depositAccountEntity.accountBalance")
    SandboxAccount map(DepositAccountEntity depositAccountEntity);

    BaseAmount map(AccountBalanceEntity accountBalanceEntity);
}
