package Pere.jdbc;

import DAO.Department;
import DAO.Employee;
import DAO.IDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostgreSQLDAO implements IDAO {

    /** PostgreSQL database connection URL */
    private final String url = "jdbc:postgresql://pm0002.conectabalear.net:5432/empresa";
    /** Database user */
    private final String user = "test";
    /** Database password */
    private final String password = "contraseña_segura_patata_12112";

    /**
     * Obtains a connection to the database.
     *
     * @return a {@link Connection} object for interacting with the database.
     * @throws SQLException if an error occurs while establishing the connection.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Retrieves all employees from the "empleado" table.
     *
     * @return a list of {@link Employee} objects.
     */
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

    /**
     * Finds an employee by its ID (empno).
     *
     * @param id the employee's identifier (should be an {@code int}).
     * @return the {@link Employee} object if found, or {@code null} otherwise.
     */
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

    /**
     * Adds a new employee to the database.
     * <p>
     * It is assumed that the "empno" column is auto-generated by the database.
     * </p>
     *
     * @param employee the {@link Employee} object to add (the id will be auto-assigned).
     */

    @Override
    public void addEmployee(Employee employee) {
        // It is assumed that the "empno" column is SERIAL and auto-generated.
        String sql = "INSERT INTO empleado (nombre, puesto, depno) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getJob());
            pstmt.setInt(3, employee.getDepartment().getId());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    employee.setId(generatedId);
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    /**
     * Updates an existing employee.
     * <p>
     * This method displays an interactive menu to select the field to update:
     * <ul>
     *   <li>1 - Name</li>
     *   <li>2 - Job</li>
     *   <li>3 - Department (depno)</li>
     * </ul>
     * </p>
     *
     * @param id the identifier of the employee to update.
     * @return the updated {@link Employee} object, or {@code null} if not found or if the update was canceled.
     */
    @Override
    public Employee updateEmployee(Object id) {

        if (!(id instanceof Employee employee)){
            return null;
        }

        // 1. Verificar en la base de datos si el empleado existe
        String sqlSelect = "SELECT empno, nombre, puesto, depno FROM empleado WHERE empno = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {

            pstmtSelect.setInt(1, employee.getId());
            try (ResultSet rs = pstmtSelect.executeQuery()) {
                if (rs.next()) {
                    // Si se encuentra el empleado, se instancia y se cargan sus datos
                    employee = new Employee();
                    employee.setId(rs.getInt("empno"));
                    employee.setName(rs.getString("nombre"));
                    employee.setJob(rs.getString("puesto"));
                    int depId = rs.getInt("depno");
                    Department dept = findDepartmentById(depId);
                    employee.setDepartment(dept);
                } else {

                    return null;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        // 2. Solicitar al usuario qué campo desea actualizar
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione el campo que desea actualizar para el empleado:");
        System.out.println("1. Nombre");
        System.out.println("2. Trabajo");
        System.out.println("3. Departamento");
        System.out.println("4. Todos los campos");
        System.out.print("Opciones: ");
        int option;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Error: La opción debe ser un número entero.");
            return null;
        }

        // 3. Actualizar el objeto employee según la opción elegida
        switch (option) {
            case 1:
                System.out.print("Introduzca el nuevo nombre: ");
                String newName = scanner.nextLine();
                employee.setName(newName);
                break;
            case 2:
                System.out.print("Introduzca el nuevo trabajo: ");
                String newJob = scanner.nextLine();
                employee.setJob(newJob);
                break;
            case 3:
                System.out.print("Introduzca el nuevo ID del departamento: ");
                int newDepId;
                try {
                    newDepId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException ex) {
                    System.out.println("Error: El ID del departamento debe ser un número entero.");
                    return null;
                }
                Department newDept = findDepartmentById(newDepId);
                if (newDept == null) {
                    System.out.println("Departamento no encontrado.");
                    return null;
                }
                employee.setDepartment(newDept);
                break;
            case 4:
                System.out.print("Introduzca el nuevo nombre: ");
                String allName = scanner.nextLine();
                System.out.print("Introduzca el nuevo trabajo: ");
                String allJob = scanner.nextLine();
                System.out.print("Introduzca el nuevo ID del departamento: ");
                int allDepId;
                try {
                    allDepId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException ex) {
                    System.out.println("Error: el ID del departamento debe ser un número entero.");
                    return null;
                }
                Department allDept = findDepartmentById(allDepId);
                if (allDept == null) {
                    System.out.println("Departamento no encontrado.");
                    return null;
                }
                employee.setName(allName);
                employee.setJob(allJob);
                employee.setDepartment(allDept);
                break;
            default:
                System.out.println("Opción inválida.");
                return null;
        }

        // 4. Actualizar la base de datos con los nuevos datos
        String sqlUpdate = "UPDATE empleado SET nombre = ?, puesto = ?, depno = ? WHERE empno = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {

            pstmtUpdate.setString(1, employee.getName());
            pstmtUpdate.setString(2, employee.getJob());
            pstmtUpdate.setInt(3, employee.getDepartment().getId());
            pstmtUpdate.setInt(4, employee.getId());

            int rowsAffected = pstmtUpdate.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Empleado actualizado correctamente en la base de datos.");
            } else {
                System.out.println("No se pudo actualizar el empleado.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return employee;
    }


    /**
     * Deletes an employee from the database.
     *
     * @param id the identifier of the employee to delete.
     * @return {@code true} if the deletion was successful; {@code false} otherwise.
     */
    @Override
    public boolean deleteEmployee(Object id) {
        boolean deleted = false;
        String sql = "DELETE FROM empleado WHERE empno = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se establece el ID del empleado a eliminar.
            pstmt.setInt(1, (int) id);

            // Ejecutamos la eliminasion y obtengo las filas que se han  cambiado
            int affectedRows = pstmt.executeUpdate();

            //Si ha efectado al menos una fila entonces true
            deleted = affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }


    /**
     * Finds employees that belong to a specific department.
     *
     * @param idDept the identifier of the department.
     * @return a list of {@link Employee} objects belonging to the specified department.
     */
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

    /**
     * Retrieves all departments from the "departamento" table.
     *
     * @return a list of {@link Department} objects.
     */
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

    /**
     * Finds a department by its ID.
     *
     * @param id the identifier of the department.
     * @return the {@link Department} object if found; {@code null} otherwise.
     */
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

    /**
     * Adds a new department to the database.
     *
     * @param department the {@link Department} object to add.
     * @return {@code true} if the insertion was successful; {@code false} otherwise.
     */
    @Override
    public boolean addDepartment(Department department) {
        boolean added = false;
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

    /**
     * Updates an existing department.
     * <p>
     * This method displays an interactive menu to update:
     * <ul>
     *   <li>1 - Name</li>
     *   <li>2 - Location</li>
     *   <li>3 - Both</li>
     * </ul>
     * </p>
     *
     * @param id the identifier of the department to update.
     * @return the updated {@link Department} object or {@code null} if not found or if the update was canceled.
     */
    @Override
    public Department updateDepartment(Object id) {
        Department dept = null;
        int departmentId = (int) id;

        // 1. Verificar que el departamento existe en la base de datos
        String sqlCheck = "SELECT depno FROM departamento WHERE depno = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setInt(1, departmentId);
            try (ResultSet rsCheck = pstmtCheck.executeQuery()) {
                if (!rsCheck.next()) {
                    System.out.println("El departamento no existe en la base de datos.");
                    return null;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        // 2. Solicitar al usuario qué campo desea actualizar
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione el campo a actualizar:");
        System.out.println("1. Nombre");
        System.out.println("2. Localidad");
        System.out.println("3. Ambas");
        System.out.print("Opcion: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        String sql = "";
        switch (option) {
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

        // 3. Ejecutar la actualización
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            switch (option) {
                case 1:
                    System.out.print("Introduce el nuevo nombre: ");
                    String newName = scanner.nextLine();
                    pstmt.setString(1, newName);
                    pstmt.setInt(2, departmentId);
                    break;
                case 2:
                    System.out.print("Introduce la nueva localidad: ");
                    String newLocation = scanner.nextLine();
                    pstmt.setString(1, newLocation);
                    pstmt.setInt(2, departmentId);
                    break;
                case 3:
                    System.out.print("Introduzca el nuevo nombre: ");
                    String name = scanner.nextLine();
                    System.out.print("Introduzca la nueva ubicación: ");
                    String location = scanner.nextLine();
                    pstmt.setString(1, name);
                    pstmt.setString(2, location);
                    pstmt.setInt(3, departmentId);
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


    /**
     * Deletes a department from the database.
     * <p>
     * The department is deleted if it exists, and the deleted object is returned; otherwise, {@code null} is returned.
     * </p>
     *
     * @param id the identifier of the department to delete.
     * @return the deleted {@link Department} object or {@code null} if not found.
     */
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
