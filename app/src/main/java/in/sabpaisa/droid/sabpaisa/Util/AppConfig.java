package in.sabpaisa.droid.sabpaisa.Util;

public class AppConfig {
    // Server user login url

    public static String Base_Url="https://portal.sabpaisa.in";
    //public static String Base_Url = "https://spl.sabpaisa.in";
    public static String App_api="/SabPaisaAppApi/";
    //public static String App_api="/SabPaisaAppApi_Q4/";
    //public static String App_api = "/SabPaisaAppApi_v16/";
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

    public static String URL_UserRole = "role";
    public static String URL_addParticularClientsFeeds = "addParticularClientsFeeds";
    public static String URL_deleteFeed = "deleteFeed";
    public static String URL_deleteFeedComments = "deleteFeedComments";
    public static String URL_addParticularClientsGroups = "addParticularClientsGroups";
    public static String URL_deleteGroup = "deleteGroup";
    public static String URL_deleteGroupComments = "deleteGroupComments";
    public static String URL_addMemberList = "addMemberList";
    public static String URL_addMember = "addMember";
    public static String URL_updateUINStatus = "updateUINStatus";
    public static String URL_clientAdmin = "clientAdmin";
    public static String URL_removeUserfromGroup = "removeUserfromGroup";
    public static String URL_groupAdmin = "groupAdmin";
    public static String URL_tableFields = "tableFields";
    public static String URL_saveUINtableData = "saveUINtableData";
    public static String URL_fetchGroupUsers = "fetchGroupUsers";
    public static String URL_decisionOnPendingRequest = "decisionOnPendingRequest";
    public static String URL_updateFeed = "updateFeed";
    public static String URL_updateGroup = "updateGroup";
    public static String URL_DynamicImages = "/clientOnBoarding/fetchOnGoingevent";
    public static String URL_privateFeedUnjoinedMember = "privateFeedUnjoinedMember";
    public static String URL_privateFeedMember = "privateFeedMember";
    public static String URL_privateFeedMemberDelete = "privateFeedMember";
    public static String URL_addSpappclient = "addSpappclient";
    public static String URL_updateSpappclient = "updateSpappclient";
    public static String URL_spAppClient = "spAppClient";
    public static String URL_spAppClients = "spAppClients";
    public static String URL_addmemberToSpappClient = "addmemberToSpappClient";
    public static String URL_addmembersToSpappClient = "addmembersToSpappClient";
    public static String URL_membersOfSPappclient = "membersOfSPappclient";
    public static String URL_removemembersToSpappClient = "removemembersToSpappClient";
    public static String URL_NoOFGroupMembersSpace = "fetchGroupUsers?appcid=";
    public static String URL_lefttheGroup = "lefttheGroup";
    public static String URL_lefthespAppClient = "lefthespAppClient";
}
