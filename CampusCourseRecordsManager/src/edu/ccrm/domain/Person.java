package edu.ccrm.domain;
import java.time.LocalDate;
import java.util.UUID;

/*
  An abstract base class representing a person in the institution.
   It demonstrates Abstraction and serves as the parent for Student and Instructor.
 */
public abstract class Person {

    // Encapsulation: fields are private 
    private final String id;
    private String fullName;
    private String email;
    private final LocalDate dateOfBirth;

    /*Constructor for the Person class.
     Initializes common properties for all persons.
      @param fullName    The full name of the person.
      @param email       The email address of the person.
      @param dateOfBirth The date of birth of the person.
     */
    public Person(String fullName, String email, LocalDate dateOfBirth) {
        this.id = UUID.randomUUID().toString(); // Generate a unique ID
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    // Abstract method to be implemented by subclasses 
    public abstract String getProfileDetails();

    // Getters and Setters (Encapsulation)

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
