package Rafa.hibernate;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IDAO dao = new Rafa.hibernate.HibernateDAO();
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
                    int id = Integer.parseInt(scanner.nextLine());
                    Employee employee = dao.findEmployeeById(id);
                    if (employee != null) {
                        System.out.println(employee);
                    } else {
                        System.err.println("No existe un empleado con ese ID");
                    }
                }
                case 3 -> {
                    System.out.print("Ingrese ID del departamento: ");
                    int deptID = Integer.parseInt(scanner.nextLine());
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
                case 4 -> {
                    Employee emp = new Employee();
                    System.out.print("Ingrese apellido: ");
                    emp.setName(scanner.nextLine());
                    System.out.print("Ingrese trabajo: ");
                    emp.setJob(scanner.nextLine());
                    System.out.println("A que departamento pertenece: ");
                    Department dep = dao.findDepartmentById(scanner.nextInt());
                    emp.setDepno(dep);
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
                    System.out.print("Ingrese ID del departamento: ");
                    int deptId = Integer.parseInt(scanner.nextLine());
                    dept.setId(deptId);
                    System.out.print("Ingrese nombre del departamento: ");
                    dept.setName(scanner.nextLine());
                    System.out.print("Donde esta ubicado este departamento: ");
                    dept.setCity(scanner.nextLine());
                    dao.addDepartment(dept);
                }
                case 10 -> {
                    System.out.print("Ingrese ID del departamento a actualizar: ");
                    int id = scanner.nextInt();
                    dao.updateDepartment(id);
                }
                case 11 -> {
                    System.out.print("Ingrese ID del departamento a eliminar: ");
                    int deptId = Integer.parseInt(scanner.nextLine());
                    if (dao.findEmployeesByDept(deptId).isEmpty()) {
                        dao.deleteDepartment(deptId);
                    } else {
                        System.err.println("No puede eliminar el departamento porque contiene empleados");
                    }
                }
                default -> System.out.println("Opción inválida, intente de nuevo.");
            }
        } while (option != 0);

    }
}
