package portalbeanz.com.doublefoot.model;

import portalbeanz.com.doublefoot.util.NetworkConstant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by thangit14 on 7/13/16.
 */
public class ItemPackage implements Serializable{
    private int id;
    private String title;
    private String image;
    private String description;

    public ArrayList<ItemPackagePrice> getItemPackagePrices() {
        return itemPackagePrices;
    }

    public void setItemPackagePrices(ArrayList<ItemPackagePrice> itemPackagePrices) {
        this.itemPackagePrices = itemPackagePrices;
    }

    private ArrayList<ItemPackagePrice> itemPackagePrices;

    public ItemPackage() {
    }

    public ItemPackage(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return NetworkConstant.DOMAIN + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
