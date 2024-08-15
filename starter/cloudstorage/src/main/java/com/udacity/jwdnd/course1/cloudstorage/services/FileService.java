package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FileService {
  private final FileMapper fileMapper;

  public FileService(FileMapper fileMapper) {
    this.fileMapper = fileMapper;
  }

  public boolean isFileNameFound(String fileName, int userId) throws SQLException {
    return fileMapper.checkFile(fileName, userId) == 1;
  }

  public int saveFile(File file) throws ArgAlreadyExistsException, NoRowsAffectedException, SQLException {
    if (isFileNameFound(file.getFileName(), file.getUserId())) {
      throw new ArgAlreadyExistsException(String.format("%s file already exists", file.getFileName()));
    }

    int rows = fileMapper.createFile(file);
    if (rows < 1) {
      throw new NoRowsAffectedException(String.format("%d file(s) created", rows));
    }

    return file.getFileId();
  }

  public List<File> getFiles(int userId) throws SQLException {
    return fileMapper.getFiles(userId);
  }

  public File getFile(String fileName, int userId) throws ArgNotFoundException, SQLException {
    if (!isFileNameFound(fileName, userId)) {
      throw new ArgNotFoundException(String.format("%s file not found", fileName));
    }

    return fileMapper.getFile(fileName, userId);
  }

  public void deleteFile(String fileName, int userId) throws ArgNotFoundException, NoRowsAffectedException, SQLException {
    if (!isFileNameFound(fileName, userId)) {
      throw new ArgNotFoundException(String.format("%s file not found", fileName));
    }

    if (!fileMapper.deleteFile(fileName, userId)) {
      throw new NoRowsAffectedException("Failed to delete file");
    }
  }
}
