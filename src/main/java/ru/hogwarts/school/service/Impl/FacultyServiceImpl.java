package ru.hogwarts.school.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.MethodNotAllowedException;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    private Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Create faculty {}", faculty);

        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty getFaculty(Long id) {
        logger.info("Find faculty by id {}", id);

        return facultyRepository.findById(id).orElseThrow(() -> new NotFoundException("Faculty is not found for id" + id));
    }

    @Override
    public Faculty editFaculty(Long id, Faculty faculty) {
        logger.info("Edit faculty {}", id);

        Faculty facultyFromDb = getFaculty(id);
        facultyFromDb.setName(faculty.getName());
        facultyFromDb.setColor(faculty.getColor());
        return facultyRepository.save(facultyFromDb);
    }

    @Override
    public void deleteFaculty(Long id) {
        logger.info("Delete faculty {}", id);

        Faculty faculty = getFaculty(id);
        if (faculty.getStudents() != null && !faculty.getStudents().isEmpty()) {
            logger.error("Can't delete faculty {}. It is not found", id);
            throw new MethodNotAllowedException("Faculty with stundets is not allowed to delete ");
        }
        facultyRepository.delete(faculty);
    }

    @Override
    public List<Faculty> findAll(String name, String color) {
        logger.info("Find all faculty with name {} and color {}", name, color);

        if (name != null && color != null) {
            return facultyRepository.findAllByNameIgnoreCaseAndColorIgnoreCase(name, color);
        } else if (name != null) {
            return facultyRepository.findAllByNameIgnoreCase(name);
        } else if (color != null) {
            return facultyRepository.findAllByColorIgnoreCase(color);
        } else {
            return facultyRepository.findAll();
        }
    }

    @Override
    public List<Student> getStudents(Long facultyId) {
        logger.info("Find students by facultyId {}", facultyId);

        return getFaculty(facultyId).getStudents();
    }

    @Override
    public String getLongestName() {
        return facultyRepository.findAll().stream().map(faculty -> faculty.getName()).reduce((x, y) -> x.length() > y.length() ? x : y).get();
    }
}
