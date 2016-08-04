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
 * Created by thangit14 on 7/14/16.
 */
public class TaskGetPackageDetail extends TaskNetworkBase<ArrayList<ItemPackage>>{
    private Context context;
    private int packageID;
    public TaskGetPackageDetail(Context mContext, int packageID) {
        super(mContext);
        this.context = mContext;
        this.packageID = packageID;
    }

    @Override
    protected ArrayList<ItemPackage> genDataFromJSON(String data) throws JSONException, ParseException {
        return JsonUtil.getItemPackage(data);
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return null;
    }

    @Override
    public String genURL() {
        return NetworkConstant.API_GET_PACKAGE_DETAIL + packageID;
    }

    @Override
    public int genMethod() {
        return Request.Method.GET;
    }
}
