package model;

public class Instructor {
    private int instructorId;
    private int courseId;
    private String name;
    private String phone;
    private String email;

    // main constructor
    public Instructor(int instructorId, int courseId, String name, String phone, String email) {
        this.instructorId = instructorId;
        this.courseId = courseId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // partial constructor
    public Instructor(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // empty constructor
    public Instructor() {
    }

    // getters & setters
    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
