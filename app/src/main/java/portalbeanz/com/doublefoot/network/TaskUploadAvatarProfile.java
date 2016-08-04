package portalbeanz.com.doublefoot.network;

import android.content.Context;

import com.android.volley.Request;
import portalbeanz.com.doublefoot.util.Constant;
import portalbeanz.com.doublefoot.util.NetworkConstant;

import org.json.JSONException;

import java.io.InputStream;

/**
 * Created by thangit14 on 6/8/16.
 */
public class TaskUploadAvatarProfile extends TaskNetworkMultiPartBase<Boolean> {
    private InputStream inputStream;
    private String imageName;

    public TaskUploadAvatarProfile(Context mContext, InputStream inputStream, String imageName) {
        super(mContext);
        this.inputStream = inputStream;
        this.imageName = imageName;
    }

    @Override
    protected String getNameEntity() {
        return Constant.AVATAR_IMAGE_NAME;
    }

    @Override
    protected Boolean genDataFromJSON(String data) throws JSONException {
        return true;
    }

    @Override
    public String genURL() {
        return isCustomer() ? NetworkConstant.API_UPLOAD_AVATAR_CUSTOMER : NetworkConstant.API_UPLOAD_AVATAR_MASSEUR;
    }

    @Override
    public int genMethod() {
        return Request.Method.POST;
    }

    @Override
    protected InputStream getInputStream() {
        return inputStream;
    }

    @Override
    protected String getFileName() {
        return imageName;
    }
}
