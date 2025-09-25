package edu.ccrm.cli;

import edu.ccrm.domain.*;

import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.util.DuplicateEnrollmentException;
import edu.ccrm.util.MaxCreditLimitExceededException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class CliManager {

    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final ImportExportService importExportService;
    private final BackupService backupService;
    private final EnrollmentService enrollmentService;

    public CliManager() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.importExportService = new ImportExportService();
        this.backupService = new BackupService();
        this.enrollmentService = new EnrollmentService();
    }

  // Starts the main application loop.
    
    public void start() {
        importExportService.importData();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1: handleStudentMenu(); break;
                case 2: handleCourseMenu(); break;
                case 3: handleEnrollmentAndGradesMenu(); break;
                case 7: 
                    System.out.println("Exporting all data...");
                    importExportService.exportData(); 
                    break;
                case 8: 
                    System.out.println("Creating data backup...");
                    backupService.createBackup(); 
                    break;
                case 9:
                    System.out.println("Saving all data...");
                    importExportService.exportData();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Thank you for using CCRM. Goodbye!");
    }

    private void printMainMenu() {
        System.out.println("\n--- CCRM Main Menu ---");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollments & Grades");
        System.out.println("--------------------------");
        System.out.println("7. Export Data");
        System.out.println("8. Backup Data");
        System.out.println("9. Save and Exit");
        System.out.print("Enter your choice: ");
    }

    // Student Menu Logic 
    private void handleStudentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add New Student");
            System.out.println("2. List All Students");
            System.out.println("3. Update Student Status");
            System.out.println("4. View Student Profile & Enrolled Courses");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getUserChoice();
            switch (choice) {
                case 1: addNewStudent(); break;
                case 2: listAllStudents(); break;
                case 3: updateStudentStatus(); break;
                case 4: viewStudentProfile(); break;
                case 9: inMenu = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addNewStudent() {
        System.out.println("\n--- Add New Student ---");
        try {
            System.out.print("Enter Registration No: ");
            String regNo = scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String fullName = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            LocalDate dob = LocalDate.parse(scanner.nextLine());
            
            studentService.addStudent(regNo, fullName, email, dob);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Please use YYYY-MM-DD. Student not added.");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private void listAllStudents() {
        System.out.println("\n--- List of All Students ---");
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found in the system.");
        } else {
            students.forEach(System.out::println);
        }
    }

    private void updateStudentStatus() {
        System.out.print("Enter student registration number to update: ");
        String regNo = scanner.nextLine();
        studentService.findStudentByRegNo(regNo).ifPresentOrElse(student -> {
            System.out.println("Current status for " + student.getFullName() + " is " + student.getStatus());
            System.out.print("Enter new status (ACTIVE, INACTIVE, GRADUATED): ");
            try {
                Student.StudentStatus newStatus = Student.StudentStatus.valueOf(scanner.nextLine().toUpperCase());
                student.setStatus(newStatus);
                System.out.println("Status updated successfully.");
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid status value. Please enter one of the listed options.");
            }
        }, () -> System.err.println("Student not found with that registration number."));
    }
    
    private void viewStudentProfile() {
        System.out.print("Enter student registration number to view profile: ");
        String regNo = scanner.nextLine();
        studentService.findStudentByRegNo(regNo).ifPresentOrElse(student -> {
            System.out.println("\n" + student.getProfileDetails());
            System.out.println("--- Enrolled Courses ---");
            if (student.getEnrolledCourses().isEmpty()) {
                System.out.println("Not enrolled in any courses.");
            } else {
                student.getEnrolledCourses().forEach(course -> System.out.println("- " + course.getTitle()));
            }
        }, () -> System.err.println("Student not found."));
    }
    
 // This is the new method that handles assigning an instructor
    private void assignInstructorToCourse() {
        System.out.println("\n--- Assign Instructor ---");
        System.out.print("Enter the course code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Enter the instructor's full name: ");
        String instructorName = scanner.nextLine();

        courseService.assignInstructor(courseCode, instructorName);
    }

    // Course Menu Logic 
    private void handleCourseMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Add New Course");
            System.out.println("2. List All Courses");
            System.out.println("3. Search Courses by Department");
            System.out.println("4. Assign Instructor to Course");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1: addNewCourse(); break;
                case 2: listAllCourses(); break;
                case 3: searchCoursesByDepartment(); break;
                case 4: assignInstructorToCourse(); break; // New case
                case 9: inMenu = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addNewCourse() {
        System.out.println("\n--- Add New Course ---");
        try {
            System.out.print("Enter Course Code (e.g., CS101): ");
            String code = scanner.nextLine();
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Credits: ");
            int credits = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Department: ");
            String dept = scanner.nextLine();
            System.out.print("Enter Semester (SPRING, SUMMER, FALL): ");
            Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());
            courseService.addCourse(code, title, credits, dept, semester);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number for credits. Course not added.");
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid value for Semester. Please use SPRING, SUMMER, or FALL. Course not added.");
        }
    }

    private void listAllCourses() {
        System.out.println("\n--- List of All Courses ---");
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found in the system.");
        } else {
            courses.forEach(System.out::println);
        }
    }

    private void searchCoursesByDepartment() {
        System.out.print("Enter department name to search for: ");
        String dept = scanner.nextLine();
        List<Course> courses = courseService.findCoursesByDepartment(dept);
        System.out.println("\n--- Courses in '" + dept + "' ---");
        if (courses.isEmpty()) {
            System.out.println("No courses found for this department.");
        } else {
            courses.forEach(System.out::println);
        }
    }
    
    //Enrollment & Grades Menu Logic
    private void handleEnrollmentAndGradesMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Enrollments & Grades ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Record Grade for Student");
            System.out.println("3. Print Student Transcript");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1: enrollStudentInCourse(); break;
                case 2: recordGradeForStudent(); break;
                case 3: printStudentTranscript(); break;
                case 9: inMenu = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void enrollStudentInCourse() {
        System.out.print("Enter student registration number: ");
        String regNo = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();
        try {
            enrollmentService.enrollStudent(regNo, courseCode);
        } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException | IllegalArgumentException e) {
            System.err.println("Enrollment Failed: " + e.getMessage());
        }
    }

    private void recordGradeForStudent() {
        System.out.print("Enter student registration number: ");
        String regNo = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Enter Grade (S, A, B, C, D, E, F): ");
        try {
            Grade grade = Grade.valueOf(scanner.nextLine().toUpperCase());
            enrollmentService.recordGrade(regNo, courseCode, grade);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid grade. Please use one of the specified letter grades.");
        }
    }
    
    private void printStudentTranscript() {
        System.out.print("Enter student registration number for transcript: ");
        String regNo = scanner.nextLine();
        studentService.findStudentByRegNo(regNo).ifPresentOrElse(student -> {
            System.out.println("\n--- TRANSCRIPT ---");
            System.out.println(student.getProfileDetails());
            System.out.println("--------------------");

            if (student.getEnrollments().isEmpty()) {
                System.out.println("No enrollment records found.");
                return;
            }

            double totalCredits = 0;
            double totalGradePoints = 0;
            
            student.getEnrollments().forEach(System.out::println);

            for (Enrollment enrollment : student.getEnrollments()) {
                if (enrollment.getGrade() != null) {
                    double credits = enrollment.getCourse().getCredits();
                    totalCredits += credits;
                    totalGradePoints += enrollment.getGrade().getGradePoint() * credits;
                }
            }

            if (totalCredits > 0) {
                double gpa = totalGradePoints / totalCredits;
                System.out.println("--------------------");
                System.out.printf("Cumulative GPA: %.2f%n", gpa);
            } else {
                System.out.println("GPA: N/A (No graded courses)");
            }

        }, () -> System.err.println("Student not found."));
    }

  
    private int getUserChoice() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // Represents an invalid (non-numeric) choice
        }
    }
}