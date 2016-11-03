package seedu.dailyplanner.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.dailyplanner.model.task.ReadOnlyTask;

public class PersonCard extends UiPart {

	private static final String FXML = "PersonListCard.fxml";

	@FXML
	private HBox cardPane;
	@FXML
	private Label name;
	@FXML
	private Label id;
	@FXML
	private Label phone;
	@FXML
	private Label endDate;
	@FXML
	private Label address;
	@FXML
	private Label email;
	@FXML
	private Label tags;
	@FXML
	private Label isComplete;

	private ReadOnlyTask person;
	private int displayedIndex;

	public PersonCard() {

	}

	public static PersonCard load(ReadOnlyTask task, int displayedIndex) {
		PersonCard card = new PersonCard();
		card.person = task;
		card.displayedIndex = displayedIndex;
		return UiPartLoader.loadUiPart(card);
	}

	@FXML
	public void initialize() {
		name.setText(person.getName().fullName);
		id.setText(displayedIndex + ". ");
		phone.setText(person.getPhone().value);
		endDate.setText(person.getPhone().getEndDate());
		address.setText(person.getAddress().value);
		email.setText(person.getEmail().value);
		tags.setText(person.tagsString());
		if (person.getCompletion().equals("COMPLETE")) {
			isComplete.setText(person.getCompletion());
		} else {
			isComplete.setText("");
		}
	}

	public HBox getLayout() {
		return cardPane;
	}

	@Override
	public void setNode(Node node) {
		cardPane = (HBox) node;
	}

	@Override
	public String getFxmlPath() {
		return FXML;
	}
}
