package in.sabpaisa.droid.sabpaisa.Model;

public class PersonalSpaceModel {

    public ProfileModel profileModel;
    public String appCid,appCname,contactNumber,emailId,address,description,clientLogoPath,clientImagePath;

    public long recentCommentTime;

    public PersonalSpaceModel() {
    }


    public PersonalSpaceModel(ProfileModel profileModel, String appCid, String appCname, String contactNumber, String emailId, String address, String description, String clientLogoPath, String clientImagePath, long recentCommentTime) {
        this.profileModel = profileModel;
        this.appCid = appCid;
        this.appCname = appCname;
        this.contactNumber = contactNumber;
        this.emailId = emailId;
        this.address = address;
        this.description = description;
        this.clientLogoPath = clientLogoPath;
        this.clientImagePath = clientImagePath;
        this.recentCommentTime = recentCommentTime;
    }

    public PersonalSpaceModel(ProfileModel profileModel, String appCid, String appCname, String contactNumber, String emailId, String address, String description, String clientLogoPath, String clientImagePath) {
        this.profileModel = profileModel;
        this.appCid = appCid;
        this.appCname = appCname;
        this.contactNumber = contactNumber;
        this.emailId = emailId;
        this.address = address;
        this.description = description;
        this.clientLogoPath = clientLogoPath;
        this.clientImagePath = clientImagePath;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    public void setProfileModel(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    public String getAppCid() {
        return appCid;
    }

    public void setAppCid(String appCid) {
        this.appCid = appCid;
    }

    public String getAppCname() {
        return appCname;
    }

    public void setAppCname(String appCname) {
        this.appCname = appCname;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientLogoPath() {
        return clientLogoPath;
    }

    public void setClientLogoPath(String clientLogoPath) {
        this.clientLogoPath = clientLogoPath;
    }

    public String getClientImagePath() {
        return clientImagePath;
    }

    public void setClientImagePath(String clientImagePath) {
        this.clientImagePath = clientImagePath;
    }


    public long getRecentCommentTime() {
        return recentCommentTime;
    }

    public void setRecentCommentTime(long recentCommentTime) {
        this.recentCommentTime = recentCommentTime;
    }
}
