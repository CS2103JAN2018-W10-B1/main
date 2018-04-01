package seedu.address.model;

import java.util.HashMap;

import seedu.address.model.person.Balance;
import seedu.address.model.person.Person;

/**
 * DebtsList of a Person, contains every person matched with the amount is owed or owes.
 */
public class DebtsList extends HashMap<Person, Balance> {
    public DebtsList() {
        super();
    }

    /**
     * Updates the debt with a person. If the person haven't debt before, the person
     * is added to the HashMap.
     * @param person that owes or is owed money.
     * @param dept to add to the old dept.
     */
    public void updateDept(Person person, Balance dept) {
        if (!this.containsKey(person)) {
            this.put(person, new Balance("0.00"));
        }
        Balance oldDebts = this.get(person);
        this.replace(person, oldDebts.add(dept));
    }

    /**
     * Displays the content of DebtsList in the terminal.
     */
    public void display() {
        System.out.print("dl =");
        this.forEach(((person, balance) -> System.out.print(person.getName().fullName
            + " : " + balance.toString())));
    }
}
