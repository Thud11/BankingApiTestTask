package com.banking.mock.banking.mappers;

import com.banking.mock.banking.api.model.account.SandboxAccount;
import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.db.converters.DepositAccountMapper;
import com.banking.mock.banking.db.model.AccountBalanceEntity;
import com.banking.mock.banking.db.model.DepositAccountEntity;
import com.banking.mock.banking.mappers.helpers.DepositAccountFixtures;
import com.banking.mock.banking.mappers.config.MapperTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {MapperTestConfiguration.class})
class DepositAccountMapperTest {

    @Autowired
    private DepositAccountMapper depositAccountMapper;

    @Test
    void testMapping() {
        testAccountMappingToModel();
        testAccountBalanceMappingToModel();

    }

    void testAccountMappingToModel() {
        // Arrange
        SandboxAccount expected = DepositAccountFixtures.getSandboxAccount();
        DepositAccountEntity depositAccountEntity = DepositAccountFixtures.getDepositAccountEntity();

        // Act
        SandboxAccount actual = depositAccountMapper.map(depositAccountEntity);

        // Assert
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }

    void testAccountBalanceMappingToModel() {

        // Arrange
        BaseAmount expected = DepositAccountFixtures.getSandboxBalanceAmount();
        AccountBalanceEntity accountBalanceEntity = DepositAccountFixtures.getDepositAccountBalanceEntity();

        // Act
        BaseAmount actual = depositAccountMapper.map(accountBalanceEntity);

        // Assert
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }

}
