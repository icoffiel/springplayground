package com.nerdery.icoffiel.web.user.controller;

import com.lowagie.text.DocumentException;
import com.nerdery.icoffiel.service.PdfService;
import com.nerdery.icoffiel.views.xlsx.UserXlsxView;
import com.nerdery.icoffiel.web.rest.user.service.UserService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

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

    @RequestMapping("/excel")
    public ModelAndView listUsersExcel() {
        Map<String, Object> model = new HashedMap();
        model.put("users", userService.findAll());
        return new ModelAndView(new UserXlsxView(), model);
    }
}
