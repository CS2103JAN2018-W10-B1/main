package seedu.address.model;

import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.exceptions.TransactionNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = new Predicate<Person>() {
        @Override
        public boolean test(Person person) {
            return true;
        }
    };

    /** {@code Predicate} that always evaluate to true */
    Predicate<Transaction> PREDICATE_SHOW_ALL_TRANSACTIONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /** Deletes the given person. */
    void deletePerson(Person target) throws PersonNotFoundException;

    /** Adds the given person */
    void addPerson(Person person) throws DuplicatePersonException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException;

    //@@author steven-jia
    /** Finds a person by name */
    Person findPersonByName(Name name) throws PersonNotFoundException;

    //@@author
    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    //@@author steven-jia
    /** Returns a set of transactions that have {@code person} as the payer */
    Set<Transaction> findTransactionsWithPayer(Person person) throws TransactionNotFoundException;

    /** Returns a set of transactions that have {@code person} as a payee */
    Set<Transaction> findTransactionsWithPayee(Person person) throws TransactionNotFoundException;

    //@@author
    /** Returns an unmodifiable view of the filtered transaction list */
    ObservableList<Transaction> getFilteredTransactionList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTransactionList(Predicate<Transaction> predicate);

    void addTransaction(Transaction transaction) throws PersonNotFoundException;

    //@@author phmignot
    /** Deletes the given person. */
    void deleteTransaction(Transaction target) throws TransactionNotFoundException;
}
