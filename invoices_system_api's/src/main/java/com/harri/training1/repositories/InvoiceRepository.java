package com.harri.training1.repositories;

import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryParameterValue;
import com.google.cloud.bigquery.TableResult;
import com.harri.training1.exceptions.InvoiceNotAddedException;
import com.harri.training1.exceptions.InvoiceNotExistException;
import com.harri.training1.exceptions.UserFoundException;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.models.entities.Item;
import com.harri.training1.utils.InvoiceUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class InvoiceRepository extends BaseRepository{
    private final InvoiceUtils utils;

    public InvoiceRepository(InvoiceUtils invoiceUtils) {
        super("warehouse-trainee");
        this.utils = invoiceUtils;
    }

    public void save(Invoice invoice) {
        try {
            String baseQuery = "INSERT INTO `warehouse-trainee.invoice_ds.invoice` (Id, Amount, CreatedAt, ItemName, ItemPrice, ItemQuantity, UserId) VALUES "
                    + "(@id, @amount, @createdAt, @itemName, @itemPrice, @itemQuantity, @userId)";

            for (Item item : invoice.getItems()) {
                QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(baseQuery)
                        .addNamedParameter("id", QueryParameterValue.int64(invoice.getId()))
                        .addNamedParameter("amount", QueryParameterValue.float64(invoice.getAmount()))
                        .addNamedParameter("createdAt", QueryParameterValue.date(invoice.getCreatedAt()))
                        .addNamedParameter("itemName", QueryParameterValue.string(item.getName()))
                        .addNamedParameter("itemPrice", QueryParameterValue.float64(item.getPrice()))
                        .addNamedParameter("itemQuantity", QueryParameterValue.int64(item.getQuantity()))
                        .addNamedParameter("userId", QueryParameterValue.int64(invoice.getUserId()))
                        .build();

                executeQuery(queryConfig);
            }
        } catch (Exception e) {
            throw new InvoiceNotAddedException(e.getMessage());
        }
    }


    public Invoice findById(Long id) {
        try {
            String query = "SELECT * FROM `warehouse-trainee.invoice_ds.invoice` WHERE Id = @id";

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    .addNamedParameter("id", QueryParameterValue.int64(id))
                    .build();

            TableResult result = executeQuery(queryConfig);
            return utils.mapToInvoice(result);
        }
        catch (Exception e) {
            throw new InvoiceNotExistException(e.getMessage());
        }
    }

    public List<Invoice> findAll() {
        try {
            String query = "SELECT * FROM `warehouse-trainee.invoice_ds.invoice`";

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                   .build();

            TableResult result = executeQuery(queryConfig);
            return utils.mapToInvoices(result);
        }
        catch (Exception e) {
            throw new InvoiceNotExistException(e.getMessage());
        }
    }

    public List<Invoice> findByUserId(Long id) {
        try {
            String query = "SELECT * FROM `warehouse-trainee.invoice_ds.invoice` WHERE UserId = @userId";

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                  .addNamedParameter("userId", QueryParameterValue.int64(id))
                  .build();

            TableResult result = executeQuery(queryConfig);
            return utils.mapToInvoices(result);
        }
        catch (Exception e) {
            throw new InvoiceNotExistException(e.getMessage());
        }
    }

    public Long deleteById(Long invoiceId) {
        try {
            String query = "DELETE FROM `warehouse-trainee.invoice_ds.invoice` WHERE Id = @id";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    .addNamedParameter("id", QueryParameterValue.int64(invoiceId)).build();

            TableResult result = executeQuery(queryConfig);

            return result.getTotalRows();

        } catch (Exception e) {
            throw new UserFoundException("Error when deleting invoice by ID: " + e.getMessage());
        }
    }
}
