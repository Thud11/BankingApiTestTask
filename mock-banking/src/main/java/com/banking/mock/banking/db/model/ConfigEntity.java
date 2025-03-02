package com.banking.mock.banking.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "sandbox_config")
@NoArgsConstructor
@AllArgsConstructor
public class ConfigEntity {

    @Id
    private Long id = 1L;

    boolean sandboxDataInitialized;
}
