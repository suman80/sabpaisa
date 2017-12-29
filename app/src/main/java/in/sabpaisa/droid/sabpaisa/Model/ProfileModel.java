package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by rajdeeps on 12/28/17.
 */

public class ProfileModel {
    String fullName,address,contactNumber,userImage;

    public ProfileModel() {
    }

    public ProfileModel(String fullName, String address, String contactNumber, String userImage) {
        this.fullName = fullName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.userImage = userImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
