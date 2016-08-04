package portalbeanz.com.doublefoot.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by thangit14 on 6/13/16.
 */
public class TaskGetUnavailableDate extends TaskNetworkBase<List<Date>> {
    private Context mContext;
    public TaskGetUnavailableDate(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    protected List<Date> genDataFromJSON(String data) throws JSONException {
        try {
            return JsonUtil.getListUnAvailableDate(mContext,data);
        } catch (ParseException e) {
            Toast.makeText(mContext, "Error format date when parser from server", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return null;
    }

    @Override
    public String genURL() {
        return NetworkConstant.API_GET_UN_AVAILABLE_DATE;
    }

    @Override
    public int genMethod() {
        return Request.Method.GET;
    }
}
