package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by archana on 22/3/18.
 */

public class ContactVO  {
    public String ContactImage;
    public String ContactName;
    public String ContactNumber;
    public int InviteButtonVisibility;

    boolean isSelected ;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
