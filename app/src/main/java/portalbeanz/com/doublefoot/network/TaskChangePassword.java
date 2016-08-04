package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import portalbeanz.com.doublefoot.util.NetworkConstant;

/**
 * Created by thangit14 on 6/21/16.
 */
public class TaskChangePassword extends TaskNetworkBase<Boolean> {

    private String oldPassword;
    private String newPassword;

    public TaskChangePassword(Context mContext, String oldPassword, String newPassword) {
        super(mContext);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException {
        return Boolean.TRUE;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("old_password", oldPassword);
        jsonObject.put("new_password", newPassword);
        return jsonObject;
    }

    @Override
    public String genURL() {
        return isCustomer() ? NetworkConstant.API_CHANGE_PASSWORD_CUSTOMER : NetworkConstant.API_CHANGE_PASSWORD_MASSEUR;
    }

    @Override
    public int genMethod() {
        return Request.Method.POST;
    }
}
