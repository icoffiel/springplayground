package com.nerdery.icoffiel.web.rest.user.repository;

import com.nerdery.icoffiel.web.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for a {@link User} object
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
