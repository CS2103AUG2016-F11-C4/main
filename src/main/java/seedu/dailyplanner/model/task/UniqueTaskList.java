package seedu.dailyplanner.model.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.dailyplanner.commons.exceptions.DuplicateDataException;
import seedu.dailyplanner.commons.util.CollectionUtil;

import java.util.*;

/**
 * A list of persons that enforces uniqueness between its elements and does not
 * allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

	/**
	 * Signals that an operation would have violated the 'no duplicates'
	 * property of the list.
	 */
	public static class DuplicatePersonException extends DuplicateDataException {
		protected DuplicatePersonException() {
			super("Operation would result in duplicate persons");
		}
	}

	/**
	 * Signals that an operation targeting a specified person in the list would
	 * fail because there is no such matching person in the list.
	 */
	public static class PersonNotFoundException extends Exception {
	}

	private final ObservableList<Task> internalList = FXCollections.observableArrayList();
	private final ObservableList<Task> pinnedList = FXCollections.observableArrayList();
	
	/**
	 * Constructs empty PersonList.
	 */
	public UniqueTaskList() {
	}

	/**
	 * Returns true if the list contains an equivalent person as the given
	 * argument.
	 */
	public boolean contains(ReadOnlyTask toCheck) {
		assert toCheck != null;
		return internalList.contains(toCheck);
	}
	
	/** Returns index of task given in argument */
	public int getIndexOf(Task task) {
	    return internalList.indexOf(task);
	}

	/**
	 * Adds a person to the list.
	 *
	 * @throws DuplicatePersonException
	 *             if the person to add is a duplicate of an existing person in
	 *             the list.
	 */
	public void add(Task toAdd) throws DuplicatePersonException {
		assert toAdd != null;
		if (contains(toAdd)) {
			throw new DuplicatePersonException();
		}
		internalList.add(toAdd);
		FXCollections.sort(internalList);
	}

	/**
	 * Removes the equivalent person from the list.
	 *
	 * @throws PersonNotFoundException
	 *             if no such person could be found in the list.
	 */
	public boolean remove(ReadOnlyTask toRemove) throws PersonNotFoundException {
		assert toRemove != null;
		final boolean personFoundAndDeleted = internalList.remove(toRemove);

		if (!personFoundAndDeleted) {
			throw new PersonNotFoundException();
		}
		FXCollections.sort(internalList);
		return personFoundAndDeleted;
	}
	
	public void resetPinBoard() {
		pinnedList.clear();
	}

	/**
	 * Marks the task indicated by taskIndex as complete
	 * @return 
	 * 
	 * @throws PersonNotFoundException
	 *             if the task index is invalid.
	 */
	public void complete(ReadOnlyTask key) throws PersonNotFoundException {

		final int completedTaskIndex = internalList.indexOf(key);
		final Task completedTask = internalList.get(completedTaskIndex);
		completedTask.markAsComplete();
		internalList.set(completedTaskIndex, completedTask);
	}

	/**
	 * Pins the task indicated by taskIndex
	 * 
	 * @return
	 */
	public void pin(ReadOnlyTask taskToPin) throws PersonNotFoundException {
		final int pinnedTaskIndex = internalList.indexOf(taskToPin);
		final Task pinnedTask = internalList.get(pinnedTaskIndex);
		pinnedTask.pin();
		internalList.set(pinnedTaskIndex, pinnedTask);
		pinnedList.add(pinnedTask);
	}

	public void unpin(int targetIndex) {
		final Task taskToUnpin = pinnedList.get(targetIndex);
		taskToUnpin.unpin();
		int internalListIndex = internalList.indexOf(taskToUnpin);
		internalList.set(internalListIndex, taskToUnpin);
		pinnedList.remove(taskToUnpin);

	}

	public ObservableList<Task> getInternalList() {
		return internalList;
	}

	public ObservableList<Task> getInternalPinnedList() {
		for (Task t : internalList) {
			if (t.isPinned()) {
				pinnedList.add(t);
			}
		}
		return pinnedList;
	}

	@Override
	public Iterator<Task> iterator() {
		return internalList.iterator();
	}

	@Override
	public boolean equals(Object other) {
		return other == this // short circuit if same object
				|| (other instanceof UniqueTaskList // instanceof handles nulls
						&& this.internalList.equals(((UniqueTaskList) other).internalList));
	}

	@Override
	public int hashCode() {
		return internalList.hashCode();
	}

}
