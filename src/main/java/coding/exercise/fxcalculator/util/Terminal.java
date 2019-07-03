package coding.exercise.fxcalculator.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Helper class to provide interaction with a user.
 */
public class Terminal {
    private final Scanner scanner;
    private final PrintStream printer;

    public Terminal(InputStream input, OutputStream output) {
        scanner = new Scanner(input);
        printer = (output instanceof PrintStream) ? (PrintStream) output : new PrintStream(output, true);
    }

    public void prompt(String message) {
        printer.print(message);
    }

    public void print(String message) {
        printer.println(message);
    }

    public boolean parse(Consumer<Scanner> parser) {
        return scanner.hasNext() && doRead(parser);
    }

    private boolean doRead(Consumer<Scanner> parser) {
        try {
            parser.accept(scanner);
        } catch (InputMismatchException e) {
            // Parsing error
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            // End of input reached
            return false;
        }

        return true;
    }
}
