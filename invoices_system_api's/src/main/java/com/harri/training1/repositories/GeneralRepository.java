package com.harri.training1.repositories;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryParameterValue;
import com.google.cloud.bigquery.TableResult;
import com.harri.training1.mapper.GenericMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public abstract class GeneralRepository<T> {

    @Value("${warehouse.project}")
    private String projectId;
    private final BigQuery bigQuery;
    private final GenericMapper<T> mapper;
    private final Class<T> clazz;

    public GeneralRepository(Class<T> clazz, GenericMapper<T> genericMapper) {
        this.bigQuery = BigQueryOptions.newBuilder().setProjectId(projectId).build().getService();
        this.mapper = genericMapper;
        this.clazz = clazz;
    }

    protected BigQuery getBigQuery() {
        return bigQuery;
    }

    protected abstract String select();

    protected String where() {
        return "";
    }

    protected String join() {
        return "";
    }

    protected String groupBy() {
        return "";
    }

    protected String orderBy() {
        return "";
    }

    protected String limit() {
        return "";
    }

    protected Map<String, QueryParameterValue> namedParameters() {
        return new HashMap<>();
    }

    public List<T> executeQuery() throws InterruptedException {
        String sql = select() + where() + join() + groupBy() + orderBy() + limit();

        QueryJobConfiguration queryConfigBuilder = QueryJobConfiguration.newBuilder(sql)
                .setNamedParameters(namedParameters()).build();

        TableResult result = bigQuery.query(queryConfigBuilder);
        List<T> results = new ArrayList<>();
        result.iterateAll().forEach(row -> results.add(mapper.mapRow(row, clazz)));
        return results;
    }
}