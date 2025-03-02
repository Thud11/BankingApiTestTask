package com.banking.mock.banking.api.model.account;

import com.banking.mock.banking.api.model.common.BaseAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SandboxAccount {
    private Long id;
    private boolean active;
    private String iban;
    private Currency currency;
    private BaseAmount balance;
}
