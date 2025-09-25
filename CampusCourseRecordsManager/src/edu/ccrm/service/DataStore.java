package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Student;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    // 1. A private static final instance of the class itself.
    private static final DataStore instance = new DataStore();

    // 2. Data fields are no longer static. They belong to the instance.
    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();

    // 3. Private constructor to prevent anyone else from creating an instance.
    private DataStore() {}

    // 4. Public static method to get the single instance.
    public static DataStore getInstance() {
        return instance;
    }

    // 5. Public methods to access the data lists.
    public List<Student> getStudents() {
        return students;
    }

    public List<Course> getCourses() {
        return courses;
    }
}