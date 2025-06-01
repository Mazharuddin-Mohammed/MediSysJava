package com.medisys.desktop.model;

public class Department {
    private Long id;
    private String name;
    private String headOfDepartment;

    // Constructors
    public Department() {}

    public Department(Long id, String name, String headOfDepartment) {
        this.id = id;
        this.name = name;
        this.headOfDepartment = headOfDepartment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", headOfDepartment='" + headOfDepartment + '\'' +
                '}';
    }
}