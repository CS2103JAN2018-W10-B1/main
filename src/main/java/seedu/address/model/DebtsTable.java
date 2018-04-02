package seedu.address.model;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.util.BalanceCalculationUtil.calculatePayeeDebt;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Balance;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;

/**
 * Stores all the debts between the persons from the addressBook.
 */
public class DebtsTable extends HashMap<Person, DebtsList> {

    private final ObservableList internalList = FXCollections.observableArrayList();

    public DebtsTable() {
        super();
    }

    /**
     * Updates the DebtsTable due to a transaction
     * payeeDebt is a negative {@Code Balance} value, because the payee owes money.
     * payerDebt is a positive {@Code Balance} value, because the payer is owed.
     * @param transaction to register the table.
     */
    public void updateDebts(String typeOfTransaction, Transaction transaction) {
        Person payer = transaction.getPayer();
        if (!this.containsKey(payer)) {
            this.add(payer);
            System.out.println("Adding payer " + payer.getName().fullName);
        }
        DebtsList payerDebtsLit = this.get(payer);
        Balance payerDebt = calculatePayeeDebt(typeOfTransaction, transaction.getAmount(), transaction.getPayees());
        Balance payeeDebt = payerDebt.getInverse();
        for (Person payee: transaction.getPayees()) {
            if (!this.containsKey(payee)) {
                this.add(payee);
                System.out.println("Adding payee " + payee.getName().fullName);
            }
            DebtsList payeeDebtsLit = this.get(payee);
            payerDebtsLit.updateDept(payee, payeeDebt);
            payeeDebtsLit.updateDept(payer, payerDebt);
        }
    }

    public void add(Person personToAdd) {
        this.putIfAbsent(personToAdd, new DebtsList());
    }

    public ObservableList asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    public void setDebtsTable(DebtsTable debtsTable) {
        requireAllNonNull(debtsTable);
        internalList.setAll(debtsTable);
    }
    /**
     * Displays the content of the Debts Table in the terminal.
     */
    public void display() {
        System.out.println("DEBTS TABLE : ");
        this.forEach(((person, debtsList) -> {
            System.out.println(person.getName().fullName + " : ");
            debtsList.display();
            System.out.println();
        }));
    }


}

