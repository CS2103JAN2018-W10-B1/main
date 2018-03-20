package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.model.person.Address;
//@authoer ongkc
/**
 * Represents a Transaction's description in the address book.
 */
public class Description {
    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Transaction description can take any values, and it should not be blank";
    /*
     * Description must not be only space or "" (a blank string).
     */
    public static final String DESCRIPTION_VALIDATION_REGEX = "^(\\s|\\S)*(\\S)+(\\s|\\S)*$";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param description A valid description.
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        this.value = description;
    }

    /**
     * Returns true if a given string is a valid person email.
     */
    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Address // instanceof handles nulls
                && this.value.equals(((Address) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }



}

