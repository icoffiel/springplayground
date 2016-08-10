package com.nerdery.icoffiel.web.rest.jwt.model;

import lombok.Data;

/**
 * Transfer object for Login.
 */
@Data
public class LoginDto {
    private String username;
    private String password;
}
