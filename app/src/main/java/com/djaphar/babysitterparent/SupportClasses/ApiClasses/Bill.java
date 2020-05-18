package com.djaphar.babysitterparent.SupportClasses.ApiClasses;

public class Bill {

    private String bill_id, theme, comment;
    private Child child;
    private Boolean status;
    private Float sum;

    public Bill(String bill_id, String theme, String comment, Child child, Boolean status, Float sum) {
        this.bill_id = bill_id;
        this.theme = theme;
        this.comment = comment;
        this.child = child;
        this.status = status;
        this.sum = sum;
    }

    public String getBillId() {
        return bill_id;
    }

    public String getTheme() {
        return theme;
    }

    public String getComment() {
        return comment;
    }

    public Child getChild() {
        return child;
    }

    public Boolean getStatus() {
        return status;
    }

    public Float getSum() {
        return sum;
    }

    public void setBillId(String bill_id) {
        this.bill_id = bill_id;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }
}
