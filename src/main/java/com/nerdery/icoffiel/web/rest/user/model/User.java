package com.nerdery.icoffiel.web.rest.user.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * User entity mapping to the user table.
 */
@Data
@Entity
@Table(name = "users")
public class User  {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities;
}