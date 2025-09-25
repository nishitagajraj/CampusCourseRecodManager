package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Service class for managing course-related operations.
 
public class CourseService {

   // Adds a new course to the data store using the Builder pattern.   @return The newly created Course object.
   
    public Course addCourse(String code, String title, int credits, String department, Semester semester) {
        Course newCourse = new Course.Builder(code, title)
                .credits(credits)
                .department(department)
                .semester(semester)
                .build();
        
        DataStore.getInstance().getCourses().add(newCourse);

        System.out.println("Course added successfully: " + newCourse.getTitle());
        return newCourse;
    }
        /*
          Assigns an instructor to a specific course.
          @param courseCode The code of the course to update.
          @param instructorName The name of the instructor to assign.
         */
        public void assignInstructor(String courseCode, String instructorName) {
            // Find the course using the existing method
            findCourseByCode(courseCode).ifPresentOrElse(
                course -> {
                    // If the course is found, set the instructor
                    course.setInstructor(instructorName);
                    System.out.println("Instructor '" + instructorName + "' assigned to course '" + course.getTitle() + "'.");
                },
                () -> {
                    // If the course is not found, print an error
                    System.err.println("Error: Course with code '" + courseCode + "' not found.");
                }
            );
        
    }

    /*
      Finds a course by its unique code.
      @param code The course code to search for.
      @return An Optional containing the course if found.
     */
    public Optional<Course> findCourseByCode(String code) {
        
        return DataStore.getInstance().getCourses().stream()
                .filter(course -> course.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    //    Retrieves a list of all courses.  @return A list of all courses.
    
    public List<Course> getAllCourses() {
        // Correct way to access the list from the Singleton DataStore ✅
        return DataStore.getInstance().getCourses();
    }

    /*
      Filters courses by department using the Stream API.
      @param department The department to filter by.
      @return A list of courses matching the department.
     */
    public List<Course> findCoursesByDepartment(String department) {
        // Correct way to access the list from the Singleton DataStore ✅
        return DataStore.getInstance().getCourses().stream()
                .filter(course -> course.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
}