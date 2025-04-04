package com.fitnessTracker.fitnessTracker.controller;

import com.fitnessTracker.fitnessTracker.model.DietPlan;
import com.fitnessTracker.fitnessTracker.model.User;
import com.fitnessTracker.fitnessTracker.model.WeightLog;
import com.fitnessTracker.fitnessTracker.service.DietPlanService;
import com.fitnessTracker.fitnessTracker.service.UserService;
import com.fitnessTracker.fitnessTracker.service.WeightLogService;
import com.fitnessTracker.fitnessTracker.service.WorkoutPlanService; // Add this import
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final WeightLogService weightLogService;
    private final DietPlanService dietPlanService;
    private final WorkoutPlanService workoutPlanService; // Add this dependency

    @Autowired
    public UserController(UserService userService, WeightLogService weightLogService, DietPlanService dietPlanService, WorkoutPlanService workoutPlanService) {
        this.userService = userService;
        this.weightLogService = weightLogService;
        this.dietPlanService = dietPlanService;
        this.workoutPlanService = workoutPlanService; // Initialize this dependency
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user"); // Get user from session
        if (user == null) {
            return "redirect:/login"; // Redirect if not logged in
        }
        model.addAttribute("user", user);
        model.addAttribute("workoutPlan", user.getWorkoutPlan());
        model.addAttribute("dietPlan", user.getDietPlan());

        // Get weight logs for the user
        List<WeightLog> weightLogs = weightLogService.getWeightLogsForUser(user.getId());
        model.addAttribute("weightLogs", weightLogs);

        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateUserProfile(@ModelAttribute("user") User updatedUser,
                                    @RequestParam("newPassword") String newPassword, //Capture new password
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // IMPORTANT: Only update allowed fields.  DO NOT blindly update the entire user object.
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());//Email logic

        if (!newPassword.isEmpty()) {
            // Hash the password before saving (VERY IMPORTANT)
            user.setPassword(newPassword);//password encryption happens here
        }
        userService.validateEmailUnique(user);
        userService.updateUser(user); // Save the changes

        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/user/profile?success";

    }

    @GetMapping("/weightlog")
    public String weightLog(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        List<WeightLog> weightLogs = weightLogService.getWeightLogsForUser(user.getId());
        model.addAttribute("weightLogs", weightLogs);
        return "user/weightlog";
    }

    @PostMapping("/weightlog/add")
    public String addWeightLog(@RequestParam("weight") double weight, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "redirect:/login";
        }
        user.setWeight(weight);
        WeightLog weightLog = new WeightLog();
        weightLog.setWeight(weight);
        weightLog.setLogDate(LocalDate.now());
        weightLog.setUser(user);
        weightLogService.saveWeightLog(weightLog);
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Weight added successfully!");
        return "redirect:/user/dashboard?success";
    }
    @GetMapping("/workoutplan")
    public String viewWorkoutPlan(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("workoutPlan", user.getWorkoutPlan());
        return "user/workoutplan";
    }

    @GetMapping("/dietplan")
    public String viewDietPlan(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("dietPlan", user.getDietPlan());
        return "user/dietplan";
    }

    @GetMapping("/subscription")
    public String subscriptionPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "user/subscription";
    }
    @PostMapping("/subscription/update")
    public String updateSubscription(@RequestParam("subscriptionStatus") boolean subscriptionStatus, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        user.setSubscriptionStatus(subscriptionStatus);
        userService.updateUser(user);
        return "redirect:/user/subscription?success";
    }

}