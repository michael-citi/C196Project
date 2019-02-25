package model;

public class Course {
    private int courseId;
    private String title;
    private String startDate;
    private String expectedEnd;
    private String status;
    private int termId;
    private String notes;

    // main constructor
    public Course(int courseId, String title, String startDate, String expectedEnd, String status, int termId, String notes) {
        this.courseId = courseId;
        this.title = title;
        this.startDate = startDate;
        this.expectedEnd = expectedEnd;
        this.status = status;
        this.termId = termId;
        this.notes = notes;
    }

    // partial constructor
    public Course(String title, String startDate, String expectedEnd, String status) {
        this.title = title;
        this.startDate = startDate;
        this.expectedEnd = expectedEnd;
        this.status = status;
    }

    // empty constructor
    public Course() {
    }

    // getters & setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpectedEnd() {
        return expectedEnd;
    }

    public void setExpectedEnd(String expectedEnd) {
        this.expectedEnd = expectedEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
