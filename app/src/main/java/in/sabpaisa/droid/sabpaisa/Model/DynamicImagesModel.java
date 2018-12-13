package in.sabpaisa.droid.sabpaisa.Model;

public class DynamicImagesModel {

    String id,
    eventSliedr,
    eventName,
    eventDescription,
    eventImagePath,
    eventClientId,
    eventClientName,
    eventPlace,
    startDate,
    endDate,
    eventUrl,
    eventSubCatId,
    eventStatus,
    eventSubCatName,
    curentdate;


    public DynamicImagesModel() {
    }


    public DynamicImagesModel(String id, String eventSliedr, String eventName, String eventDescription, String eventImagePath, String eventClientId, String eventClientName, String eventPlace, String startDate, String endDate, String eventUrl, String eventSubCatId, String eventStatus, String eventSubCatName, String curentdate) {
        this.id = id;
        this.eventSliedr = eventSliedr;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventImagePath = eventImagePath;
        this.eventClientId = eventClientId;
        this.eventClientName = eventClientName;
        this.eventPlace = eventPlace;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventUrl = eventUrl;
        this.eventSubCatId = eventSubCatId;
        this.eventStatus = eventStatus;
        this.eventSubCatName = eventSubCatName;
        this.curentdate = curentdate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventSliedr() {
        return eventSliedr;
    }

    public void setEventSliedr(String eventSliedr) {
        this.eventSliedr = eventSliedr;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventImagePath() {
        return eventImagePath;
    }

    public void setEventImagePath(String eventImagePath) {
        this.eventImagePath = eventImagePath;
    }

    public String getEventClientId() {
        return eventClientId;
    }

    public void setEventClientId(String eventClientId) {
        this.eventClientId = eventClientId;
    }

    public String getEventClientName() {
        return eventClientName;
    }

    public void setEventClientName(String eventClientName) {
        this.eventClientName = eventClientName;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getEventSubCatId() {
        return eventSubCatId;
    }

    public void setEventSubCatId(String eventSubCatId) {
        this.eventSubCatId = eventSubCatId;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventSubCatName() {
        return eventSubCatName;
    }

    public void setEventSubCatName(String eventSubCatName) {
        this.eventSubCatName = eventSubCatName;
    }

    public String getCurentdate() {
        return curentdate;
    }

    public void setCurentdate(String curentdate) {
        this.curentdate = curentdate;
    }
}
