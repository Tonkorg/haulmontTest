package ru.tyaga.haulmonttest.services;

import org.springframework.stereotype.Service;


import ru.tyaga.haulmonttest.entity.Student;
import ru.tyaga.haulmonttest.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service

public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Optional<Student> existingStudentOpt = studentRepository.findById(id);
        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setLastName(updatedStudent.getLastName());
            existingStudent.setPatronymic(updatedStudent.getPatronymic());
            existingStudent.setBirthDate(updatedStudent.getBirthDate());
            existingStudent.setGroup(updatedStudent.getGroup());
            return studentRepository.save(existingStudent);
        } else {
            throw new IllegalArgumentException("Студент с ID " + id + " не найден.");
        }
    }

    public void deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Студент с ID " + id + " не найден.");
        }
    }

    public List<Student> filterByLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            return findAllStudents();
        }
        return studentRepository.findByLastNameContaining(lastName);
    }

    public List<Student> getStudentsByGroupId(Long groupId) {
        return studentRepository.findByGroup_Id(groupId);
    }
}
