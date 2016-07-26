package com.nerdery.icoffiel.web.rest.user.model;

import lombok.Data;

import java.util.List;

/**
 * Transfer Object for Users.
 */
@Data
public class UserDTO {
    private String username;
    private String password;
    private List<Authority> authorities;
}
