//@@author steven-jia
package seedu.address.model.transaction;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import seedu.address.logic.util.BalanceCalculationUtil;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Represents a Transaction in SmartSplit.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Transaction extends BalanceCalculationUtil {
    private static Integer lastTransactionId = 0;
    private final Integer id;
    private final Date dateTime;
    private final Person payer;
    private final Amount amount;
    private final Description description;
    private final UniquePersonList payees;

    public Transaction(Person payer, Amount amount, Description description, Date dateTime, UniquePersonList payees) {
        this.dateTime = dateTime;
        this.id = lastTransactionId++;
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payees = payees;

    }

    public Transaction(Person payer, Amount amount, Description description, Date dateTime, Set<Person> payeesToAdd) {
        UniquePersonList payees = new UniquePersonList();
        for (Person p: payeesToAdd) {
            try {
                payees.add(p);
            } catch (DuplicatePersonException e) {
                System.out.println("Duplicate person" + p.getName() + " not added to list of payees");
            }
        }

        this.dateTime = dateTime;
        this.id = lastTransactionId++;
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payees = payees;
    }

    public Integer getId() {
        return id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public Person getPayer() {
        return payer;
    }

    public Amount getAmount() {
        return amount;
    }

    public Description getDescription() {
        return description;
    }

    public UniquePersonList getPayees() {
        return payees;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Transaction)) {
            return false;
        }

        Transaction otherTransaction = (Transaction) other;
        return otherTransaction.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, payer, amount, description, payees);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Transaction id: ")
                .append(getId())
                .append("\n Created on: ")
                .append(getDateTime())
                .append("\n Transaction paid by: ")
                .append(getPayer().getName())
                .append("\n Amount: ")
                .append(getAmount().toString())
                .append("\r\n Description: ")
                .append(getDescription().toString())
                .append("\n Payees: ")
                .append(getPayees().asObservableList().toString());
        return builder.toString();
    }

    /**
     * Tests if a person is implied in this transaction.
     * @param person to check his implication.
     * @return true if the person is the payer or one of the payee;
     * false otherwise.
     */
    public boolean isImplied(Person person) {
        return (payer.equals(person) || payees.contains(person));
    }
}
