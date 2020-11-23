package com.example.diplom;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class Admin_activity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    private Admin_activity smb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        ViewPager vpPager = findViewById(R.id.vpPager);
        smb = this;
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), smb);
        vpPager.setAdapter(adapterViewPager);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Admin_activity.this, MainActivity.class);
        startActivity(intent);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private Admin_activity smb;

        public MyPagerAdapter(FragmentManager fragmentManager, Admin_activity smb) {
            super(fragmentManager);
            this.smb = smb ;
        }

        // возращает кол-во фрагментов
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Возвращает фрагмент для отображения на этой странице
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Это покажет первый фрагмент
                    return new TestListTab(smb);
                case 1: // Это покажет второй фрагмент
                    return new Results(smb);
                default:
                    return null;
            }
        }

        // Установка заголовков
        @Override
            public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Тесты";
                case 1:
                    return "Результаты";
                default:
                    return null;
            }
        }
    }
}