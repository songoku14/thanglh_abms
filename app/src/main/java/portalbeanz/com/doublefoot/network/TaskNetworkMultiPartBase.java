package portalbeanz.com.doublefoot.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import portalbeanz.com.doublefoot.model.ItemUserBasicInfo;
import portalbeanz.com.doublefoot.network.exception.ExceptionConstant;
import portalbeanz.com.doublefoot.network.exception.VolleyErrorHelper;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.MySingleton;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thanglh on 13/11/2014.
 */
public abstract class TaskNetworkMultiPartBase<T extends Object> {
    private static final int NETWORK_TIME_OUT = 60000;
    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private static final String NETWORK_SESSION = "NETWORK SESSION";
    private Request<T> mRequest;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private String session;
    private SharedPreferences sharedPreferences;

    private MultipartEntityBuilder mEntityBuilder;

    protected TaskNetworkMultiPartBase(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(Constant.ABMS_SHARED_PREFRENCES, Context.MODE_PRIVATE);
        session = sharedPreferences.getString(Constant.ACCESS_TOKEN, "");
    }

    public final void request(final Response.Listener<T> successListener, final ErrorListener errorListener) {
        buildMultipartEntity();

        mRequest = new Request<T>(genMethod(), genURL(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 * if volleyError is a instance of VolleyErrorHelper ->error from sever
                 * else create new VolleyErrorHelper to handle error from Volley
                 */
                VolleyErrorHelper volleyErrorHelper;
                if (volleyError instanceof VolleyErrorHelper) {
                    volleyErrorHelper = (VolleyErrorHelper) volleyError;
                } else {
                    volleyErrorHelper = new VolleyErrorHelper(volleyError);
                }
                errorListener.onErrorListener(volleyErrorHelper.getErrorCode(), volleyErrorHelper.getMessageError());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "multipart/form-data");
                headers.put("session", session);
                headers.put("os", "Android");

                headers.put("udid", "none");
                headers.put("agent", "none");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                HttpEntity httpEntity = mEntityBuilder.build();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    httpEntity.writeTo(bos);
                    Log.e("REQUEST --> upload multi part","size = "+ bos.size() + "byte");

                } catch (IOException e) {
                    VolleyLog.e("IOException writing to ByteArrayOutputStream");
                }
                return bos.toByteArray();
            }

            @Override
            public String getBodyContentType() {
                String contentTypeHeader = mEntityBuilder.build().getContentType().getValue();
                return contentTypeHeader;
            }

            @Override
            protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
                String data = new String(networkResponse.data);
                Log.e("TaskNetworkBase --> parseNetworkResponse", "ServerRespon: " + data);

                T result = null;
                VolleyErrorHelper volleyErrorHelper;
                try {
                    volleyErrorHelper = validData(data);
                    if (null == volleyErrorHelper) {
                        result = genDataFromJSON(data);
                        return Response.success(result, getCacheEntry());
                    } else {
                        return Response.error(volleyErrorHelper);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    volleyErrorHelper = new VolleyErrorHelper(ex.getMessage(), ExceptionConstant.ERROR_CODE_PARSE_JSON);
                    return Response.error(volleyErrorHelper);
                }
            }

            @Override
            protected void deliverResponse(T data) {
                successListener.onResponse(data);
            }
        };

        mRequestQueue = MySingleton.getInstance(mContext).getRequestQueue();
        mRequest.setRetryPolicy(new DefaultRetryPolicy(NETWORK_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mRequest);
    }

    private VolleyErrorHelper validData(String json) {
        VolleyErrorHelper volleyErrorHelper = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt(CODE);
            if (code != 0) {
                String message = " $_$ " + jsonObject.getString(MESSAGE);
                volleyErrorHelper = new VolleyErrorHelper(message, code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            volleyErrorHelper = new VolleyErrorHelper(" $_$ " + e.getMessage(), ExceptionConstant.ERROR_CODE_PARSE_JSON);
        }
        return volleyErrorHelper;
    }

    private void buildMultipartEntity() {
        mEntityBuilder = MultipartEntityBuilder.create();
        mEntityBuilder.addBinaryBody(getNameEntity(), getInputStream(), ContentType.create("image/jpeg"), getFileName());
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mEntityBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }

    protected abstract String getNameEntity();

    protected abstract T genDataFromJSON(String data) throws JSONException;

    public abstract String genURL();

    public abstract int genMethod();

    protected abstract InputStream getInputStream();

    protected abstract String getFileName();

    public interface ErrorListener {
        public void onErrorListener(int errorCode, String errorMessage);
    }

    public boolean isCustomer() {
        String type = sharedPreferences.getString(Constant.USER_TYPE, "");
        return ItemUserBasicInfo.UserType.Customer.toString().equals(type);
    }

}
