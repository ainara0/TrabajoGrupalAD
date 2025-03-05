package Utils;

import java.util.Scanner;

public class Ask {
    Scanner scanner = new Scanner(System.in);
    public static int askForNumber() {
        String input = scanner.nextLine();
        if (!isNumeric(input)) {
            System.out.println("Input is not a number. Try again.");
        } else {
            int number = Integer.parseInt(input);
            return number;
        }
        return -1;
    }

    public static int askForNumber(int min, int max) {
        String input = scanner.nextLine();
        if (!isNumeric(input)) {
            System.out.println("Input is not a number. Try again.");
        } else {
            int number = Integer.parseInt(input);
            if (!((number > (min - 1)) && (number < (max + 1)))) {
                System.out.println("Input is not a valid number. Try again.");
            } else {
                return number;
            }
        }
        return -1;
    }

    private static boolean isNumeric(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static String askForString() {
        String texto;
        do {
            Scanner in = new Scanner(System.in);
            texto = in.nextLine().trim();
        } while (!texto.matches(regex) || texto.equalsIgnoreCase("exit"));
        if (texto.equalsIgnoreCase("exit")) {
            return null;
        }
        return texto;
    }
}
