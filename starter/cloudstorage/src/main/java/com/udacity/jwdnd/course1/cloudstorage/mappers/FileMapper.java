package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.BlobTypeHandler;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface FileMapper {
  @Results({
      @Result(property = "fileData", column = "filedata", jdbcType = JdbcType.BLOB, typeHandler = BlobTypeHandler.class)
  })
  @Select("SELECT * FROM FILES WHERE userid = #{userId}")
  List<File> getFiles(int userId) throws SQLException;

  @Results({
      @Result(property = "fileData", column = "filedata", jdbcType = JdbcType.BLOB, typeHandler = BlobTypeHandler.class)
  })
  @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
  File getFile(String fileName, int userId) throws SQLException;

  @Select("SELECT COUNT(1) AS count FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
  @Results({@Result(column = "count", javaType = Long.class)})
  long checkFile(String fileName, int userId) throws SQLException;

  @Insert("INSERT INTO FILES (fileid, filename, contenttype, filesize, userid, filedata)" +
      "VALUES (#{fileId}, #{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
  @Options(useGeneratedKeys = true, keyColumn = "fileid", keyProperty = "fileId")
  int createFile(File file) throws SQLException;

  @Delete("DELETE FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
  boolean deleteFile(String fileName, int userId) throws SQLException;
}
