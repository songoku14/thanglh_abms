package portalbeanz.com.doublefoot.network.exception;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by thangit14 on 1/21/16.
 */
public class VolleyErrorHelper extends VolleyError {
    private String messageError="";
    private int errorCode;

    public VolleyErrorHelper(String messageError, int errorCode) {
        this.messageError = messageError;
        this.errorCode = errorCode;
    }

    /**
     * catch error type By VolleyError
     *
     * @param volleyError
     */
    public VolleyErrorHelper(VolleyError volleyError) {
        if (volleyError instanceof TimeoutError) {
            this.errorCode = ExceptionConstant.SERVER_BUSY;
//            this.messageError = volleyError.getMessage();
            this.messageError = "code 701: Server Busy";
        } else if (isNetworkProblem(volleyError)) {
            this.errorCode = ExceptionConstant.NO_NETWORK;
            this.messageError = volleyError.getMessage();
        } else {
            if (null != volleyError.networkResponse) {
                this.errorCode = volleyError.networkResponse.statusCode;
            } else {
                this.errorCode = ExceptionConstant.UNKNOWN_EXCEPTION;
            }
            this.messageError = volleyError.getClass().toString() +
                    (volleyError.getMessage() == null ? "" : " /\n " + volleyError.getMessage()
//                            +" /\n "+volleyError.getCause().getCause().getMessage()
                    );
        }
    }

    private boolean isNetworkProblem(VolleyError volleyError) {
        return (volleyError instanceof NetworkError) || (volleyError instanceof NoConnectionError);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessageError() {
        return messageError;
    }
}
