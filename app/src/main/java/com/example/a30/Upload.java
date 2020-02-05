package com.example.a30;

class Upload {

    private String Name;
    private String Phone;
    private String ReportingPerson;

    private String imageURL;

    public Upload() {

    }

    public Upload(String name,String phone,String reportingPerson, String url) {

        this.Name = name;
        this.Phone = phone;
        this.ReportingPerson=reportingPerson;
        this.imageURL= url;
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

    public String getImageURL() {
        return imageURL;
    }
}
