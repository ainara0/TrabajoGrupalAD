package Rafa.hibernate;

import DAO.Department;
import DAO.Employee;
import Rafa.main.HibernateMenu;
import DAO.IDAO;
import Utils.JpaConverter;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class HibernateDAO implements IDAO {
    SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    private EntityManager entityManager = sessionFactory.createEntityManager();

    @Override
    public List<Employee> findAllEmployees() {
        System.out.println("");
        System.out.println("Has escodigo la opcion de listar empleados");
        System.out.println("");
        List<Employee> employees = new ArrayList<>();
        List<EmployeeJPA> employeesJAR = entityManager.createQuery("SELECT e FROM EmployeeJPA e", EmployeeJPA.class).getResultList();
        for (EmployeeJPA employeeJPA : employeesJAR) {
            Employee employee = new Employee(
                    employeeJPA.getId(),
                    employeeJPA.getName(),
                    employeeJPA.getJob(),
                    JpaConverter.convertToEntity(employeeJPA.getDepno())

            );
            employees.add(employee);
        }

        return employees;
    }

    @Override
    public Employee findEmployeeById(Object id) {
        EmployeeJPA employeeJPA = entityManager.find(EmployeeJPA.class, id);

        return new Employee(
                employeeJPA.getId(),
                employeeJPA.getName(),
                employeeJPA.getJob(),
                findDepartmentById(employeeJPA.getDepno().getId())
        );
    }

    @Override
    public void addEmployee(Employee employee) {
        EmployeeJPA employeeJPA = new EmployeeJPA(
                employee.getName(),
                employee.getJob(),
                JpaConverter.convertToJPA(employee.getDepartment())
        );
        entityManager.getTransaction().begin();
        entityManager.persist(employeeJPA);
        entityManager.getTransaction().commit();
    }


    @Override
    public Employee updateEmployee(Object id) {
        entityManager.getTransaction().begin();

        EmployeeJPA employeeJpa = entityManager.find(EmployeeJPA.class, id);

        if (employeeJpa == null) {
            System.err.println("Empleado no encontrado.");
            entityManager.getTransaction().rollback();
            return null;
        }

        int option;

        menuUpdateEmployee();
        option = HibernateMenu.askForNumber(1, 5);

        switch (option) {
            case 1 -> {
                if (askForNameFromEmployee(employeeJpa)) return null;
            }
            case 2 -> {
                if (askForJobFromEmployee(employeeJpa)) return null;
            }
            case 3 -> {
                if (askForDepartmentFromEmployee()) return null;
            }
            case 4 -> {
                if (askForNameFromEmployee(employeeJpa)) return null;

                if (askForJobFromEmployee(employeeJpa)) return null;

                if (askForDepartmentFromEmployee()) return null;
            }
            case 5 -> {
                entityManager.getTransaction().rollback();
                return null;
            }

        }


        entityManager.merge(employeeJpa);
        entityManager.getTransaction().commit();
        System.out.println("Empleado actualizado correctamente.");

        return new Employee(
                employeeJpa.getId(),
                employeeJpa.getName(),
                employeeJpa.getJob(),
                findDepartmentById(employeeJpa.getDepno().getId())
        );
    }

    private static void menuUpdateEmployee() {
        System.out.println("\n--- Actualizar Empleado ---");
        System.out.println("1. Cambiar nombre");
        System.out.println("2. Cambiar puesto");
        System.out.println("3. Cambiar departamento");
        System.out.println("4. Cambiar todo");
        System.out.println("5. Atrás");
        System.out.print("Seleccione una opción: ");
    }

    private boolean askForDepartmentFromEmployee() {
        System.out.print("Ingrese el id de departamento: ");
        int deptId = HibernateMenu.askForNumber();

        DepartmentJPA deptJpa = entityManager.find(DepartmentJPA.class, deptId);
        if (deptJpa == null) {
            System.err.println("Departamento no encontrado.");
            entityManager.getTransaction().rollback();
            return true;
        }
        return false;
    }

    private boolean askForJobFromEmployee(EmployeeJPA employee) {
        System.out.print("Ingrese el nuevo puesto: ");
        String job = HibernateMenu.askForString();
        if (job == null) {
            entityManager.getTransaction().rollback();
            return true;
        }
        employee.setJob(job);
        return false;
    }

    private boolean askForNameFromEmployee(EmployeeJPA employee) {
        System.out.print("Ingrese el nuevo apellido: ");
        String name = HibernateMenu.askForString();
        if (name == null) {
            entityManager.getTransaction().rollback();
            return true;
        }
        employee.setName(name);
        return false;
    }


    @Override
    public boolean deleteEmployee(Object id) {
        entityManager.getTransaction().begin();
        Employee employee = entityManager.find(Employee.class, id);
        if (employee != null) {
            entityManager.remove(employee);
            entityManager.getTransaction().commit();
            return true;
        } else {
            System.err.println("El empleado no existe");
        }
        return false;
    }

    @Override
    public List<Department> findAllDepartments() {
        List<DepartmentJPA> departmentsJPA = entityManager.createQuery("SELECT e FROM DepartmentJPA e", DepartmentJPA.class).getResultList();
        List<Department> departments = new ArrayList<>();
        for (DepartmentJPA departmentJPA : departmentsJPA) {
            departments.add(new Department(
                    departmentJPA.getId(),
                    departmentJPA.getName(),
                    departmentJPA.getCity()
            ));
        }
        return departments;
    }

    @Override
    public Department findDepartmentById(Object id) {
        DepartmentJPA departmentJPA = entityManager.find(DepartmentJPA.class, id);
        return new Department(
                departmentJPA.getId(),
                departmentJPA.getName(),
                departmentJPA.getCity()
        );
    }

    @Override
    public boolean addDepartment(Department department) {
        entityManager.getTransaction().begin();
        entityManager.persist(department);
        entityManager.getTransaction().commit();
        return true;
    }

    @Override
    public Department updateDepartment(Object id) {
        entityManager.getTransaction().begin();

        DepartmentJPA departmentJPA = entityManager.find(DepartmentJPA.class, id);

        if (departmentJPA == null) {
            System.err.println("Departamento no encontrado.");
            entityManager.getTransaction().rollback();
            return null;
        }

        int option;

        System.out.println("\n--- Actualizar Departamento ---");
        System.out.println("1. Cambiar id");
        System.out.println("2. Cambiar nombre");
        System.out.println("3. Cambiar ubicacion");
        System.out.println("4. Cambiar Ambos");
        System.out.println("5. Atrás");
        System.out.print("Seleccione una opción: ");
        option = HibernateMenu.askForNumber();


        switch (option) {
            case 1 -> {
                askForIdFromDepartment(departmentJPA);
            }
            case 2 -> {
                if (askForNameFromDepartment(departmentJPA)) return null;
            }
            case 3 -> {
                if (askForCityFromDepartment(departmentJPA)) return null;
            }
            case 4 -> {
                askForIdFromDepartment(departmentJPA);

                if (askForNameFromDepartment(departmentJPA)) return null;

                if (askForCityFromDepartment(departmentJPA)) return null;
            }
            case 5 -> {
                System.out.println("Regresando al menú principal...");
                entityManager.getTransaction().rollback();
                return null;
            }

        }


        entityManager.merge(departmentJPA);
        entityManager.getTransaction().commit();
        System.out.println("Empleado actualizado correctamente.");

        return new Department(
                departmentJPA.getId(),
                departmentJPA.getName(),
                departmentJPA.getCity()
        );
    }

    private boolean askForCityFromDepartment(DepartmentJPA department) {
        System.out.print("Ingrese la nueva ubicacion: ");
        String city = HibernateMenu.askForString();
        if (city == null) {
            entityManager.getTransaction().rollback();
            return true;
        }
        department.setCity(city);
        return false;
    }

    private boolean askForNameFromDepartment(DepartmentJPA department) {
        System.out.print("Ingrese el nuevo nombre: ");
        String name = HibernateMenu.askForString();
        if (name == null) {
            entityManager.getTransaction().rollback();
            return true;
        }
        department.setName(name);
        return false;
    }

    private void askForIdFromDepartment(DepartmentJPA department) {
        System.out.print("Ingrese el nuevo Id: ");
        int deptId = HibernateMenu.askForNumber();
        Department dept = findDepartmentById(deptId);
        if (dept == null) {
            department.setId(deptId);
        } else {
            System.err.println("Ese id de departamento ya existe");
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public Department deleteDepartment(Object id) {
        entityManager.getTransaction().begin();
        DepartmentJPA departmentJPA = entityManager.find(DepartmentJPA.class, id);
        if (departmentJPA != null) {
            entityManager.remove(departmentJPA);
            entityManager.getTransaction().commit();
            return new Department(
                    departmentJPA.getId(),
                    departmentJPA.getName(),
                    departmentJPA.getCity()
            );
        }
        return null;
    }

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {
        List<Employee> employees = new ArrayList<>();
        List<EmployeeJPA> employeesJPA = entityManager.createQuery("SELECT e FROM EmployeeJPA e WHERE e.depno.id = :deptId", EmployeeJPA.class)
                .setParameter("deptId", idDept)
                .getResultList();
    
        for (EmployeeJPA employeeJPA : employeesJPA) {
            employees.add(new Employee(
                    employeeJPA.getId(),
                    employeeJPA.getName(),
                    employeeJPA.getJob(),
                    findDepartmentById(employeeJPA.getDepno())
            ));
        }
        return employees;
    }

}
