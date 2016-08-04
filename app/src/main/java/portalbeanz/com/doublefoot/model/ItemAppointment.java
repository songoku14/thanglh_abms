package portalbeanz.com.doublefoot.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by thangit14 on 6/10/16.
 */
public class ItemAppointment implements Serializable {
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    private int appointmentId;
    private int status;

    private ItemUser itemUser;

    private Date startDate = null;
    private Date endDate = null;

    public ItemAppointment(int itemType) {
        this.itemType = itemType;
    }

    public ItemAppointment() {
    }

    /**
     * user for section listview
     */
    private int itemType = 0;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Date getStartDate() {

        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ItemUser getItemUser() {
        return itemUser;
    }

    public void setItemUser(ItemUser itemUser) {
        this.itemUser = itemUser;
    }

    public boolean isItemType() {
        return itemType == ITEM;
    }

    public boolean isHeaderType() {
        return !isItemType();
    }
}
