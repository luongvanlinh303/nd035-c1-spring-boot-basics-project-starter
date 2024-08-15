package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
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
@RequestMapping("/home/note")
public class NotesController {
  private final NoteService noteService;
  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(NotesController.class);

  public NotesController(NoteService noteService, UserService userService) {
    this.noteService = noteService;
    this.userService = userService;
  }

  @PostMapping("add")
  public String add(@ModelAttribute Note note, Authentication authentication, RedirectAttributes model) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      note.setUserId(userId);
      noteService.createNote(note);
    } catch (ArgAlreadyExistsException | NoRowsAffectedException | ArgNotFoundException e) {
      model.addFlashAttribute("resultError", e.getMessage());
    } catch (SQLException e) {
      logger.error("Unexpected error", e);
      model.addFlashAttribute("resultError", "Unexpected error, please try again.");
    }

    return "redirect:/home/result";
  }

  @PostMapping("edit")
  public String edit(@ModelAttribute Note note, Authentication authentication, RedirectAttributes model) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      note.setUserId(userId);
      noteService.editNote(note);
    } catch (NoRowsAffectedException | ArgNotFoundException e) {
      model.addFlashAttribute("resultError", e.getMessage());
    } catch (SQLException e) {
      logger.error("Unexpected error", e);
      model.addFlashAttribute("resultError", "Unexpected error, please try again.");
    }
    return "redirect:/home/result";
  }

  @PostMapping("delete")
  public String delete(@ModelAttribute("noteId") Integer noteId, Authentication authentication, RedirectAttributes model) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      noteService.deleteNote(noteId, userId);
    } catch (ArgNotFoundException | NoRowsAffectedException e) {
      model.addFlashAttribute("resultError", e.getMessage());
    } catch (SQLException e) {
      logger.error("Unexpected error", e);
      model.addFlashAttribute("resultError", "Unexpected error, please try again.");
    }
    return "redirect:/home/result";
  }
}
