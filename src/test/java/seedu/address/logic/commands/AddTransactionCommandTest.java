package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.exceptions.TransactionNotFoundException;
import seedu.address.testutil.TransactionBuilder;
import seedu.address.testutil.TypicalTransactions;

//@@author ongkc
public class AddTransactionCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullTransaction_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddTransactionCommand(null);
    }

    @Test
    public void execute_transactionAcceptedByModel_addSuccessful() throws Exception {
        AddTransactionCommandTest.ModelStubAcceptingTransactionAdded modelStub =
                new AddTransactionCommandTest.ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = new TransactionBuilder().build();

        CommandResult commandResult = getAddTransactionCommand(validTransaction, modelStub).execute();
        assertEquals(String.format(AddTransactionCommand.MESSAGE_SUCCESS, validTransaction),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTransaction), modelStub.transactionsAdded);
    }


    @Test
    public void equals() {
        Transaction one = new TransactionBuilder().withPayees(TypicalTransactions.getTypicalPayees().get(3)).build();
        Transaction two = new TransactionBuilder().withPayees(TypicalTransactions.getTypicalPayees().get(4)).build();
        AddTransactionCommand addOneCommand = new AddTransactionCommand(one);
        AddTransactionCommand addTwoCommand = new AddTransactionCommand(two);

        // same object -> returns true
        assertTrue(addOneCommand.equals(addOneCommand));

        // same values -> returns true
        AddTransactionCommand addOneCommandCopy = new AddTransactionCommand(one);
        assertTrue(addOneCommand.equals(addOneCommandCopy));

        // different types -> returns false
        assertFalse(addOneCommand.equals(1));

        // null -> returns false
        assertFalse(addOneCommand.equals(null));

        // different Transaction -> returns false
        assertFalse(addOneCommand.equals(addTwoCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given transaction.
     */
    private AddTransactionCommand getAddTransactionCommand(Transaction transaction, Model model) {
        AddTransactionCommand command = new AddTransactionCommand(transaction);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public Person findPersonByName(Name name) throws PersonNotFoundException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public UniquePersonList getPayeesList(ArgumentMultimap argMultimap, Model model) throws PersonNotFoundException,
                IllegalValueException {
            return null;
        }

        @Override
        public void findPersonInTransaction(Name name) throws PersonNotFoundException {

        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public boolean findTransactionsWithPayer(Person person) throws TransactionNotFoundException {
            fail("This method should not be called.");
            return true;
        }

        @Override
        public boolean findTransactionsWithPayee(Person person) throws TransactionNotFoundException {
            fail("This method should not be called.");
            return true;
        }

        @Override
        public ObservableList<Transaction> getFilteredTransactionList() {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredTransactionList(Predicate<Transaction> predicate) {

        }

        @Override
        public void addTransaction(Transaction transaction) {}

        @Override
        public void deleteTransaction(Transaction transaction) throws TransactionNotFoundException {}
    }

    public class ModelStubImpl extends AddTransactionCommandTest.ModelStub { }


    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingTransactionAdded extends AddTransactionCommandTest.ModelStub {
        final ArrayList<Transaction> transactionsAdded = new ArrayList<>();

        @Override
        public void addTransaction(Transaction transaction) {
            requireNonNull(transaction);
            transactionsAdded.add(transaction);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
