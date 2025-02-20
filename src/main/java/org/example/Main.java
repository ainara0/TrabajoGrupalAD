package org.example;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Utilizamos el componente basado en fichero (puedes cambiar a PostgreSQLDAO si lo prefieres)
        IDAO dao = new TextFileDAO();

        // ===============================
        // Eliminar Departamento "DeptAct"
        // ===============================
        System.out.println("=== Eliminando departamento 'DeptAct' ===");
        // Obtenemos todos los departamentos
        List<Department> allDepartments = dao.findAllDepartments();
        // Filtramos aquellos que tengan el nombre "DeptAct"
        List<Department> deptToDelete = allDepartments.stream()
                .filter(d -> d.getName().equalsIgnoreCase("DeptAct"))
                .collect(Collectors.toList());
        if (deptToDelete.isEmpty()) {
            System.out.println("No se encontró ningún departamento con el nombre 'DeptAct'.");
        } else {
            for (Department dept : deptToDelete) {
                Department deletedDept = dao.deleteDepartment(dept.getId());
                if (deletedDept != null) {
                    System.out.println("Departamento eliminado: " + deletedDept);
                } else {
                    System.out.println("No se pudo eliminar el departamento: " + dept);
                }
            }
        }

        // ===========================================
        // Eliminar empleados cuyo nombre sea "Martín"
        // ===========================================
        System.out.println("\n=== Eliminando empleados llamados 'Martín' ===");
        // Obtenemos todos los empleados
        List<Employee> allEmployees = dao.findAllEmployees();
        // Filtramos los empleados cuyo primer nombre es "Martín"
        List<Employee> employeesToDelete = allEmployees.stream()
                .filter(emp -> emp.getFirstName().equalsIgnoreCase("Lucía"))
                .collect(Collectors.toList());
        if (employeesToDelete.isEmpty()) {
            System.out.println("No se encontró ningún empleado llamado 'Martín'.");
        } else {
            for (Employee emp : employeesToDelete) {
                boolean deleted = dao.deleteEmployee(emp.getId());
                if (deleted) {
                    System.out.println("Empleado eliminado: " + emp);
                } else {
                    System.out.println("No se pudo eliminar el empleado: " + emp);
                }
            }
        }
    }
}
