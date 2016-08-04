package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;
import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by thangit14 on 6/18/16.
 */
public class TaskUpdateProfile extends TaskNetworkBase<Boolean>{
    private ItemUser itemUser;
    private Context context;

    public TaskUpdateProfile(Context mContext, ItemUser itemUser) {
        super(mContext);
        this.itemUser = itemUser;
        this.context = mContext;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException, ParseException {
        return true;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return JsonUtil.genParamToUpdateProfile(itemUser);
    }

    @Override
    public String genURL() {
        return isCustomer() ? NetworkConstant.API_UPDATE_PROFILE_CUSTOMER : NetworkConstant.API_UPDATE_PROFILE_MASSEUR;

    }

    @Override
    public int genMethod() {
        return Request.Method.PUT;
    }
}
