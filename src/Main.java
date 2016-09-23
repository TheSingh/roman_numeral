import romannumeral.Exceptions.RomanNumeralIllegalArgumentException;
import romannumeral.Exceptions.RomanNumeralOverflowException;
import romannumeral.RomanNumeral;

import java.util.Scanner;

/**
 *
 * Known Restrictions:
 *      Roman Numeral must be in range 1-4999 (larger numbers are not supported due to dependency on special chars)
 *      Only supported operation: addition
 */
public class Main {

    public static void main(String[] args) {
        RomanNumeral rn1, rn2;
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter two Roman Numerals separated by spaces.");

        try {
            String numeral1 = scan.next();
            rn1 = new RomanNumeral(numeral1);

            String numeral2 = scan.next();
            rn2 = new RomanNumeral(numeral2);

            System.out.println("Result: " + rn1.add(rn2));
        } catch(RomanNumeralIllegalArgumentException | RomanNumeralOverflowException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
//TODO: new class for hmaps