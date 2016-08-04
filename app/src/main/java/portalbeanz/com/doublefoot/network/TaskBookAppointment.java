package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

/**
 * Created by thangit14 on 6/14/16.
 */
public class TaskBookAppointment extends TaskNetworkBase<ItemAppointment> {
    private ItemUser itemUser;
    private String startDate;
    private String endDate;
    private Context mContext;
    private int packageId;

    public TaskBookAppointment(Context mContext, ItemUser itemUser, String startDate, String endDate, int packageId) {
        super(mContext);
        this.itemUser = itemUser;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mContext = mContext;
        this.packageId = packageId;
    }

    @Override
    protected ItemAppointment genDataFromJSON(String data) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonData = jsonObject.getJSONObject("data");
        ItemAppointment itemAppointment = JsonUtil.getItemAppointment(mContext, jsonData, false);
        return itemAppointment;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return JsonUtil.genParamToBookAppointment(mContext, itemUser.getUserId(), startDate, endDate, packageId);

    }

    @Override
    public String genURL() {
        return NetworkConstant.API_BOOK_APPOINTMENT;
    }

    @Override
    public int genMethod() {
        return Request.Method.POST;
    }

}
