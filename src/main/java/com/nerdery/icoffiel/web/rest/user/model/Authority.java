package com.nerdery.icoffiel.web.rest.user.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Authority model mapping to the authority table.
 */
@Data
@Entity
public class Authority {
    @Id
    private Long id;
    private String authority;
}
