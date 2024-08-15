package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import java.sql.SQLException;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
  @Select("SELECT * FROM USERS WHERE username = #{username}")
  User getUser(String userName) throws SQLException;

  @Select("SELECT COUNT(1) AS count FROM USERS WHERE username = #{username}")
  @Results({@Result(column = "count", javaType = Long.class)})
  long checkUser(String userName) throws SQLException;

  @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname)" +
      "VALUES (#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
  @Options(useGeneratedKeys = true, keyColumn = "userid", keyProperty = "userId")
  int addUser(User user) throws SQLException;
}
