package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.core.Router;
import com.udacity.jwdnd.course1.cloudstorage.dtos.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.pages.CredentialPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {
  @LocalServerPort
  private int port;

  private WebDriver driver;
  private Router router;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.firefoxdriver().setup();
  }

  @BeforeEach
  public void beforeEach() {
    this.driver = new FirefoxDriver();
    router = Router.getInstance(driver, port);
  }

  @AfterEach
  public void afterEach() {
    if (this.driver != null) {
      driver.quit();
    }
  }

  @Test
  public void addCredential() {
    router.defaultLogin(true);
    CredentialPage credentialPage = router.credentialPage();
    CredentialDTO cred = credentialPage.generateCredentials();
    credentialPage.createCredentials(cred);
    Assertions.assertTrue(credentialPage.resultStatus(), credentialPage.resultBody.getText());

    credentialPage = router.credentialPage();
    CredentialDTO newCred = credentialPage.getLastCredentials();

    Assertions.assertEquals(cred.getUrl(), newCred.getUrl());
    Assertions.assertEquals(cred.getUsername(), newCred.getUsername());
    Assertions.assertNotEquals(cred.getDecryptedPassword(), newCred.getPassword());
    Assertions.assertEquals(cred.getDecryptedPassword(), newCred.getDecryptedPassword());
  }

  @Test
  public void editCredential() {
    router.defaultLogin(true);
    CredentialPage credentialPage = router.credentialPage();
    CredentialDTO oldCred = credentialPage.getLastCredentials();
    CredentialDTO cred = credentialPage.generateCredentials();
    credentialPage.editLastCredentials(cred);
    Assertions.assertTrue(credentialPage.resultStatus(), credentialPage.resultBody.getText());
    credentialPage.backToHome();
    credentialPage = router.credentialPage();
    CredentialDTO newCred = credentialPage.getLastCredentials();

    Assertions.assertEquals(cred.getUrl(), newCred.getUrl());
    Assertions.assertEquals(cred.getUsername(), newCred.getUsername());
    Assertions.assertNotEquals(cred.getDecryptedPassword(), newCred.getPassword());
    Assertions.assertEquals(cred.getDecryptedPassword(), newCred.getDecryptedPassword());
    Assertions.assertNotEquals(oldCred.getUrl(), newCred.getUrl());
    Assertions.assertNotEquals(oldCred.getUsername(), newCred.getUsername());
    Assertions.assertNotEquals(oldCred.getPassword(), newCred.getPassword());
    Assertions.assertNotEquals(oldCred.getDecryptedPassword(), newCred.getDecryptedPassword());
  }

  @Test
  public void deleteCredential() {
    router.defaultLogin(true);
    CredentialPage credentialPage = router.credentialPage();
    int deletedIdx = credentialPage.deleteLastCredential();
    Assertions.assertTrue(credentialPage.resultStatus(), credentialPage.resultBody.getText());
    Assertions.assertEquals(deletedIdx, 0, "Credentials count is not changed");
  }
}
