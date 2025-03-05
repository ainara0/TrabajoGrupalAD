package Pere.jdbc;

import DAO.Department;
import DAO.Employee;
import DAO.IDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostgreSQLDAO implements IDAO {

    // Parametros de conexión a la base de datos PostgreSQL
    private final String url = "jdbc:postgresql://pm0002.conectabalear.net:5432/empresa";
    private final String user = "test";
    private final String password = "contraseña_segura_patata_12112";

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
                        new Department(rs.getInt("depno"), null, null)
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
                        new Department(rs.getInt("depno"), null, null)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }


    @Override
    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO empleado (empno, nombre, puesto, depno) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getJob());
            pstmt.setInt(4, employee.getDepartment().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) { // Código de violación de clave foránea en PostgreSQL
                System.out.println("Error: El departamento especificado no existe. Por favor, inserte primero el departamento correspondiente.");
            } else {
                e.printStackTrace();
            }
        }
    }


    @Override
    public Employee updateEmployee(Object id) {
        // Usamos Scanner para leer la opción del usuario
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione el campo a actualizar:");
        System.out.println("1. Nombre");
        System.out.println("2. Puesto");
        System.out.println("3. Departamento (depno)");
        System.out.print("Opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        String campo = "";
        switch (opcion) {
            case 1:
                campo = "nombre";
                break;
            case 2:
                campo = "puesto";
                break;
            case 3:
                campo = "depno";
                break;
            default:
                System.out.println("Opción inválida.");
                return null;
        }

        System.out.print("Ingrese el nuevo valor para " + campo + ": ");
        String nuevoValor = scanner.nextLine();

        // Construimos la sentencia SQL de forma dinámica según el campo a actualizar
        String sql = "UPDATE empleado SET " + campo + " = ? WHERE empno = ? RETURNING empno, nombre, puesto, depno";
        Employee emp = null;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Si se actualiza el departamento se debe convertir a entero
            if (campo.equals("depno")) {
                pstmt.setInt(1, Integer.parseInt(nuevoValor));
            } else {
                pstmt.setString(1, nuevoValor);
            }
            pstmt.setInt(2, (int) id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Dado que Employee espera un objeto Department, lo instanciamos con el id obtenido
                emp = new Employee(
                        rs.getInt("empno"),
                        rs.getString("nombre"),
                        rs.getString("puesto"),
                        new Department(rs.getInt("depno"), null, null)
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
                        new Department(rs.getInt("depno"), null, null)
                );
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public List<Department> findAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT depno, nombre, ubicacion FROM departamento";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Department dept = new Department(
                        rs.getInt("depno"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion")
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
        String sql = "SELECT depno, nombre, ubicacion FROM departamento WHERE depno = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, (int) id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dept = new Department(
                        rs.getInt("depno"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion")
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
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione el campo a actualizar:");
        System.out.println("1. Nombre");
        System.out.println("2. Ubicación");
        System.out.println("3. Ambos");
        System.out.print("Opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        String sql = "";
        switch (opcion) {
            case 1:
                sql = "UPDATE departamento SET nombre = ? WHERE depno = ? RETURNING depno, nombre, ubicacion";
                break;
            case 2:
                sql = "UPDATE departamento SET ubicacion = ? WHERE depno = ? RETURNING depno, nombre, ubicacion";
                break;
            case 3:
                sql = "UPDATE departamento SET nombre = ?, ubicacion = ? WHERE depno = ? RETURNING depno, nombre, ubicacion";
                break;
            default:
                System.out.println("Opción inválida.");
                return null;
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nuevo nombre: ");
                    String nuevoNombre = scanner.nextLine();
                    pstmt.setString(1, nuevoNombre);
                    pstmt.setInt(2, (int) id);
                    break;
                case 2:
                    System.out.print("Ingrese la nueva ubicación: ");
                    String nuevaUbicacion = scanner.nextLine();
                    pstmt.setString(1, nuevaUbicacion);
                    pstmt.setInt(2, (int) id);
                    break;
                case 3:
                    System.out.print("Ingrese el nuevo nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese la nueva ubicación: ");
                    String ubicacion = scanner.nextLine();
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, ubicacion);
                    pstmt.setInt(3, (int) id);
                    break;
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dept = new Department(
                        rs.getInt("depno"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dept;
    }



    //todo mirar si tiene empleado y si no tiene eliminarlo
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
