package seedu.address.testutil;

import seedu.dailyplanner.commons.exceptions.IllegalValueException;
import seedu.dailyplanner.model.tag.Tag;
import seedu.dailyplanner.model.task.*;

/**
 *
 */
public class PersonBuilder {

    private TestPerson person;

    public PersonBuilder() {
        this.person = new TestPerson();
    }

    public PersonBuilder withName(String name) throws IllegalValueException {
        this.person.setName(new Name(name));
        return this;
    }

    public PersonBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            person.getTags().add(new Tag(tag));
        }
        return this;
    }

    public PersonBuilder withAddress(String address) throws IllegalValueException {

        this.person.setEndTime(new EndTime(address));
        return this;
    }

    public PersonBuilder withPhone(String phone) throws IllegalValueException {
        this.person.setDate(new Date1(phone));
        return this;
    }

    public PersonBuilder withEmail(String email) throws IllegalValueException {
        this.person.setStartTime(new StartTime(email));
        return this;
    }
    
    public PersonBuilder withCompletion(boolean completion) throws IllegalValueException {
        return this;
    }
  


    public TestPerson build() {
        return this.person;
    }

}
