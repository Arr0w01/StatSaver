package com.example.arrowstatsaver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import fragment.instaFragment;
import fragment.savedFragment;
import fragment.statusFragment;

import static org.apache.commons.io.comparator.LastModifiedFileComparator.LASTMODIFIED_COMPARATOR;

public class MainActivity extends AppCompatActivity implements UpdateHelper.onUpdateCheckListener {

    ImageView whatsappOpen;
    final String Whatsapp_Status_Location = "/WhatsApp/Media/.Statuses";
    final String and11 = "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
    final String Statsaver_location = "/StatSaver";
    public  static  int REQUEST_CODE = 1;
    public static String version;
    public static File[] files;
    public static File[] savedFiles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();



        whatsappOpen = findViewById(R.id.whatsappOpen);
        whatsappOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Opening WhatsApp", Toast.LENGTH_SHORT).show();
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
               startActivity(intent);



            }




        });

//
            File a = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Whatsapp_Status_Location);
               files = a.listFiles();
            Arrays.sort(files,LASTMODIFIED_COMPARATOR);
            ArrayList<File> f = getListFiles(a);
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           f.sort(LASTMODIFIED_COMPARATOR);
//
             }

        File saved = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Statsaver_location);
        savedFiles = saved.listFiles();
        Arrays.sort(savedFiles,LASTMODIFIED_COMPARATOR);
        ArrayList<File> x = getListFiles(saved);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            x.sort(LASTMODIFIED_COMPARATOR);
//
        }


//
           final Handler handler = new Handler(Looper.getMainLooper());
//
//
       handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//
           UpdateHelper.with(MainActivity.this).onUpdateCheck(MainActivity.this).check();
//
            }
        },15000);


           PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo( getPackageName(),0);
             version = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



    }


    public static ArrayList<File> getListFiles(File parentDir){
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null)
        {
            for (File file : files){
                Log.e("check", file.getName());
                if (file.getName().endsWith(".jpg") ||
                     file.getName().endsWith(".mp4") ||
                     file.getName().endsWith(".gif")) {
                    if (!inFiles.contains(file))
                    {
                        inFiles.add(file);
                    }

                }
            }
        }
        return inFiles;
    }


    @Override
    public void onUpdateCheckListener(String urlApp) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("New Version Available")
                .setMessage("Please Update to the new version for better experience")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//
//
                              String url = "http://statsaver.epizy.com/";
                              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                              startActivity(Intent.createChooser(intent, "Choose browser"));// Choose browser is arbitrary :)
                              Toast.makeText(MainActivity.this, "Choose a Browser", Toast.LENGTH_LONG).show();

                                }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dailog);
    }
    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new statusFragment(),"Status");
        viewPagerAdapter.addFragments(new instaFragment(),"IG");
        viewPagerAdapter.addFragments(new savedFragment(), "Saved");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.whatsapp_color);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_instagram);
        tabLayout.getTabAt(2).setIcon(R.drawable.dload);
        viewPager.setOffscreenPageLimit(3);

    }

    public static class viewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        public viewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }
        void addFragments(Fragment fragment, String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}