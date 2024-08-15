package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NoteMapper {

  @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
  List<Note> getNotes(int userId) throws SQLException;

  @Select("SELECT * FROM NOTES WHERE noteid = #{noteId} AND userid = #{userId}")
  Note getNote(int noteId, int userId) throws SQLException;

  @Select("SELECT COUNT(1) AS count FROM NOTES WHERE noteid = #{noteId} AND userid = #{userId}")
  long checkNote(int noteId, int userId) throws SQLException;

  @Insert("INSERT INTO NOTES (notetitle, notedescription, userid)" +
      "VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
  @Options(useGeneratedKeys = true, keyColumn = "noteid", keyProperty = "noteId")
  int createNote(Note note) throws SQLException;

  @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription}" +
      "WHERE noteid = #{noteId} AND userid = #{userId}")
  int editNote(Note note) throws SQLException;

  @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND userid = #{userId}")
  boolean deleteNote(int noteId, int userId) throws SQLException;
}
