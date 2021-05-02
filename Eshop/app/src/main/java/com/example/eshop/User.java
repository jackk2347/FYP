/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

public class User {
    private String Password;
    private String FirstName;
    private String LastName;
    private String Birthday;
    private String Email;
    private String Sex;
    private int balance;

    public User(){ }

    public User(String Email,String Password,String FirstName,String LastName,String Birthday,String Sex,int balance){
        this.Password=Password;
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.Birthday=Birthday;
        this.Email = Email;
        this.Sex=Sex;
        this.balance=balance;
    }


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
