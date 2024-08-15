package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
  private final NoteMapper noteMapper;

  public NoteService(NoteMapper noteMapper) {
    this.noteMapper = noteMapper;
  }

  public boolean isNoteIdFound(int noteId, int userId) throws SQLException {
    return noteMapper.checkNote(noteId, userId) == 1;
  }

  public int createNote(Note note) throws ArgAlreadyExistsException, NoRowsAffectedException, SQLException {
    int rows = noteMapper.createNote(note);
    if (rows < 1) {
      throw new NoRowsAffectedException(String.format("%d note(s) created", rows));
    }

    return note.getNoteId();
  }

  public List<Note> getNotes(int userId) throws SQLException {
    return noteMapper.getNotes(userId);
  }

  public Note getNote(int noteId, int userId) throws ArgNotFoundException, SQLException {
    if (!isNoteIdFound(noteId, userId)) {
      throw new ArgNotFoundException(String.format("%s note not found", noteId));
    }

    return noteMapper.getNote(noteId, userId);
  }

  public int editNote(Note note) throws NoRowsAffectedException, SQLException {
    int rows = noteMapper.editNote(note);
    if (rows < 1) {
      throw new NoRowsAffectedException(String.format("%d note(s) modified", rows));
    }

    return rows;
  }

  public void deleteNote(int noteId, int userId) throws ArgNotFoundException, NoRowsAffectedException, SQLException {
    if (!isNoteIdFound(noteId, userId)) {
      throw new ArgNotFoundException(String.format("%s note not found", noteId));
    }

    if (!noteMapper.deleteNote(noteId, userId)) {
      throw new NoRowsAffectedException("Failed to delete note");
    }
  }
}
