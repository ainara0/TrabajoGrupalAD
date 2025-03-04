package Rafa.main;

import DAO.Department;
import DAO.Employee;
import DAO.IDAO;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione una base de datos:");
        System.out.println("1. Hibernate");
        System.out.println("2. Db4o");
        System.out.println("3. MongoDB");
        System.out.println("4. JDBC");
        System.out.print("Ingrese opción: ");
        int dbOption = scanner.nextInt();

        IDAO dao = DAOFactory.getDAO(dbOption);  // Obtiene la implementación correcta

        int option;
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
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> dao.findAllEmployees().forEach(System.out::println);
                case 2 -> {
                    System.out.print("Ingrese ID del empleado: ");
                    int id = scanner.nextInt();
                    System.out.println(dao.findEmployeeById(id));
                }
                case 3 -> {
                    System.out.print("Ingrese ID del departamento: ");
                    int idDept = scanner.nextInt();
                    dao.findEmployeesByDept(idDept).forEach(System.out::println);
                }
                case 4 -> {
                    Employee emp = new Employee();
                    System.out.print("Ingrese apellido: ");
                    emp.setName(scanner.nextLine());
                    System.out.print("Ingrese trabajo: ");
                    emp.setJob(scanner.nextLine());
                    dao.addEmployee(emp);
                }
                case 5 -> {
                    System.out.print("Ingrese ID del empleado a actualizar: ");
                    int id = scanner.nextInt();
                    dao.updateEmployee(id);
                }
                case 6 -> {
                    System.out.print("Ingrese ID del empleado a eliminar: ");
                    int id = scanner.nextInt();
                    dao.deleteEmployee(id);
                }
                case 7 -> dao.findAllDepartments().forEach(System.out::println);
                case 8 -> {
                    System.out.print("Ingrese ID del departamento: ");
                    int id = scanner.nextInt();
                    System.out.println(dao.findDepartmentById(id));
                }
                case 9 -> {
                    Department dept = new Department();
                    System.out.print("Ingrese nombre del departamento: ");
                    dept.setName(scanner.nextLine());
                    dao.addDepartment(dept);
                }
                case 10 -> {
                    System.out.print("Ingrese ID del departamento a actualizar: ");
                    int id = scanner.nextInt();
                    dao.updateDepartment(id);
                }
                case 11 -> {
                    System.out.print("Ingrese ID del departamento a eliminar: ");
                    int id = scanner.nextInt();
                    dao.deleteDepartment(id);
                }
            }
        } while (option != 0);

        scanner.close();
    }
}
