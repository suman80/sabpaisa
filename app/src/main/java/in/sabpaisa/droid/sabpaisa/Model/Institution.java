package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by abc on 15-06-2017.
 */

public class Institution {
    String logo;
    String pic;
    String name;
    String location;

    public Institution(String coa, String s) {
        name = coa;
        location = s;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
