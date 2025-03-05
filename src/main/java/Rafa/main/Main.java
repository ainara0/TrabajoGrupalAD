package Rafa.main;

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
        System.out.print("Ingrese ID del departamento a eliminar: ");
        int id = Utils.Ask.askForNumber();
        Department department = dao.deleteDepartment(id);
        if (department == null) {
            System.err.println("El departamento con id " + id + " no existe.");
        }
    }

    private void updateDepartment() {
        System.out.print("Ingrese ID del departamento a actualizar: ");
        int id = Utils.Ask.askForNumber();
        Department department = dao.updateDepartment(id);
        if (department == null) {
            System.out.println("El departamento no existe");
        }
    }

    private void addDepartment() {
        Department dept = new Department();
        System.out.println("Ingrese el ID del nuevo departamento: ");
        dept.setId(Utils.Ask.askForNumber());
        System.out.print("Ingrese nombre del departamento: ");
        dept.setName(Utils.Ask.askForString());
        System.out.println("Ingrese la ubicación del departamento: ");
        dept.setLocation(Utils.Ask.askForString());
        boolean isAdded = dao.addDepartment(dept);
        if (!isAdded) {
            System.err.println("El departamento con id " + dept.getId() + " ya existe");
        }
    }

    private void findAllDepartments() {
        List<Department> departments = dao.findAllDepartments();
        if (departments.isEmpty() && departments != null) {
            for (Department department : departments) {
                System.out.println(department);
            }
        } else {
            System.err.println("No existen departamentos.");
        }
    }

    private void deleteEmployee() {
        System.out.print("Ingrese ID del empleado a eliminar: ");
        int id = Utils.Ask.askForNumber();
        boolean deleted = dao.deleteEmployee(id);
        if (!deleted) {
            System.out.println("El empleado con id " + id + " no existe");
        }
    }

    private void updateEmployee() {
        System.out.print("Ingrese ID del empleado a actualizar: ");
        int id = Utils.Ask.askForNumber();
        Employee employee = dao.updateEmployee(id);
        if (employee == null) {
            System.out.println("El empleado con id " + id + " no existe.");
        }
    }

    private Department findDepartmentById() {
        System.out.print("Ingrese ID del departamento: ");
        int id = Utils.Ask.askForNumber();
        Department department = dao.findDepartmentById(id);
        if (department == null) {
            System.out.println("No existe el departamento");
            return null;
        }
        return department;
    }

    private void addEmployee() {
        Employee emp = new Employee();
        System.out.print("Ingrese el nombre: ");
        emp.setName(Utils.Ask.askForString());
        System.out.print("Ingrese el puesto de trabajo: ");
        emp.setJob(Utils.Ask.askForString());
        System.out.println("ingrese el id del departamento");
        Department dept = findDepartmentById();
        if (dept == null) {
            return;
        }
        emp.setDepartment(dept);
        dao.addEmployee(emp);
    }

    private void findEmployeeByDepartment() {
        System.out.print("Ingrese ID del departamento: ");
        int idDept = Utils.Ask.askForNumber();
        List<Employee> employees = dao.findEmployeesByDept(idDept);
        if (employees != null && !employees.isEmpty()) {
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } else {
            System.out.println("No existen empleados en el departamento " + idDept);
        }
    }

    private void findEmployeeById() {
        System.out.print("Ingrese ID del empleado: ");
        int id = Utils.Ask.askForNumber();
        Employee employee = dao.findEmployeeById(id);
        if (employee == null) {
            System.out.println("El empleado no existe");
        }
    }

    private void printEmployeesTF() {
        List<Employee> employees = dao.findAllEmployees();
        if (employees != null && !employees.isEmpty()) {
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } else {
            System.out.println("No existen empleados");
        }
    }
}