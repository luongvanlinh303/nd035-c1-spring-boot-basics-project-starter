package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/credential")
public class CredentialsController {
  private final CredentialService credentialService;
  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(CredentialsController.class);

  public CredentialsController(CredentialService credentialService, UserService userService) {
    this.credentialService = credentialService;
    this.userService = userService;
  }

  @PostMapping("add")
  public String add(@ModelAttribute Credential credential, Authentication authentication, RedirectAttributes model) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      System.out.println("userId:" + userId);
      credential.setUserId(userId);
      credentialService.createCredential(credential);
    } catch (NoRowsAffectedException | ArgNotFoundException e) {
      model.addFlashAttribute("resultError", e.getMessage());
    } catch (SQLException e) {
      logger.error("Unexpected error", e);
      model.addFlashAttribute("resultError", "Unexpected error, please try again.");
    }

    return "redirect:/home/result";
  }

  @PostMapping("edit")
  public String edit(@ModelAttribute Credential credential, Authentication authentication, RedirectAttributes model) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      credential.setUserId(userId);
      credentialService.updateCredential(credential);
    } catch (NoRowsAffectedException | ArgNotFoundException e) {
      model.addFlashAttribute("resultError", e.getMessage());
    } catch (SQLException e) {
      logger.error("Unexpected error", e);
      model.addFlashAttribute("resultError", "Unexpected error, please try again.");
    }
    return "redirect:/home/result";
  }

  @PostMapping("delete")
  public String delete(@ModelAttribute("credentialId") Integer credentialId, Authentication authentication, RedirectAttributes model) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      credentialService.deleteCredential(credentialId, userId);
    } catch (ArgNotFoundException | NoRowsAffectedException e) {
      model.addFlashAttribute("resultError", e.getMessage());
    } catch (SQLException e) {
      logger.error("Unexpected error", e);
      model.addFlashAttribute("resultError", "Unexpected error, please try again.");
    }
    return "redirect:/home/result";
  }
}
