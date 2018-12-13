package in.sabpaisa.droid.sabpaisa.Model;

public class FilterClientModel {

    public String organizationId;
    public String organization_name;
    public String relationship_type;
    public String orgLogo;
    public String orgWal;
    public String orgAddress;
    public String orgDesc;
    public String conPersonName;
    public Long conPersonMobNO;
    public String conPersonEmail;
    public String stateId;
    public Long createdDate;

    public FilterClientModel() {
    }


    public FilterClientModel(String organizationId, String organization_name, String relationship_type, String orgLogo, String orgWal, String orgAddress, String orgDesc, String conPersonName, Long conPersonMobNO, String conPersonEmail, String stateId, Long createdDate) {
        this.organizationId = organizationId;
        this.organization_name = organization_name;
        this.relationship_type = relationship_type;
        this.orgLogo = orgLogo;
        this.orgWal = orgWal;
        this.orgAddress = orgAddress;
        this.orgDesc = orgDesc;
        this.conPersonName = conPersonName;
        this.conPersonMobNO = conPersonMobNO;
        this.conPersonEmail = conPersonEmail;
        this.stateId = stateId;
        this.createdDate = createdDate;
        this.organizationId = organizationId;
        this.organization_name = organization_name;
        this.relationship_type = relationship_type;
        this.orgLogo = orgLogo;
        this.orgWal = orgWal;
        this.orgAddress = orgAddress;
        this.orgDesc = orgDesc;
        this.conPersonName = conPersonName;
        this.conPersonMobNO = conPersonMobNO;
        this.conPersonEmail = conPersonEmail;
        this.createdDate = createdDate;
    }


    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(String relationship_type) {
        this.relationship_type = relationship_type;
    }

    public String getOrgLogo() {
        return orgLogo;
    }

    public void setOrgLogo(String orgLogo) {
        this.orgLogo = orgLogo;
    }

    public String getOrgWal() {
        return orgWal;
    }

    public void setOrgWal(String orgWal) {
        this.orgWal = orgWal;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    public String getConPersonName() {
        return conPersonName;
    }

    public void setConPersonName(String conPersonName) {
        this.conPersonName = conPersonName;
    }

    public Long getConPersonMobNO() {
        return conPersonMobNO;
    }

    public void setConPersonMobNO(Long conPersonMobNO) {
        this.conPersonMobNO = conPersonMobNO;
    }

    public String getConPersonEmail() {
        return conPersonEmail;
    }

    public void setConPersonEmail(String conPersonEmail) {
        this.conPersonEmail = conPersonEmail;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }


    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}
