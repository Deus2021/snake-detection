package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStart = findViewById(R.id.button1);
        Button buttonGetInfo = findViewById(R.id.button2);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetImageActivity();
            }
        });

        buttonGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetPlantInformation.class);
                startActivity(intent);
            }
        });

        // Initialize ViewPager and PagerAdapter
        ViewPager viewPager = findViewById(R.id.viewPager);
        SliderPagerAdapter pagerAdapter = new SliderPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    private void startSetImageActivity() {
        Intent intent = new Intent(MainActivity.this, SetImage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private static class SliderPagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_PAGES = 3;

        public SliderPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            return SliderFragment.newInstance(position);
        }
    }

    public static class SliderFragment extends Fragment {

        private static final String ARG_POSITION = "position";

        private int position;

        private TextView textView;

        public SliderFragment() {
            // Required empty public constructor
        }

        public static SliderFragment newInstance(int position) {
            SliderFragment fragment = new SliderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                position = getArguments().getInt(ARG_POSITION);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.textviewone, container, false);
            textView = view.findViewById(R.id.textViewPage);
            setSlideContent();
            return view;
        }

        private void setSlideContent() {
            switch (position) {
                case 0:
                    textView.setText("Slide 1");
                    break;
                case 1:
                    textView.setText("Slide 2");
                    break;
                case 2:
                    textView.setText("Slide 3");
                    break;
                default:
                    break;
            }
        }
    }

}


