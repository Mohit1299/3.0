package com.example.a30;

class Upload {

    private String mName;
    private String mPhone;
    private String mReportingPerson;
    private String mUrl;

    public Upload()
    {

    }

    public Upload(String Name,String Phone,String ReportingPerson)
    {
        this.mName = Name;
        this.mPhone = Phone;
        this.mReportingPerson = ReportingPerson;
    }

    public String getmName()
    {
        return mName;
    }

    public void setmName (String Name)
    {
        mName = Name ;
    }

    public String getmPhone()
    {
        return mPhone;
    }

    public void setmPhone (String Phone)
    {
        mPhone = Phone ;
    }

    public String getmReportingPerson()
    {
        return mReportingPerson;
    }

    public void setmReportingPerson (String ReportingPerson)
    {
        mReportingPerson = ReportingPerson ;
    }

    public String getmUri()
    {
        return mUrl;
    }

    public void setmUri (String Url)
    {
        mUrl = Url ;
    }

}
