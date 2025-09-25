package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.util.DuplicateEnrollmentException;
import edu.ccrm.util.MaxCreditLimitExceededException;

public class EnrollmentService {
    public static final int MAX_CREDITS_PER_SEMESTER = 20;

    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();

    public void enrollStudent(String studentRegNo, String courseCode) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        
        Student student = studentService.findStudentByRegNo(studentRegNo)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentRegNo));

        Course course = courseService.findCourseByCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseCode));

        boolean alreadyEnrolled = student.getEnrolledCourses().contains(course);
        if (alreadyEnrolled) {
            throw new DuplicateEnrollmentException(student.getFullName() + " is already enrolled in " + course.getTitle());
        }

        int currentCredits = student.getEnrolledCourses().stream()
                .filter(c -> c.getSemester() == course.getSemester())
                .mapToInt(Course::getCredits)
                .sum();
        
        if (currentCredits + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException("Cannot enroll. Exceeds max credit limit of " + MAX_CREDITS_PER_SEMESTER);
        }

     
        Enrollment newEnrollment = new Enrollment(course);
        student.getEnrollments().add(newEnrollment);
        System.out.println("Enrollment successful!");
    }
    
  
    public void recordGrade(String studentRegNo, String courseCode, Grade grade) {
        Student student = studentService.findStudentByRegNo(studentRegNo)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentRegNo));
                
        student.getEnrollments().stream()
            .filter(enrollment -> enrollment.getCourse().getCode().equalsIgnoreCase(courseCode))
            .findFirst()
            .ifPresentOrElse(
                enrollment -> {
                    enrollment.setGrade(grade);
                    System.out.println("Grade recorded successfully.");
                },
                () -> System.err.println("Error: Student is not enrolled in this course.")
            );
    }
}
