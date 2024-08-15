package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
  private final UserService userService;
  private final FileService fileService;
  private final NoteService noteService;
  private final CredentialService credentialService;
  private final Logger logger = LoggerFactory.getLogger(HomeController.class);

  public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService) {
    this.userService = userService;
    this.fileService = fileService;
    this.noteService = noteService;
    this.credentialService = credentialService;
  }

  @GetMapping
  public String homeView(Model model, Authentication authentication) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      model.addAttribute("files", fileService.getFiles(userId));
      model.addAttribute("notes", noteService.getNotes(userId));
      model.addAttribute("credentials", credentialService.getCredentials(userId));
    } catch (ArgNotFoundException e) {
      return "redirect:/login";
    } catch (SQLException e) {
      logger.error("Unexpected error", e);
      return "redirect:/login";
    }

    return "home";
  }

  @GetMapping("result")
  public String resultView() {
    return "result";
  }
}
