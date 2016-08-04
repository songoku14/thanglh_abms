package portalbeanz.com.doublefoot.fragment;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.util.NetworkConstant;

/**
 * Created by thangit14 on 6/18/16.
 */
public class TaskLogout extends TaskNetworkBase<Boolean>{

    protected TaskLogout(Context mContext) {
        super(mContext);
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException, ParseException {
        removeSession();
        return true;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return null;
    }

    @Override
    public String genURL() {
        return isCustomer() ? NetworkConstant.API_LOGOUT_CUSTOMER : NetworkConstant.API_LOGOUT_MASSEUR;

    }

    @Override
    public int genMethod() {
        return Request.Method.GET;
    }
}
