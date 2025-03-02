package com.banking.mock.banking.db.converters;

import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.mock.banking.db.model.AccountTransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountTransactionMapper {


    SandboxAccountTransaction toModel (AccountTransactionEntity accountTransactionEntity);
    List<SandboxAccountTransaction> toModel (List<AccountTransactionEntity> accountTransactionEntities);



    @Mapping(target = "id", ignore = true)
    AccountTransactionEntity toEntity (SandboxAccountTransaction accountTransaction);
    List<AccountTransactionEntity> toEntity (List<SandboxAccountTransaction> accountTransactions);

}
