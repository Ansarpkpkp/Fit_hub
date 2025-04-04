package com.fitnessTracker.fitnessTracker.controller;

import com.fitnessTracker.fitnessTracker.model.Admin;
import com.fitnessTracker.fitnessTracker.model.User;
import com.fitnessTracker.fitnessTracker.service.AdminService;
import com.fitnessTracker.fitnessTracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public AuthController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String index() {
        return "index"; // Load index.html as the default page
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String email,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {

        Optional<User> userOptional = userService.findUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) { // Direct password comparison (Not Secure)
                session.setAttribute("user", user);
                return "redirect:/user/dashboard";
            }
        }

        Optional<Admin> adminOptional = adminService.findAdminByEmail(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            if (admin.getPassword().equals(password)) { // Direct password comparison (Not Secure)
                session.setAttribute("admin", admin);
                return "redirect:/admin/dashboard";
            }
        }

        model.addAttribute("errorMessage", "Invalid username or password.");
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (!userService.isEmailUnique(user.getEmail())) {
            model.addAttribute("errorMessage", "Email already exists");
            return "register";
        }
        userService.registerUser(user); // Store password as plain text (Not Secure)
        return "redirect:/login?registerSuccess";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
