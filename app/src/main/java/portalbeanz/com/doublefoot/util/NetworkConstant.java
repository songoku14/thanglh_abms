package portalbeanz.com.doublefoot.util;

/**
 * Created by thangit14 on 1/21/16.
 */
public class NetworkConstant {

    private final static String DOMAIN_DEBUG = "http://apiambs.tk/";
    private final static String DOMAIN_PUBLISH = "http://sitebeanz.com/double-foot/public/";

    public final static String DOMAIN = Config.DEBUG_MODE ? DOMAIN_DEBUG : DOMAIN_PUBLISH;

    public static final String SENDER_ID = "180301950528";

    public static final String API_LOGIN_MASSEUR = DOMAIN + "masseur/login/email";
    public static final String API_LOGIN_CUSTOMER = DOMAIN + "customer/login/email";
    public static final String API_REGISTER_CUSTOMER = DOMAIN + "customer/register";
    public static final String API_REGISTER_MASSEUR = DOMAIN + "masseur/register";

    public static final String API_UPLOAD_AVATAR_MASSEUR = DOMAIN + "masseur/profile/avatar";
    public static final String API_UPLOAD_AVATAR_CUSTOMER = DOMAIN + "customer/profile/avatar";

    public static final String API_SET_UN_AVAILABLE = DOMAIN + "masseur/set_unavailable_time";
    public static final String API_GET_UN_AVAILABLE_DATE = DOMAIN + "masseur/list_unavailable_time";
    public static final String API_BOOK_APPOINTMENT = DOMAIN + "customer/book_appointment";
    public static final String API_GET_LIST_MASSEUR = DOMAIN + "customer/list_masseur_availability";
    public static final String API_CUSTOMER_GET_LIST_APPOINTMENT = DOMAIN + "customer/list_appointments";
    public static final String API_MASSEUR_GET_LIST_APPOINTMENT = DOMAIN + "masseur/list_appointments";
    public static final String API_CANCEL_APPOINTMENT = DOMAIN + "customer/cancel_appointment";

    public static final String API_PROFILE_CUSTOMER = DOMAIN + "customer/profile";
    public static final String API_PROFILE_MASSEUR = DOMAIN + "masseur/profile";
    public static final String API_LOGOUT_MASSEUR = DOMAIN + "masseur/logout";
    public static final String API_LOGOUT_CUSTOMER = DOMAIN + "customer/logout";

    public static final String API_UPDATE_PROFILE_MASSEUR = DOMAIN + "masseur/profile";
    public static final String API_UPDATE_PROFILE_CUSTOMER = DOMAIN + "customer/profile";
    public static final String API_RESET_PASSWORD= DOMAIN + "reset_password";


    public static final String API_CHANGE_PASSWORD_CUSTOMER = DOMAIN + "customer/changepassword";
    public static final String API_CHANGE_PASSWORD_MASSEUR = DOMAIN + "masseur/changepassword";
    public static final String API_GET_LIST_PACKAGES = DOMAIN+"customer/list_package/0";
    public static final String API_GET_PACKAGE_DETAIL = DOMAIN + "customer/list_package/";
}
