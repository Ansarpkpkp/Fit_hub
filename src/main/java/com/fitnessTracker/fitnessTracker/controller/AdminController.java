package com.fitnessTracker.fitnessTracker.controller;

import com.fitnessTracker.fitnessTracker.model.DietPlan;
import com.fitnessTracker.fitnessTracker.model.User;
import com.fitnessTracker.fitnessTracker.model.WorkoutPlan;
import com.fitnessTracker.fitnessTracker.service.DietPlanService;
import com.fitnessTracker.fitnessTracker.service.UserService;
import com.fitnessTracker.fitnessTracker.service.WorkoutPlanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String ADMIN_SESSION_KEY = "admin";

    private final UserService userService;
    private final WorkoutPlanService workoutPlanService;
    private final DietPlanService dietPlanService;

    @Autowired
    public AdminController(UserService userService,
                           WorkoutPlanService workoutPlanService,
                           DietPlanService dietPlanService) {
        this.userService = userService;
        this.workoutPlanService = workoutPlanService;
        this.dietPlanService = dietPlanService;
    }

    // ========== SESSION MANAGEMENT ==========
    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute(ADMIN_SESSION_KEY) != null;
    }

    // ========== DASHBOARD ==========
    @GetMapping("/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";

        }
        return "admin/dashboard";
    }

    // ========== USER MANAGEMENT ==========
    @GetMapping("/users")
    public String userList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            return "admin/user_list";
        } catch (Exception e) {
            logger.error("Error loading user list", e);
            model.addAttribute("errorMessage", "Error loading users");
            return "admin/user_list";
        }
    }

    @GetMapping("/user/{id}/details")
    public String userDetails(@PathVariable Long id,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            Optional<User> userOptional = userService.findUserById(id);
            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return "redirect:/admin/users";
            }

            User user = userOptional.get();
            List<WorkoutPlan> workoutPlans = workoutPlanService.getAllWorkoutPlans();
            List<DietPlan> dietPlans = dietPlanService.getAllDietPlans();

            model.addAttribute("user", user);
            model.addAttribute("workoutPlans", workoutPlans);
            model.addAttribute("dietPlans", dietPlans);

            return "admin/user_details";

        } catch (Exception e) {
            logger.error("Error loading user details for ID: {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading user details");
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/user/{id}/updatePlans")
    public String updateUserPlans(@PathVariable Long id,
                                  @RequestParam(required = false) Long workoutPlanId,
                                  @RequestParam(required = false) Long dietPlanId,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            Optional<User> userOptional = userService.findUserById(id);
            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return "redirect:/admin/users";
            }

            User user = userOptional.get();

            // Update workout plan
            user.setWorkoutPlan(workoutPlanId != null ?
                    workoutPlanService.getWorkoutPlanById(workoutPlanId) : null);

            // Update diet plan
            user.setDietPlan(dietPlanId != null ?
                    dietPlanService.getDietPlanById(dietPlanId) : null);

            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User plans updated successfully");

        } catch (Exception e) {
            logger.error("Error updating plans for user ID: {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user plans");
        }

        return "redirect:/admin/user/" + id + "/details";
    }

    @PostMapping("/user/{id}/toggleSubscription")
    public String toggleSubscription(@PathVariable Long id,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            Optional<User> userOptional = userService.findUserById(id);
            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return "redirect:/admin/users";
            }

            User user = userOptional.get();
            user.setSubscriptionStatus(!user.isSubscriptionStatus());
            userService.updateUser(user);

            String status = user.isSubscriptionStatus() ? "activated" : "deactivated";
            redirectAttributes.addFlashAttribute("successMessage",
                    "Subscription " + status + " for " + user.getFirstName() + " " + user.getLastName());

        } catch (Exception e) {
            logger.error("Error toggling subscription for user ID: {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating subscription");
        }

        return "redirect:/admin/users";
    }

    // ========== WORKOUT PLAN MANAGEMENT ==========
    @GetMapping("/workoutplans")
    public String workoutPlanList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            List<WorkoutPlan> workoutPlans = workoutPlanService.getAllWorkoutPlans();
            model.addAttribute("workoutPlans", workoutPlans);
            return "admin/workout_plan_list";
        } catch (Exception e) {
            logger.error("Error loading workout plans", e);
            model.addAttribute("errorMessage", "Error loading workout plans");
            return "admin/workout_plan_list";
        }
    }

    @GetMapping("/workoutplan/create")
    public String createWorkoutPlanForm(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("workoutPlan", new WorkoutPlan());
        return "admin/workout_plan_form";
    }

    @PostMapping("/workoutplan/create")
    public String createWorkoutPlan(@Valid @ModelAttribute("workoutPlan") WorkoutPlan workoutPlan,
                                    BindingResult bindingResult,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            return "admin/workout_plan_form";
        }

        try {
            workoutPlanService.createWorkoutPlan(workoutPlan);
            redirectAttributes.addFlashAttribute("successMessage", "Workout plan created successfully");
            return "redirect:/admin/workoutplans";
        } catch (Exception e) {
            logger.error("Error creating workout plan", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating workout plan");
            return "admin/workout_plan_form";
        }
    }

    @GetMapping("/workoutplan/edit/{id}")
    public String editWorkoutPlanForm(@PathVariable Long id,
                                      Model model,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
            if (workoutPlan == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Workout plan not found");
                return "redirect:/admin/workoutplans";
            }
            model.addAttribute("workoutPlan", workoutPlan);
            return "admin/workout_plan_form";
        } catch (Exception e) {
            logger.error("Error loading workout plan for editing", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading workout plan");
            return "redirect:/admin/workoutplans";
        }
    }

    @PostMapping("/workoutplan/edit/{id}")
    public String updateWorkoutPlan(@PathVariable Long id,
                                    @Valid @ModelAttribute("workoutPlan") WorkoutPlan workoutPlan,
                                    BindingResult bindingResult,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            return "admin/workout_plan_form";
        }

        try {
            workoutPlan.setId(id);
            workoutPlanService.updateWorkoutPlan(workoutPlan);
            redirectAttributes.addFlashAttribute("successMessage", "Workout plan updated successfully");
            return "redirect:/admin/workoutplans";
        } catch (Exception e) {
            logger.error("Error updating workout plan", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating workout plan");
            return "admin/workout_plan_form";
        }
    }

    @PostMapping("/workoutplan/delete/{id}")
    public String deleteWorkoutPlan(@PathVariable Long id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            workoutPlanService.deleteWorkoutPlan(id);
            redirectAttributes.addFlashAttribute("successMessage", "Workout plan deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting workout plan", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting workout plan");
        }
        return "redirect:/admin/workoutplans";
    }

    // ========== DIET PLAN MANAGEMENT ==========
    @GetMapping("/dietplans")
    public String dietPlanList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            List<DietPlan> dietPlans = dietPlanService.getAllDietPlans();
            model.addAttribute("dietPlans", dietPlans);
            return "admin/diet_plan_list";
        } catch (Exception e) {
            logger.error("Error loading diet plans", e);
            model.addAttribute("errorMessage", "Error loading diet plans");
            return "admin/diet_plan_list";
        }
    }

    @GetMapping("/dietplan/create")
    public String createDietPlanForm(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("dietPlan", new DietPlan());
        return "admin/diet_plan_form";
    }

    @PostMapping("/dietplan/create")
    public String createDietPlan(@Valid @ModelAttribute("dietPlan") DietPlan dietPlan,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            return "admin/diet_plan_form";
        }

        try {
            dietPlanService.createDietPlan(dietPlan);
            redirectAttributes.addFlashAttribute("successMessage", "Diet plan created successfully");
            return "redirect:/admin/dietplans";
        } catch (Exception e) {
            logger.error("Error creating diet plan", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating diet plan");
            return "admin/diet_plan_form";
        }
    }

    @GetMapping("/dietplan/edit/{id}")
    public String editDietPlanForm(@PathVariable Long id,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            DietPlan dietPlan = dietPlanService.getDietPlanById(id);
            if (dietPlan == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Diet plan not found");
                return "redirect:/admin/dietplans";
            }
            model.addAttribute("dietPlan", dietPlan);
            return "admin/diet_plan_form";
        } catch (Exception e) {
            logger.error("Error loading diet plan for editing", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading diet plan");
            return "redirect:/admin/dietplans";
        }
    }

    @PostMapping("/dietplan/edit/{id}")
    public String updateDietPlan(@PathVariable Long id,
                                 @Valid @ModelAttribute("dietPlan") DietPlan dietPlan,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            return "admin/diet_plan_form";
        }

        try {
            dietPlan.setId(id);
            dietPlanService.updateDietPlan(dietPlan);
            redirectAttributes.addFlashAttribute("successMessage", "Diet plan updated successfully");
            return "redirect:/admin/dietplans";
        } catch (Exception e) {
            logger.error("Error updating diet plan", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating diet plan");
            return "admin/diet_plan_form";
        }
    }

    @PostMapping("/dietplan/delete/{id}")
    public String deleteDietPlan(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            dietPlanService.deleteDietPlan(id);
            redirectAttributes.addFlashAttribute("successMessage", "Diet plan deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting diet plan", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting diet plan");
        }
        return "redirect:/admin/dietplans";
    }}

