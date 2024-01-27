package com.entities;

public class DeptAndJobTitle {
    private String dept;
    private String jobTitle;

    public DeptAndJobTitle(String dept, String jobTitle) {
        this.dept = dept;
        this.jobTitle = jobTitle;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
