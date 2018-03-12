package seedu.address.model.transaction;

import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Represent a transaction
 */
public class Transaction {

    private final Name payer;
    private final Amount amount;
    private final Description description;
    private final Name payee;

    public Transaction(Name payer, Amount amount, Description description, Name payee) {
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payee = payee;
    }

    public Name getPayer() {
        return payer;
    }

    public Amount getAmount() {
        return amount;
    }

    public Description getDescription() {
        return description;
    }

    public Name getPayee() {
        return payee;
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
        return otherTransaction.getPayer().equals(this.getPayer())
                && otherTransaction.getAmount().equals(this.getAmount())
                && otherTransaction.getDescription().equals(this.getDescription())
                && otherTransaction.getPayee().equals(this.getPayee());
    }

    @Override
    public int hashCode() {
        return Objects.hash(payer, amount, description, payee);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Transaction paid by: ")
                .append(getPayer().toString())
                .append("\n Amount: ")
                .append(getAmount().toString())
                .append("\n Description: ")
                .append(getDescription().toString())
                .append("\n Payees: ")
                .append(getPayee().toString());
        return builder.toString();
    }

}
