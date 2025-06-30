package com.laplateforme.tracker.model;

/**
 * Represents a student entity.
 */
public class Student {
  private int id;
  private String firstName;
  private String lastName;
  private int age;
  private double grade;

  // Default constructor
  public Student() {
  }

  // Parameterized constructor
  public Student(int id, String firstName, String lastName, int age, double grade) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.grade = grade;
  }

  // Parameterized constructor without id (for inserts)
  public Student(String firstName, String lastName, int age, double grade) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.grade = grade;
  }

  // Getters and setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public double getGrade() {
    return grade;
  }

  public void setGrade(double grade) {
    this.grade = grade;
  }

  @Override
  public String toString() {
    return "Student{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", age=" + age +
        ", grade=" + grade +
        '}';
  }
}