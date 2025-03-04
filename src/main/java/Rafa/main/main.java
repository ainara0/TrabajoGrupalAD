package Rafa.main;

import static Rafa.main.HibernateMenu.scanner;

public class main {
    public static void main(String[] args) {

        System.out.println("Escoge el componente que quieres usar.");
        int opcion;
        do {
            System.out.println("1.Hibernate");
            System.out.println("2.Db4o");
            System.out.println("3.MongoDB");
            System.out.println("4.JDBC");
            opcion = askForNumber(0, 4);
            switch (opcion) {
                case 1:
                    HibernateMenu.run();
                case 2:
                    Db4oMenu.run();
                case 3:
                    MongoDBMenu.run();
                case 4:
                    JDBCMenu.run();

            }
        } while (opcion != 0);
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
}
