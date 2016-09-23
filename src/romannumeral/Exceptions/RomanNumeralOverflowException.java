package romannumeral.Exceptions;

/**
 * Thrown when adding integers results in integer larger than 4999.
 */
public class RomanNumeralOverflowException extends Exception {
    public RomanNumeralOverflowException(String message) {
        super(message);
    }
}
