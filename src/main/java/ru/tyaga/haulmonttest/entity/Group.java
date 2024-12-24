package ru.tyaga.haulmonttest.entity;

import jakarta.persistence.*;

import java.util.List;


//@Data
@Entity
@Table(name = "student_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private String faculty;

    @OneToMany(mappedBy = "group")
    private List<Student> students;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        if (number != null || faculty != null) {
            return number + " " + faculty; // Например, возвращаем название и ID группы
        }else return "Не выбрана";
    }
}
