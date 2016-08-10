package com.nerdery.icoffiel.web.rest.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Transfer object for a JWT.
 */
@Data
@AllArgsConstructor
public class JwtDto {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
