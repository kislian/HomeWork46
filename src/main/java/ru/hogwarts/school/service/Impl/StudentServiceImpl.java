package ru.hogwarts.school.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final FacultyService facultyService;

    public StudentServiceImpl(StudentRepository studentRepository, FacultyService facultyService) {
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }

    @Override
    public Student getStudent(Long id) {
        LOGGER.info("Find student by id {}", id);

        return studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Student is not found for id" + id));
    }

    @Override
    public Student createStudent(long facultyId, Student student) {
        LOGGER.info("Create student {} with facultyId {}", student, facultyId);

        Faculty faculty = facultyService.getFaculty(facultyId);
        student.setFaculty(faculty);
        return studentRepository.save(student);
    }

    @Override
    public Student editStudent(Long id, Student student) {
        LOGGER.info("Edit student {} with id {}", student, id);

        Student studentFromBd = getStudent(id);
        studentFromBd.setName(student.getName());
        studentFromBd.setAge(student.getAge());
        return studentRepository.save(studentFromBd);
    }

    @Override
    public void deleteStudent(Long id) {
        LOGGER.info("Delete student with id {}", id);

        studentRepository.delete(getStudent(id));
    }

    @Override
    public List<Student> filterByAge(int age) {
        LOGGER.info("Filter by age {}", age);

        return studentRepository.findAllByAge(age);
    }

    @Override
    public List<Student> filterByAAndInUpperCase() {
        return studentRepository.findAll().stream().
                filter(student -> student.getName().startsWith("A"))
                .map(student -> {
                    student.setName(student.getName().toUpperCase());
                    return student;
                }).collect(Collectors.toList());
    }

    @Override
    public List<Student> findAllByAgeBetween(int fromAge, int toAge) {
        LOGGER.info("Find all by age between {} {}", fromAge, toAge);

        return studentRepository.findAllByAgeBetween(fromAge, toAge);
    }

    @Override
    public Faculty getFaculty(Long studentId) {
        LOGGER.info("Get faculty for studentId {}", studentId);

        return getStudent(studentId).getFaculty();
    }

    @Override
    public Integer getAllStudentsCount() {
        LOGGER.info("Get all students Count");

        return studentRepository.getAllCount();
    }

    @Override
    public Integer getAvgAge() {
        LOGGER.info("Get avg Age");

        return studentRepository.getAvgAge();
    }

    @Override
    public Integer getAvgAge2() {
        LOGGER.info("Get avg Age");

        return (int) studentRepository.findAll().stream().mapToInt(student -> student.getAge()).average().getAsDouble();
    }

    @Override
    public List<Student> getLastNStudents(int limit) {
        return studentRepository.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
    }

    @Override
    public void printParallel() {
        List<Student> students = studentRepository.findAll();
        LOGGER.info(students.get(0).getName());
        LOGGER.info(students.get(1).getName());
        new Thread(() -> {
            LOGGER.info(students.get(2).getName());
            LOGGER.info(students.get(3).getName());
        }).start();
        new Thread(() -> {
            LOGGER.info(students.get(4).getName());
            LOGGER.info(students.get(5).getName());
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printSynhronized() {
        List<Student> students = studentRepository.findAll();
        printName(students.get(0));
        printName(students.get(1));
        new Thread(() -> {
            printName(students.get(2));
            printName(students.get(3));
        }).start();
        new Thread(() -> {
            printName(students.get(4));
            printName(students.get(5));
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void printName(Student student) {
        synchronized (this) {
            LOGGER.info(String.format("Print with sync on monitor %s : name = %s", this, student.getName()));
        }
    }
}


