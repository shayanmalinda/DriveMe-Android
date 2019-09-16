package com.driveme.driveme;

public class Parents {
    public Parents(String parentemail, String parentphone, String parentaddress, String parentpass, String childname, String childage, String childschool, String childschoolphone) {
        this.parentemail = parentemail;
        this.parentphone = parentphone;
        this.parentaddress = parentaddress;
        this.parentpass = parentpass;
        this.childname = childname;
        this.childage = childage;
        this.childschool = childschool;
        this.childschoolphone = childschoolphone;
    }

    public String getParentemail() {
        return parentemail;
    }

    public String getParentphone() {
        return parentphone;
    }

    public String getParentaddress() {
        return parentaddress;
    }

    public String getParentpass() {
        return parentpass;
    }

    public String getChildname() {
        return childname;
    }

    public String getChildage() {
        return childage;
    }

    public String getChildschool() {
        return childschool;
    }

    public String getChildschoolphone() {
        return childschoolphone;
    }

    private String parentemail;
    private String parentphone;
    private String parentaddress;
    private String parentpass;
    private String childname;
    private String childage;
    private String childschool;
    private String childschoolphone;
}
