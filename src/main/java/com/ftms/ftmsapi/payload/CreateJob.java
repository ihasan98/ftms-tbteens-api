package com.ftms.ftmsapi.payload;
import javax.validation.constraints.NotBlank;


public class CreateJob {
    @NotBlank
    private String jobTitle;

    @NotBlank
    private String description;

    @NotBlank
    private String siteName;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
