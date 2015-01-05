/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.pfe_project.model;

/**
 *
 * @author Pierre Ghazal
 */
public class Employee implements ModelInterface {
    private String lastname;
    private String firstname;
    private String birthday;
    private Integer age;
    private String gender;
    
    public Employee() {
        this("lastname", "firstname", "dd/mm/yyyy", 30, "gender");
    }

    public Employee(String lastname, String firstname, String birthday, Integer age, String gender) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthday = birthday;
        this.age = age;
        this.gender = gender;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    
}
