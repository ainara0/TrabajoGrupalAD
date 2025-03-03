package Hibernate;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class HibernateDAO implements IDAO {
    SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    private EntityManager entityManager = sessionFactory.createEntityManager();

    @Override
    public List<Employee> findAllEmployees() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    @Override
    public Employee findEmployeeById(Object id) {
        return entityManager.find(Employee.class, id);
    }

    @Override
    public void addEmployee(Employee employee) {
        entityManager.getTransaction().begin();
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
    }


    @Override
    public Employee updateEmployee(Object id) {
        Scanner scanner = new Scanner(System.in);
        entityManager.getTransaction().begin();

        Employee employee = entityManager.find(Employee.class, id);

        if (employee == null) {
            System.err.println("Empleado no encontrado.");
            entityManager.getTransaction().rollback();
            return null;
        }

        int option;

        System.out.println("\n--- Actualizar Empleado ---");
        System.out.println("1. Cambiar nombre");
        System.out.println("2. Cambiar puesto");
        System.out.println("3. Cambiar departamento");
        System.out.println("4. Cambiar todo");
        System.out.println("5. Atrás");
        System.out.print("Seleccione una opción: ");
        option = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        switch (option) {
            case 1 -> {
                System.out.print("Ingrese el nuevo apellido: ");
                employee.setName(scanner.nextLine());
            }
            case 2 -> {
                System.out.print("Ingrese el nuevo puesto: ");
                employee.setJob(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Ingrese el id de departamento: ");
                Department dept = findDepartmentById(Integer.parseInt(scanner.nextLine()));
                if (dept == null) {
                    System.err.println("Departamento no encontrado.");
                    entityManager.getTransaction().rollback();
                }
            }
            case 4 -> {
                System.out.print("Ingrese el nuevo apellido: ");
                employee.setName(scanner.nextLine());
                System.out.print("Ingrese el nuevo puesto: ");
                employee.setJob(scanner.nextLine());
                System.out.print("Ingrese el id de departamento: ");
                Department dept = findDepartmentById(Integer.parseInt(scanner.nextLine()));
                if (dept == null) {
                    System.err.println("Departamento no encontrado.");
                    entityManager.getTransaction().rollback();
                }
            }
            case 5 -> {
                System.out.println("Regresando al menú principal...");
                entityManager.getTransaction().rollback();
                return null;
            }

        }


        entityManager.merge(employee);
        entityManager.getTransaction().commit();
        System.out.println("Empleado actualizado correctamente.");

        return employee;
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
        return entityManager.createQuery("SELECT d FROM Department d", Department.class).getResultList();
    }

    @Override
    public Department findDepartmentById(Object id) {
        return entityManager.find(Department.class, id);
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
        Scanner scanner = new Scanner(System.in);
        entityManager.getTransaction().begin();

        Department department = entityManager.find(Department.class, id);

        if (department == null) {
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
        option = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        switch (option) {
            case 1 -> {
                System.out.print("Ingrese el nuevo Id: ");
                int deptId = Integer.parseInt(scanner.nextLine());
                Department dept = findDepartmentById(deptId);
                if (dept == null) {
                    department.setId(deptId);
                } else {
                    System.err.println("Ese id de departamento ya existe");
                    entityManager.getTransaction().rollback();
                }
            }
            case 2 -> {
                System.out.print("Ingrese el nuevo nombre: ");
                department.setName(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Ingrese la nueva ubicacion: ");
                department.setCity(scanner.nextLine());
            }
            case 4 -> {
                System.out.print("Ingrese el nuevo Id: ");
                int deptId = Integer.parseInt(scanner.nextLine());
                Department dept = findDepartmentById(deptId);
                if (dept == null) {
                    department.setId(deptId);
                } else {
                    System.err.println("Ese id de departamento ya existe");
                    entityManager.getTransaction().rollback();
                }
                System.out.print("Ingrese el nuevo nombre: ");
                department.setName(scanner.nextLine());
                System.out.print("Ingrese la nueva ubicacion: ");
                department.setCity(scanner.nextLine());
            }
            case 5 -> {
                System.out.println("Regresando al menú principal...");
                entityManager.getTransaction().rollback();
                return null;
            }

        }


        entityManager.merge(department);
        entityManager.getTransaction().commit();
        System.out.println("Empleado actualizado correctamente.");

        return department;
    }

    @Override
    public Department deleteDepartment(Object id) {
        entityManager.getTransaction().begin();
        Department department = entityManager.find(Department.class, id);
        if (department != null) {
            entityManager.remove(department);
            entityManager.getTransaction().commit();
            return department;
        }
        return null;
    }

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {

        return entityManager.createQuery("SELECT e FROM Employee e WHERE e.depno.id = :deptId", Employee.class)
                .setParameter("deptId", idDept)
                .getResultList();
    }

}