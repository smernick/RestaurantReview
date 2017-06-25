package com.nucleuslife.restaurantreview.structures;

public class Citation
{
    private String critical;
    private String inspectionType;
    private String inspectionDate;
    private String score;
    private String violationCode;
    private String violationDesciption;

    public Citation(String critical, String inspectionType, String inspectionDate, String score, String violationCode, String violationDesciption)
    {
        this.critical = critical;
        this.inspectionType = inspectionType;
        this.inspectionDate = inspectionDate;
        this.score = score;
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

    public String getScore()
    {
        return score;
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
