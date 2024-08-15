package com.udacity.jwdnd.course1.cloudstorage.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidUserNameException extends AuthenticationException {
  public InvalidUserNameException(String msg) {
    super(msg);
  }
}
