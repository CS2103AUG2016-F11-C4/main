package seedu.address.testutil;

import seedu.dailyplanner.model.tag.UniqueTagList;
import seedu.dailyplanner.model.task.*;

/**
 * A mutable person object. For testing only.
 */
public class TestPerson implements ReadOnlyTask {

    private Name name;
    private EndTime address;
    private StartTime email;
    private Date phone;
    private UniqueTagList tags;

    public TestPerson() {
        tags = new UniqueTagList();
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setAddress(EndTime address) {
        this.address = address;
    }

    public void setEmail(StartTime email) {
        this.email = email;
    }

    public void setPhone(Date phone) {
        this.phone = phone;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Date getPhone() {
        return phone;
    }

    @Override
    public StartTime getEmail() {
        return email;
    }

    @Override
    public EndTime getAddress() {
        return address;
    }

    @Override
    public UniqueTagList getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getName().fullName + " ");
        sb.append("d/" + this.getPhone().value + " ");
        sb.append("s/" + this.getEmail().value + " ");
        sb.append("e/" + this.getAddress().value + " ");
        this.getTags().getInternalList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
        return sb.toString();
    }

    @Override
    public boolean isComplete() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void setDate(Date date) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setStartTime(StartTime time) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setEndTime(EndTime time) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void markAsComplete() {
	// TODO Auto-generated method stub
	
    }
}
