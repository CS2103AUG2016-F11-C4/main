package seedu.dailyplanner.logic.parser;

import static seedu.dailyplanner.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.dailyplanner.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.dailyplanner.commons.exceptions.IllegalValueException;
import seedu.dailyplanner.commons.util.StringUtil;
import seedu.dailyplanner.logic.commands.*;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern PERSON_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<name>[^/]+)"
                    + " (?<isPhonePrivate>p?)d/(?<date>[^/]+)"
                    + " (?<isEmailPrivate>p?)st/(?<starttime>[^/]+)"
                    + " (?<isAddressPrivate>p?)et/(?<endtime>[^/]+)"
                    + "(?<tagArguments>(?: t/[^/]+)*)"); // variable number of tags

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Parses arguments in the context of the add person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        System.out.println(args);
        String taskName = "",date = "",startTime = "",endTime = "",isRecurring = "";
        HashMap<String,String> mapArgs = parseAdd(args.trim());
        
        //If arguments are in hashmap, pass them to addCommand, if not pass them as empty string
        
        if (mapArgs.containsKey("taskName")) {
            taskName = mapArgs.get("taskName");
        }
        if (mapArgs.containsKey("date")) {
            date = mapArgs.get("date");
        } else {
            date = "today";
        }
        if (mapArgs.containsKey("startTime")) {
            startTime = mapArgs.get("startTime");
        }
        if (mapArgs.containsKey("endTime")) {
            endTime = mapArgs.get("endTime");
        }
        if (mapArgs.containsKey("isRecurring")) {
            isRecurring = mapArgs.get("isRecurring");
        }
        
        Set<String> emptySet = new HashSet<String>();
        
        
        try {
            return new AddCommand(
                    taskName,
                    date,
                    startTime,
                    endTime,
                    emptySet
                    //getTagsFromArgs(matcher.group("tagArguments")
            );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    
    /**
     * Parses the arguments given by the user in the add command and returns it to 
     * prepareAdd in a HashMap with keys taskName, date, startTime, endTime, isRecurring
     */
    
    private HashMap<String,String> parseAdd(String arguments) {
        HashMap<String,String> mapArgs = new HashMap<String,String>();
        String[] splitArgs = arguments.split(" ");
        mapArgs.put("taskName", splitArgs[0]);
        //loop through rest of arguments, add them to hashmap if valid
        for (int i=1; i<splitArgs.length; i++) {
            if (splitArgs[i].substring(0, 2).equals("d/")) {
                mapArgs.put("date", splitArgs[i].substring(2));
            }
            if (splitArgs[i].substring(0, 2).equals("s/")) {
                mapArgs.put("startTime", splitArgs[i].substring(2));
            }
            if (splitArgs[i].substring(0, 2).equals("e/")) {
                mapArgs.put("endTime", splitArgs[i].substring(2));
            }
            if (splitArgs[i].substring(0, 2).equals("r/")) {
                mapArgs.put("isRecurring", splitArgs[i].substring(2));
            }
        }
        
        return mapArgs;
    }

    /**
     * Extracts the new person's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }

    /**
     * Parses arguments in the context of the select person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = PERSON_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the find person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }

}