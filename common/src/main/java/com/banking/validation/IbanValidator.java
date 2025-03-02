package com.banking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.IBANValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class IbanValidator implements ConstraintValidator<IBAN, String> {

    @Value("${validators.config.enabledIbanValidator}")
    private boolean enabledIbanValidator;

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        if (enabledIbanValidator) {
            return IBANValidator.getInstance().isValid(iban);
        } else return true;
    }
}
