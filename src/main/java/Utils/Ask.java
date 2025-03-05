package Utils;

import java.util.Scanner;

public class Ask {
    static String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$";

    static Scanner scanner = new Scanner(System.in);
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
        String finalInput = null;
        do {
            String input = askForString();
            if (!isNumeric(input)) {
                System.out.println("Input is not a number. Try again.");
                finalInput = null;
            } else {
                int number = Integer.parseInt(input);
                if (!((number > (min - 1)) && (number < (max + 1)))) {
                    System.out.println("Input is not a valid number. Try again.");
                } else {
                    finalInput = input;
                    return number;
                }
            }
        } while (!isNumeric(finalInput));
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
