package com.example.arrowstatsaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveActivity extends AppCompatActivity {
    ImageView back, main, download, share, whatsapp;
    VideoView videoView;
    public ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        constraintLayout = findViewById(R.id.ml);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

        Intent intent =  getIntent();

        String uri = intent.getStringExtra("Image") ;
        download = findViewById(R.id.save);
        share = findViewById(R.id.share);
        whatsapp = findViewById(R.id.whatsapp);

        if (!uri.contains("mp4")) {
            Bitmap bitmap = BitmapFactory.decodeFile(uri);
            main = findViewById(R.id.imageView);
            main.setImageBitmap(bitmap);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveImage(bitmap, getBaseContext(),constraintLayout);
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sintent = new Intent(Intent.ACTION_SEND);
                    sintent.setType("image/jpeg");
                    Uri u = Uri.parse(uri);
                    sintent.putExtra(Intent.EXTRA_STREAM, u );
                    startActivity(Intent.createChooser(sintent, "Share image Using.."));
                }
            });
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SaveActivity.this, "Sending to WhatsApp..", Toast.LENGTH_SHORT).show();
                    Intent wintent = new Intent(Intent.ACTION_SEND);
                    wintent.setType("image/jpeg");
                    Uri u = Uri.parse(uri);
                    wintent.putExtra(Intent.EXTRA_STREAM, u );
                    wintent.setPackage("com.whatsapp");
                    startActivity(wintent);
                }
            });

        }else {
            videoView = findViewById(R.id.videoView);
            videoView.setVisibility(View.VISIBLE);
            Uri video = Uri.parse(uri);
            videoView.setVideoURI(video);
            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                videoView.start();
            });

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveVideo(video, getBaseContext(),constraintLayout);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sintent = new Intent(Intent.ACTION_SEND);

                    sintent.setType("video/mp4");
                    sintent.putExtra(Intent.EXTRA_STREAM, video );
                    startActivity(Intent.createChooser(sintent, "Share video Using.."));
                }
            });
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SaveActivity.this, "Sending to WhatsApp..", Toast.LENGTH_SHORT).show();
                    Intent wintent = new Intent(Intent.ACTION_SEND);
                    wintent.setType("video/mp4");
                    wintent.putExtra(Intent.EXTRA_STREAM, video);
                    wintent.setPackage("com.whatsapp");
                    startActivity(wintent);

                }
            });

        }

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    public static void saveImage(Bitmap image, Context context, View layout){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/StatSaver");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "StatSaver"+timeStamp+".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,100, out);
            out.flush();
            out.close();


            Snackbar.make(layout, "Image Saved to"+myDir, Snackbar.LENGTH_LONG)
                                            .setAction("OPEN", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                                    intent.setType( "image/*");
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(intent);


                                                }
                                            }).setActionTextColor(Color.WHITE).show();

            MediaScannerConnection.scanFile(context, new String[]{myDir.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void saveVideo(Uri video, Context context,View layout){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root+ "/StatSaver");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Statsaver"+timeStamp+".mp4";
        File file = new File(myDir,fname);

        String sourceFilename = video.getPath();

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(file.toString(), false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            }while (bis.read(buf) != -1);
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();

                Snackbar.make(layout, "Image Saved to"+myDir, Snackbar.LENGTH_LONG)
                        .setAction("OPEN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                intent.setType( "image/*");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);


                            }
                        }).setActionTextColor(Color.WHITE).show();


                MediaScannerConnection.scanFile(context, new String[]{myDir.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}