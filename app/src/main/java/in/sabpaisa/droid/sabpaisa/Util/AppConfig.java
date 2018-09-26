package in.sabpaisa.droid.sabpaisa.Util;

public class AppConfig {
    // Server user login url

    //public static String Base_Url="https://portal.sabpaisa.in";
    public static String Base_Url = "https://spl.sabpaisa.in";
    //public static String App_api="/SabPaisaAppApi/";
    public static String App_api = "/SabPaisaAppApi_v17/";
    public static String URL_REGISTER = "register-user";
    public static String URL_Contacts = "SPuserInUsercontactList";
    public static String URL_AllTransaction = "/SabPaisaResponseHandler/saveSPtranscationId?token=";
    public static String URL_TransactionReport = "TransactionData?SptransacationId=";
    public static String URL_AllTransactionReport = "SPtranscationIds?token=";
    public static String URL_NoOFGroupMembeers = "fetchGroupUsers?clientId=";
    public static String URL_LOGIN = "sign-in-user";
    public static String URL_forgotpasswprd = "changePassword";
    public static String url_clientsall = "clientlist";
    public static String URL_StateList = "state_list";
    public static String URL_ClientBasedOnState = "client-based-on-state?state=";
    public static String URL_ServiceBasedOnState = "client-based-on-state-and-service?state=";

    public static String URL_ClientBasedOnClientId = "client-based-on-clientId?clientId=";
    public static String URL_VerifyUin = "VerifyUIN?uin_no=";
    //public static String URL_RequestUin ="http://localhost:8080/sabPaisaApp/RequestForUIN?client_Id=";
    public static String URL_RequestUin = "RequestForUIN?client_Id=";

    public static String URL_ADD_Member = "addMember?";
    public static String URL_Show_Member = "fetchMembersOfClient?";
    public static String URL_Show_UserProfile = "userProfile";
    public static String URL_Show_UserProfileImage = "fetchUserImageByUserToken";
    public static String URL_UserProfileUpdate = "updateUser";
    public static String URL_UserProfileImageUpdate = "updateUserImage";

    public static String URL_Payment = "getLandingPages";//Added on 1st Feb
    public static String SAb_Api = "http://205.147.103.27:6060/SabPaisaAppApi";
    public static String URL_Token = "getAccessToken";
    public static String URl_PayMesage = "paymentQueryMessage?token=";
    public static String URl_FCM_TOKEN = "FCMToken?";

}
