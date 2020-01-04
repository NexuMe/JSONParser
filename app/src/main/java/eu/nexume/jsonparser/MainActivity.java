package eu.nexume.jsonparser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ContactsAdapter adapter;

    //    ArrayList<HashMap<String, String>> contactList;

    List<ContactsModel> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lv = findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.androidhive.info/contacts/";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // Variables for JSON parsing
                    JSONObject c;
                    JSONObject phone;
                    String id;
                    String name;
                    String email;
                    String address;
                    String gender;
                    String mobile;
                    String home;
                    String office;

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        c = contacts.getJSONObject(i);
                        id = c.getString("id");
                        name = c.getString("name");
                        email = c.getString("email");
                        address = c.getString("address");
                        gender = c.getString("gender");

                        // Phone node is JSON Object
                        phone = c.getJSONObject("phone");
                        mobile = phone.getString("mobile");
                        home = phone.getString("home");
                        office = phone.getString("office");

                        // tmp hash map for single contact
//                        HashMap<String, String> contact = new HashMap<>();
//
//                        // adding each child node to HashMap key => value
//                        contact.put("id", id);
//                        contact.put("name", name);
//                        contact.put("email", email);
//                        contact.put("mobile", mobile);

                        ContactsModel itemContacts = new ContactsModel(name, email, mobile);
                        // adding contact to contact list
                        contactList.add(itemContacts);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter = new ContactsAdapter(
                    MainActivity.this, contactList);
            lv.setAdapter(adapter);

            // Set setOnItemClickListener on ListView
            // to make the list items clickable
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ContactsModel currentContact = adapter.getItem(position);

                    String itemName = String.valueOf(currentContact.getName());
                    String itemEmail = String.valueOf(currentContact.getEmail());
                    String itemMobile = String.valueOf(currentContact.getMobile());

                    Toast.makeText(MainActivity.this, itemName, Toast.LENGTH_SHORT).show();
                }
            });

//            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
//                    R.layout.list_item, new String[]{"name", "email", "mobile"},
//                    new int[]{R.id.name, R.id.email, R.id.mobile});
//            lv.setAdapter(adapter);
        }
    }
}