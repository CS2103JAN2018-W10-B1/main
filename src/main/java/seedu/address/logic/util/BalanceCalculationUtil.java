package seedu.address.logic.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import seedu.address.logic.commands.AddTransactionCommand;
import seedu.address.logic.commands.DeleteTransactionCommand;
import seedu.address.model.person.Balance;

import seedu.address.model.person.UniquePersonList;
import seedu.address.model.transaction.Amount;

/**
 * Contains utility methods used for calculating balances of Persons.
 */
public class BalanceCalculationUtil {

    private static final int NUMBER_OF_DECIMAL_PLACES = 2;

    /**
     * Returns the dept that a {@code payee} owes to the payer.
     */

    public static Balance calculatePayerDebt(String transactionType, Amount amount,
                                             UniquePersonList payees) {
        switch (transactionType) {
        case AddTransactionCommand.COMMAND_WORD:
            return calculateAddTransactionPayerDebt(amount, payees);
        case DeleteTransactionCommand.COMMAND_WORD:
            return calculateDeleteTransactionPayerDebt(amount, payees);
        default:
            return null;
        }
    }
    /**
     * Returns an updated balance for {@code payee}
     */
    public static Balance calculatePayeeDebt(String transactionType, Amount amount,
                                             UniquePersonList payees) {
        switch (transactionType) {
        case AddTransactionCommand.COMMAND_WORD:
            return calculateAddTransactionPayeeDebt(amount, payees);
        case DeleteTransactionCommand.COMMAND_WORD:
            return calculateDeleteTransactionPayeeDebt(amount, payees);
        default:
            return null;
        }
    }
    /**
     * Calculate new payee(s) balance after a new transaction is added
     */
    public static Balance calculateAddTransactionPayeeDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double dept = -Double.valueOf(amount.value) / numberOfInvolvedPersons;
        dept = round(dept, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(dept)));
    }
    /**
     * Calculate new payer balance a transaction is deleted
     */
    public static Balance calculateDeleteTransactionPayerDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double dept = -Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                / numberOfInvolvedPersons;
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(dept)));
    }
    /**
     * Calculate new payer balance after a new transaction is added
     */
    public static Balance calculateAddTransactionPayerDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double dept = Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                / numberOfInvolvedPersons;
        dept = round(dept, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(dept)));
    }
    /**
     * Calculate new payee(s) balance after a transaction is deleted
     */
    public static Balance calculateDeleteTransactionPayeeDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double dept = Double.valueOf(amount.value) / numberOfInvolvedPersons;
        dept = round(dept, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(dept)));
    }
    /**
     * Calculate the number of people involved in a transaction
     */
    public static int calculateNumberOfInvolvedPersons(UniquePersonList payees) {
        return payees.asObservableList().size() + 1;
    }
    private static double round(double value, int places) {
        BigDecimal amount = new BigDecimal(value);
        amount = amount.setScale(places, RoundingMode.HALF_UP);
        return amount.doubleValue();
    }

}


