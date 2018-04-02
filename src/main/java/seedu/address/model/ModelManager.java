package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.commands.DeleteTransactionCommand.MESSAGE_NONEXISTENT_PAYER_PAYEES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYEE;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddTransactionCommand;
import seedu.address.logic.commands.DeleteTransactionCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonFoundException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.exceptions.TransactionNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Transaction> filteredTransactions;


    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredTransactions = new FilteredList<>(this.addressBook.getTransactionList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(Person target) throws PersonNotFoundException, CommandException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Person person) throws DuplicatePersonException, PersonFoundException {
        if (findTransactionsWithPayer(person) & findTransactionsWithPayee(person)) {
            addressBook.addPerson(person);
        }
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }
    //@@author steven-jia
    @Override
    public Person findPersonByName(Name name) throws PersonNotFoundException {
        Set<Person> matchingPersons = addressBook.getPersonList()
                .stream()
                .filter(person -> person.getName().equals(name))
                .collect(Collectors.toSet());

        if (!matchingPersons.isEmpty()) {
            return matchingPersons.iterator().next();
        } else {
            throw new PersonNotFoundException();
        }
    }

    //@@author steven-jia
    public UniquePersonList getPayeesList(ArgumentMultimap argMultimap, Model model)
            throws PersonNotFoundException, IllegalValueException {
        UniquePersonList payees = new UniquePersonList();
        List<String> payeeNamesToAdd = argMultimap.getAllValues(PREFIX_PAYEE);

        if (!payeeNamesToAdd.isEmpty()) {
            for (String payeeName: payeeNamesToAdd) {
                payees.add(model.findPersonByName(ParserUtil.parseName(payeeName)));
            }
        }
        return payees;
    }
    public UniquePersonList getPayeesList(UniquePersonList transactionPayees)
            throws PersonNotFoundException {
        UniquePersonList payees = new UniquePersonList();

        for (Person payee: transactionPayees) {
            try {
                payees.add(findPersonByName(payee.getName()));
            } catch (PersonNotFoundException pnfe) {
                throw new PersonNotFoundException();
            } catch (DuplicatePersonException e) {
                e.printStackTrace();
            }
        }

        return payees;
    }

    @Override
    public void findPersonInTransaction(Name name) throws PersonNotFoundException {
        Set<Person> matchingPersons = addressBook.getPersonList()
                .stream()
                .filter(person -> person.getName().equals(name))
                .collect(Collectors.toSet());
        if (matchingPersons.isEmpty()) {
            throw new PersonNotFoundException();
        }
    }

    @Override
    public boolean findTransactionsWithPayer(Person person) throws PersonFoundException {
        Set<Transaction> matchingTransactions = addressBook.getTransactionList()
                .stream()
                .filter(transaction -> transaction.getPayer().equals(person))
                .collect(Collectors.toSet());

        if (matchingTransactions.isEmpty()) {
            return true;
        } else {
            throw new PersonFoundException();
        }
    }

    @Override
    public boolean findTransactionsWithPayee(Person person) throws PersonFoundException {
        Set<Transaction> matchingTransactions = addressBook.getTransactionList()
                .stream()
                .filter(transaction -> transaction.getPayees().contains(person))
                .collect(Collectors.toSet());

        if (matchingTransactions.isEmpty()) {
            return true;
        } else {
            throw new PersonFoundException();
        }
    }

    //@@author ongkc
    /**
     * Returns an unmodifiable view of the list of {@code Transaction}
     */
    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return FXCollections.unmodifiableObservableList(filteredTransactions);
    }

    //@@author ongkc
    @Override
    public void addTransaction(Transaction transaction) {
        String transactionType = AddTransactionCommand.COMMAND_WORD;
        addressBook.addTransaction(transaction);
        addressBook.updatePayerAndPayeesDebt(transactionType , transaction.getAmount(),
                transaction.getPayer(), transaction.getPayees());
        updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        updateFilteredPersonList(PREDICATE_SHOW_NO_PERSON);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    //@@author phmignot
    @Override
    public void deleteTransaction(Transaction target) throws TransactionNotFoundException, CommandException {
        String transactionType = DeleteTransactionCommand.COMMAND_WORD;
        try {
            addressBook.updatePayerAndPayeesDebt(transactionType , target.getAmount(),
                    findPersonByName(target.getPayer().getName()), getPayeesList(target.getPayees()));
        }   catch (PersonNotFoundException e) {
            throw new CommandException(MESSAGE_NONEXISTENT_PAYER_PAYEES);
        }
        addressBook.removeTransaction(target);
        updateFilteredPersonList(PREDICATE_SHOW_NO_PERSON);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }
    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }


    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //@author ongkc
    @Override
    public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        requireNonNull(predicate);
        filteredTransactions.setPredicate(predicate);
    }


    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredPersons.equals(other.filteredPersons);
    }

}
