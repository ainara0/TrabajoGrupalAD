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

public class Connection implements Closeable, IDAO {
    static MongoClient mongoClient;
    static MongoDatabase db;

    private Connection() {
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
                .append("department_id", employee.getDeptId())
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
        MongoCollection<Document> matchesCollection = db.getCollection("Employee");
        try (MongoCursor<Document> cursor = matchesCollection.find(Filters.eq("_id", id)).iterator()) {
            if (cursor.hasNext()) {
                Document document = cursor.next();
                return toEmployee(document);
            }
        }
        return null;
    }

    @Override
    public Employee updateEmployee(Object id) {
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        Bson query = Filters.eq("_id",employee.getId());
        Bson updates = Updates.combine(
                Updates.set("name", employee.getName()),
                Updates.set("job", employee.getJob()),
                Updates.set("department_id", employee.getDeptId())
        );
        employeesCollection.findOneAndUpdate(query,updates);
    }

    @Override
    public boolean deleteEmployee(Object id) {
        MongoCollection<Document> matchesCollection = db.getCollection("Employee");
        matchesCollection.findOneAndDelete(Filters.eq("_id", employee.getId()));
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
        MongoCollection<Document> matchesCollection = db.getCollection("Department");

        Document departmentDocument = matchesCollection.find(Filters.eq("_id", id)).first();

        if (departmentDocument != null) {
            return toDepartment(departmentDocument);
        } else {
            return null;
        }
    }

    @Override
    public Department updateDepartment(Object id) {
        MongoCollection<Document> departmentsCollection = db.getCollection("Department");
        Bson query = Filters.eq("_id",department.getId());
        Bson updates = Updates.combine(
                Updates.set("first_name", department.getName()),
                Updates.set("location", department.getLocation())
        );
        departmentsCollection.findOneAndUpdate(query,updates);
    }

    @Override
    public Department deleteDepartment(Object id) {
        MongoCollection<Document> matchesCollection = db.getCollection("Department");
        matchesCollection.findOneAndDelete(Filters.eq("_id", department.getId()));
    }

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        List<Employee> employees = new ArrayList<>();
        Bson query = Filters.eq("department_id",id);
        try (MongoCursor<Document> cursor = employeesCollection.find(query).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                employees.add(toEmployee(document));
            }
        }
        return employees;
    }


    private static Department toDepartment(Document document) {
        return new Department(
                document.getInteger("_id"),
                document.getString("name"),
                document.getString("location")
        );
    }
    private static Employee toEmployee(Document document) {
        return new Employee(
                document.getInteger("_id"),
                document.getString("name"),
                document.getString("job"),
                document.getInteger("department_id")
        );
    }

}