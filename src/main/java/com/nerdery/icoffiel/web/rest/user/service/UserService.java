package com.nerdery.icoffiel.web.rest.user.service;

import com.nerdery.icoffiel.web.rest.user.model.User;
import com.nerdery.icoffiel.web.rest.user.model.UserDTO;
import com.nerdery.icoffiel.web.rest.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to retrieve Users from the database.
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(UserDTO userDTO) {
        log.debug("Attempting to create user with username: " + userDTO.getUsername());

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEnabled(true);
        user.setAuthorities(userDTO.getAuthorities());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.debug("Attempting to return all users");
        return userRepository.findAll();
    }

    public User findOneByUsername(String username) {
        log.debug("Finding user with username");
        return userRepository.findByUsername(username);
    }
}
