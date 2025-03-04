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

public class Placeholder implements Closeable, IDAO {
    static MongoClient mongoClient;
    static MongoDatabase db;
    static Placeholder placeholder;

    private Placeholder() {
        String connectionString = "mongodb+srv://admin:0000@cluster0.0pqj1.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        mongoClient = MongoClients.create(connectionString);
        db = mongoClient.getDatabase("GroupProject");
        placeholder = this;
    }

    public static boolean createConnection() {
        if (placeholder == null) {
            try {
                placeholder = new Placeholder();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    public static Placeholder getConnection() {
        return placeholder;
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

    public void updateDepartment(Department department) {
        MongoCollection<Document> departmentsCollection = db.getCollection("Department");
        Bson query = Filters.eq("_id",department.getId());
        Bson updates = Updates.combine(
                Updates.set("first_name", department.getName()),
                Updates.set("location", department.getLocation())
        );
        departmentsCollection.findOneAndUpdate(query,updates);
    }
    public void updateEmployee(Employee employee) {
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        Bson query = Filters.eq("_id",employee.getId());
        Bson updates = Updates.combine(
                Updates.set("name", employee.getName()),
                Updates.set("job", employee.getJob()),
                Updates.set("department_id", employee.getDeptId())
        );
        employeesCollection.findOneAndUpdate(query,updates);
    }

    public void deleteDepertment(Department department){
        MongoCollection<Document> matchesCollection = db.getCollection("Department");
        matchesCollection.findOneAndDelete(Filters.eq("_id", department.getId()));
    }
    public void deleteEmployee(Employee employee){
        MongoCollection<Document> matchesCollection = db.getCollection("Employee");
        matchesCollection.findOneAndDelete(Filters.eq("_id", employee.getId()));
    }


    public Department getDepartment(int id){
        MongoCollection<Document> matchesCollection = db.getCollection("Department");
        try (MongoCursor<Document> cursor = matchesCollection.find(Filters.eq("_id", id)).iterator()) {
            if (cursor.hasNext()) {
                Document document = cursor.next();
                return toDepartment(document);
            }
        }
        return null;
    }
    public Employee getEmployee(int id){
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
    public List<Employee> findAllEmployees() {
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        List<Employee> leagues = new ArrayList<>();
        try (MongoCursor<Document> cursor = employeesCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                leagues.add(toEmployee(document));
            }
        }
        return leagues;
    }

    @Override
    public Employee findEmployeeById(Object id) {
        return null;
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
        MongoCollection<Document> employeesCollection = db.getCollection("Employee");
        List<Employee> leagues = new ArrayList<>();
        try (MongoCursor<Document> cursor = employeesCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                leagues.add(toEmployee(document));
            }
        }
        return leagues;
    }

    @Override
    public Department findDepartmentById(Object id) {
        MongoCollection<Document> matchesCollection = db.getCollection("Department");
        try (MongoCursor<Document> cursor = matchesCollection.find(Filters.eq("_id", id)).iterator()) {
            if (cursor.hasNext()) {
                Document document = cursor.next();
                return toDepartment(document);
            }
        }
        return null;
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