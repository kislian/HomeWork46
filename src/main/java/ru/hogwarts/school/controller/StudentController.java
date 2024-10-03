package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.stream.Stream;
/*
The controller only has a method for calling
the service and there is no unnecessary logic.
 */
@RestController
@RequestMapping("students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestParam(name = "facultyId") long facultyId, @RequestBody Student student) {
        return studentService.createStudent(facultyId, student);
    }

    @GetMapping("{id}")
    public Student getStudent(@PathVariable long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.editStudent(id, student);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("list-by-age")
    public List<Student> filterByAge(@RequestParam int age) {
        return studentService.filterByAge(age);
    }

    @GetMapping("a-list-in-upper-case")
    public List<Student> filterByAAndInUpperCase() {
        return studentService.filterByAAndInUpperCase();
    }


    @GetMapping("list-in-age-range")
    public List<Student> findAllByAgeBetween(@RequestParam(name = "fromAge") int fromAge, @RequestParam(name = "toAge"
    ) int toAge) {
        return studentService.findAllByAgeBetween(fromAge, toAge);
    }

    @GetMapping("{id}/faculty")
    public Faculty getFaculty(@PathVariable long id) {
        return studentService.getFaculty(id);
    }

    @GetMapping("count")
    public Integer getAllStudentsCount() {
        return studentService.getAllStudentsCount();
    }

    @GetMapping("avg-age")
    public Integer getAvgAge() {
        return studentService.getAvgAge();
    }

    @GetMapping("avg-age2")
    public Integer getAvgAge2() {
        return studentService.getAvgAge2();
    }

    @GetMapping("last")
    public  List<Student> getLastNStudents(@RequestParam(name = "limit", required = false, defaultValue = "5") int limit) {
        return studentService.getLastNStudents(limit);
    }

    @GetMapping("magic-number")
    public  Integer getNumber() {
        return Stream.iterate(1, a -> a +1) .limit(1_000_000).parallel().reduce(0, (a, b) -> a + b );
    }

    @GetMapping("print-parallel")
    public void printParallel() {
        studentService.printParallel();
    }

    @GetMapping("print-synchronized")
    public void printSynhronized() {
        studentService.printSynhronized();
    }
}
