package seedu.dailyplanner.model;

import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import seedu.dailyplanner.commons.core.UnmodifiableObservableList;
import seedu.dailyplanner.logic.commands.Command;
import seedu.dailyplanner.history.HistoryManager;
import seedu.dailyplanner.model.task.ReadOnlyTask;
import seedu.dailyplanner.model.task.Task;
import seedu.dailyplanner.model.task.UniqueTaskList;
import seedu.dailyplanner.model.task.UniqueTaskList.PersonNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    

	/** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();
    
    HistoryManager getHistory();

    /** Deletes the given person. */
    void deletePerson(ReadOnlyTask target) throws UniqueTaskList.PersonNotFoundException;

    /** Adds the given person */
    void addPerson(Task person) throws UniqueTaskList.DuplicatePersonException;

    /** Marks the given task as complete  */
    void markTaskAsComplete(ReadOnlyTask taskToComplete) throws PersonNotFoundException;
    
    /** Pins the given task. */
    void pinTask(int targetIndex) throws PersonNotFoundException;

    /** Unpins the given task. */
	void unpinTask(int i) throws PersonNotFoundException;
    
    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredPersonList();
    
    /** Returns the list of pinned task as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyTask> getPinnedTaskList();

    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered person list to filter by the given keywords*/
    void updateFilteredPersonList(Set<String> keywords);
    
    /** Updates the filter of the filtered person list to filter by the given date*/
    void updateFilteredPersonListByDate(Set<String> keywords);

    /** Updates the filter of the filtered person list to show only completed tasks*/
	void updateFilteredPersonListByCompletion(Set<String> keywords);

    /** Returns the index of the last task that was added to the task list */
    public int getLastTaskAddedIndex();
    
    /** Sets the stored index of the last task added */
    public void setLastTaskAddedIndex(int index);
    
    /** Returns the last task added index as the property itself */
    public IntegerProperty getLastTaskAddedIndexProperty();
    
    /** Returns last shown date command */
    public String getLastShowDate();
    
    /** Sets last shown date given by show command*/
    public void setLastShowDate(String showInput);
    
    /** Returns the StringProperty holding the last shown date command */
    public StringProperty getLastShowDateProperty();

    /** Resets the pinboard to an empty pinboard */
	public void resetPinBoard();
	
	/** Uncompletes task with given index in taskList */
    public void uncompleteTask(int indexInTaskList);
	
}
