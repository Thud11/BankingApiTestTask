package com.banking.psd2.api.model.account;

import lombok.Builder;
import lombok.Data;

import java.util.Currency;

@Data
@Builder
public class Account {
    private Long id;
    private boolean active;
    private String iban;
    private Currency currency;
    private BalanceAmount balance;
}
