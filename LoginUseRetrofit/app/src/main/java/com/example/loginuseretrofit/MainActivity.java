package com.example.loginuseretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    ArrayList<ContactModel> modelList;
    private ViewPager viewPager;

    private void prepareContactList() {
        modelList = getAllContacts();
    }

    private void prepareCRMContactList() {
        modelList = getAllCRMContacts();
    }

    private  void configureView() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setText("CRM Contacts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount(), this.modelList);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
//                    getAllContacts();
                    pagerAdapter.modelList = modelList;
                } else {
//                    getAllCRMContacts();
                    pagerAdapter.modelList = modelList;
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareContactList();
        configureView();
    }

    private ArrayList getAllCRMContacts() {
    //Retrofit call, API interface, data parsing - creating model objects and prepare list
        modelList = new ArrayList<ContactModel>();
        ContactModel contactInfo = new ContactModel();
        contactInfo.setDisplayName("Pavan");
        contactInfo.setContactId("id");
        modelList.add(contactInfo);
        return modelList;
    }

    private ArrayList getAllContacts() {
        ContentResolver cr = getContentResolver();
        modelList = new ArrayList<ContactModel>();

        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if ((cursor != null ?  cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                ContactModel contactInfo = new ContactModel();
                String id  = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                contactInfo.setDisplayName(name);
                contactInfo.setContactId(id);

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0){
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new  String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactInfo.setPhoneNumber(phoneNo);

                    }
                    pCur.close();

                    modelList.add(contactInfo);
                }
            }
        }
        if (cursor != null){
            cursor.close();
        }
        return modelList;
    }
    }
