package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/file")
public class FilesController {
  private final FileService fileService;
  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(FilesController.class);

  public FilesController(FileService fileService, UserService userService) {
    this.fileService = fileService;
    this.userService = userService;
  }

  @PostMapping("upload")
  public String upload(
      @RequestParam("fileUpload") MultipartFile file,
      Authentication authentication,
      RedirectAttributes model
  ) {
    String uploadErrorMessage = null;
    try {
      if (file.getSize() == 0 || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
        throw new ArgNotFoundException("Uploaded file is empty");
      }
      if (file.getSize() / 1024 / 1024 > 5) {
        throw new NoRowsAffectedException("File exceeded limit 5 MB");
      }
      logger.info(String.format("Uploading file %s [%d bytes]", file.getOriginalFilename(), file.getSize()));
      int userId = userService.getUser(authentication.getName()).getUserId();
      File fileModel = new File(null, file.getOriginalFilename(), file.getContentType(),
          String.format("%d KB", file.getSize() / 1024), userId, file.getInputStream().readAllBytes());
      fileService.saveFile(fileModel);
    } catch (ArgNotFoundException | ArgAlreadyExistsException | NoRowsAffectedException e) {
      uploadErrorMessage = e.getMessage();
    } catch (IOException | SQLException e) {
      uploadErrorMessage = "Unexpected error, please try again.";
      logger.error("File Upload Error", e);
    }
    model.addFlashAttribute("resultError", uploadErrorMessage);

    return "redirect:/home/result";
  }

  @PostMapping("view")
  public ResponseEntity<StreamingResponseBody> view(
      @ModelAttribute("fileName") String fileName,
      Authentication authentication,
      RedirectAttributes model
  ) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      File file = fileService.getFile(fileName, userId);
      StreamingResponseBody responseBody = outputStream -> outputStream.write(file.getFileData());
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s", fileName))
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(responseBody);
    } catch (ArgNotFoundException e) {
      logger.error(e.getMessage(), e);
      model.addFlashAttribute("resultError", e.getMessage());
    } catch (Exception e) {
      logger.error("Unexpected error", e);
      model.addFlashAttribute("resultError", "Unexpected error, please try again.");
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/home/result"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @PostMapping("delete")
  public String delete(
      @ModelAttribute("fileName") String fileName,
      Authentication authentication,
      RedirectAttributes model
  ) {
    try {
      int userId = userService.getUser(authentication.getName()).getUserId();
      fileService.deleteFile(fileName, userId);
    } catch (ArgNotFoundException | NoRowsAffectedException e) {
      logger.error("Unable to delete file", e);
      model.addFlashAttribute("resultError", e.getMessage());
    } catch (SQLException e) {
      logger.error("Unexpected error", e);
      model.addFlashAttribute("resultError", "Unexpected error, please try again.");
    }

    return "redirect:/home/result";
  }
}
