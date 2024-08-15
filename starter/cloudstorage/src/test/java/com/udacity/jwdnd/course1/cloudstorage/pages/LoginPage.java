package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

  @FindBy(xpath = "/html/body/div/h1")
  public WebElement mainHeading;

  @FindBy(xpath = "//*[@id=\"inputUsername\"]")
  private WebElement usernameField;

  @FindBy(xpath = "//*[@id=\"inputPassword\"]")
  private WebElement passwordField;

  @FindBy(xpath = "/html/body/div/form/div[1]")
  private WebElement resultAlert;

  @FindBy(xpath = "/html/body/div/form/button")
  private WebElement loginButton;

  public LoginPage(WebDriver driver) {
    PageFactory.initElements(driver, this);
  }

  public void login(String username, String password) {
    usernameField.sendKeys(username);
    passwordField.sendKeys(password);
    loginButton.click();
  }
}
