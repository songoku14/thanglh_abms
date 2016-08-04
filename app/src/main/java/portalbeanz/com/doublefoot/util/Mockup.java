package portalbeanz.com.doublefoot.util;

import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.model.ItemUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

/**
 * Created by thangit14 on 6/11/16.
 */
public class Mockup {

    public static ArrayList<ItemAppointment> getListAppointment(boolean isCustomer) {
        ArrayList<ItemAppointment> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        ItemAppointment itemAppointment;
        for (int i = 0; i < 13; i++) {

            if (i == 0) {
                itemAppointment = new ItemAppointment(1);

                itemAppointment.setStartDate(calendar.getTime());
            } else if (i == 7) {
                itemAppointment = new ItemAppointment(1);
                calendar.add(Calendar.DATE, 1);
                itemAppointment.setStartDate(calendar.getTime());

            } else {
                itemAppointment = new ItemAppointment(0);
                itemAppointment.setStartDate(calendar.getTime());
                ItemUser itemUser = getItemUser(isCustomer ? ItemUser.CUSTOMER : ItemUser.MASSEUR);
                itemAppointment.setItemUser(itemUser);
            }
            itemAppointment.setAppointmentId(i);
            result.add(itemAppointment);
        }
        return result;
    }

    public static ItemUser getItemUser(int itemType) {
        ItemUser itemUser = new ItemUser();
        itemUser.setType(itemType);
        itemUser.setAge(11);
        itemUser.setAvatarUrl("http://www.cfau-pd.net/data/wallpapers/229/WDF_2657113.jpg");
        itemUser.setBod("1999/1/1");
        itemUser.setEmail("test@gmail.com");
        itemUser.setIntroduce("description");
        itemUser.setKind(1);
        itemUser.setWorkingExperience(3);
        itemUser.setGender(1);
        itemUser.setUserId(11111);
        itemUser.setFirstName("Le Hoang");
        itemUser.setLastName("Thang");
        itemUser.setPhone("0988888888");
//        itemUser.setA
        return itemUser;
    }

    public static ArrayList<ItemUser> getItemMasseurs() {
        ArrayList<ItemUser> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemUser itemUser = getItemUser(ItemUser.MASSEUR);
            itemUser.setUserId(i);
            result.add(itemUser);
        }
        return result;
    }

    public static ArrayList<ItemUser> getItemCustomers() {
        ArrayList<ItemUser> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemUser itemUser = getItemUser(ItemUser.CUSTOMER);
            itemUser.setUserId(i);
            result.add(itemUser);
        }
        return result;
    }

    public static LinkedHashMap<Integer, Integer[]> getAvailabeTime() {
        LinkedHashMap<Integer, Integer[]> availableTime = new LinkedHashMap<>();
        availableTime.put(1, new Integer[]{0, 2, 4, 8});
        availableTime.put(2, new Integer[]{0, 3, 4, 8});
        availableTime.put(5, new Integer[]{0, 8, 10, 14, 22, 24, 25, 26, 44, 60});
        availableTime.put(6, new Integer[]{0, 4, 10, 14, 26, 44, 60});
        availableTime.put(7, new Integer[]{0, 1, 19});
        availableTime.put(10, new Integer[]{0, 1, 8});
        availableTime.put(21, new Integer[]{0, 1, 10, 14, 26, 44, 60});
        availableTime.put(22, new Integer[]{0, 1, 4, 8});
        availableTime.put(23, new Integer[]{0, 1, 14, 26, 44, 60});
        return availableTime;
    }
}
