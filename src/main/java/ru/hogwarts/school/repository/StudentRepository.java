package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findAllByAgeBetween(int fromAge, int toAge);

    @Query("SELECT COUNT(*) FROM Student")
    Integer getAllCount();

    @Query("SELECT AVG(s.age) FROM Student s")
    Integer getAvgAge();
}
