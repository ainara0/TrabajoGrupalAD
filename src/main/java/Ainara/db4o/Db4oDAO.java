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
    public ObjectContainer getContainer() {
        return container;
    }
    public boolean connectToDatabase() {
        try {
            container = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),"football");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public boolean store(Object o) {
        try {
            container.store(o);
        } catch (Exception e) {
            return false;
        }
        return true;

    }
    public boolean commit() {
        try {
            container.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public void close() { //TODO: tratar la excepción cuando se use
        container.close();
    }
    public boolean delete(Object o) {
        try {
            container.delete(o);
        } catch (Exception e) {
            return false;
        }
        return true;
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
        if (!isNumeric(id)) { return null; }
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
        if (!(employeeObject instanceof Employee employee)) {
            return null;
        }
        try {
            container.store(employee);
        } catch (Exception e) {
            return null;
        }
        return employee;
    }

    @Override
    public boolean deleteEmployee(Object employeeObject) {
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
        try {
            container.store(department);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Department updateDepartment(Object departmentObject) {
        if (!(departmentObject instanceof Department department)) {
            return null;
        }
        try {
            container.store(department);
        } catch (Exception e) {
            return null;
        }
        return department;
    }

    @Override
    public Department deleteDepartment(Object departmentObject) {
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
