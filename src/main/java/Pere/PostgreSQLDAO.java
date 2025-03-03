package Pere;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLDAO implements IDAO {

    // Parametros de conexión a la base de datos PostgreSQL
    private final String url = "jdbc:postgresql://localhost:5432/empresa";
    private final String user = "postgres";
    private final String password = "patata";

    //Para la conecxion
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // METODOS PARA EL EMPLEADO (tabla "empleado")
    @Override
    public List<Employee> findAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT empno, nombre, puesto, depno FROM empleado";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("empno"),
                        rs.getString("nombre"),
                        rs.getString("puesto"),
                        0.0, // No existe columna de salario en la BD
                        rs.getInt("depno")
                );
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public Employee findEmployeeById(Object id) {
        Employee emp = null;
        String sql = "SELECT empno, nombre, puesto, depno FROM empleado WHERE empno = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, (int) id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                emp = new Employee(
                        rs.getInt("empno"),
                        rs.getString("nombre"),
                        rs.getString("puesto"),
                        0.0,
                        rs.getInt("depno")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }

    @Override
    public void addEmployee(Employee employee) {
        // Se insertan empno, nombre, puesto y depno (salary no existe en la BD)
        String sql = "INSERT INTO empleado (empno, nombre, puesto, depno) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setString(3, employee.getLastName());
            pstmt.setInt(4, employee.getDepartmentId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee updateEmployee(Object id) {
        Employee emp = null;
        // Actualizamos el puesto a un valor fijo (por ejemplo, "Actualizado")
        String sql = "UPDATE empleado SET puesto = ? WHERE empno = ? RETURNING empno, nombre, puesto, depno";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String nuevoPuesto = "Actualizado"; // Valor de ejemplo
            pstmt.setString(1, nuevoPuesto);
            pstmt.setInt(2, (int) id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                emp = new Employee(
                        rs.getInt("empno"),
                        rs.getString("nombre"),
                        rs.getString("puesto"),
                        0.0,
                        rs.getInt("depno")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }

    @Override
    public boolean deleteEmployee(Object id) {
        boolean deleted = false;
        String sql = "DELETE FROM empleado WHERE empno = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, (int) id);
            int affectedRows = pstmt.executeUpdate();
            deleted = affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT empno, nombre, puesto, depno FROM empleado WHERE depno = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, (int) idDept);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("empno"),
                        rs.getString("nombre"),
                        rs.getString("puesto"),
                        0.0,
                        rs.getInt("depno")
                );
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // METODOS PARA el DEPARTAMENTO (tabla "departamento")
    @Override
    public List<Department> findAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT depno, nombre FROM departamento";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Department dept = new Department(
                        rs.getInt("depno"),
                        rs.getString("nombre")
                );
                departments.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public Department findDepartmentById(Object id) {
        Department dept = null;
        String sql = "SELECT depno, nombre FROM departamento WHERE depno = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, (int) id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dept = new Department(
                        rs.getInt("depno"),
                        rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dept;
    }

    @Override
    public boolean addDepartment(Department department) {
        boolean added = false;
        // Insertamos en la tabla departamento: depno, nombre y ubicacion (se inserta NULL en ubicacion)
        String sql = "INSERT INTO departamento (depno, nombre, ubicacion) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, department.getId());
            pstmt.setString(2, department.getName());
            pstmt.setNull(3, Types.VARCHAR);
            int affectedRows = pstmt.executeUpdate();
            added = affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }

    @Override
    public Department updateDepartment(Object id) {
        Department dept = null;
        String sql = "UPDATE departamento SET nombre = ? WHERE depno = ? RETURNING depno, nombre";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String nuevoNombre = "DeptActual"; // Valor de ejemplo que cumple con varchar(14)
            pstmt.setString(1, nuevoNombre);
            pstmt.setInt(2, (int) id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dept = new Department(
                        rs.getInt("depno"),
                        rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dept;
    }


    @Override
    public Department deleteDepartment(Object id) {
        Department dept = findDepartmentById(id);
        if (dept != null) {
            String sql = "DELETE FROM departamento WHERE depno = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, (int) id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dept;
    }
}
