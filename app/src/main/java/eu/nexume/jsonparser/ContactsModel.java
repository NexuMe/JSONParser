package eu.nexume.jsonparser;

public class ContactsModel {

    private String mName;

    private String mEmail;

    private String mMobile;

    private String mAddress;

    public ContactsModel(String name, String email, String mobile, String address) {
        mName = name;
        mEmail = email;
        mMobile = mobile;
        mAddress = address;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getMobile() {
        return mMobile;
    }

    public String getAddress() {
        return mAddress;
    }
}
