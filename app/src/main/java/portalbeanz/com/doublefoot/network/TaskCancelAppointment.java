package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

/**
 * Created by thangit14 on 6/14/16.
 */
public class TaskCancelAppointment extends TaskNetworkBase<Boolean>{
    private int appointmentID;

    public TaskCancelAppointment(Context mContext, int appointmentID) {
        super(mContext);
        this.appointmentID = appointmentID;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException {
        return true;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return JsonUtil.genParamToCancelAppointment(appointmentID);
    }

    @Override
    public String genURL() {
        return NetworkConstant.API_CANCEL_APPOINTMENT;
    }

    @Override
    public int genMethod() {
        return Request.Method.POST;
    }
}
