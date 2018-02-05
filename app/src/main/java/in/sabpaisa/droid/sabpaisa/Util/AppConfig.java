package in.sabpaisa.droid.sabpaisa.Util;

public class AppConfig {
	// Server user login url



	public static String URL_REGISTER = "http://205.147.103.27:6060/SabPaisaAppApi/register-user";
	public static String  URL_LOGIN = "http://205.147.103.27:6060/SabPaisaAppApi/sign-in-user";
	public static String  URL_forgotpasswprd = "http://205.147.103.27:6060/SabPaisaAppApi/changePassword";
	public static String  url_clientsall ="http://205.147.103.27:6060/SabPaisaAppApi/clientlist";
	public static String URL_StateList = "http://205.147.103.27:6060/SabPaisaAppApi/state_list";
	public static String URL_ClientBasedOnState ="http://205.147.103.27:6060/SabPaisaAppApi/client-based-on-state?state=";
	public static String URL_ServiceBasedOnState ="http://205.147.103.27:6060/SabPaisaAppApi/client-based-on-state-and-service?state=";

	public static String URL_ClientBasedOnClientId ="http://205.147.103.27:6060/SabPaisaAppApi/client-based-on-clientId?clientId=";
	public static String URL_VerifyUin ="http://205.147.103.27:6060/SabPaisaAppApi/VerifyUIN?uin_no=";
	//public static String URL_RequestUin ="http://localhost:8080/sabPaisaApp/RequestForUIN?client_Id=";
	public static String URL_RequestUin="http://205.147.103.27:6060/SabPaisaAppApi/RequestForUIN?client_Id=";

	public static String URL_ADD_Member ="http://205.147.103.27:6060/SabPaisaAppApi/addMember?";
	public static String URL_Show_Member ="http://205.147.103.27:6060/SabPaisaAppApi/fetchMembersOfClient?";
	public static String URL_Show_UserProfile ="http://205.147.103.27:6060/SabPaisaAppApi/userProfile";
	public static String URL_Show_UserProfileImage ="http://205.147.103.27:6060/SabPaisaAppApi/fetchUserImageByUserToken";
	public static String URL_UserProfileUpdate ="http://205.147.103.27:6060/SabPaisaAppApi/updateUser";
	public static String URL_UserProfileImageUpdate ="http://205.147.103.27:6060/SabPaisaAppApi/updateUserImage";

	public static String URL_Payment ="http://205.147.103.27:6060/SabPaisaAppApi/getLandingPages";//Added on 1st Feb
	public static String SAb_Api ="http://205.147.103.27:6060/SabPaisaAppApi";


}
