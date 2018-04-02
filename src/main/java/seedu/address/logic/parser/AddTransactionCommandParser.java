package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddTransactionCommand.MESSAGE_NONEXISTENT_PERSON;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYEE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYER;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddTransactionCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.model.transaction.Transaction;
//@auother ongkc
/**
 * Parses input arguments and creates a new AddTransactionCommand object
 */
public class AddTransactionCommandParser implements Parser<AddTransactionCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddTransactionCommand
     * and returns an AddTransactionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTransactionCommand parse(String args, Model model) throws ParseException, CommandException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PAYER, PREFIX_AMOUNT, PREFIX_DESCRIPTION, PREFIX_PAYEE);

        if (!arePrefixesPresent(argMultimap, PREFIX_PAYER, PREFIX_AMOUNT, PREFIX_DESCRIPTION, PREFIX_PAYEE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddTransactionCommand.MESSAGE_USAGE));
        }

        try {
            Person payer = model.findPersonByName(ParserUtil.parseName(argMultimap.getValue(PREFIX_PAYER)).get());
            Amount amount = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT)).get();
            Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).get();
            UniquePersonList payees = model.getPayeesList(argMultimap, model);
            Date dateTime = Date.from(Instant.now(Clock.system(ZoneId.of("Asia/Singapore"))));

            Transaction transaction = new Transaction(payer, amount, description, dateTime, payees);

            return new AddTransactionCommand(transaction);
        } catch (PersonNotFoundException pnfe) {
            throw new CommandException(MESSAGE_NONEXISTENT_PERSON);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }



    //@@author
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

