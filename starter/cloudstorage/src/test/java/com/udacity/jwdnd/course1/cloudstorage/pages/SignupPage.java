package com.udacity.jwdnd.course1.cloudstorage.pages;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignupPage {
  private final WebDriverWait wait;
  @FindBy(xpath = "/html/body/div/h1")
  public WebElement mainHeading;
  @FindBy(xpath = "//*[@id=\"signup-success\"]")
  public List<WebElement> successAlert;
  @FindBy(xpath = "//*[@id=\"signup-error\"]")
  public List<WebElement> errorAlert;
  @FindBy(xpath = "//*[@id=\"inputFirstName\"]")
  private WebElement firstNameField;
  @FindBy(xpath = "//*[@id=\"inputLastName\"]")
  private WebElement lastNameField;
  @FindBy(xpath = "//*[@id=\"inputUsername\"]")
  private WebElement usernameField;
  @FindBy(xpath = "//*[@id=\"inputPassword\"]")
  private WebElement passwordField;
  @FindBy(xpath = "/html/body/div/form/button")
  private WebElement signupButton;

  public SignupPage(WebDriver driver) {
    this.wait = new WebDriverWait(driver, 10);
    PageFactory.initElements(driver, this);
  }

  public void signup(
      String firstName,
      String lastName,
      String username,
      String password
  ) {
    firstNameField.sendKeys(firstName);
    lastNameField.sendKeys(lastName);
    usernameField.sendKeys(username);
    passwordField.sendKeys(password);
    signupButton.click();
  }

  public boolean signupStatus() {
    if (successAlert.size() != 0) {
      return successAlert.get(0).getText().contains("You successfully signed up!");
    }

    return false;
  }
}
