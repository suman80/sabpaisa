package in.sabpaisa.droid.sabpaisa.Model;

public class AddMemberTableModel {

    String Field,Type,Null,Extra,Default,Key;

    public AddMemberTableModel() {
    }

    public AddMemberTableModel(String field, String type, String aNull, String extra, String aDefault, String key) {
        Field = field;
        Type = type;
        Null = aNull;
        Extra = extra;
        Default = aDefault;
        Key = key;
    }


    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNull() {
        return Null;
    }

    public void setNull(String aNull) {
        Null = aNull;
    }

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public String getDefault() {
        return Default;
    }

    public void setDefault(String aDefault) {
        Default = aDefault;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
