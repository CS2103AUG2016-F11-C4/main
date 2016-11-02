package seedu.dailyplanner.logic.commands;

import seedu.dailyplanner.model.AddressBook;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Daily Planner has been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
    	model.getPreviousQuery().setQuery("clear");
        assert model != null;
        model.resetData(AddressBook.getEmptyAddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
