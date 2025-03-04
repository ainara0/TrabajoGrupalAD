package Rafa.main;

import DAO.Department;
import DAO.IDAO;
import Rafa.hibernate.*;

import java.util.Scanner;

public class HibernateMenu {
    static Scanner scanner = new Scanner(System.in);
    static IDAO dao = new Rafa.hibernate.HibernateDAO();
    static String regex = "^[a-zA-Z]+$";

    public static void run() {
        int option;
        System.out.println("__________________________________");
        System.out.println("|------------HIBERNATE------------|");
        System.out.println("|_________________________________|");
        do {
            System.out.println("1. Listar empleados");
            System.out.println("2. Buscar empleado por ID");
            System.out.println("3. Buscar empleado por departamento");
            System.out.println("4. Agregar empleado");
            System.out.println("5. Actualizar empleado");
            System.out.println("6. Eliminar empleado");
            System.out.println("7. Listar departamentos");
            System.out.println("8. Buscar departamento por ID");
            System.out.println("9. Agregar departamento");
            System.out.println("10. Actualizar departamento");
            System.out.println("11. Eliminar departamento");
            System.out.println("0. Salir");
            System.out.println("Seleccione una opción: ");
            option = askForNumber(0, 11);

            switch (option) {
                case 1 -> dao.findAllEmployees().forEach(System.out::println);
                case 2 -> findEmployeeById();
                case 3 -> searchEmployeeByDepartment();
                case 4 -> addEmployee();
                case 5 -> updateEmployee();
                case 6 -> deleteEmployee();
                case 7 -> dao.findAllDepartments().forEach(System.out::println);
                case 8 -> searchDepartmentById();
                case 9 -> addDepartment();
                case 10 -> updateDepartment();
                case 11 -> deleteDepartment();
                case 0 -> System.out.println("Hasta la próxima!");
                default -> System.out.println("Opción inválida, intente de nuevo.");
            }
        } while (option != 0);

    }

    private static void deleteDepartment() {
        System.out.print("Ingrese ID del departamento a eliminar: ");
        int deptId = askForNumber();
        if (dao.findEmployeesByDept(deptId).isEmpty()) {
            dao.deleteDepartment(deptId);
        } else {
            System.err.println("No puede eliminar un departamento que contiene empleados");
        }
    }

    private static void updateDepartment() {
        System.out.print("Ingrese ID del departamento a actualizar: ");
        int id = askForNumber();
        dao.updateDepartment(id);
    }

    private static void addDepartment() {
        Department dept = new Department();
        System.out.print("Ingrese ID del departamento: ");
        int deptId = askForNumber();
        if (dao.findDepartmentById(deptId) != null) {
            System.err.println("El departamento ya existe");
            return;
        }
        dept.setId(deptId);
        System.out.print("Ingrese nombre del departamento: ");
        String name = askForString();
        if (name == null) {
            return;
        }
        dept.setName(name);
        System.out.print("Donde esta ubicado este departamento: ");
        String city = askForString();
        if (city == null) {
            return;
        }
        dept.setCity(city);
        dao.addDepartment(dept);
    }

    private static void searchDepartmentById() {
        System.out.print("Ingrese ID del departamento: ");
        int id = askForNumber();
        Department dept = dao.findDepartmentById(id);
        if (dept == null) {
            System.err.println("El departamento no existe");
        } else {
            System.out.println(dept);
        }
    }

    private static void deleteEmployee() {
        System.out.print("Ingrese ID del empleado a eliminar: ");
        int id = scanner.nextInt();
        dao.deleteEmployee(id);
    }

    private static void updateEmployee() {
        System.out.print("Ingrese ID del empleado a actualizar: ");
        int id = askForNumber();
        dao.updateEmployee(id);
    }

    private static void addEmployee() {
        Employee emp = new Employee();

        System.out.print("Ingrese apellido: ");
        String name = askForString();
        if (name == null) {
            return;
        }
        emp.setName(name);

        System.out.print("Ingrese trabajo: ");
        String job = askForString();
        if (job == null) {
            return;
        }
        emp.setJob(job);

        System.out.println("A que departamento pertenece: ");
        int deptId = askForNumber();
        Department dep = dao.findDepartmentById(deptId);
        if (dep == null) {
            System.err.println("El departamento no existe");
            return;
        }
        emp.setDepno(dep);
        dao.addEmployee(emp);
    }

    private static void searchEmployeeByDepartment() {
        System.out.print("Ingrese ID del departamento: ");
        int deptID = askForNumber();
        Department department = dao.findDepartmentById(deptID);
        if (department == null) {
            System.err.println("El departamento no existe");
        } else {
            if (dao.findEmployeesByDept(deptID) == null) {
                System.out.println("No existen empleados en este departamento");
            } else {
                dao.findEmployeesByDept(deptID).forEach(System.out::println);
            }
        }
    }

    private static void findEmployeeById() {
        System.out.print("Ingrese ID del empleado: ");
        int id = askForNumber();
        Employee employee = dao.findEmployeeById(id);
        if (employee != null) {
            System.out.println(employee);
        } else {
            System.err.println("No existe un empleado con ese ID");
        }
    }

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