package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by thangit14 on 6/13/16.
 */
public class TaskSetUnavailableDate extends TaskNetworkBase<Boolean>{
    private List<Date> dates;
    private Context context;
    public TaskSetUnavailableDate(Context mContext, List<Date> dates) {
        super(mContext);
        this.dates = dates;
        this.context = mContext;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException {
        return true;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return JsonUtil.genParamToSetUnAvailable(context,dates);
    }

    @Override
    public String genURL() {
        return NetworkConstant.API_SET_UN_AVAILABLE;
    }

    @Override
    public int genMethod() {
        return Request.Method.POST;
    }
}
