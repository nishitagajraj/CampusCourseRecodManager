package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/*
  Service class for managing student-related operations.
  This class contains the business logic for students.
 */
public class StudentService {

    /*
      Adds a new student to the data store.
      @param regNo The registration number.
      @param fullName The student's full name.
      @param email The student's email.
      @param dateOfBirth The student's date of birth.
      @return The newly created Student object.
     */
    public Student addStudent(String regNo, String fullName, String email, LocalDate dateOfBirth) {
        Student newStudent = new Student(regNo, fullName, email, dateOfBirth);
        // Correct way to access the list from the Singleton DataStore
        DataStore.getInstance().getStudents().add(newStudent);
        System.out.println("Student added successfully: " + newStudent.getFullName());
        return newStudent;
    }

    /*
      Finds a student by their registration number.
      @param regNo The registration number to search for.
     *@return An Optional containing the student if found, otherwise an empty Optional.
     */
    public Optional<Student> findStudentByRegNo(String regNo) {
        // Correct way to access the list from the Singleton DataStore
        return DataStore.getInstance().getStudents().stream()
                .filter(student -> student.getRegNo().equalsIgnoreCase(regNo))
                .findFirst();
    }

    /*
      Retrieves a list of all students.
      @return A list of all students in the data store.
     */
    public List<Student> getAllStudents() {
        // This line is already correct!
        return DataStore.getInstance().getStudents();
    }
}