package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.util.JsonUtil;
import portalbeanz.com.doublefoot.util.NetworkConstant;

/**
 * Created by thangit14 on 6/14/16.
 */
public class TaskGetListMasseur extends TaskNetworkBase<ArrayList<ItemUser>>{
    private Context context;
//    private int page;
//    private int perpage;

    public TaskGetListMasseur(Context context) {
        super(context);
        this.context = context;
//        this.page = page;
//        this.perpage = perpage;
    }

    @Override
    protected ArrayList<ItemUser> genDataFromJSON(String data) throws JSONException, ParseException {
        return JsonUtil.getListItemUser(context,data, false);
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return JsonUtil.genParamToGetListMasseur(context, Calendar.getInstance());
    }

    @Override
    public String genURL() {
        return NetworkConstant.API_GET_LIST_MASSEUR;
    }

    @Override
    public int genMethod() {
        return Request.Method.GET;
    }
}
