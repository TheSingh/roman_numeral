package romannumeral;

import romannumeral.Exceptions.RomanNumeralIllegalArgumentException;
import romannumeral.Exceptions.RomanNumeralOverflowException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representation of a Roman Numeral.
 * Currently, this class only supports the addition operation.
 */
public class RomanNumeral {
    /**
     * This is the string representation of the Roman Numeral.
     */
    private String numeral;

    /**
     * Roman Numeral Regex
     * This validates that the input is indeed a Roman Numeral
     * It will validate roman numeral equivalents between 1 and 4999.
     */
    private final String rnRegex = "^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";

    /**
     * Used to convert to and from subtractives (i.e. IV, IX, etc.)
     */
    private final HashMap<String, String> subtractives;


    /**
     * Used to collapse duplicates of a smaller character into a larger one (i.e. IIIII -> V)
     */
    private final HashMap<String, String> collapsers;

    /**
     * Used to determine which char is of a higher magnitude
     */
    private final HashMap<Character, Integer> magnitudes;


    //Constructors

    /**
     * The only constructor for this class, as this class is designed for only our very specific use case.
     * @param expr the Roman Numeral expression used to instantiate this object. valid range: I-MMMMCMXCIX
     * @throws RomanNumeralIllegalArgumentException {expr is empty} or {expr is not a valid roman numeral}
     */
    public RomanNumeral(String expr) throws RomanNumeralIllegalArgumentException{
        if (expr.length() == 0) {
            throw new RomanNumeralIllegalArgumentException("Zero length RomanNumeral");
        }

        if (isRomanNumeral(expr)) {
            numeral = expr;
        } else {
            throw new RomanNumeralIllegalArgumentException(expr + " is not a RomanNumeral");
        }

        subtractives = new HashMap(){{
            put("IV", "IIII");
            put("IX", "VIIII");
            put("XL", "XXXX");
            put("XC", "LXXXX");
            put("CD", "CCCC");
            put("CM", "DCCCC");
            put("IIII", "IV");
            put("VIIII", "IX");
            put("XXXX", "XL");
            put("LXXXX", "XC");
            put("CCCC", "CD");
            put("DCCCC", "CM");
        }};

        collapsers = new HashMap(){{
            put("IIIII", "V");
            put("VV", "X");
            put("XXXXX", "L");
            put("LL", "C");
            put("CCCCC", "D");
            put("DD", "M");
        }};

        magnitudes = new HashMap(){{
            put('I', 1);
            put('V', 5);
            put('X', 10);
            put('L', 50);
            put('C', 100);
            put('D', 500);
            put('M', 1000);
        }};
    }

    /**
     * Helper function for the constructor to check if the expr is a valid input.
     * @param expr the expression you want to check the validity of.
     * @return true if expr is a valid roman numeral representation, false otherwise.
     */
    private boolean isRomanNumeral(String expr) {
        //use a regex expression to determine whether the input was a match.
        Matcher m = Pattern.compile(rnRegex).matcher(expr);
        return m.matches();
    }

    /**
     * Replaces subtractives in the roman numeral with their expanded form.
     * @param s the numeral you wish to expand
     * @return expanded version of the roman numeral as a string
     */
    private String expandSubtractives(String s) {
        String result = "";
        int i;
        for (i = 0; i + 1 < s.length(); ++i) {
            if (subtractives.get(s.substring(i, i+2)) != null) {
                result += subtractives.get(s.substring(i, i+2));
                ++i;
            } else {
                result += s.charAt(i);
            }
        }

        // this can be incorporated into the loop but you save computations this way, but get uglier code.
        if (i < s.length()) {
            result += s.charAt(i);
        }

        return result;
    }

    /**
     * Replaces chars with their subtractive form.
     * @param s the numeral you wish to collapse
     * @return collapsed version of the numeral
     */
    private String collapseSubtractives(String s) {
        String result = "";
        int i;
        for (i = 0; i < s.length(); ++i) {
            if ((i+4<s.length()) && subtractives.get(s.substring(i, i+5)) != null) {
                result += subtractives.get(s.substring(i, i+5));
                i+=4;
            } else if ((i+3<s.length()) && subtractives.get(s.substring(i, i+4)) != null) {
                result += subtractives.get(s.substring(i, i+4));
                i+=3;
            } else {
                result += s.charAt(i);
            }
        }

        // this can be incorporated into the loop but you save computations this way, but get uglier code.
        if (i < s.length()) {
            result += s.charAt(i);
        }

        return result;
    }

    /**
     * This is a helper function for collapseAllDuplicates
     * Collapses smaller symbols that can be replaced with a larger symbol.
     * @param s the numeral you wish to collapse
     * @return true if modifications were made
     */
    private String collapseDuplicates(String s) {
        String result = "";
        boolean modified = false;
        int i;

        for (i=s.length(); i> 0; --i) {
            if ((i-5 > -1) && collapsers.get(s.substring(i-5, i)) != null) {
                //place the char with the higher magnitude first.
                int counter = 0;
                while((i-counter-5 > 0)  && s.charAt(i-counter-6) == s.charAt(i-5)) {
                    result = s.charAt(i-5) + result;
                    ++counter;
                }

                modified = true;
                result = collapsers.get(s.substring(i-5, i)) + result;
                i -= counter+4;
            } else if ((i-2 >-1) && collapsers.get(s.substring(i-2, i)) != null) {
                //place the char with the higher magnitude first.
                int counter = 0;
                while((i-counter-2 > 0) && s.charAt(i-counter-3) == s.charAt(i-2)) {
                    result = s.charAt(i-2) + result;
                    ++counter;
                }

                modified = true;
                result = collapsers.get(s.substring(i-2, i)) + result;
                i -= counter+1;
            } else {
                result = s.charAt(i-1)+result;
            }
        }

        if (modified) {
            return collapseDuplicates(result);
        } else {
            return result;
        }
    }

    /**
     * Concatenates two numerals and sorts them from highest to lowest magnitude.
     * @param n1 roman numeral 1
     * @param n2 roman numeral 2
     * @return the concatenated and sorted roman numeral.
     */
    private String mergeAndSort(String n1, String n2) {
        String result = "";
        int i = 0, j = 0;

        while((i < n1.length()) && (j < n2.length())) {
            if(magnitudes.get(n1.charAt(i)) > magnitudes.get(n2.charAt(j))) {
                result += n1.charAt(i++);
            } else {
                result += n2.charAt(j++);
            }
        }

        while(i < n1.length()) {
            result += n1.charAt(i++);
        }
        while(j < n2.length()) {
            result += n2.charAt(j++);
        }

        return result;
    }

    /**
     * Adds the two RomanNumerals in four steps and returns a new RomanNumearl with the result.
     * @param romanNumeral the RomanNumeral you wish to add wish to add with this.
     * @return The resulting RomanNumeral containing the value of romanNumeral1 + romanNumeral2.
     * @throws RomanNumeralOverflowException logical follow from integer overflow.
     */
    public RomanNumeral add(RomanNumeral romanNumeral) throws RomanNumeralOverflowException {
        String number1, number2, result;

        //these are the four steps to addition.
        number1 = expandSubtractives(this.numeral);
        number2 = expandSubtractives(romanNumeral.numeral);

        result = mergeAndSort(number1, number2);
        result = collapseDuplicates(result);
        result = collapseSubtractives(result);


        try {
            return new RomanNumeral(result);
        }catch(RomanNumeralIllegalArgumentException e) {
            throw new RomanNumeralOverflowException("Resulting Roman Numeral larger than 4999");
        }
    }

    /**
     * Prints the string representation of the RomanNumeral.
     * @return internally stored string representation
     */
    public String toString() {
        return numeral;
    }

}
