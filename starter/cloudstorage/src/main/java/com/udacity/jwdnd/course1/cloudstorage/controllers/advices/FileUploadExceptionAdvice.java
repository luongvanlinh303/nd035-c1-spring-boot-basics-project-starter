package com.udacity.jwdnd.course1.cloudstorage.controllers.advices;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice {
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public String handleMaxSizeException(RedirectAttributes model) {
    model.addFlashAttribute("resultError", String.format("File should not exceed %d/100 MB", 5));
    return "redirect:/home/result";
  }
}
