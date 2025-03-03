package Hibernate;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_gen")
    @SequenceGenerator(name = "employee_id_gen", sequenceName = "employee_empno_seq", allocationSize = 1)
    @Column(name = "empno", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 10)
    private String name;

    @Column(name = "puesto", length = 15)
    private String job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depno")
    private Department depno;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Department getDepno() {
        return depno;
    }

    public void setDepno(Department depno) {
        this.depno = depno;
    }
    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name=" + name + ", job=" + job + "depno=" + depno + '}';
    }

}