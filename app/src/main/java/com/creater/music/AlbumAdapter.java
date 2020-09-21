package com.creater.music;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.myHolder> {
    private Context mContext;
    private ArrayList<MusicFiles> albumFiles;
View view;
    public AlbumAdapter(Context mContext, ArrayList<MusicFiles> albuamFiles) {
        this.mContext = mContext;
        this.albumFiles = albuamFiles;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(mContext).inflate(R.layout.album_items,parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final int position) {
        holder.textView.setText(albumFiles.get(position).getAlbum());
        byte[] image=getAlbumArt(albumFiles.get(position).getPath());
        if (image!=null)
        {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.imageView);
        }
        else{
            Glide.with(mContext).asBitmap()
                    .load(R.drawable.music).into(holder.imageView);
        }
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(mContext,AlbumDetails.class);
        intent.putExtra("albumName",albumFiles.get(position).getAlbum());
        mContext.startActivity(intent);
    }
});

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.album_image);
            textView=itemView.findViewById(R.id.album_name);
        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
