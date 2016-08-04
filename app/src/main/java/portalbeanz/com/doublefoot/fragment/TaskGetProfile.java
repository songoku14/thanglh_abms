package portalbeanz.com.doublefoot.fragment;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.network.TaskNetworkBase;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

/**
 * Created by thangit14 on 6/18/16.
 */
public class TaskGetProfile extends TaskNetworkBase<ItemUser>{

    private Context context;
    protected TaskGetProfile(Context mContext) {
        super(mContext);
        this.context = mContext;
    }

    @Override
    protected ItemUser genDataFromJSON(String data) throws JSONException, ParseException {
        return JsonUtil.getItemUserFromGetProfile(context, data, isCustomer());
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return null;
    }

    @Override
    public String genURL() {
        return isCustomer() ? NetworkConstant.API_PROFILE_CUSTOMER : NetworkConstant.API_PROFILE_MASSEUR;
    }

    @Override
    public int genMethod() {
        return Request.Method.GET;
    }
}
