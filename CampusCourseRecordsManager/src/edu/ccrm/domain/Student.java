package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Student extends Person {

    private final String regNo;
    private StudentStatus status;
    private final List<Enrollment> enrollments;
    private final LocalDateTime registrationDate;

    public enum StudentStatus {
        ACTIVE,
        INACTIVE,
        GRADUATED
    }

    public Student(String regNo, String fullName, String email, LocalDate dateOfBirth) {
        super(fullName, email, dateOfBirth);
        this.regNo = regNo;
        this.status = StudentStatus.ACTIVE;
        this.enrollments = new ArrayList<>(); // Initialize the new list
        this.registrationDate = LocalDateTime.now();
    }

    //Getters and Setters
    public String getRegNo() { return regNo; }
    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }

   
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
  
    public List<Course> getEnrolledCourses() {
        List<Course> courses = new ArrayList<>();
        for (Enrollment e : enrollments) {
            courses.add(e.getCourse());
        }
        return courses;
    }

    @Override
    public String getProfileDetails() {
        return String.format("Student Profile:\nID: %s\nReg No: %s\nName: %s\nEmail: %s\nStatus: %s",
                getId(), getRegNo(), getFullName(), getEmail(), getStatus());
    }

    @Override
    public String toString() {
        return "Student{" + "regNo='" + regNo + '\'' + ", fullName='" + getFullName() + '\'' + ", status=" + status + '}';
    }
}