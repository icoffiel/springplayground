package com.nerdery.icoffiel.web.rest.jwttoken.model;


import com.nerdery.icoffiel.web.rest.user.model.User;
import lombok.Data;

import javax.persistence.*;

/**
 * JWT Token entity mapping to the refresh_tokens table.
 */
@Data
@Entity
@Table(name = "refresh_tokens")
public class JwtToken {
    @Id
    @GeneratedValue
    Long id;
    String token;
    Boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
