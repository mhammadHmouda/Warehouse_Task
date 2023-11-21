package com.harri.training1.repositories;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

public abstract class BaseRepository {
    private final BigQuery bigQuery;

    public BaseRepository(String projectId) {
        this.bigQuery = BigQueryOptions.newBuilder().setProjectId(projectId).build().getService();
    }

    public TableResult executeQuery(QueryJobConfiguration queryConfig) throws InterruptedException {
        return bigQuery.query(queryConfig);
    }
}
