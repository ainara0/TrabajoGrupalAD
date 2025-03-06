package Ainara.db4o;

import DAO.Department;
import DAO.Employee;
import DAO.IDAO;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import java.io.Closeable;
import java.util.List;

public class Db4oDAO implements Closeable, IDAO {
    ObjectContainer container;

    public Db4oDAO() {
        connectToDatabase();
    }

    public ObjectContainer getContainer() {
        return container;
    }
    public boolean connectToDatabase() {
        try {
            container = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),"GroupProject");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public void close() { //TODO: tratar la excepción cuando se use
        container.close();
    }
    @Override
    public List<Employee> findAllEmployees() {
        ObjectSet<Employee> result = container.query(Employee.class);
        if (result.isEmpty()) {
            return null;
        }
        return result.stream().toList();
    }

    @Override
    public Employee findEmployeeById(Object id) {
        if (!isNumeric(id)) {return null;}
        ObjectSet<Employee> result = container.query(new Predicate<>() {
            public boolean match(Employee employee) {
                return employee.getId() == Integer.parseInt(id.toString());
            }
        });
        if (result.isEmpty()) {
            return null;
        }
        return (result.getFirst());
    }

    @Override
    public void addEmployee(Employee employee) {
        container.store(employee);
    }

    @Override
    public Employee updateEmployee(Object employeeObject) {
        // todo se pasa por parámetro un objeto tipo Employee, nosotros pasamos int
        if (!(employeeObject instanceof Employee employee)) {return null;}
        try {
            container.store(employee);
        } catch (Exception e) {
            return null;
        }
        return employee;
    }

    @Override
    public boolean deleteEmployee(Object employeeObject) {
        // todo se pasa por parámetro un objeto tipo Employee, nosotros pasamos int
        if (!(employeeObject instanceof Employee employee)) {return false;}
        try {
            container.delete(employee);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Department> findAllDepartments() {
        ObjectSet<Department> result = container.query(Department.class);
        if (result.isEmpty()) {
            return null;
        }
        return result.stream().toList();
    }

    @Override
    public Department findDepartmentById(Object id) {
        // todo si es numerico null?? pasas int y hay que devolver null o Department
        if (!isNumeric(id)) { return null; }
        ObjectSet<Department> result = container.query(new Predicate<>() {
            public boolean match(Department department) {
                return department.getId() == Integer.parseInt(id.toString());
            }
        });
        if (result.isEmpty()) {
            return null;
        }
        return (result.getFirst());
    }

    @Override
    public boolean addDepartment(Department department) {
        // todo el .store comprueba si existe el departamento antes de añadirlo?
        try {
            container.store(department);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Department updateDepartment(Object departmentObject) {
        // todo no pasamos Department, pasamos int
        if (!(departmentObject instanceof Department department)) {return null;}
        try {
            container.store(department);
        } catch (Exception e) {
            return null;
        }
        return department;
    }

    @Override
    public Department deleteDepartment(Object departmentObject) {
        // todo pasamos int no Department
        if (!(departmentObject instanceof Department department)) {return null;}
        try {
            container.delete(department);
        } catch (Exception e) {
            return null;
        }
        return department;
    }

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {
        if (!isNumeric(idDept)) { return null; }
        ObjectSet<Employee> result = container.query(new Predicate<>() {
            public boolean match(Employee employee) {
                return employee.getDepartment().getId() == Integer.parseInt(idDept.toString());
            }
        });
        if (result.isEmpty()) {
            return null;
        }
        return result.stream().toList();
    }

    private static boolean isNumeric(Object num) {
        try {
            Integer.parseInt(num.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
