package com.harri.training1.repositories;

import com.google.cloud.bigquery.QueryParameterValue;
import com.harri.training1.mapper.GenericMapper;
import com.harri.training1.models.entities.Invoice;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InvoicesRepository extends GeneralRepository<Invoice> {

    public InvoicesRepository(GenericMapper<Invoice> genericMapper) {
        super(Invoice.class, genericMapper);
    }

    @Override
    protected String select() {
        return "SELECT * FROM `warehouse-trainee.invoice_ds.invoice`";
    }

    @Override
    protected String where() {
        return " WHERE UserId = @userId";
    }

    @Override
    protected Map<String, QueryParameterValue> namedParameters() {
        Map<String, QueryParameterValue> namedParameters = new HashMap<>();
        namedParameters.put("userId", QueryParameterValue.int64(1));
        return namedParameters;
    }
}