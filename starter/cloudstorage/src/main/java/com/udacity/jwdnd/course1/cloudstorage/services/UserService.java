package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import java.sql.SQLException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserMapper userMapper;
  private final HashService hashService;

  public UserService(UserMapper userMapper, HashService hashService) {
    this.userMapper = userMapper;
    this.hashService = hashService;
  }

  public boolean isUsernameAvailable(String userName) throws SQLException {
    return userMapper.checkUser(userName) == 1;
  }

  public User getUser(String userName) throws ArgNotFoundException, SQLException {
    if (!isUsernameAvailable(userName)) {
      throw new ArgNotFoundException("Username does not exist");
    }

    return userMapper.getUser(userName);
  }

  public int createUser(User user) throws ArgAlreadyExistsException, NoRowsAffectedException, SQLException {
    if (user == null) {
      throw new IllegalArgumentException("User data is empty");
    }

    if (isUsernameAvailable(user.getUsername())) {
      throw new ArgAlreadyExistsException("Username already exists");
    }

    String encodedSalt = hashService.generateEncodedSalt();
    String password = hashService.getHashedValue(user.getPassword(), encodedSalt);
    user.setUserId(null);
    user.setSalt(encodedSalt);
    user.setPassword(password);

    int rows = userMapper.addUser(user);
    if (rows < 1) {
      throw new NoRowsAffectedException(String.format("%d user(s) created", rows));
    }

    return user.getUserId();
  }

  public boolean checkPassword(String userName, String password) throws ArgNotFoundException, SQLException {
    if (!isUsernameAvailable(userName)) {
      throw new ArgNotFoundException("Username does not exist");
    }

    User user = userMapper.getUser(userName);
    String encodedSalt = user.getSalt();
    String hashedPassword = hashService.getHashedValue(password, encodedSalt);

    return hashedPassword.equals(user.getPassword());
  }
}
