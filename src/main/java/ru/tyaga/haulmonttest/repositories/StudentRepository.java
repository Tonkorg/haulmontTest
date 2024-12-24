package ru.tyaga.haulmonttest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tyaga.haulmonttest.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByLastNameContaining(String lastName);
    List<Student> findByGroup_Id(Long groupId);
}
