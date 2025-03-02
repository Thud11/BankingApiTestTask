package com.banking.psd2.api.model.payment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
public class TransactionFees {

    private BigDecimal amount;
    private Currency currency;
}
