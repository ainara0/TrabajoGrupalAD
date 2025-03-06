package Ainara.mongoDB;

import DAO.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class MongoDBDAO implements Closeable, IDAO {
    static MongoClient mongoClient;
    static MongoDatabase db;

    public MongoDBDAO() {
        String connectionString = "mongodb+srv://admin:0000@cluster0.0pqj1.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        mongoClient = MongoClients.create(connectionString);
        db = mongoClient.getDatabase("GroupProject");
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    @Override
    public boolean addDepartment(Department department) {
        // todo se duplica si hay exactamente los mismos datos? se comprueba que existe?
        MongoCollection<Document> departmentsCollection = db.getCollection("Department");
        Document document = new Document()
                .append("_id", department.getId())
                .append("name", department.getName())
                .append("location", department.getLocation());
        departmentsCollection.insertOne(document);
        //TODO return
        return true;
    }
    @Override
    public void addEmployee(Employee employee) {
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        Document document = new Document()
                .append("_id", employee.getId())
                .append("name", employee.getName())
                .append("job", employee.getJob())
                .append("department_id", employee.getDepartment())
                ;
        employeesCollection.insertOne(document);
    }
    @Override
    public List<Employee> findAllEmployees() {
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        List<Employee> employees = new ArrayList<>();
        try (MongoCursor<Document> cursor = employeesCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                employees.add(toEmployee(document));
            }
        }
        return employees;
    }

    @Override
    public Employee findEmployeeById(Object id) {
        MongoCollection<Document> departmentsCollection = db.getCollection("Employee");
        try (MongoCursor<Document> cursor = departmentsCollection.find(Filters.eq("_id", id)).iterator()) {
            if (cursor.hasNext()) {
                Document document = cursor.next();
                return toEmployee(document);
            }
        }
        return null;
    }

    @Override
    public Employee updateEmployee(Object employeeObject) {
        if (!(employeeObject instanceof Employee employee)) {
            return null;
        }

        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        Bson query = Filters.eq("_id", employee.getId());
        Bson updates = Updates.combine(
                Updates.set("name", employee.getName()),
                Updates.set("job", employee.getJob()),
                Updates.set("department_id", employee.getDepartment().getId())
        );
        Document updatedEmployee = employeesCollection.findOneAndUpdate(query,updates);
        return updatedEmployee != null ? toEmployee(updatedEmployee) : null;
    }

    @Override
    public boolean deleteEmployee(Object id) {
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        Document employee = employeesCollection.findOneAndDelete(Filters.eq("_id", id));
        return employee != null;
    }

    @Override
    public List<Department> findAllDepartments() {
        MongoCollection<Document> departmentsCollection = db.getCollection("Department");
        List<Department> departments = new ArrayList<>();
        try (MongoCursor<Document> cursor = departmentsCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                departments.add(toDepartment(document));
            }
        }
        return departments;
    }

    @Override
    public Department findDepartmentById(Object id) {
        MongoCollection<Document> departmentsCollections = db.getCollection("Department");
        Document departmentDocument = departmentsCollections.find(Filters.eq("_id", id)).first();
        return departmentDocument != null ? toDepartment(departmentDocument) : null;
    }

    @Override
    public Department updateDepartment(Object departmentObject) {
        if (!(departmentObject instanceof Department department)) {
            return null;
        }
        MongoCollection<Document> departmentsCollection = db.getCollection("Department");
        Bson query = Filters.eq("_id", department.getId());
        Bson updates = Updates.combine(
                Updates.set("first_name", department.getName()),
                Updates.set("location", department.getLocation())
        );
        Document updatedDepartment = departmentsCollection.findOneAndUpdate(query,updates);
        return updatedDepartment != null ? toDepartment(updatedDepartment) : null;
    }

    @Override
    public Department deleteDepartment(Object id) {
        MongoCollection<Document> departmentsCollection = db.getCollection("Department");
        Document department = departmentsCollection.findOneAndDelete(Filters.eq("_id", id));
        return department != null ? toDepartment(department) : null;
    }

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        List<Employee> employees = new ArrayList<>();
        Bson query = Filters.eq("department_id",idDept);
        try (MongoCursor<Document> cursor = employeesCollection.find(query).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                employees.add(toEmployee(document));
            }
        }
        return employees;
    }

    private Department toDepartment(Document document) {
        return new Department(
                document.getInteger("_id"),
                document.getString("name"),
                document.getString("location")
        );
    }

    private Employee toEmployee(Document document) {
        return new Employee(
                document.getInteger("_id"),
                document.getString("name"),
                document.getString("job"),
                findDepartmentById(document.getInteger("department_id"))
        );
    }
}