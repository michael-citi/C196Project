package model;

public class Term {
    private int termId;
    private String title;
    private String startDate;
    private String endDate;

    // main constructor
    public Term(int termId, String title, String startDate, String endDate) {
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // partial constructor
    public Term(String title, String startDate, String endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // empty constructor
    public Term() {
    }

    // getters & setters
    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
