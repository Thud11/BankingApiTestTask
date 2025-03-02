package com.banking.mock.banking.db.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "account_balance")
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String currency;
    private BigDecimal amount;

    @UpdateTimestamp
    private LocalDateTime updated;




}
