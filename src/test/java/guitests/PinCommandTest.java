package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.testplanner.testutil.TestTask;

public class PinCommandTest extends DailyPlannerGuiTest {

	@Test
	public void pin() {

		TestTask[] currentList = td.getTypicalTasks();
		TestTask taskToPin = td.CS2103_Project;
		assertPinSuccess("pin 1", taskToPin);

	}

	private void assertPinSuccess(String command, TestTask taskToPin) {

		commandBox.runCommand(command);

		// confirm there is now 1 task in the pinned list
		assertEquals(pinnedListPanel.getNumberOfPeople(), 1);
	}
}
