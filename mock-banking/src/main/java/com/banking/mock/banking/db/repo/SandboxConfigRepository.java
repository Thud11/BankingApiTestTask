package com.banking.mock.banking.db.repo;

import com.banking.mock.banking.db.model.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SandboxConfigRepository extends JpaRepository<ConfigEntity, Long> {
}
