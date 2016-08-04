package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

/**
 * Created by thangit14 on 6/14/16.
 */
public class TaskGetListAppointment extends TaskNetworkBase<ArrayList<ItemAppointment>> {
    private Context context;
    public TaskGetListAppointment(Context mContext) {
        super(mContext);
        this.context = mContext;
    }

    @Override
    protected ArrayList<ItemAppointment> genDataFromJSON(String data) throws JSONException {
        try {
            return JsonUtil.getListItemAppointment(context,data, isMasseur());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return null;
    }

    @Override
    public String genURL() {
        return isCustomer() ? NetworkConstant.API_CUSTOMER_GET_LIST_APPOINTMENT : NetworkConstant.API_MASSEUR_GET_LIST_APPOINTMENT;
    }

    @Override
    public int genMethod() {
        return Request.Method.GET;
    }
}
