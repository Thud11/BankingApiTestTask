package com.banking.mock.banking.services.validators;

import com.banking.mock.banking.api.model.payment.SandboxPaymentRequest;
import com.banking.mock.banking.services.SandboxAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRequestValidator {

    private final SandboxAccountService accountService;


    public boolean hasInvalidConditions(SandboxPaymentRequest request) {
        return isDebtorAccountInvalid(request) ||
               isCreditorAccountInvalid(request) ||
               hasInsufficientFunds(request);
    }

    private boolean isDebtorAccountInvalid(SandboxPaymentRequest request) {
        return accountService.accountExistsByIban(request.getDebtorAccount().getIban());
    }

    private boolean isCreditorAccountInvalid(SandboxPaymentRequest request) {
        return accountService.accountExistsByIban(request.getCreditorAccount().getIban());
    }

    private boolean hasInsufficientFunds(SandboxPaymentRequest request) {
        return !accountService.checkSufficientFunds(
                request.getDebtorAccount().getIban(),
                request.getInstructedAmount().getAmount()
        );
    }
}
