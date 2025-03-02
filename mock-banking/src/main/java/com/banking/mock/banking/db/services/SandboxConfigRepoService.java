package com.banking.mock.banking.db.services;

import com.banking.mock.banking.db.model.ConfigEntity;
import com.banking.mock.banking.db.repo.SandboxConfigRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SandboxConfigRepoService {

    private final SandboxConfigRepository repository;

    private static final Long CONFIG_ID = 1L;

    @PostConstruct
    @Transactional
    public void initConfig() {
        if (repository.findById(1L).isEmpty()) {
            repository.save(new ConfigEntity(1L, false));
        }
    }

    @Transactional
    public void updateConfig(boolean initialized) {
        ConfigEntity config = getConfig();
        config.setSandboxDataInitialized(initialized);
    }

    public boolean isSandboxDataInitialized() {
        return getConfig().isSandboxDataInitialized();
    }

    private ConfigEntity getConfig() {
        return repository.findById(CONFIG_ID)
                .orElseThrow(() -> new RuntimeException("Config not found"));
    }


}
