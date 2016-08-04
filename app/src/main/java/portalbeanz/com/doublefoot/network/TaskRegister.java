package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;
import portalbeanz.com.doublefoot.model.ItemUserBasicInfo;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thangit14 on 6/8/16.
 */
public class TaskRegister extends TaskNetworkBase<Boolean> {

    private ItemUserBasicInfo itemUserBasicInfo;

    public TaskRegister(Context mContext, ItemUserBasicInfo itemUserBasicInfo) {
        super(mContext);
        this.itemUserBasicInfo = itemUserBasicInfo;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException {
        String session = JsonUtil.getSession(data);
        saveSession(session);
        saveUserType(itemUserBasicInfo.getUserType());
        return true;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        JSONObject jsonObject = new JSONObject(new Gson().toJson(itemUserBasicInfo,ItemUserBasicInfo.class));
        return jsonObject;
    }

    @Override
    public String genURL() {
        return itemUserBasicInfo.isCustomer() ? NetworkConstant.API_REGISTER_CUSTOMER : NetworkConstant.API_REGISTER_MASSEUR;
    }

    @Override
    public int genMethod() {
        return Request.Method.POST;
    }
}
