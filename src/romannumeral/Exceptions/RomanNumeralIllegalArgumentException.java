package romannumeral.Exceptions;

/**
 * Thrown when trying to instantiate a roman numeral with an illegal argument string.
 */
public class RomanNumeralIllegalArgumentException extends Exception{
    public RomanNumeralIllegalArgumentException(String message) {
        super(message);
    }
}
