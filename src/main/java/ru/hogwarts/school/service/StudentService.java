package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {
    Student getStudent(Long id);

    Student createStudent(long facultyId, Student student);

    Student editStudent(Long id, Student student);

    void deleteStudent(Long id);

    List<Student> filterByAge(int age);

    List<Student> filterByAAndInUpperCase();

    List<Student> findAllByAgeBetween(int fromAge, int toAge);

    Faculty getFaculty(Long studentId);

    Integer getAllStudentsCount();

    Integer getAvgAge();

    Integer getAvgAge2();

    List<Student> getLastNStudents(int limit);
    void printParallel();

    void printSynhronized();
}