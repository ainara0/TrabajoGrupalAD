package Ainara.db4o;

import DAO.Department;
import DAO.Employee;
import DAO.IDAO;

import java.util.List;

public class Db4oDAO implements IDAO {
    @Override
    public List<Employee> findAllEmployees() {
        return List.of();
    }

    @Override
    public Employee findEmployeeById(Object id) {
        return null;
    }

    @Override
    public void addEmployee(Employee employee) {

    }

    @Override
    public Employee updateEmployee(Object id) {
        return null;
    }

    @Override
    public boolean deleteEmployee(Object id) {
        return false;
    }

    @Override
    public List<Department> findAllDepartments() {
        return List.of();
    }

    @Override
    public Department findDepartmentById(Object id) {
        return null;
    }

    @Override
    public boolean addDepartment(Department department) {
        return false;
    }

    @Override
    public Department updateDepartment(Object id) {
        return null;
    }

    @Override
    public Department deleteDepartment(Object id) {
        return null;
    }

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {
        return List.of();
    }
}
