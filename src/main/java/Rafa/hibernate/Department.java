package Rafa.hibernate;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "departamento")
public class Department {
    @Id
    @Column(name = "depno", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 14)
    private String name;

    @Column(name = "ubicacion", length = 13)
    private String city;

    @OneToMany(mappedBy = "depno")
    private Set<Rafa.hibernate.Employee> employees = new LinkedHashSet<>();


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

    public String getCity() {
        return city;
    }

    public void setCity(String location) {
        this.city = location;
    }

    public Set<Rafa.hibernate.Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Rafa.hibernate.Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Department [id=" + id + ", name=" + name + ", location=" + city + "]";
    }

}