package com.udacity.jwdnd.course1.cloudstorage.dtos;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;

public class CredentialDTO extends Credential {
  private String decryptedPassword;

  public CredentialDTO(
      Integer credentialId,
      String url,
      String username,
      String key,
      String password,
      Integer userid,
      String decryptedPassword
  ) {
    super(credentialId, url, username, key, password, userid);
    this.decryptedPassword = decryptedPassword;
  }

  public String getDecryptedPassword() {
    return decryptedPassword;
  }

  public void setDecryptedPassword(String decryptedPassword) {
    this.decryptedPassword = decryptedPassword;
  }
}
