package com.nucleuslife.restaurantreview.structures;

public class Citation
{
    private String critical;
    private String inspectionType;
    private String inspectionDate;
    private String grade;
    private String violationCode;
    private String violationDesciption;

    public Citation(String critical, String inspectionType, String inspectionDate, String grade, String violationCode, String violationDesciption)
    {
        this.critical = critical;
        this.inspectionType = inspectionType;
        this.inspectionDate = inspectionDate;
        this.grade = grade;
        this.violationCode = violationCode;
        this.violationDesciption = violationDesciption;
    }

    public String getCritical()
    {
        return critical;
    }

    public String getInspectionType()
    {
        return inspectionType;
    }

    public String getInspectionDate()
    {
        return inspectionDate;
    }

    public String getGrade()
    {
        return grade;
    }

    public String getViolationCode()
    {
        return violationCode;
    }

    public String getCitationDescription()
    {
        return violationDesciption;
    }
}
