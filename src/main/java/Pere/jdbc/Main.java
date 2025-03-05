package Pere.jdbc;
import java.util.List;
import java.util.Scanner;
import DAO.Employee;
import DAO.Department;
import Pere.file.TextFileDAO;
import Pere.jdbc.PostgreSQLDAO;
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
                        System.out.println("ID: " + emp.getId()
                                + ", Nombre: " + emp.getName()
                                + ", Puesto: " + emp.getJob()
                                + ", Departamento: " + (emp.getDepartment() != null ? emp.getDepartment().getId() : "N/A"));
                    }

                    break;
                case 2:
                    // Buscar empleado por ID
                    System.out.print("Ingrese el ID del empleado: ");
                    int idBuscar = scanner.nextInt();
                    Employee empleadoBuscado = dao.findEmployeeById(idBuscar);
                    if (empleadoBuscado != null) {
                        System.out.println("ID: " + empleadoBuscado.getId());
                        System.out.println("Nombre: " + empleadoBuscado.getName());
                        System.out.println("Puesto: " + empleadoBuscado.getJob());
                        // Se asume que department no es nulo y quieres mostrar el id del departamento:
                        System.out.println("Departamento: " + (empleadoBuscado.getDepartment() != null ? empleadoBuscado.getDepartment().getId() : "N/A"));
                    } else {
                        System.out.println("Empleado no encontrado.");
                    }

                    break;
                case 3:
                    // Asegurarse de que no quede un salto de línea en el buffer
                    // (si ya se llamó a scanner.nextLine() después de nextInt(), puede no ser necesario)
                    // Aquí se llama una vez más para estar seguros:
                    scanner.nextLine();

                    System.out.print("Ingrese el nombre: ");
                    String name = scanner.nextLine();

                    System.out.print("Ingrese el puesto: ");
                    String job = scanner.nextLine();

                    System.out.print("Ingrese el id del departamento: ");
                    int deptno;
                    try {
                        deptno = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Error: el id del departamento debe ser un número entero.");
                        break;
                    }

                    // Se busca el departamento; si no existe se impide la inserción
                    Department dept = dao.findDepartmentById(deptno);
                    if (dept == null) {
                        System.out.println("El departamento no existe. No se puede agregar el empleado.");
                        break;
                    }

                    // Se asigna 0 al id, ya que la base de datos lo genera automáticamente
                    Employee nuevoEmpleado = new Employee(0, name, job, dept);
                    dao.addEmployee(nuevoEmpleado);
                    System.out.println("Empleado agregado con ID: " + nuevoEmpleado.getId());
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
                        System.out.println("ID: " + e.getId()
                                + ", Nombre: " + e.getName()
                                + ", Puesto: " + e.getJob());
                    }
                    break;
                case 7:
                    // Listar todos los departamentos
                    List<Department> listaDept = dao.findAllDepartments();
                    System.out.println("\n--- Departamentos ---");
                    for (Department dept1 : listaDept) {
                        System.out.println("ID: " + dept1.getId()
                                + ", Nombre: " + dept1.getName()
                                + ", Ubicación: " + dept1.getLocation());
                    }
                    break;

                case 8:
                    // Buscar departamento por ID
                    System.out.print("Ingrese el ID del departamento: ");
                    int deptIdBuscar = scanner.nextInt();
                    Department deptBuscado = dao.findDepartmentById(deptIdBuscar);
                    if(deptBuscado != null) {
                        System.out.println("ID: " + deptBuscado.getId()
                                + ", Nombre: " + deptBuscado.getName()
                                + ", Ubicación: " + deptBuscado.getLocation());
                    } else {
                        System.out.println("Departamento no encontrado.");
                    }
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
//
//public class Main {
//    public static void main(String[] args) {
//        TextFileDAO dao = new TextFileDAO();
//        Scanner scanner = new Scanner(System.in);
//        int opcion = -1;
//
//        while (opcion != 0) {
//            System.out.println("\n===== MENÚ =====");
//            System.out.println("1. Listar empleados");
//            System.out.println("2. Buscar empleado por ID");
//            System.out.println("3. Agregar empleado");
//            System.out.println("4. Actualizar empleado");
//            System.out.println("5. Eliminar empleado");
//            System.out.println("6. Buscar empleados por departamento");
//            System.out.println("7. Listar departamentos");
//            System.out.println("8. Buscar departamento por ID");
//            System.out.println("9. Agregar departamento");
//            System.out.println("10. Actualizar departamento");
//            System.out.println("11. Eliminar departamento");
//            System.out.println("0. Salir");
//            System.out.print("Seleccione una opción: ");
//            opcion = scanner.nextInt();
//            scanner.nextLine(); // Consumir salto de línea
//
//            switch(opcion) {
//                case 1:
//                    List<Employee> empleados = dao.findAllEmployees();
//                    System.out.println("\n--- Empleados ---");
//                    for (Employee e : empleados) {
//                        System.out.println("ID: " + e.getId() +
//                                ", Name: " + e.getName() +
//                                ", Job: " + e.getJob() +
//                                ", Dept ID: " + e.getDepartment().getId());
//                    }
//                    break;
//                case 2:
//                    System.out.print("Ingrese el ID del empleado: ");
//                    int idEmp = scanner.nextInt();
//                    scanner.nextLine();
//                    Employee empBuscado = dao.findEmployeeById(idEmp);
//                    if (empBuscado != null) {
//                        System.out.println("ID: " + empBuscado.getId());
//                        System.out.println("Name: " + empBuscado.getName());
//                        System.out.println("Job: " + empBuscado.getJob());
//                        System.out.println("Dept ID: " + empBuscado.getDepartment().getId());
//                    } else {
//                        System.out.println("Empleado no encontrado.");
//                    }
//                    break;
//                case 3:
//                    System.out.print("Ingrese empno: ");
//                    int empno = scanner.nextInt();
//                    scanner.nextLine();
//                    System.out.print("Ingrese name: ");
//                    String name = scanner.nextLine();
//                    System.out.print("Ingrese job: ");
//                    String job = scanner.nextLine();
//                    System.out.print("Ingrese department id: ");
//                    int deptId = scanner.nextInt();
//                    scanner.nextLine();
//                    // Se busca el departamento; si no existe se impide la inserción
//                    Department dept = dao.findDepartmentById(deptId);
//                    if (dept == null) {
//                        System.out.println("Departamento no existe. No se puede agregar el empleado.");
//                        break;
//                    }
//                    Employee nuevoEmp = new Employee(empno, name, job, dept);
//                    dao.addEmployee(nuevoEmp);
//                    System.out.println("Empleado agregado.");
//                    break;
//                case 4:
//                    System.out.print("Ingrese el ID del empleado a actualizar: ");
//                    int idActEmp = scanner.nextInt();
//                    scanner.nextLine();
//                    Employee empActualizado = dao.updateEmployee(idActEmp);
//                    if (empActualizado != null) {
//                        System.out.println("Empleado actualizado:");
//                        System.out.println("ID: " + empActualizado.getId() +
//                                ", Name: " + empActualizado.getName() +
//                                ", Job: " + empActualizado.getJob() +
//                                ", Dept ID: " + empActualizado.getDepartment().getId());
//                    } else {
//                        System.out.println("Empleado no encontrado o actualización cancelada.");
//                    }
//                    break;
//                case 5:
//                    System.out.print("Ingrese el ID del empleado a eliminar: ");
//                    int idElimEmp = scanner.nextInt();
//                    scanner.nextLine();
//                    boolean elimEmp = dao.deleteEmployee(idElimEmp);
//                    System.out.println(elimEmp ? "Empleado eliminado." : "Empleado no encontrado.");
//                    break;
//                case 6:
//                    System.out.print("Ingrese el ID del departamento: ");
//                    int idDept = scanner.nextInt();
//                    scanner.nextLine();
//                    List<Employee> empPorDept = dao.findEmployeesByDept(idDept);
//                    System.out.println("\n--- Empleados del departamento " + idDept + " ---");
//                    for (Employee e : empPorDept) {
//                        System.out.println("ID: " + e.getId() +
//                                ", Name: " + e.getName() +
//                                ", Job: " + e.getJob());
//                    }
//                    break;
//                case 7:
//                    List<Department> deps = dao.findAllDepartments();
//                    System.out.println("\n--- Departamentos ---");
//                    for (Department d : deps) {
//                        System.out.println("ID: " + d.getId() +
//                                ", Name: " + d.getName() +
//                                ", City: " + d.getLocation());
//                    }
//                    break;
//                case 8:
//                    System.out.print("Ingrese el ID del departamento: ");
//                    int idDep = scanner.nextInt();
//                    scanner.nextLine();
//                    Department depBuscado = dao.findDepartmentById(idDep);
//                    if (depBuscado != null) {
//                        System.out.println("ID: " + depBuscado.getId());
//                        System.out.println("Name: " + depBuscado.getName());
//                        System.out.println("City: " + depBuscado.getLocation());
//                    } else {
//                        System.out.println("Departamento no encontrado.");
//                    }
//                    break;
//                case 9:
//                    System.out.print("Ingrese el ID del departamento: ");
//                    int idNewDep = scanner.nextInt();
//                    scanner.nextLine();
//                    System.out.print("Ingrese el name del departamento: ");
//                    String depName = scanner.nextLine();
//                    System.out.print("Ingrese la city del departamento: ");
//                    String city = scanner.nextLine();
//                    Department nuevoDep = new Department(idNewDep, depName, city);
//                    boolean depAgregado = dao.addDepartment(nuevoDep);
//                    System.out.println(depAgregado ? "Departamento agregado." : "Departamento ya existe.");
//                    break;
//                case 10:
//                    System.out.print("Ingrese el ID del departamento a actualizar: ");
//                    int idActDep = scanner.nextInt();
//                    scanner.nextLine();
//                    Department depActualizado = dao.updateDepartment(idActDep);
//                    if (depActualizado != null) {
//                        System.out.println("Departamento actualizado:");
//                        System.out.println("ID: " + depActualizado.getId() +
//                                ", Name: " + depActualizado.getName() +
//                                ", City: " + depActualizado.getLocation());
//                    } else {
//                        System.out.println("Departamento no encontrado o actualización cancelada.");
//                    }
//                    break;
//                case 11:
//                    System.out.print("Ingrese el ID del departamento a eliminar: ");
//                    int idElimDep = scanner.nextInt();
//                    scanner.nextLine();
//                    Department depEliminado = dao.deleteDepartment(idElimDep);
//                    System.out.println(depEliminado != null ? "Departamento eliminado." : "Departamento no encontrado o no se puede eliminar.");
//                    break;
//                case 0:
//                    System.out.println("Saliendo del programa.");
//                    break;
//                default:
//                    System.out.println("Opción no válida. Intente de nuevo.");
//            }
//        }
//        scanner.close();
//    }
//}