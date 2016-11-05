package seedu.dailyplanner.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.transformation.FilteredList;
import seedu.dailyplanner.commons.core.ComponentManager;
import seedu.dailyplanner.commons.core.LogsCenter;
import seedu.dailyplanner.commons.core.UnmodifiableObservableList;
import seedu.dailyplanner.commons.events.model.AddressBookChangedEvent;
import seedu.dailyplanner.commons.util.StringUtil;
import seedu.dailyplanner.logic.commands.Command;
import seedu.dailyplanner.logic.commands.DeleteCommand;
import seedu.dailyplanner.history.HistoryManager;
import seedu.dailyplanner.model.task.ReadOnlyTask;
import seedu.dailyplanner.model.task.Task;
import seedu.dailyplanner.model.task.UniqueTaskList;
import seedu.dailyplanner.model.task.UniqueTaskList.PersonNotFoundException;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the address book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Task> filteredPersons;
    private final FilteredList<Task> pinnedTasks;
    private final HistoryManager history;
    private IntegerProperty lastTaskAddedIndex;

    /**
     * Initializes a ModelManager with the given AddressBook AddressBook and its
     * variables should not be null
     */
    public ModelManager(AddressBook src, UserPrefs userPrefs) {
	super();
	assert src != null;
	assert userPrefs != null;

	logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);

	addressBook = new AddressBook(src);
	filteredPersons = new FilteredList<>(addressBook.getPersons());
	pinnedTasks = new FilteredList<>(addressBook.getPinnedTasks());
	history = new HistoryManager();
	lastTaskAddedIndex = new SimpleIntegerProperty(0);
    }

    public ModelManager() {
	this(new AddressBook(), new UserPrefs());
    }

    public ModelManager(ReadOnlyAddressBook initialData, UserPrefs userPrefs) {
	addressBook = new AddressBook(initialData);
	filteredPersons = new FilteredList<>(addressBook.getPersons());
	pinnedTasks = new FilteredList<>(addressBook.getPinnedTasks());
	history = new HistoryManager();
    lastTaskAddedIndex =  new SimpleIntegerProperty(0);
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
	addressBook.resetData(newData);
	indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
	return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
	raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(ReadOnlyTask target) throws PersonNotFoundException {
	addressBook.removePerson(target);
	indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Task person) throws UniqueTaskList.DuplicatePersonException {
	addressBook.addPerson(person);
	setLastTaskAddedIndex(addressBook.indexOf(person));
	updateFilteredListToShowAll();
	indicateAddressBookChanged();
    }

    public synchronized void markTaskAsComplete(int targetIndex) throws PersonNotFoundException {
	addressBook.markTaskAsComplete(targetIndex);
	indicateAddressBookChanged();
    }
    
    @Override
    public void pinTask(int targetIndex) throws PersonNotFoundException {
	addressBook.pinTask(targetIndex);
	indicateAddressBookChanged();
    }
    
    @Override
    public void unpinTask(int targetIndex) throws PersonNotFoundException {
    	addressBook.unpinTask(targetIndex);
    	indicateAddressBookChanged();
    }

    // =========== Filtered Person List Accessors
    // ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredPersonList() {
	return new UnmodifiableObservableList<>(filteredPersons);
    }

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getPinnedTaskList() {
	return new UnmodifiableObservableList<>(pinnedTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
	filteredPersons.setPredicate(null);
    }

    @Override
    public void updateFilteredPersonList(Set<String> keywords) {
	updateFilteredPersonList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredPersonList(Expression expression) {
	filteredPersons.setPredicate(expression::satisfies);
    }

    @Override
    public void updateFilteredPersonListByDate(Set<String> keywords) {
	updateFilteredPersonList(new PredicateExpression(new DateQualifier(keywords)));
    }

    private void updateFilteredPersonListByDate(Expression expression) {
	filteredPersons.setPredicate(expression::satisfies);
    }

    @Override
    public void updateFilteredPersonListByCompletion(Set<String> keywords) {
	updateFilteredPersonList(new PredicateExpression(new CompletionQualifier(keywords)));
    }

    private void updateFilteredPersonListByCompletion(Expression expression) {
	filteredPersons.setPredicate(expression::satisfies);
    }
    
    @Override
    public int getLastTaskAddedIndex() {
        return lastTaskAddedIndex.get();
    }
    @Override
    public void setLastTaskAddedIndex(int index) {
        lastTaskAddedIndex.set(index);
    }
    
    @Override 
    public IntegerProperty getLastTaskAddedIndexProperty() {
        return lastTaskAddedIndex;
    }

    // ========== Inner classes/interfaces used for filtering
    // ==================================================

    interface Expression {
	boolean satisfies(ReadOnlyTask person);

	String toString();
    }

    private class PredicateExpression implements Expression {

	private final Qualifier qualifier;

	PredicateExpression(Qualifier qualifier) {
	    this.qualifier = qualifier;
	}

	@Override
	public boolean satisfies(ReadOnlyTask person) {
	    return qualifier.run(person);
	}

	@Override
	public String toString() {
	    return qualifier.toString();
	}
    }

    interface Qualifier {
	boolean run(ReadOnlyTask person);

	String toString();
    }

    private class CompletionQualifier implements Qualifier {
	private Set<String> completionKeywords;

	CompletionQualifier(Set<String> completionKeyword) {
	    this.completionKeywords = completionKeyword;
	}

	@Override
	public boolean run(ReadOnlyTask person) {
	    return completionKeywords.contains(person.getCompletion().toLowerCase());
	}

	@Override
	public String toString() {
	    return "completion=" + String.join(", ", completionKeywords);
	}
    }

    private class DateQualifier implements Qualifier {
	private Set<String> dateKeyWords;

	DateQualifier(Set<String> dateKeyWords) {
	    this.dateKeyWords = dateKeyWords;
	}

	@Override
	public boolean run(ReadOnlyTask person) {
	    return dateKeyWords.stream().filter(keyword -> StringUtil.withinDateRange(person.getPhone(), keyword))
		    .findAny().isPresent();
	}

	@Override
	public String toString() {
	    return "date=" + String.join(", ", dateKeyWords);
	}
    }

    private class NameQualifier implements Qualifier {
	private Set<String> nameKeyWords;

	NameQualifier(Set<String> nameKeyWords) {
	    this.nameKeyWords = nameKeyWords;
	}

	@Override
	public boolean run(ReadOnlyTask person) {
	    return nameKeyWords.stream()
		    .filter(keyword -> StringUtil.containsIgnoreCase(person.getName().fullName, keyword)).findAny()
		    .isPresent();
	}

	@Override
	public String toString() {
	    return "name=" + String.join(", ", nameKeyWords);
	}
    }

    @Override
    public HistoryManager getHistory() {

	return history;
    }

}
