package portalbeanz.com.doublefoot.model;

import java.io.Serializable;

/**
 * Created by datnx on 12/16/14.
 */
public class ItemGender implements Serializable {
    public static final int MALE = 1;
    public static final int FEMALE = 0;
    private int gender;
    private String title;
    private boolean isSelected;

    public ItemGender(int gender, String title, boolean isSelected) {
        this.gender = gender;
        this.title = title;
        this.isSelected = isSelected;
    }

    public ItemGender(int gender) {
        this.gender = gender;
    }

    public ItemGender() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void updateSelected() {
        isSelected = !isSelected;
    }

    public int getGender() {
        return gender;
    }


    public void setGender(int gender) {
        this.gender = gender;
    }
}
