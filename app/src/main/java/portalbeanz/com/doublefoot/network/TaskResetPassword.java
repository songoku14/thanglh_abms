package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thangit14 on 6/18/16.
 */
public class TaskResetPassword extends TaskNetworkBase<Boolean> {

    private final String email;

    public TaskResetPassword(Context mContext, String email) {
        super(mContext);
        this.email = email;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException {
        return true;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException{
        JSONObject jsonObject = JsonUtil.genParamToResetPassword(email);
        return jsonObject;
    }

    @Override
    public String genURL() {
        return NetworkConstant.API_RESET_PASSWORD;
    }

    @Override
    public int genMethod() {
        return Request.Method.POST;
    }
}
