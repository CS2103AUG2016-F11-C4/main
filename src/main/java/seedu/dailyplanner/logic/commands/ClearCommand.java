package seedu.dailyplanner.logic.commands;

import seedu.dailyplanner.model.DailyPlanner;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Daily Planner has been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(DailyPlanner.getEmptyAddressBook());
        model.resetPinBoard();
        model.setLastTaskAddedIndex(0);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
