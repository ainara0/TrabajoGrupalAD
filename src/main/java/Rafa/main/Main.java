package Rafa.main;

import Ainara.db4o.Db4oDAO;
import DAO.Department;
import DAO.Employee;
import DAO.IDAO;

import java.util.List;

public class Main {
    IDAO dao = null;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        boolean isFinished = false;
        do {
            System.out.println("Seleccione una base de datos:");
            System.out.println("1. Hibernate");
            System.out.println("2. Db4o");
            System.out.println("3. MongoDB");
            System.out.println("4. JDBC");
            System.out.println("5. TextFile");
            System.out.println("0. Salir");
            System.out.print("Ingrese opción: ");
            int dbOption = Utils.Ask.askForNumber(0, 5);
            try {
                if (dao instanceof Db4oDAO db4oDAO){
                    db4oDAO.close();
                }
                dao = DAOFactory.getDAO(dbOption);
                if (dao == null) {
                    isFinished = true;
                } else {
                    actionsMenu(dao);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!isFinished);

    }

    private void actionsMenu(IDAO dao) {
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
            option = Utils.Ask.askForNumber(0, 11);
            if (option == 0) {
                System.out.print("____________________________________\n");
            }
            switch (option) {
                case 1 -> printEmployeesTF();
                case 2 -> findEmployeeById();
                case 3 -> findEmployeeByDepartment();
                case 4 -> addEmployee();
                case 5 -> updateEmployee();
                case 6 -> deleteEmployee();
                case 7 -> findAllDepartments();
                case 8 -> findDepartmentById();
                case 9 -> addDepartment();
                case 10 -> updateDepartment();
                case 11 -> deleteDepartment();
            }
        } while (option != 0);
    }

    private void deleteDepartment() {
        System.out.println(" -----------------------------------");
        System.out.println("|--------ELIMINR DEPRTMENTO--------|");
        System.out.println(" -----------------------------------");
        System.out.print("Ingrese ID del departamento a eliminar: ");
        int id = Utils.Ask.askForNumber();
        Department department = dao.deleteDepartment(id);
        if (department == null) {
            System.out.println("El departamento con id " + id + " no existe. \n");
        } else {
            System.out.println("El departamento con id " + id + " eliminado. \n");
        }
    }

    private void updateDepartment() {
        System.out.println(" -----------------------------------");
        System.out.println("|------ACTUALIZA DEPARTAMENTO------|");
        System.out.println(" -----------------------------------");
        System.out.print("Ingrese ID del departamento a actualizar: ");
        int id = Utils.Ask.askForNumber();
        Department department = dao.findDepartmentById(id);
        if (askForConfirmation("Actualizar nombre?")) {
            System.out.println("Cual es el nuevo nombre? ");
            String name = Utils.Ask.askForString();
            department.setName(name);
        }
        if (askForConfirmation("Actualizar ubicación?")) {
            System.out.println("Cual es la nueva ubicación? ");
            String location = Utils.Ask.askForString();
            department.setLocation(location);
        }
        dao.updateDepartment(department);
        if (department == null) {
            System.out.println("El departamento no existe");
        } else {
            System.out.println("El departamento con id " + id + " actualizado. \n");
        }
    }

    private void addDepartment() {
        System.out.println(" -----------------------------------");
        System.out.println("|--------AÑADE DEPARTAMENTO--------|");
        System.out.println(" -----------------------------------");
        Department dept = new Department();
        System.out.println("Ingrese el ID del nuevo departamento: ");
        int id = Utils.Ask.askForNumber();
        if (dao.findDepartmentById(id) != null) {
            System.out.println("El departamento con id " + id + " ya existe. \n");
            return;
        }
        dept.setId(id);
        System.out.print("Ingrese nombre del departamento: ");
        dept.setName(Utils.Ask.askForStringOnlyLetters());
        System.out.println("Ingrese la ubicación del departamento: ");
        dept.setLocation(Utils.Ask.askForStringOnlyLetters());
        boolean isAdded = dao.addDepartment(dept);
        if (isAdded) {
            System.out.println("El departamento añadido correctamente \n");
        }
    }

    private void findAllDepartments() {
        System.out.println(" -----------------------------------");
        System.out.println("|---BUSCA TODOS LOS DEPARTMENTOS---|");
        System.out.println(" -----------------------------------");
        List<Department> departments = dao.findAllDepartments();
        if (!departments.isEmpty() && departments != null) {
            for (Department department : departments) {
                System.out.println(department);
            }
        } else {
            System.out.println("No existen departamentos. \n");
        }
    }

    private void deleteEmployee() {
        System.out.println(" -----------------------------------");
        System.out.println("|---------ELIMINAR EMPLEADO---------|");
        System.out.println(" -----------------------------------");
        System.out.print("Ingrese ID del empleado a eliminar: ");
        int id = Utils.Ask.askForNumber();
        boolean deleted = dao.deleteEmployee(id);
        if (!deleted) {
            System.out.println("El empleado con id " + id + " no existe \n");
        } else {
            System.out.println("El empleado con id " + id + " eliminado \n");
        }
    }

    private void updateEmployee() {
        System.out.println(" -----------------------------------");
        System.out.println("|--------ACTUALIZA EMPLEADO---------|");
        System.out.println(" -----------------------------------");
        System.out.print("Ingrese ID del empleado a actualizar: ");
        int id = Utils.Ask.askForNumber();
        Employee employee = dao.findEmployeeById(id);
        if (askForConfirmation("Actualizar nombre?")) {
            System.out.println("Cual es el nuevo nombre? ");
            String name = Utils.Ask.askForString();
            employee.setName(name);
        }
        if (askForConfirmation("Actualizar puesto?")) {
            System.out.println("Cual es el nuevo puesto? ");
            String location = Utils.Ask.askForString();
            employee.setJob(location);
        }
        if (askForConfirmation("Actualizar departamento?")) {
            System.out.println("Cual es el nuevo departamento? ");
            int deptID = Utils.Ask.askForNumber();
            Department dept = dao.findDepartmentById(deptID);
            if (dept != null) {
                employee.setDepartment(dept);
            } else {
                System.out.println("El departamento con id " + deptID + " no existe");
            }
        }

        dao.updateEmployee(employee);
        System.out.println("El empleado con id " + id + " actualizado. \n");
    }

    private Department findDepartmentById() {
        System.out.println(" -----------------------------------");
        System.out.println("|-----BUSCA DEPARTAMENTO POR ID-----|");
        System.out.println(" -----------------------------------");
        System.out.print("Ingrese ID del departamento: ");
        int id = Utils.Ask.askForNumber();
        Department department = dao.findDepartmentById(id);
        if (department == null) {
            System.out.println("No existe el departamento \n");
            return null;
        }
        System.out.println(department);
        return department;
    }

    private void addEmployee() {
        System.out.println(" -----------------------------------");
        System.out.println("|----------AÑADIR EMPLEADO----------|");
        System.out.println(" -----------------------------------");
        Employee emp = new Employee();
        System.out.print("Ingrese el nombre: ");
        emp.setName(Utils.Ask.askForStringOnlyLetters());
        System.out.print("Ingrese el puesto de trabajo: ");
        emp.setJob(Utils.Ask.askForStringOnlyLetters());
        System.out.print("Ingrese el departamento: ");
        Department dept = findDepartmentById();
        if (dept == null) {
            System.out.println("El departamento no existe \n");
            return;
        }
        emp.setDepartment(dept);
        dao.addEmployee(emp);
        System.out.println("El empleado se ha añadido con éxito. \n");
    }

    private void findEmployeeByDepartment() {
        System.out.println(" -----------------------------------");
        System.out.println("|BUSCANDO EMPLEADOS POR DEPARTAMENTO|");
        System.out.println(" -----------------------------------");
        System.out.print("Ingrese ID del departamento: ");
        int idDept = Utils.Ask.askForNumber();
        List<Employee> employees = dao.findEmployeesByDept(idDept);
        if (employees != null && !employees.isEmpty()) {
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } else {
            System.out.println("No existen empleados en el departamento " + idDept + "\n");
        }
    }

    private void findEmployeeById() {
        System.out.println(" -----------------------------------");
        System.out.println("|-----BUSCANDO EMPLEADOS POR ID-----|");
        System.out.println(" -----------------------------------");
        System.out.print("Ingrese ID del empleado: ");
        int id = Utils.Ask.askForNumber();
        Employee employee = dao.findEmployeeById(id);
        if (employee == null) {
            System.out.println("El empleado no existe \n");
        }
        System.out.println(employee);
    }

    private void printEmployeesTF() {
        System.out.println(" -----------------------------------");
        System.out.println("|---------IMPRIME EMPLEADOS---------|");
        System.out.println(" -----------------------------------");
        List<Employee> employees = dao.findAllEmployees();
        if (employees != null && !employees.isEmpty()) {
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } else {
            System.out.println("No existen empleados \n");
        }
    }




    private static boolean askForConfirmation(String question) {
        System.out.println(question + " (y/n)");
        while (true) {
            String answer = Utils.Ask.askForString();
            if (answer.equalsIgnoreCase("y")) {

                return true;
            }
            if (answer.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Please enter a correct answer.");
        }
    }


}