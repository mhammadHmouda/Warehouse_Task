package com.harri.training1.repositories;

import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryParameterValue;
import com.google.cloud.bigquery.TableResult;
import com.harri.training1.exceptions.UserFoundException;
import com.harri.training1.models.entities.User;
import com.harri.training1.utils.UserUtils;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class UserRepository extends BaseRepository{

    private final UserUtils utils;

    public UserRepository(UserUtils userUtils) {
        super("warehouse-trainee");
        this.utils = userUtils;
    }

    public List<User> findAll() {
        try {
            String query = "SELECT * FROM `warehouse-trainee.invoice_ds.user`";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

            TableResult result = executeQuery(queryConfig);
            return utils.mapToUsers(result);
        }
        catch (Exception e) {
            throw new UserFoundException("User not found!");
        }
    }

    public User findByUsername(String username) {
        try {
            String query = "SELECT * FROM `warehouse-trainee.invoice_ds.user` WHERE Username = @username";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    .addNamedParameter("username", QueryParameterValue.string(username)).build();

            TableResult result = executeQuery(queryConfig);
            return result.getTotalRows() <= 0 ? null : utils.mapToUser(result);
        } catch (Exception e) {
            throw new UserFoundException(e.getMessage());
        }
    }


    public User findById(Long userId) {
        try {
            String query = "SELECT * FROM `warehouse-trainee.invoice_ds.user` WHERE Id = @id";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    .addNamedParameter("id", QueryParameterValue.int64(userId)).build();

            TableResult result = executeQuery(queryConfig);
            return utils.mapToUser(result);
        }
        catch (Exception e) {
            throw new UserFoundException("User not found!");
        }
    }

    public List<User> findByRole(String roleName) {
        try {
            String query = "SELECT * FROM `warehouse-trainee.invoice_ds.user` WHERE Role = @role";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    .addNamedParameter("role", QueryParameterValue.string(roleName)).build();

            TableResult result = executeQuery(queryConfig);

            return utils.mapToUsers(result);
        }
        catch (Exception e) {
            throw new UserFoundException("User not found!");
        }
    }

    public void save(User user) {
        try {
            String query = "INSERT INTO `warehouse-trainee.invoice_ds.user` (Id, Username, Email, Password, Role)"
                    + " VALUES (@id, @username, @email, @password, @role)";

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    .addNamedParameter("id", QueryParameterValue.int64(user.getId()))
                    .addNamedParameter("username", QueryParameterValue.string(user.getUsername()))
                    .addNamedParameter("email", QueryParameterValue.string(user.getEmail()))
                    .addNamedParameter("password", QueryParameterValue.string(user.getPassword()))
                    .addNamedParameter("role", QueryParameterValue.string(user.getRole()))
                    .build();

            executeQuery(queryConfig);
        } catch (Exception e) {
            throw new UserFoundException(e.getMessage());
        }
    }

    public Long deleteById(Long userId) {
        try {
            String query = "DELETE FROM `warehouse-trainee.invoice_ds.user` WHERE Id = @id";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    .addNamedParameter("id", QueryParameterValue.int64(userId)).build();

            TableResult result = executeQuery(queryConfig);

            return result.getTotalRows();

        } catch (Exception e) {
            throw new UserFoundException("Error when deleting user by ID: " + e.getMessage());
        }
    }

    public Long update(User user) {
        try {
            String query = "UPDATE `warehouse-trainee.invoice_ds.user` SET Role = @role WHERE Id = @id";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                    .addNamedParameter("id", QueryParameterValue.int64(user.getId()))
                    .addNamedParameter("role", QueryParameterValue.string(user.getRole()))
                    .build();

            TableResult result = executeQuery(queryConfig);
            return result.getTotalRows();
        } catch (Exception e) {
            throw new UserFoundException("Error when updating user role by ID: " + e.getMessage());
        }
    }

}
