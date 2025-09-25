package edu.ccrm.domain;

public class Course {

    private final String code;
    private final String title;
    private final int credits;
    private final String department;
    private final Semester semester;
    private String instructor;

    // The constructor is now private
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.department = builder.department;
        this.semester = builder.semester;
        this.instructor = "TBD";
    }

    // Static Nested Builder Class
    public static class Builder {
        private String code;
        private String title;
        private int credits;
        private String department;
        private Semester semester;

        public Builder(String code, String title) { // Required parameters
            this.code = code;
            this.title = title;
        }

        public Builder credits(int credits) {
            this.credits = credits;
            return this; // Return builder for chaining
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }

        public Course build() {
            // Returns the final constructed Course object
            return new Course(this);
        }
    }

    //  Getters and Setters remain here 
    
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public String getInstructor() { return instructor; }
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getDepartment() { return department; }
    public Semester getSemester() { return semester; }
    
    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", credits=" + credits +
                ", instructor='" + instructor + '\'' +  '}';
    }
}