package model;

public class Objective {
    private int objectiveId;
    private String title;
    private int courseId;
    private String time;

    // main constructor
    public Objective(int objectiveId, String title, int courseId, String time) {
        this.objectiveId = objectiveId;
        this.title = title;
        this.courseId = courseId;
        this.time = time;
    }

    // partial constructor
    public Objective(String title, String time) {
        this.title = title;
        this.time = time;
    }

    // empty constructor
    public Objective() {
    }

    public int getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(int objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
