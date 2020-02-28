package tictactoe;

import java.util.Scanner;

/**
 * Contains various utility methods.
 *
 * @author Tapio Malmberg
 */
public class Utils {
    /**
    * Returns a random integer in the range [min, max].
    * @param min the inclusive lower bound for the random number.
    * @param max the inclusive upper bound for the random number.
    * @return a random integer between min and max.
    */
    public static int getRandom(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * Keeps reading from the standard input stream until the user inputs an
     * integer.
     * @param errorMessage the error message that is shown when the input cannot be
     *                     converted into an integer.
     * @return an integer read from the input stream.
     */
    public static int readInt(String errorMessage) {
        Scanner input = new Scanner(System.in);
        int number = 0;
        boolean incorrectInput = true;

        while (incorrectInput) {
            String str = input.nextLine().trim();

            try {
                number = Integer.parseInt(str);
                incorrectInput = false;
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }

        return number;
    }

    /**
     * Keeps reading from the standard input stream until the user inputs an integer
     * that is in the range [min, max].
     * @param min                      the inclusive lower bound for the integer.
     * @param max                      the inclusive upper bound for the integer.
     * @param errorMessageNonNumeric   the error message that is shown when the
     *                                 input cannot be converted into an integer.
     * @param errorMessageNonMinAndMax the error message that is shown when the
     *                                 input is not between min and max.
     * @return an integer read from the input stream.
     */
    public static int readInt(int min, int max, String errorMessageNonNumeric, String errorMessageNonMinAndMax) {
        boolean incorrectInput = true;
        int number = 0;

        while(incorrectInput) {
            number = readInt(errorMessageNonNumeric);

            if (number < min || number > max) {
                System.out.println(errorMessageNonMinAndMax);
            } else {
                incorrectInput = false;
            }
        }

        return number;
    }
}