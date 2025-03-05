package Rafa.main;

import DAO.Department;
import DAO.Employee;
import DAO.IDAO;

public class Main {
    public static void main(String[] args) {
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

            IDAO dao = DAOFactory.getDAO(dbOption);  // Obtiene la implementación correcta
            if (dao == null) {
                isFinished = true;
            }
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
                    case 1 -> dao.findAllEmployees().forEach(System.out::println);

                    case 2 -> {
                        System.out.print("Ingrese ID del empleado: ");
                        int id = Utils.Ask.askForNumber();
                        dao.findEmployeeById(id);
                    }
                    case 3 -> {
                        System.out.print("Ingrese ID del departamento: ");
                        int idDept = Utils.Ask.askForNumber();
                        dao.findEmployeesByDept(idDept).forEach(System.out::println);
                    }
                    case 4 -> {
                        Employee emp = new Employee();
                        System.out.print("Ingrese el nombre: ");
                        emp.setName(Utils.Ask.askForString());
                        System.out.print("Ingrese el puesto de trabajo: ");
                        emp.setJob(Utils.Ask.askForString());
                        System.out.println("ingrese el id del departamento");
                        Department dept = dao.findDepartmentById(Utils.Ask.askForNumber());
                        emp.setDepartment(dept);
                        dao.addEmployee(emp);
                    }
                    case 5 -> {
                        System.out.print("Ingrese ID del empleado a actualizar: ");
                        int id = Utils.Ask.askForNumber();
                        dao.updateEmployee(id);
                    }
                    case 6 -> {
                        System.out.print("Ingrese ID del empleado a eliminar: ");
                        int id = Utils.Ask.askForNumber();
                        dao.deleteEmployee(id);
                    }
                    case 7 -> dao.findAllDepartments().forEach(System.out::println);
                    case 8 -> {
                        System.out.print("Ingrese ID del departamento: ");
                        int id = Utils.Ask.askForNumber();
                        System.out.println(dao.findDepartmentById(id));
                    }
                    case 9 -> {
                        Department dept = new Department();
                        System.out.println("Ingrese el ID del nuevo departamento: ");
                        dept.setId(Utils.Ask.askForNumber());
                        System.out.print("Ingrese nombre del departamento: ");
                        dept.setName(Utils.Ask.askForString());
                        System.out.println("Ingrese la ubicación del departamento: ");
                        dept.setLocation(Utils.Ask.askForString());
                        dao.addDepartment(dept);
                    }
                    case 10 -> {
                        System.out.print("Ingrese ID del departamento a actualizar: ");
                        int id = Utils.Ask.askForNumber();
                        dao.updateDepartment(id);
                    }
                    case 11 -> {
                        System.out.print("Ingrese ID del departamento a eliminar: ");
                        int id = Utils.Ask.askForNumber();
                        dao.deleteDepartment(id);
                    }
                }
            } while (option != 0);
        } while (!isFinished);
    }
}
