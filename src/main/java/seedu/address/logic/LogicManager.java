package seedu.address.logic;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_NO_PERSON;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.DebtsList;
import seedu.address.model.DebtsTable;
import seedu.address.model.Model;
import seedu.address.model.person.Balance;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.TransactionContainsPersonPredicate;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final AddressBookParser addressBookParser;
    private final UndoRedoStack undoRedoStack;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        addressBookParser = new AddressBookParser(model);
        undoRedoStack = new UndoRedoStack();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = addressBookParser.parseCommand(commandText);
            command.setData(model, history, undoRedoStack);
            CommandResult result = command.execute();
            undoRedoStack.push(command);
            return result;
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return model.getFilteredTransactionList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }

    @Override
    public void updateFilteredTransactionList() {
        model.updateFilteredTransactionList(Model.PREDICATE_SHOW_ALL_TRANSACTIONS);
    }

    @Override
    public void updateFilteredTransactionList(Person person) {
        TransactionContainsPersonPredicate predicate = new TransactionContainsPersonPredicate(person);
        model.updateFilteredTransactionList(predicate);
    }
    //@@author ongkc
    @Override
    public void updateFilteredPersonList(Person person) {
        DebtsTable debtsTable = model.getAddressBook().getDebtsTable();
        DebtsList debtsList = debtsTable.get(person);
        resetDebt();
        updateDebt(debtsList);
        //        model.updateFilteredPersonList(PREDICATE_SHOW_NO_PERSON);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

    }
    @Override
    public void updateFilteredPersonList() {
        resetDebt();
        model.updateFilteredPersonList(PREDICATE_SHOW_NO_PERSON);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }
    /**
     * Update amount of debt owed to other people
     */
    private void updateDebt(DebtsList debtsList) {
        ObservableList<Person> persons = model.getAddressBook().getPersonList();
        for (Person person: persons) {
            if (debtsList.get(person) != null) {
                person.setDebt(debtsList.get(person));
            }
        }
    }
    //@@author
    /**
     * Reset amount of debt owed to other people
     */
    private void resetDebt() {
        ObservableList<Person> persons = model.getAddressBook().getPersonList();
        for (Person person: persons) {
            person.setDebt(new Balance("0.00"));
        }
    }
}
