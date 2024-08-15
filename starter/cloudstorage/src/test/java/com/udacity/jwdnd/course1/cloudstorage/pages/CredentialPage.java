package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.dtos.CredentialDTO;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialPage extends HomePage {
  @FindBy(xpath = "//*[@id=\"nav-credentials\"]/button")
  public WebElement addButton;

  @FindBy(xpath = "//*[@id=\"credential-url\"]")
  public WebElement urlField;

  @FindBy(xpath = "//*[@id=\"credential-username\"]")
  public WebElement usernameField;

  @FindBy(xpath = "//*[@id=\"credential-password\"]")
  public WebElement passwordField;

  @FindBy(xpath = "//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]")
  public WebElement saveButton;

  @FindBy(xpath = "(//*[@id=\"credentialTable\"]/tbody/tr/th)[last()]")
  public WebElement lastUrlCell;

  @FindBy(xpath = "(//*[@id=\"credentialTable\"]/tbody/tr/td[2])[last()]")
  public WebElement lastUsernameCell;

  @FindBy(xpath = "(//*[@id=\"credentialTable\"]/tbody/tr/td[2])[last()]")
  public WebElement lastPasswordCell;

  @FindBy(xpath = "(//*[@id=\"credentialTable\"]/tbody/tr/td[1]/button)[last()]")
  public WebElement lastEditCellButton;

  @FindBy(xpath = "(//*[@id=\"credentialTable\"]/tbody/tr/td[1]/form/button)[last()]")
  public WebElement lastDeleteCellButton;


  @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[1]/form/button")
  public List<WebElement> deleteCellButtons;

  @FindBy(xpath = "//*[@id=\"credentialModal\"]/div/div/div[3]/button[1]")
  public WebElement closeDialogButton;

  public CredentialPage(WebDriver driver) {
    super(driver);
  }

  public CredentialDTO generateCredentials() {
    return new CredentialDTO(
        null,
        String.format("modify%d.duper.com", new Random().nextInt(101)),
        "super" + new Random().nextInt(101),
        null,
        null,
        null,
        "drive" + new Random().nextInt(101)
    );
  }

  public CredentialDTO getLastCredentials() {
    wait.until(ExpectedConditions.elementToBeClickable(lastUrlCell));
    wait.until(ExpectedConditions.elementToBeClickable(lastUsernameCell));
    wait.until(ExpectedConditions.elementToBeClickable(lastPasswordCell));

    String url = lastUrlCell.getText();
    String username = lastUsernameCell.getText();
    String encryptedPassword = lastPasswordCell.getText();

    wait.until(ExpectedConditions.elementToBeClickable(lastEditCellButton));
    lastEditCellButton.click();

    wait.until(ExpectedConditions.visibilityOf(passwordField));
    String decryptedPassword = passwordField.getText();
    decryptedPassword = decryptedPassword.isEmpty() ? passwordField.getAttribute("value") : decryptedPassword;

    wait.until(ExpectedConditions.elementToBeClickable(closeDialogButton));
    closeDialogButton.click();

    return new CredentialDTO(
        null,
        url,
        username,
        null,
        encryptedPassword,
        null,
        decryptedPassword
    );
  }

  public void createCredentials(CredentialDTO credential) {
    wait.until(ExpectedConditions.elementToBeClickable(addButton));
    addButton.click();
    wait.until(ExpectedConditions.elementToBeClickable(urlField));
    wait.until(ExpectedConditions.elementToBeClickable(usernameField));
    wait.until(ExpectedConditions.elementToBeClickable(passwordField));
    urlField.sendKeys(credential.getUrl());
    usernameField.sendKeys(credential.getUsername());
    passwordField.sendKeys(credential.getDecryptedPassword());
    wait.until(ExpectedConditions.elementToBeClickable(saveButton));
    saveButton.click();
  }

  public void editLastCredentials(CredentialDTO credential) {
    wait.until(ExpectedConditions.elementToBeClickable(lastEditCellButton));
    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.modal-backdrop.fade")));
    lastEditCellButton.click();
    wait.until(ExpectedConditions.elementToBeClickable(urlField));
    wait.until(ExpectedConditions.elementToBeClickable(usernameField));
    wait.until(ExpectedConditions.elementToBeClickable(passwordField));
    urlField.clear();
    urlField.sendKeys(credential.getUrl());
    usernameField.clear();
    usernameField.sendKeys(credential.getUsername());
    passwordField.clear();
    passwordField.sendKeys(credential.getDecryptedPassword());
    wait.until(ExpectedConditions.elementToBeClickable(saveButton));
    saveButton.click();
  }

  public int getLastCredentialIdx() {
    int lastIdx = deleteCellButtons.size() - 1;
    WebElement deleteButton = deleteCellButtons.get(lastIdx);
    wait.until(ExpectedConditions.elementToBeClickable(deleteButton));

    return lastIdx;
  }

  public int deleteLastCredential() {
    int lastIdx = deleteCellButtons.size() - 1;
    WebElement deleteButton = deleteCellButtons.get(lastIdx);
    wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
    deleteButton.click();

    return lastIdx;
  }
}
