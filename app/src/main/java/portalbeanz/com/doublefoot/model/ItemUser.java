package portalbeanz.com.doublefoot.model;

import portalbeanz.com.doublefoot.util.NetworkConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by thangit14 on 6/10/16.
 */
public class ItemUser implements Serializable {
    public static final int CUSTOMER = 0;
    public static final int MASSEUR = 1;

    private int type = CUSTOMER;
    private int userId;
    private int kind;
    private String firstName;
    private String lastName;
    private int gender;
    private String phone;
    private String email;
    private String introduce;
    private String avatarUrl;
    private int age;
    private String bod;
    private int workingExperience = 0;
    private String specialization = "Massage Therapy";
    private String displayName;

    private HashMap<Date,Date> bookedTimes;
    private ArrayList<Date> unavailableDates;

    public HashMap<Date,Date> getBookedTimes() {
        return bookedTimes;
    }

    public void setBookedTimes(HashMap<Date,Date> bookedTimes) {
        this.bookedTimes = bookedTimes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAvatarUrl() {
        if (avatarUrl.length() != 0 && !avatarUrl.equals("")) {
            return NetworkConstant.DOMAIN + avatarUrl;
        }
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBod() {
        return bod;
    }

    public void setBod(String bod) {
        this.bod = bod;
    }

    public int getWorkingExperience() {
        return workingExperience;
    }

    public void setWorkingExperience(int workingExperience) {
        this.workingExperience = workingExperience;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getType() {
        return type;
    }

    public boolean isCustomer() {
        return type == CUSTOMER;
    }

    public boolean isMasseur() {
        return type == MASSEUR;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ArrayList<Date> getUnavailableDates() {
        return unavailableDates;
    }

    public void setUnavailableDates(ArrayList<Date> unavailableDates) {
        this.unavailableDates = unavailableDates;
    }
}
