package com.harri.training1.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;

    private String username;

    private String email;

    private String password;

    private String role;
}
