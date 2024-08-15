package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialMapper {

  @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
  List<Credential> getCredentials(int userId) throws SQLException;

  @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
  Credential getCredential(int credentialId, int userId) throws SQLException;

  @Select("SELECT COUNT(1) AS count FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
  long checkCredential(int credentialId, int userId) throws SQLException;

  @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid)" +
      "VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
  @Options(useGeneratedKeys = true, keyColumn = "credentialid", keyProperty = "credentialId")
  int createCredential(Credential credential) throws SQLException;

  @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password}" +
      "WHERE credentialid = #{credentialId} AND userid = #{userId}")
  int editCredential(Credential credential);

  @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
  boolean deleteCredential(int credentialId, int userId) throws SQLException;
}
