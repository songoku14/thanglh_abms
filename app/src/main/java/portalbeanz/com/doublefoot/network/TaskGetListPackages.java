package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import portalbeanz.com.doublefoot.model.ItemPackage;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

/**
 * Created by thangit14 on 7/13/16.
 */
public class TaskGetListPackages extends TaskNetworkBase<ArrayList<ItemPackage>>{
    private Context context;
    public TaskGetListPackages(Context mContext) {
        super(mContext);
        this.context = mContext;
    }

    @Override
    protected ArrayList<ItemPackage> genDataFromJSON(String data) throws JSONException, ParseException {
        return JsonUtil.getListItemPackages(data);
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return null;
    }

    @Override
    public String genURL() {
        return NetworkConstant.API_GET_LIST_PACKAGES;
    }

    @Override
    public int genMethod() {
        return Request.Method.GET;
    }

}
