package portalbeanz.com.doublefoot.model;

import java.io.Serializable;

/**
 * Created by thangit14 on 7/14/16.
 */
public class ItemPackagePrice implements Serializable{
    private int time;
    private float price;
    private float pricePlus;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPricePlus() {
        return pricePlus;
    }

    public void setPricePlus(float pricePlus) {
        this.pricePlus = pricePlus;
    }
}
