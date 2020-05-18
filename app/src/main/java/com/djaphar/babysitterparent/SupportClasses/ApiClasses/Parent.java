package com.djaphar.babysitterparent.SupportClasses.ApiClasses;

public class Parent {

    private String parent_id, relation_degree, phone, photo_link, name, surname, patronymic, child_id;

    public Parent(String parent_id, String relation_degree, String phone, String photo_link, String name, String surname, String patronymic, String child_id) {
        this.parent_id = parent_id;
        this.relation_degree = relation_degree;
        this.phone = phone;
        this.photo_link = photo_link;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.child_id = child_id;
    }

    public String getParentId() {
        return parent_id;
    }

    public String getRelationDegree() {
        return relation_degree;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoLink() {
        return photo_link;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getChildId() {
        return child_id;
    }

    public void setParentId(String parent_id) {
        this.parent_id = parent_id;
    }

    public void setRelationDegree(String relation_degree) {
        this.relation_degree = relation_degree;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhotoLink(String photo_link) {
        this.photo_link = photo_link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setChildId(String child_id) {
        this.child_id = child_id;
    }
}
