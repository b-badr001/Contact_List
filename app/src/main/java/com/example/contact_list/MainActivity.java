package com.example.contact_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button btn;

    Button delete;

    EditText edit;

    String selectedItem;
    int selectedItemId;

    ListView listContacts;

    ArrayAdapter<String> adapter = null;

    String url = "http://10.0.2.2:80/listcontact/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listContacts = findViewById(R.id.listContacts);
        btn = findViewById(R.id.addButton);
        delete = findViewById(R.id.deleteButton);
        edit = findViewById(R.id.editName);

        Retrofit retrofit = new Retrofit.Builder( ).baseUrl(url).addConverterFactory(GsonConverterFactory.create( )).build( );

        MyApi api = retrofit.create(MyApi.class);

        Call<List<Contact>> call = api.getallcontacts( );

        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                    List<Contact> data = response.body();

                    adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, Listin(data));
                    listContacts.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                process( );
            }
        });

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = adapter.getItem(position);
                String[] parts = selectedItem.split(":");
                selectedItemId = Integer.parseInt(parts[0].trim());
                edit.setText(selectedItem);
            }
        });

        delete.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                deleteContact();
            }
        });


    }

    public void process() {
        Retrofit retrofit = new Retrofit.Builder( )
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create( ))
                .build( );

        MyApi api = retrofit.create(MyApi.class);
        Call<Contact> call = api.addContact(edit.getText( ).toString( ));
        call.enqueue(new Callback<Contact>( ) {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {

                Contact addedContact = response.body();
                if (addedContact != null) {
                    adapter.add(addedContact.getID() + ": " + addedContact.getName());
                    adapter.notifyDataSetChanged();
                    edit.setText("");
                    fetchContacts();
                    Toast.makeText(getApplicationContext(), "Contact added successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {

            }
        });


    }

    public void deleteContact() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi api = retrofit.create(MyApi.class);

        Call<Contact> call = api.deleteContact(selectedItemId);
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Log.d("DeleteResponse", response.toString());
                adapter.remove(selectedItem);
                adapter.notifyDataSetChanged();
                edit.setText("");
                Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.e("DeleteError", "Failed to delete contact", t);
            }
        });
    }


    ArrayList Listin(List<Contact> l){
        ArrayList<String> maliste = new ArrayList<>( );
        for (int i = 0; i < l.size(); i++) {
            maliste.add(l.get(i).getID() + ": " + l.get(i).getName());
        }
        return maliste;
    }

    public void fetchContacts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi api = retrofit.create(MyApi.class);
        Call<List<Contact>> call = api.getallcontacts();
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                List<Contact> data = response.body();

                adapter.clear(); // Clear the existing data in the adapter

                if (data != null) {
                    adapter.addAll(Listin(data)); // Add the updated list of contacts
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                // Handle failure
            }
        });
    }

}