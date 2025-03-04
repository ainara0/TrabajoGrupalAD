package Ainara.mongoDB;

import DAO.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.Closeable;

public class Connection implements Closeable {
    static MongoClient mongoClient;
    static MongoDatabase db;
    static Connection connection;

    private Connection() {
        String connectionString = "mongodb+srv://admin:0000@cluster0.0pqj1.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        mongoClient = MongoClients.create(connectionString);
        db = mongoClient.getDatabase("GroupProject");
        connection = this;
    }

    public static boolean createConnection() {
        if (connection == null) {
            try {
                connection = new Connection();
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

    public static Connection getConnection() {
        return connection;
    }

    public void insertDepartment(Department department) {
        MongoCollection<Document> departmentsCollection = db.getCollection("Department");
        Document document = new Document()
                .append("_id", department.getId())
                .append("name", department.getName())
                .append("location", department.getLocation());
        departmentsCollection.insertOne(document);
    }
    public void insertEmployee(Employee employee) {
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