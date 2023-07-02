package com.example.contact_list;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyApi {

    @FormUrlEncoded
    @POST("DeleteContact.php")
    Call<Contact> deleteContact(@Field("ID") int id);

    @FormUrlEncoded
    @POST("InsertContact.php")
    Call<Contact> addContact(@Field("Name") String Name);


    @GET("getAllContacts.php")
    Call<List<Contact>> getallcontacts();
}
