package seedu.address.model.group;

import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Group in the address book.
 * Guarantees: contains at least one Person.
 */
public class Group {

    private final Name name;
    private UniquePersonList persons;

    public Group(String name, List<Person> persons) throws DuplicatePersonException {
        this.name = new Name(name);
        this.persons.setPersons(persons);
    }

    public Name getName() {
        return name;
    }

    public UniquePersonList getPersons() {
        return persons;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return otherGroup.getName().equals(this.getName())
                && otherGroup.getPersons().equals(this.getPersons());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, persons);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Group name: ")
                .append(getName())
                .append("\n\n")
                .append(" Members in this group: ");

        for(Person person : persons) {
            builder.append("\n")
                    .append(person.getName())
                    .append(" Phone: ")
                    .append(person.getPhone())
                    .append(" Email: ")
                    .append(person.getEmail())
                    .append(" Address: ")
                    .append(person.getAddress())
                    .append(" Tags: ");
            person.getTags().forEach(builder::append);
        }

        return builder.toString();
    }

}
