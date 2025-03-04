package DAO;

public class Employee {
    private int id;
    private String name;
    private String job;
    private int deptId;

    public Employee() {
    }

    public Employee(int id, String name, String job, int deptId) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.deptId = deptId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
