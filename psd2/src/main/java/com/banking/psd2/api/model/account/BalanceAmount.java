package com.banking.psd2.api.model.account;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
public class BalanceAmount {
    Currency currency;
    BigDecimal amount;
}
