package com.banking.psd2.db.services;

import com.banking.psd2.api.model.payment.Payment;
import com.banking.psd2.api.model.payment.PaymentStatus;
import com.banking.psd2.db.converters.PaymentMapper;
import com.banking.psd2.db.model.PaymentEntity;
import com.banking.psd2.db.repo.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentRepoService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public Long savePayment(Payment payment) {
        PaymentEntity paymentEntity = paymentMapper.toPaymentEntity(payment);
        return paymentRepository.save(paymentEntity).getId();

    }

    @Transactional
    public void updatePaymentStatus(Long id, UUID paymentId,PaymentStatus paymentStatus ) {
        Optional<PaymentEntity> paymentOptional = paymentRepository.findById(id);
        PaymentEntity paymentEntity = paymentOptional.orElseThrow(() -> new RuntimeException("Payment not found"));
        paymentEntity.setPaymentId(paymentId);
        paymentEntity.setStatus(paymentStatus);
    }
}
