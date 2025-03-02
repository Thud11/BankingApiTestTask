package com.banking.mock.banking.db.services.helpers;

import com.banking.mock.banking.db.model.AccountTransactionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransactionsQueryBuilder {

    private final EntityManager entityManager;

    public Query prepeareSearchTransactionQuery(String iban, LocalDateTime fromDate, LocalDateTime toDate, int limit) {
        String sql = buildSearchTransactionQuery(fromDate, toDate);
        Query query = entityManager.createNativeQuery(sql, AccountTransactionEntity.class);
        query.setParameter("iban", iban);
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        query.setParameter("limit", limit);
        return query;
    }


    private static String buildSearchTransactionQuery(LocalDateTime fromDate, LocalDateTime toDate) {
        StringBuilder sb = new StringBuilder("""
        SELECT at.*
        FROM account_transactions at
        JOIN deposit_account da ON da.id = at.account_id
        WHERE da.iban = :iban
    """);

        if (fromDate != null) {
            sb.append(" AND at.created_at >= :fromDate");
        }
        if (toDate != null) {
            sb.append(" AND at.created_at <= :toDate");
        }
        sb.append(" ORDER BY at.created_at DESC LIMIT :limit");
        return sb.toString();
    }

}
