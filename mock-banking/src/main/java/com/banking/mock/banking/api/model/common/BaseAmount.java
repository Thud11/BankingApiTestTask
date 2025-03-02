package com.banking.mock.banking.api.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseAmount {
    private Currency currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;
}
