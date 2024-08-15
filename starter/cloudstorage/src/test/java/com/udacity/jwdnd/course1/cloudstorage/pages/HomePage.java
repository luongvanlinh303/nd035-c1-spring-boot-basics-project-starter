package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
  @FindBy(xpath = "//*[@id=\"result-body\"]")
  public WebElement resultBody;
  protected WebDriverWait wait;
  @FindBy(xpath = "//*[@id=\"logoutDiv\"]/form/button")
  private WebElement logoutButton;

  @FindBy(xpath = "/html/body/div/div[2]/div/span/a")
  private WebElement backLink;

  @FindBy(xpath = "//*[@id=\"result-head\"]")
  private WebElement resultHeading;

  public HomePage(WebDriver driver) {
    PageFactory.initElements(driver, this);
    wait = new WebDriverWait(driver, 10);
  }

  public void logout() {
    logoutButton.click();
  }

  public void backToHome() {
    backLink.click();
  }

  public boolean resultStatus() {
    wait.until(ExpectedConditions.visibilityOf(resultHeading));

    return resultHeading.getText().equals("Success");
  }
}
