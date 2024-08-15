package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.core.Router;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTests {

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
  void getUnauthorizedPage() {
    router.homePage();
    String defaultRedirectUrl = "http://localhost:" + this.port + "/login";
    Assertions.assertEquals(defaultRedirectUrl, driver.getCurrentUrl());
  }

  @Test
  @Order(2)
  void getLoginPage() {
    LoginPage loginPage = router.loginPage();
    Assertions.assertEquals("Login", driver.getTitle());
    Assertions.assertEquals("Login", loginPage.mainHeading.getText());
  }

  @Test
  void getSignupPage() {
    driver.get("http://localhost:" + this.port + "/signup");
    SignupPage signupPage = router.signupPage();
    Assertions.assertEquals("Sign Up", driver.getTitle());
    Assertions.assertEquals("Sign Up", signupPage.mainHeading.getText());
  }

  @Test
  void SignupLoginLogoutUnauthorizedAccess() {
    getSignupPage();
    SignupPage signupPage = router.signupPage();
    User user = new User(null, "test_account1", null,
        "testPa$$w0rd1", "Mostafa", "Elsheikh");
    signupPage.signup(
        user.getFirstName(),
        user.getLastName(),
        user.getUsername(),
        user.getPassword()
    );
    Assertions.assertTrue(signupPage.signupStatus(),
        signupPage.errorAlert.size() != 0 ? signupPage.errorAlert.get(0).getText() : "Alert not found");
    LoginPage loginPage = router.loginPage();
    loginPage.login(
        user.getUsername(),
        user.getPassword()
    );
    HomePage homePage = router.homePage();
    homePage.logout();

    String defaultRedirectUrl = "http://localhost:" + this.port + "/login";
    Assertions.assertEquals(defaultRedirectUrl, driver.getCurrentUrl());
  }
}
