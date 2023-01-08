package com.ulille.mmolist.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ulille.mmolist.R;
import com.ulille.mmolist.adapters.ViewPagerAdapter;

public class ViewPagerActivity extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager2 viewPager;
    String[] urisImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            urisImage = extras.getStringArray("urisImage");
        } else {
            String errMess = "Unable to fetch images";
            Toast.makeText(this, errMess, Toast.LENGTH_LONG).show();
        }
        tablayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getApplicationContext(), urisImage));
        TabLayoutMediator tlm = new TabLayoutMediator(tablayout, viewPager, (tab, position) -> tab.setText("Screenshot " + ++position));
        tlm.attach();
    }

}
