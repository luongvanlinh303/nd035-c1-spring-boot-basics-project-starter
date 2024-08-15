package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class NotePage extends HomePage {
  @FindBy(xpath = "//*[@id=\"nav-notes\"]/button")
  private WebElement addButton;

  @FindBy(xpath = "//*[@id=\"note-title\"]")
  private WebElement titleField;

  @FindBy(xpath = "//*[@id=\"note-description\"]")
  private WebElement descriptionField;

  @FindBy(xpath = "//*[@id=\"noteModal\"]/div/div/div[3]/button[2]")
  private WebElement saveButton;

  @FindBy(xpath = "(//*[@id=\"noteTable\"]/tbody/tr/th)[last()]")
  private WebElement lastTitleCell;

  @FindBy(xpath = "(//*[@id=\"noteTable\"]/tbody/tr/td[2])[last()]")
  private WebElement lastDescriptionCell;

  @FindBy(xpath = "(//*[@id=\"noteTable\"]/tbody/tr/td[1]/button)[last()]")
  private WebElement lastEditCellButton;

  @FindBy(xpath = "//*[@id=\"noteTable\"]/tbody/tr/td[1]/form/button)[last()]")
  private WebElement lastDeleteCellButton;

  @FindBy(xpath = "//*[@id=\"noteTable\"]/tbody/tr/td[1]/form/button")
  private List<WebElement> deleteButtons;

  public NotePage(WebDriver driver) {
    super(driver);
  }

  public Note generateNote() {
    return new Note(
        null,
        "Note Edit #" + new Random().nextInt(101),
        "Note Edit #" + new Random().nextInt(101),
        null
    );
  }

  public Note getLastNote() {
    wait.until(ExpectedConditions.elementToBeClickable(lastTitleCell));
    wait.until(ExpectedConditions.elementToBeClickable(lastDescriptionCell));

    return new Note(
        null,
        lastTitleCell.getText(),
        lastDescriptionCell.getText(),
        null
    );
  }

  public void addNote(Note note) {
    wait.until(ExpectedConditions.elementToBeClickable(addButton));
    addButton.click();
    wait.until(ExpectedConditions.elementToBeClickable(titleField));
    wait.until(ExpectedConditions.elementToBeClickable(descriptionField));
    titleField.sendKeys(note.getNoteTitle());
    descriptionField.sendKeys(note.getNoteDescription());
    wait.until(ExpectedConditions.elementToBeClickable(saveButton));
    saveButton.click();
  }

  public void editNote(Note note) {
    wait.until(ExpectedConditions.elementToBeClickable(lastEditCellButton));
    lastEditCellButton.click();
    wait.until(ExpectedConditions.elementToBeClickable(titleField));
    wait.until(ExpectedConditions.elementToBeClickable(descriptionField));
    titleField.clear();
    titleField.sendKeys(note.getNoteTitle());
    descriptionField.clear();
    descriptionField.sendKeys(note.getNoteDescription());
    wait.until(ExpectedConditions.elementToBeClickable(saveButton));
    saveButton.click();
  }

  public int getLastNoteIdx() {
    int lastIdx = deleteButtons.size() - 1;
    WebElement deleteButton = deleteButtons.get(lastIdx);
    wait.until(ExpectedConditions.elementToBeClickable(deleteButton));

    return lastIdx;
  }

  public int deleteLastNote() {
    int lastIdx = deleteButtons.size() - 1;
    WebElement deleteButton = deleteButtons.get(lastIdx);
    wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
    deleteButton.click();

    return lastIdx;
  }
}
