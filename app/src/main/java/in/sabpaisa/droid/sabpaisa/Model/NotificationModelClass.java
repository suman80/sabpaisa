package in.sabpaisa.droid.sabpaisa.Model;

public class NotificationModelClass {

    String id;
    String count;
    String imagePath;
    String logoPath;
    String name;
    String description;
String identify;

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public NotificationModelClass(String id, String count, String imagePath, String logoPath, String name, String description, String identify) {

        this.id = id;
        this.count = count;
        this.imagePath = imagePath;
        this.logoPath = logoPath;
        this.name = name;
        this.description = description;
        this.identify = identify;
    }

    public NotificationModelClass(String id, String count, String imagePath, String logoPath, String name, String description) {
        this.id = id;
        this.count = count;
        this.imagePath = imagePath;
        this.logoPath = logoPath;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public NotificationModelClass() {

    }

    public String getImagePath() {
        return imagePath;
    }




    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


}
