package eu.nexume.jsonparser;

public class ContactsModel {

        private String mName;

        private String mEmail;

        private String mMobile;


        public ContactsModel(String name, String email, String mobile) {
            mName = name;
            mEmail = email;
            mMobile = mobile;
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
    }
