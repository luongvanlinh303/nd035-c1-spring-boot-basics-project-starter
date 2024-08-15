package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {
  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(SignupController.class);

  public SignupController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public String signupView() {
    return "signup";
  }

  @PostMapping
  public String signupUser(@ModelAttribute User user, RedirectAttributes redirectModel, Model model) {
    String signupErrorMessage = null;
    boolean signupSuccess = false;
    try {
      int userId = userService.createUser(user);
      model.addAttribute("newUserId", userId);
      if (userId <= 0) {
        signupErrorMessage = "Unexpected error, please try again.";
      } else {
        signupSuccess = true;
        redirectModel.addFlashAttribute("signupSuccess", signupSuccess);
        return "redirect:login";
      }
    } catch (ArgAlreadyExistsException e) {
      signupErrorMessage = e.getMessage();
      logger.error(e.getMessage());
    } catch (NoRowsAffectedException | SQLException e) {
      signupErrorMessage = "Unable to create user, please try again.";
      logger.error(e.getMessage());
    } finally {
      model.addAttribute("signupSuccess", signupSuccess);
      model.addAttribute("signupErrorMessage", signupErrorMessage);
    }

    return "signup";
  }
}
