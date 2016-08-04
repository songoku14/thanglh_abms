package portalbeanz.com.doublefoot.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.model.ItemPackage;
import portalbeanz.com.doublefoot.model.ItemPackagePrice;
import portalbeanz.com.doublefoot.model.ItemUser;

/**
 * Created by thangit14 on 1/21/16.
 */
public class JsonUtil {

    public static String getSession(String data) throws JSONException {
        JSONObject jsonData = getJsonData(data);
        return jsonData.getString("session");
    }

    private static JSONObject getJsonData(String data) throws JSONException {
        return new JSONObject(data).getJSONObject("data");
    }

    public static JSONObject genParamToSetUnAvailable(Context context, List<Date> dates) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        ArrayList<String> strDates = new ArrayList<>(dates.size());
        for (Date date : dates) {
            String strDate = Utils.getDefaultFormatDateString(date, context);
            strDates.add(strDate);
        }
        jsonObject.put("date", new JSONArray(strDates));
        return jsonObject;
    }

    public static ArrayList<Date> getListUnAvailableDate(Context context, String data) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonDate = jsonObject.getJSONObject("data");
        JSONArray jsonArray = jsonDate.getJSONArray("unavailable_time");
        ArrayList<Date> dates = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String strDate = jsonArray.getString(i);
            Date date = Utils.getDate(context.getString(R.string.format_date_2), strDate);
            dates.add(date);
        }
        return dates;
    }

    public static ArrayList<Date> getListUnAvailableDate(Context context, JSONObject jsonDate) throws JSONException, ParseException {
        JSONArray jsonArray = jsonDate.getJSONArray("unavailable_time");
        ArrayList<Date> dates = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String strDate = jsonArray.getString(i);
            Date date = Utils.getDateWithTPattern(context, strDate);
            dates.add(date);
        }
        return dates;
    }

    public static JSONObject genParamToBookAppointment(Context context, int masseurID, String startDate, String endDate, int packageId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("masseur_id", masseurID);
        jsonObject.put("start_time", startDate);
        jsonObject.put("end_time", endDate);
        jsonObject.put("package_id", packageId + "");
        return jsonObject;
    }

    public static JSONObject genParamToGetListMasseur(Context context, Calendar calendar) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", Utils.getDefaultFormatDateString(calendar.getTime(), context));
        return jsonObject;
    }

    public static ArrayList<ItemUser> getListItemUser(Context context, String data, boolean getCustomerItem) throws JSONException, ParseException {
        ArrayList<ItemUser> itemUsers = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonItemUser = jsonArray.getJSONObject(i);
            itemUsers.add(getItemUser(context, jsonItemUser, getCustomerItem));
        }
        return itemUsers;
    }

    private static ItemUser getItemUser(Context context, JSONObject jsonItemUser, boolean getCustomerItem) throws JSONException, ParseException {
        ItemUser itemUser = new ItemUser();
        if (getCustomerItem) {
            itemUser.setUserId(jsonItemUser.getInt("customer_id"));
        } else {
            itemUser.setUserId(jsonItemUser.getInt("masseur_id"));
        }
        itemUser.setKind(jsonItemUser.getInt("kind"));
        if (jsonItemUser.isNull("display_name")) {
            itemUser.setDisplayName("");
        } else {
            itemUser.setDisplayName(jsonItemUser.getString("display_name"));
        }
        if (jsonItemUser.isNull("first_name")) {
            itemUser.setFirstName("");
        } else {
            itemUser.setFirstName(jsonItemUser.getString("first_name"));
        }
        if (jsonItemUser.isNull("last_name")) {
            itemUser.setLastName("");
        } else {
            itemUser.setLastName(jsonItemUser.getString("last_name"));
        }
        if (jsonItemUser.isNull("gender")) {
            itemUser.setGender(0);
        } else {
            itemUser.setGender(jsonItemUser.getInt("gender"));
        }
        itemUser.setPhone(jsonItemUser.getString("phone"));
        itemUser.setEmail(jsonItemUser.getString("email"));
        if (jsonItemUser.isNull("description")) {
            itemUser.setIntroduce("");
        } else {
            itemUser.setIntroduce(jsonItemUser.getString("description"));
        }
        if (jsonItemUser.isNull("avatar")) {
            itemUser.setAvatarUrl("");
        } else {
            itemUser.setAvatarUrl(jsonItemUser.getString("avatar"));
        }
        if (jsonItemUser.isNull("age") || jsonItemUser.getString("age").length() == 0) {
            itemUser.setAge(0);
        } else {
            itemUser.setAge(jsonItemUser.getInt("age"));
        }
        if (jsonItemUser.isNull("bod")) {
            itemUser.setBod("");
        } else {
            itemUser.setBod(jsonItemUser.getString("bod"));
        }
        if (jsonItemUser.isNull("working_experience")) {
            itemUser.setWorkingExperience(0);
        } else {
            itemUser.setWorkingExperience(jsonItemUser.getInt("working_experience"));
        }
        if (jsonItemUser.isNull("specialization")) {
            itemUser.setSpecialization("");
        } else {
            itemUser.setSpecialization(jsonItemUser.getString("specialization"));
        }

        if (jsonItemUser.has("booked_time")) {
            itemUser.setBookedTimes(getBookedTime(context, jsonItemUser.getJSONArray("booked_time")));
        }

        if (jsonItemUser.has("unavailable_time")) {
            itemUser.setUnavailableDates(getListUnAvailableDate(context, jsonItemUser));
        }

        if (itemUser.getDisplayName() == null || itemUser.getDisplayName().length() == 0) {
            if (itemUser.getFirstName().length() == 0 && itemUser.getLastName().length() == 0) {
                itemUser.setDisplayName(itemUser.getEmail());
            } else {
                itemUser.setDisplayName(itemUser.getLastName() + " " + itemUser.getFirstName());
            }
        }
        return itemUser;
    }

    private static HashMap<Date, Date> getBookedTime(Context context, JSONArray jsonArray) throws JSONException, ParseException {
        HashMap<Date, Date> list = new HashMap<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String strStart = jsonObject.getString("start_time");
            String strEnd = jsonObject.getString("end_time");

            Date startTime = Utils.getDateWithTPattern(context, strStart);
            Date endTime = Utils.getDateWithTPattern(context, strEnd);

            list.put(startTime, endTime);
        }
        return list;
    }

    public static ArrayList<ItemAppointment> getListItemAppointment(Context context, String data, boolean getCustomerItem) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonListItemAppointment = jsonObject.getJSONArray("data");
        ArrayList<ItemAppointment> itemAppointments = new ArrayList<>(jsonListItemAppointment.length());
        for (int i = 0; i < jsonListItemAppointment.length(); i++) {
            JSONObject jsonItem = jsonListItemAppointment.getJSONObject(i);
            ItemAppointment itemAppointment = getItemAppointment(context, jsonItem, getCustomerItem);
            itemAppointments.add(itemAppointment);
        }
        Utils.sortByDate(itemAppointments);
        itemAppointments = addHeaderDate(itemAppointments);
        return itemAppointments;
    }

    public static ItemAppointment getItemAppointment(Context context, JSONObject jsonItem, boolean getCustomerItem) throws JSONException, ParseException {
        ItemAppointment itemAppointment = new ItemAppointment();
        itemAppointment.setAppointmentId(jsonItem.getInt("appointment_id"));
//        itemAppointment.setStatus(jsonItem.getInt("status"));
        itemAppointment.setStartDate(Utils.getDateWithTPattern(context, jsonItem.getString("start_time")));
        itemAppointment.setEndDate(Utils.getDateWithTPattern(context, jsonItem.getString("end_time")));
        ItemUser itemUser;
        if (getCustomerItem) {
            itemUser = getItemUser(context, jsonItem.getJSONObject("customer"), getCustomerItem);
        } else {
            itemUser = getItemUser(context, jsonItem.getJSONObject("masseur"), getCustomerItem);
        }
        itemAppointment.setItemUser(itemUser);
        return itemAppointment;
    }

    private static ArrayList<ItemAppointment> addHeaderDate(ArrayList<ItemAppointment> itemAppointments) {
        ArrayList<ItemAppointment> result = new ArrayList<>();
        Date date = null;
        for (int i = 0, n = itemAppointments.size(); i < n; i++) {
            ItemAppointment itemAppointment = itemAppointments.get(i);
            if (date == null || !Utils.isSameDay(date, itemAppointment.getStartDate())) {
                ItemAppointment header = new ItemAppointment(ItemAppointment.SECTION);
                header.setStartDate(itemAppointment.getStartDate());
                result.add(header);
                date = itemAppointment.getStartDate();
            }
            result.add(itemAppointment);
        }
        return result;
    }

    public static JSONObject genParamToCancelAppointment(int appointmentID) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appointment_id", appointmentID);
        return jsonObject;
    }

    public static ItemUser getItemUserFromGetProfile(Context context, String data, boolean getCustomerItem) throws JSONException, ParseException {
        JSONObject jsonObject = getJsonData(data);
        return getItemUser(context, jsonObject, getCustomerItem);
    }

    public static JSONObject genParamToUpdateProfile(ItemUser itemUser) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonProfile = new JSONObject();
        jsonProfile.put("first_name", itemUser.getFirstName());
        jsonProfile.put("last_name", itemUser.getLastName());
        jsonProfile.put("display_name", itemUser.getDisplayName());
        jsonProfile.put("gender", itemUser.getGender());
        jsonProfile.put("phone", itemUser.getPhone());
        jsonProfile.put("email", itemUser.getEmail());
        jsonProfile.put("description", itemUser.getIntroduce());
        jsonProfile.put("bod", itemUser.getBod());
        jsonProfile.put("working_experience", itemUser.getWorkingExperience());
        jsonProfile.put("specialization", itemUser.getSpecialization());
        jsonObject.put("profile", jsonProfile);
        return jsonObject;
    }

    public static JSONObject genParamToResetPassword(String email) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        return jsonObject;
    }

    public static ArrayList<ItemPackage> getListItemPackages(String data) throws JSONException {
        ArrayList<ItemPackage> itemPackages = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonItem = (JSONObject) jsonArray.get(i);
            ItemPackage itemPackage = new ItemPackage();
            itemPackage.setId(jsonItem.getInt("package_id"));
            itemPackage.setImage(jsonItem.getString("image"));
            itemPackage.setTitle(jsonItem.getString("package_name"));
            itemPackage.setDescription(jsonItem.isNull("package_description") ? ""
                    : jsonItem.getString("package_description"));
            itemPackages.add(itemPackage);
        }
        return itemPackages;
    }

    public static ArrayList<ItemPackage> getItemPackage(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        ArrayList<ItemPackage> itemPackages = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonItem = (JSONObject) jsonArray.get(i);

            ItemPackage itemPackage = new ItemPackage();
            itemPackage.setId(jsonItem.getInt("package_id"));
            itemPackage.setTitle(jsonItem.getString("package_name"));
            itemPackage.setDescription(jsonItem.isNull("package_description") ? ""
                    : jsonItem.getString("package_description"));

            ArrayList<ItemPackagePrice> itemPackagePrices = new ArrayList<>();
            JSONArray jsonPrices = jsonItem.getJSONArray("package_price");
            for (int j = 0; j < jsonPrices.length(); j++) {
                JSONObject jsonPrice = (JSONObject) jsonPrices.get(j);
                ItemPackagePrice itemPackagePrice = new ItemPackagePrice();
                itemPackagePrice.setTime(jsonPrice.getInt("time"));
                itemPackagePrice.setPrice((float) jsonPrice.getDouble("price"));
//                itemPackagePrice.setPricePlus((float) jsonPrice.getDouble("price_plus"));
                itemPackagePrices.add(itemPackagePrice);

            }
            itemPackage.setItemPackagePrices(itemPackagePrices);
            itemPackages.add(itemPackage);
        }
        return itemPackages;
    }
}
