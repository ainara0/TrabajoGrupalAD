package Pere.jdbc;

import java.util.List;
import java.util.Scanner;
import DAO.Employee;
import DAO.Department;

public class Main {
    public static void main(String[] args) {
        PostgreSQLDAO dao = new PostgreSQLDAO();
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Listar todos los empleados");
            System.out.println("2. Buscar empleado por ID");
            System.out.println("3. Agregar empleado");
            System.out.println("4. Actualizar empleado");
            System.out.println("5. Borrar empleado");
            System.out.println("6. Buscar empleados por departamento");
            System.out.println("7. Listar todos los departamentos");
            System.out.println("8. Buscar departamento por ID");
            System.out.println("9. Agregar departamento");
            System.out.println("10. Actualizar departamento");
            System.out.println("11. Borrar departamento");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    // Listar todos los empleados
                    List<Employee> listaEmpleados = dao.findAllEmployees();
                    System.out.println("\n--- Empleados ---");
                    for (Employee emp : listaEmpleados) {
                        System.out.println(emp);
                    }
                    break;
                case 2:
                    // Buscar empleado por ID
                    System.out.print("Ingrese el ID del empleado: ");
                    int idBuscar = scanner.nextInt();
                    Employee empleadoBuscado = dao.findEmployeeById(idBuscar);
                    System.out.println(empleadoBuscado != null ? empleadoBuscado : "Empleado no encontrado.");
                    break;
                case 3:
                    // Agregar empleado
                    System.out.print("Ingrese empno: ");
                    int empno = scanner.nextInt();
                    System.out.print("Ingrese nombre: ");
                    String nombre = scanner.next();
                    System.out.print("Ingrese puesto: ");
                    String puesto = scanner.next();
                    System.out.print("Ingrese deptno: ");
                    int deptno = scanner.nextInt();
                    // Dado que Employee siempre devuelve un objeto Department, creamos uno con el id proporcionado.
                    Department dept = new Department(deptno, null, null);
                    Employee nuevoEmpleado = new Employee(empno, nombre, puesto, dept);
                    dao.addEmployee(nuevoEmpleado);
                    System.out.println("Empleado agregado.");
                    break;
                case 4:
                    // Actualizar empleado (se actualiza el puesto a "Actualizado")
                    System.out.print("Ingrese el ID del empleado a actualizar: ");
                    int idActualizar = scanner.nextInt();
                    Employee empleadoActualizado = dao.updateEmployee(idActualizar);
                    System.out.println(empleadoActualizado != null ? "Empleado actualizado: " + empleadoActualizado : "Empleado no encontrado.");
                    break;
                case 5:
                    // Borrar empleado
                    System.out.print("Ingrese el ID del empleado a borrar: ");
                    int idBorrar = scanner.nextInt();
                    boolean borrado = dao.deleteEmployee(idBorrar);
                    System.out.println(borrado ? "Empleado borrado." : "Empleado no encontrado.");
                    break;
                case 6:
                    // Buscar empleados por departamento
                    System.out.print("Ingrese el ID del departamento: ");
                    int idDept = scanner.nextInt();
                    List<Employee> empleadosDept = dao.findEmployeesByDept(idDept);
                    System.out.println("\n--- Empleados del departamento " + idDept + " ---");
                    for (Employee e : empleadosDept) {
                        System.out.println(e);
                    }
                    break;
                case 7:
                    // Listar todos los departamentos
                    List<Department> listaDept = dao.findAllDepartments();
                    System.out.println("\n--- Departamentos ---");
                    for (Department d : listaDept) {
                        System.out.println(d);
                    }
                    break;
                case 8:
                    // Buscar departamento por ID
                    System.out.print("Ingrese el ID del departamento: ");
                    int deptIdBuscar = scanner.nextInt();
                    Department deptBuscado = dao.findDepartmentById(deptIdBuscar);
                    System.out.println(deptBuscado != null ? deptBuscado : "Departamento no encontrado.");
                    break;
                case 9:
                    // Agregar departamento
                    System.out.print("Ingrese depno: ");
                    int depno = scanner.nextInt();
                    System.out.print("Ingrese nombre del departamento: ");
                    String depNombre = scanner.next();
                    System.out.print("Ingrese ubicación del departamento (se puede dejar vacío): ");
                    String ubicacion = scanner.next();
                    Department nuevoDept = new Department(depno, depNombre, ubicacion);
                    boolean deptAgregado = dao.addDepartment(nuevoDept);
                    System.out.println(deptAgregado ? "Departamento agregado." : "Error al agregar el departamento.");
                    break;
                case 10:
                    // Actualizar departamento (se actualiza el nombre a "DeptActual")
                    System.out.print("Ingrese el ID del departamento a actualizar: ");
                    int idDeptActualizar = scanner.nextInt();
                    Department deptActualizado = dao.updateDepartment(idDeptActualizar);
                    System.out.println(deptActualizado != null ? "Departamento actualizado: " + deptActualizado : "Departamento no encontrado.");
                    break;
                case 11:
                    // Borrar departamento
                    System.out.print("Ingrese el ID del departamento a borrar: ");
                    int idDeptBorrar = scanner.nextInt();
                    Department deptBorrado = dao.deleteDepartment(idDeptBorrar);
                    System.out.println(deptBorrado != null ? "Departamento borrado: " + deptBorrado : "Departamento no encontrado.");
                    break;
                case 0:
                    System.out.println("Saliendo del sistema.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
        scanner.close();
    }
}
