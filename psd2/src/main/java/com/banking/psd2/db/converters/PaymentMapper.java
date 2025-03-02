package com.banking.psd2.db.converters;

import com.banking.psd2.api.model.payment.Payment;
import com.banking.psd2.db.model.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMapper {

    @Mapping(target = "senderIban", source = "payment.creditorAccount.iban")
    @Mapping(target = "recipientIban", source = "payment.debtorAccount.iban")
    @Mapping(target = "currency", source = "payment.instructedAmount.currency")
    @Mapping(target = "amount", source = "payment.instructedAmount.amount")
    @Mapping(target = "id", ignore = true)
    PaymentEntity toPaymentEntity(Payment payment);
}
