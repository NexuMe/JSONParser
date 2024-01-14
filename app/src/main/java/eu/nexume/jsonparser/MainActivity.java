package eu.nexume.jsonparser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ContactsAdapter adapter;

    //    ArrayList<HashMap<String, String>> contactList;

    List<ContactsModel> contactList;
    private Handler mainThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lv = findViewById(R.id.list);

        Executor executor = Executors.newSingleThreadExecutor();
        mainThreadHandler = new Handler(Looper.getMainLooper());
        executor.execute(getContacts);
    }

    private final Runnable getContacts = new Runnable() {
        @Override
        public void run() {

            try {

                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String url = "https://doctrinalocus.web.app/json_contacts.json";
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
//                        contact.put("address", address);
//
                            ContactsModel itemContacts = new ContactsModel(name, email, mobile, address);
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

                mainThreadHandler.post(postContacts);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private final Runnable postContacts = new Runnable() {
        @Override
        public void run() {
            try {
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
                        String itemAddress = String.valueOf(currentContact.getAddress());

                        Toast.makeText(MainActivity.this, itemName, Toast.LENGTH_SHORT).show();
                    }
                });

//            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
//                    R.layout.list_item, new String[]{"name", "email", "mobile", "address"},
//                    new int[]{R.id.name, R.id.email, R.id.mobile, R.id.address});
//            lv.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}