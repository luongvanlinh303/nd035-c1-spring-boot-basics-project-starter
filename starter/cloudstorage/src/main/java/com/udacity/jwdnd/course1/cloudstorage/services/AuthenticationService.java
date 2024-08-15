package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.InvalidUserNameException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticationProvider {

  private final UserService userService;

  public AuthenticationService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String userName = authentication.getName();
    String password = authentication.getCredentials().toString();

    try {
      if (userService.checkPassword(userName, password)) {
        return new UsernamePasswordAuthenticationToken(userName, password, new ArrayList<>());
      }
    } catch (ArgNotFoundException | SQLException e) {
      throw new InvalidUserNameException(e.getMessage());
    }

    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
