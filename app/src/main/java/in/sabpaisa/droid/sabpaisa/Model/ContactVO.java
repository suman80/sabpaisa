package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by archana on 22/3/18.
 */

public class ContactVO  {
    private String ContactImage;
    private String ContactName;
    private String ContactNumber;
    private int InviteButtonVisibility;

    public int getInviteButtonVisibility() {
        return InviteButtonVisibility;
    }

    public void setInviteButtonVisibility(int inviteButtonVisibility) {
        this.InviteButtonVisibility = inviteButtonVisibility;
    }

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String contactImage) {
        this.ContactImage = ContactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
