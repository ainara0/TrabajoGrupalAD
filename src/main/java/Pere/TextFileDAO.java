package Pere;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileDAO implements IDAO {

    // Ruta del fichero de datos (asegúrate de que esté en el directorio correcto)
    private final String filePath = "empresa.txt";

    // Listas para almacenar los datos en memoria
    private List<Department> departments;
    private List<Employee> employees;

    // Constructor: carga los datos del fichero al iniciar
    public TextFileDAO() {
        departments = new ArrayList<>();
        employees = new ArrayList<>();
        loadData();
    }

    //Metodo carga los datos al fichero en la listas
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Ignorar líneas vacías o comentarios
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }
                // Procesar departamentos
                if (line.startsWith("department(")) {
                    // Ejemplo: department(10,Contabilidad,Madrid)
                    int start = line.indexOf('(');
                    int end = line.lastIndexOf(')');
                    if (start != -1 && end != -1) {
                        String content = line.substring(start + 1, end);
                        String[] parts = content.split(",");
                        if (parts.length >= 2) {
                            int id = Integer.parseInt(parts[0].trim());
                            String name = parts[1].trim();
                            // Se ignora la ciudad (parts[2])
                            departments.add(new Department(id, name));
                        }
                    }
                }
                // Procesar empleados
                else if (line.startsWith("employee(")) {
                    // employee(1,García,Dependiente,20)
                    int start = line.indexOf('(');
                    int end = line.lastIndexOf(')');
                    if (start != -1 && end != -1) {
                        String content = line.substring(start + 1, end);
                        String[] parts = content.split(",");
                        if (parts.length >= 4) {
                            int id = Integer.parseInt(parts[0].trim());
                            String surname = parts[1].trim(); // Se mapea a firstName
                            String job = parts[2].trim();     // Se mapea a lastName
                            int depId = Integer.parseInt(parts[3].trim());
                            employees.add(new Employee(id, surname, job, 0.0, depId));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Escribir (gualdar) los datos al fichero
    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            // Escribir cabecera de departamentos
            pw.println("-- Adaptado del anexo 7 del libro Acceso a datos con IntelliJ IDEA (2ª ed.) de José Ramón García Sevilla.");
            pw.println("-- departments: department(id,name,city).");
            for (Department d : departments) {
                // Para el campo ciudad se usa un valor por defecto (por ejemplo, "SinCiudad")
                pw.println("department(" + d.getId() + "," + d.getName() + ",SinCiudad)");
            }
            pw.println(); // Línea en blanco
            pw.println("-- employees: employee(id,surname,job,department_id).");
            for (Employee e : employees) {
                pw.println("employee(" + e.getId() + "," + e.getFirstName() + "," + e.getLastName() + "," + e.getDepartmentId() + ")");
            }
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Implementacion de los metodos de la interfaz IDAO para EMPLEADOS

    @Override
    public List<Employee> findAllEmployees() {
        return new ArrayList<>(employees);
    }

    @Override
    public Employee findEmployeeById(Object id) {
        int empId = (int) id;
        for (Employee e : employees) {
            if (e.getId() == empId) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void addEmployee(Employee employee) {
        // Comprobar que el departamento existe
        boolean deptExists = false;
        for (Department d : departments) {
            if (d.getId() == employee.getDepartmentId()) {
                deptExists = true;
                break;
            }
        }
        if (!deptExists) {
            System.out.println("No existe el departamento con id: " + employee.getDepartmentId());
            return;
        }
        employees.add(employee);
        saveData();
    }

    @Override
    public Employee updateEmployee(Object id) {
        int empId = (int) id;
        for (Employee e : employees) {
            if (e.getId() == empId) {
                // Por ejemplo, actualizamos el campo job (lastName) a "Actualizado"
                e.setLastName("Actualizado");
                saveData();
                return e;
            }
        }
        return null;
    }

    @Override
    public boolean deleteEmployee(Object id) {
        int empId = (int) id;
        Employee toRemove = null;
        for (Employee e : employees) {
            if (e.getId() == empId) {
                toRemove = e;
                break;
            }
        }
        if (toRemove != null) {
            employees.remove(toRemove);
            saveData();
            return true;
        }
        return false;
    }

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {
        int depId = (int) idDept;
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getDepartmentId() == depId) {
                result.add(e);
            }
        }
        return result;
    }

    // Implementasion de los métodos de la interfaz IDAO para DEPARTAMENTOS

    @Override
    public List<Department> findAllDepartments() {
        return new ArrayList<>(departments);
    }

    @Override
    public Department findDepartmentById(Object id) {
        int depId = (int) id;
        for (Department d : departments) {
            if (d.getId() == depId) {
                return d;
            }
        }
        return null;
    }

    @Override
    public boolean addDepartment(Department department) {
        // Comprobar que no exista ya un departamento con ese id
        for (Department d : departments) {
            if (d.getId() == department.getId()) {
                return false;
            }
        }
        departments.add(department);
        saveData();
        return true;
    }

    @Override
    public Department updateDepartment(Object id) {
        int depId = (int) id;
        for (Department d : departments) {
            if (d.getId() == depId) {
                // Por exemplo actualizamos el nombre a "DeptAct"
                d.setName("DeptAct");
                saveData();
                return d;
            }
        }
        return null;
    }

    @Override
    public Department deleteDepartment(Object id) {
        int depId = (int) id;
        Department toRemove = null;
        for (Department d : departments) {
            if (d.getId() == depId) {
                toRemove = d;
                break;
            }
        }
        if (toRemove != null) {
            departments.remove(toRemove);
            //  podemos eliminar los empleados asociados a ese departamento
            employees.removeIf(e -> e.getDepartmentId() == depId);
            saveData();
        }
        return toRemove;
    }
}
