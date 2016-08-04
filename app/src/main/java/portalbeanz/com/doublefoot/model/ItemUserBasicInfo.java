package portalbeanz.com.doublefoot.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thangit14 on 6/8/16.
 */
public class ItemUserBasicInfo {

    /**
     * email : xxxxx@gmail.com
     * password : 12345
     * device_id : xxxxxx
     */

    private String email;
    private String password;
    private transient UserType userType;
    @SerializedName("display_name")
    private String displayName;

    @SerializedName("device_id")
    private String deviceId;

    public boolean isCustomer() {
        return userType == UserType.Customer;
    }

    public boolean isMasseur() {
        return userType == UserType.Masseur;
    }
    public UserType getUserType() {
        return userType;
    }

    public ItemUserBasicInfo(String email, String password, String deviceId, UserType userType) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
        this.userType = userType;
    }

    public ItemUserBasicInfo(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public enum UserType {Customer, Masseur,}

}
