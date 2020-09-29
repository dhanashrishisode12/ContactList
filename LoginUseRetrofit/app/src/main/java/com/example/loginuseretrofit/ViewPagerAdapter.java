package com.example.loginuseretrofit;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    ArrayList<ContactModel> modelList;

    public ViewPagerAdapter(Context context, FragmentManager fm, int totalTabs, ArrayList<ContactModel> contactList) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.modelList = contactList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentContact contactFragment = new FragmentContact(this.modelList);
                return contactFragment;
            default:
                FragmentCrmContacts fragmentCrmContacts = new FragmentCrmContacts();
                return fragmentCrmContacts;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
