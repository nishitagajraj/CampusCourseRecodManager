package edu.ccrm.domain;

import java.time.LocalDateTime;

public class Enrollment {
    private final Course course;
    private Grade grade;
    private final LocalDateTime enrollmentDate;

    public Enrollment(Course course) {
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
        this.grade = null; // Grade is null until assigned
    }

    public Course getCourse() {
        return course;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    @Override
    public String toString() {
        String gradeStr = (grade == null) ? "In Progress" : grade.name() + " (" + grade.getGradePoint() + ")";
        return String.format("Course: %-40s | Grade: %s", course.getTitle() + " (" + course.getCode() + ")", gradeStr);
    }
}