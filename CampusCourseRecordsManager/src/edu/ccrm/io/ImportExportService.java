package edu.ccrm.io;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Student;
import edu.ccrm.service.DataStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportExportService {
    private static final Path DATA_DIRECTORY = Paths.get("data");
    private static final Path STUDENTS_FILE = DATA_DIRECTORY.resolve("students.csv");
    private static final Path COURSES_FILE = DATA_DIRECTORY.resolve("courses.csv");

    public void exportData() {
        try {
            if (!Files.exists(DATA_DIRECTORY)) {
                Files.createDirectories(DATA_DIRECTORY);
            }

            // Use the Singleton getInstance() method to get the student list
            List<String> studentLines = DataStore.getInstance().getStudents().stream()
                .map(s -> String.join(",", s.getRegNo(), s.getFullName(), s.getEmail(), s.getDateOfBirth().toString(), s.getStatus().name()))
                .collect(Collectors.toList());
            Files.write(STUDENTS_FILE, studentLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Successfully exported " + studentLines.size() + " students.");

            // Use the Singleton getInstance() method to get the course list
            List<String> courseLines = DataStore.getInstance().getCourses().stream()
                .map(c -> String.join(",", c.getCode(), c.getTitle(), String.valueOf(c.getCredits()), c.getDepartment(), c.getSemester().name()))
                .collect(Collectors.toList());
            Files.write(COURSES_FILE, courseLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Successfully exported " + courseLines.size() + " courses.");

        } catch (IOException e) {
            System.err.println("Error exporting data: " + e.getMessage());
        }
    }

    public void importData() {
        if (Files.exists(STUDENTS_FILE)) {
            try (Stream<String> lines = Files.lines(STUDENTS_FILE)) {
                List<Student> importedStudents = lines.map(line -> {
                    String[] parts = line.split(",");
                    Student student = new Student(parts[0], parts[1], parts[2], LocalDate.parse(parts[3]));
                    student.setStatus(Student.StudentStatus.valueOf(parts[4]));
                    return student;
                }).collect(Collectors.toList());
                
            
                DataStore.getInstance().getStudents().clear();
                DataStore.getInstance().getStudents().addAll(importedStudents);
                System.out.println("Successfully imported " + importedStudents.size() + " students.");
            } catch (IOException e) {
                System.err.println("Error importing students: " + e.getMessage());
            }
        }
        
        if (Files.exists(COURSES_FILE)) {
             try (Stream<String> lines = Files.lines(COURSES_FILE)) {
                List<Course> importedCourses = lines.map(line -> {
                    String[] parts = line.split(",");
                    // FIX: Use the Builder pattern to create the Course
                    return new Course.Builder(parts[0], parts[1])
                            .credits(Integer.parseInt(parts[2]))
                            .department(parts[3])
                            .semester(Semester.valueOf(parts[4]))
                            .build();
                }).collect(Collectors.toList());
          
                DataStore.getInstance().getCourses().clear();
                DataStore.getInstance().getCourses().addAll(importedCourses);
                System.out.println("Successfully imported " + importedCourses.size() + " courses.");
            } catch (IOException e) {
                System.err.println("Error importing courses: " + e.getMessage());
            }
        }
    }
}