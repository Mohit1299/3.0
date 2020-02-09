package com.example.a30;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class Upload {

    private String Name;
    private String Phone;
    private String ReportingPerson;
    private String Purpose;
    private String Electronics;
    private String CurrentDateandTime;

    private String imageURL;

    public Upload() {

    }


    Date date=java.util.Calendar.getInstance().getTime();
    String currentdateandtime = date.toString();

    public Upload(String name,String phone,String reportingPerson,String purpose,String electronics,String currentdateandtime, String url) {

        this.Name = name;
        this.Phone = phone;
        this.ReportingPerson=reportingPerson;
        this.Purpose = purpose;
        this.Electronics = electronics;
        this.imageURL= url;
        this.CurrentDateandTime = currentdateandtime;
    }

    public String getImageName() {
        return Name;
    }
    public String getPhone() {
        return Phone;
    }
    public String getReportingPerson() {
        return ReportingPerson;
    }
    public String getPurpose(){return Purpose;}
    public String getElectronics(){return Electronics;}
    public String getCurrentdateandtime(){return CurrentDateandTime;}

    public String getImageURL() {
        return imageURL;
    }
}
