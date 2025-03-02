package com.banking.mock.banking.mappers;

import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.mock.banking.db.converters.AccountTransactionMapper;
import com.banking.mock.banking.db.model.AccountTransactionEntity;
import com.banking.mock.banking.mappers.helpers.TransactionFixtures;
import com.banking.mock.banking.mappers.config.MapperTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {MapperTestConfiguration.class})
class AccountTransactionMapperTest {

    @Autowired
    private AccountTransactionMapper accountTransactionMapper;

    @Test
    void testMapping() {
        testTransactionMappingToModel();
        testTransactionMappingToEntity();
    }


    private void testTransactionMappingToModel() {
        // Arrange
        SandboxAccountTransaction expected = TransactionFixtures.getTransactionModel();
        AccountTransactionEntity entity = TransactionFixtures.getTransactionEntity();

        // Act
        SandboxAccountTransaction actual = accountTransactionMapper.toModel(entity);

        // Assert
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }

    private void testTransactionMappingToEntity() {
        // Arrange
        AccountTransactionEntity expected = TransactionFixtures.getTransactionEntity();
        SandboxAccountTransaction model = TransactionFixtures.getTransactionModel();

        // Act
        AccountTransactionEntity actual = accountTransactionMapper.toEntity(model);

        // Assert
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }



}
