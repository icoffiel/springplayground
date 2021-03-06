package com.nerdery.icoffiel.web.user.controller;

import com.lowagie.text.DocumentException;
import com.nerdery.icoffiel.service.PdfService;
import com.nerdery.icoffiel.web.rest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Controller for Users
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PdfService pdfService;

    @Autowired
    public UserController(UserService userService, PdfService pdfService) {
        this.userService = userService;
        this.pdfService = pdfService;
    }

    @RequestMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @RequestMapping("/pdf")
    public String listUsersPdf() throws IOException, DocumentException {
        pdfService.generateProposal();
        return "users";
    }
}
