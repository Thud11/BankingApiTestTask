package com.banking.mock.banking.init;

import com.banking.mock.banking.db.model.DepositAccountEntity;
import com.banking.mock.banking.db.repo.DepositAccountRepository;
import com.banking.mock.banking.db.services.SandboxConfigRepoService;
import com.banking.utils.ResourceLoaderUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SandboxInitializer  {

    private final DepositAccountRepository depositAccountRepository;
    private final SandboxConfigRepoService sandboxConfigRepoService;
    private static final String ACCOUNTS = "classpath:/sandbox/bank-accounts.json";


    @EventListener(ContextRefreshedEvent.class)
    public void run() {
        boolean sandboxDataInitialized = sandboxConfigRepoService.isSandboxDataInitialized();
        if (!sandboxDataInitialized) {
            initConfig();
            sandboxConfigRepoService.updateConfig(true);
        } else {
            log.info("Sandbox already initialized");
        }

    }

    private void initConfig() {
        log.info("Initializing sandbox content");
        initBankAccounts();
        log.info("Sandbox content initialized");
    }

    private void initBankAccounts() {
        log.info("Init sandbox accounts");
        List<DepositAccountEntity> depositAccountEntities = getSandboxAccounts();
        depositAccountRepository.saveAll(depositAccountEntities);
        log.info("Sandbox accounts initialized");
    }

    private List<DepositAccountEntity> getSandboxAccounts() {
        return ResourceLoaderUtil.readResource(ACCOUNTS, new TypeReference<>() {
        });
    }
}
