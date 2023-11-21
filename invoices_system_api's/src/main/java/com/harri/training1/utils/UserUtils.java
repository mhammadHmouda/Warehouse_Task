package com.harri.training1.utils;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.harri.training1.models.entities.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserUtils {
    public User mapToUser(TableResult result) {
        FieldValueList row = result.iterateAll().iterator().next();
        return buildUser(row);
    }

    public List<User> mapToUsers(TableResult result) {
        List<User> users = new ArrayList<>();
        result.iterateAll().forEach(row -> {
            User user = buildUser(row);
            users.add(user);
        });
        return users;
    }

    private User buildUser(FieldValueList row) {
        User user = new User();
        user.setId(row.get("Id").getLongValue());
        user.setUsername(row.get("Username").getStringValue());
        user.setEmail(row.get("Email").getStringValue());
        user.setPassword(row.get("Password").getStringValue());
        user.setRole(row.get("Role").getStringValue());
        return user;
    }
}
