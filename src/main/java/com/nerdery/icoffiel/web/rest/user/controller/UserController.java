package com.nerdery.icoffiel.web.rest.user.controller;

import com.nerdery.icoffiel.web.rest.user.model.User;
import com.nerdery.icoffiel.web.rest.user.model.UserDTO;
import com.nerdery.icoffiel.web.rest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling user requests.
 */
@RestController
@RequestMapping("/rest/users")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(UserDTO user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }
}
