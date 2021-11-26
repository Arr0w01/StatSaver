package com.example.arrowstatsaver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.arrowstatsaver.SaveActivity.saveImage;
import static com.example.arrowstatsaver.SaveActivity.saveVideo;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
final Context context;
//final ArrayList<File> modelFeedArrayList;
final File[] modelFeedArrayList;
    public  static Bitmap myBitmap;
public static  String bitmapUri;
public static File currentFile;
private static final String Directory_to_save_Media_Now = "/StatSaver/";
public ListAdapter(Context context, final File[] modelFeedArrayList){
    this.context = context;
    this.modelFeedArrayList = modelFeedArrayList;
}

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items ,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       currentFile = modelFeedArrayList[position];
//     currentFile = modelFeedArrayList.get(position);
                Imageload(holder);
      Long time =      currentFile.lastModified();
        Date date = new Date(time);
        SimpleDateFormat local = new SimpleDateFormat("hh:mm aa");
        String times = local.format(date);
      holder.time.setText(times);




    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            bitmapUri = modelFeedArrayList.get(position).getAbsolutePath();
            bitmapUri = modelFeedArrayList[position].getAbsolutePath();
            Intent intent = new Intent(context, SaveActivity.class);
            intent.putExtra("Image", bitmapUri);
            context.startActivity(intent);
        }
    });

    }

    @Override
    public int getItemCount() {

//    return modelFeedArrayList.size();
     return modelFeedArrayList.length;
}

    public static class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageViewImageMedia, play;
    CardView  cardViewImageMedia, saveCard;
    RelativeLayout relativeLayout;
    TextView time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            saveCard = itemView.findViewById(R.id.saveCard);
            imageViewImageMedia = itemView.findViewById(R.id.imageViewImageMedia);
            cardViewImageMedia = itemView.findViewById(R.id.cardViewImageMedia);
            play = itemView.findViewById(R.id.play);
            relativeLayout = itemView.findViewById(R.id.Realtive);
            time = itemView.findViewById(R.id.time);

        }
    }
    private void Imageload(MyViewHolder holder)
    {
        if (currentFile.getAbsolutePath().endsWith(".mp4")){

            Uri video = Uri.parse(currentFile.getAbsolutePath());

            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(video.toString(), MediaStore.Video.Thumbnails.MICRO_KIND);

            Glide.with(context).load(thumb).dontAnimate().thumbnail(0.5f).into(holder.imageViewImageMedia);

            holder.saveCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveVideo(video, context,holder.relativeLayout);
                }
            });
        }


        else  {
            myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());

            Glide.with(context).load(myBitmap).dontAnimate().thumbnail(0.5f).into(holder.imageViewImageMedia);
            holder.play.setVisibility(View.GONE);




            holder.saveCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveImage(myBitmap, context, holder.relativeLayout);
}
            });
        }

    }
}
