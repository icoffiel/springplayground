package com.nerdery.icoffiel.web.rest.jwttoken.repository;

import com.nerdery.icoffiel.web.rest.jwttoken.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for a {@link JwtToken} object.
 */
@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long>{
    JwtToken findOneByToken(String token);
}
