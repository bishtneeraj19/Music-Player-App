package com.creater.music;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.creater.music.AlbumDetailsAdapter.albumFiles;
import static com.creater.music.MainActivity.musicFiles;
import static com.creater.music.MainActivity.repeateBololean;
import static com.creater.music.MainActivity.shuffleBoolean;
import static com.creater.music.MusicAdapter.mFiles;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{
TextView songName,songArtist,totalDuration,atDuration;
ImageView shuffle,repeate,next,previous,back,cover_art;
FloatingActionButton play;
SeekBar seekBar;
int position=-1;
static ArrayList<MusicFiles> ListSongs=new ArrayList<>();
static Uri uri;
static MediaPlayer mediaPlayer;
private Handler handler=new Handler();
private Thread playThread,prevThread,nextThread;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
//    Toolbar toolbar=findViewById(R.id.toolbar);
//    toolbar.setTitle("Now Playing");
//    setSupportActionBar(toolbar);
getSupportActionBar().setTitle("Now Playing");

    initViews();
    getIntentMethod();
    songName.setText(ListSongs.get(position).getTitle());
    songArtist.setText(ListSongs.get(position).getArtist());
    mediaPlayer.setOnCompletionListener(this);
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(mediaPlayer!=null&&fromUser){
                mediaPlayer.seekTo(progress*1000);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    PlayerActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer!=null){
         int mCurrent=mediaPlayer.getCurrentPosition();
         seekBar.setProgress(mCurrent/1000);
         atDuration.setText(formattedTime(mCurrent/1000));
            }
            handler.postDelayed(this,1000);
        }
    });
    shuffle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(shuffleBoolean){
                shuffleBoolean=false;
                shuffle.setImageResource(R.drawable.shuffle);
            }
            else{
                shuffleBoolean=true;
                shuffle.setImageResource(R.drawable.shuffleon);
            }
        }
    });
    repeate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(repeateBololean){
                repeateBololean=false;
                repeate.setImageResource(R.drawable.ic_repeat_black_24dp);
            }
            else{
                repeateBololean=true;
                repeate.setImageResource(R.drawable.repeaton);
            }
        }
    });
    }

    @Override
    protected void onResume() {
    playThreadBtn();
    preThreadBtn();
    nextThreadBtn();

        super.onResume();
    }

    private void preThreadBtn() {
        prevThread=new Thread(){
            @Override
            public void run() {
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            if(shuffleBoolean&&!repeateBololean){
                                position=getRandom(ListSongs.size()-1);
                            }
                            else if(!shuffleBoolean&&!repeateBololean){
                                position = ((position - 1)<0? ListSongs.size()-1:position-1);}

                            uri = Uri.parse(ListSongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            songName.setText(ListSongs.get(position).getTitle());
                            songArtist.setText(ListSongs.get(position).getArtist());
                            PlayerActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int mCurrent = mediaPlayer.getCurrentPosition();
                                        seekBar.setProgress(mCurrent / 1000);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            play.setBackgroundResource(R.drawable.pause);
                            mediaPlayer.start();
                        }
                        else {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            if(shuffleBoolean&&!repeateBololean){
                                position=getRandom(ListSongs.size()-1);
                            }
                            else if(!shuffleBoolean&&!repeateBololean){
                                position = ((position - 1)<0? ListSongs.size()-1:position-1);}
                            uri = Uri.parse(ListSongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            songName.setText(ListSongs.get(position).getTitle());
                            songArtist.setText(ListSongs.get(position).getArtist());
                            PlayerActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int mCurrent = mediaPlayer.getCurrentPosition();
                                        seekBar.setProgress(mCurrent / 1000);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });

                            play.setBackgroundResource(R.drawable.play);
                        }
                    }
                });
                super.run();
            }
        }    ;
        prevThread.start();
}

    private void nextThreadBtn() {
        nextThread=new Thread(){
            @Override
            public void run() {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            if(shuffleBoolean&&!repeateBololean){
                                position=getRandom(ListSongs.size()-1);
                            }
                            else if(!shuffleBoolean&&!repeateBololean){
                            position = ((position + 1) % ListSongs.size());}

                            uri = Uri.parse(ListSongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            songName.setText(ListSongs.get(position).getTitle());
                            songArtist.setText(ListSongs.get(position).getArtist());
                            PlayerActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int mCurrent = mediaPlayer.getCurrentPosition();
                                        seekBar.setProgress(mCurrent / 1000);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });

                            play.setBackgroundResource(R.drawable.pause);
                            mediaPlayer.start();
                        }
                        else {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            if(shuffleBoolean&&!repeateBololean){
                                position=getRandom(ListSongs.size()-1);
                            }
                            else if(!shuffleBoolean&&!repeateBololean){
                                position = ((position + 1) % ListSongs.size());}
                            uri = Uri.parse(ListSongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            songName.setText(ListSongs.get(position).getTitle());
                            songArtist.setText(ListSongs.get(position).getArtist());
                            PlayerActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int mCurrent = mediaPlayer.getCurrentPosition();
                                        seekBar.setProgress(mCurrent / 1000);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                                                  play.setBackgroundResource(R.drawable.play);
                        }
                    }});
                super.run();
            }
        }    ;
        nextThread.start();
}

    private int getRandom(int i) {
        Random random=new Random();
    return random.nextInt(i+1);
}

    private void playThreadBtn() {
playThread=new Thread(){
    @Override
    public void run() {
      play.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(mediaPlayer.isPlaying()){
                  play.setImageResource(R.drawable.play);
                  mediaPlayer.pause();
                  seekBar.setMax(mediaPlayer.getDuration()/1000);
                  PlayerActivity.this.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          if(mediaPlayer!=null){
                              int mCurrent=mediaPlayer.getCurrentPosition();
                              seekBar.setProgress(mCurrent/1000);
                          }
                          handler.postDelayed(this,1000);
                      }
                  });
              }
              else {
                  play.setImageResource(R.drawable.pause);
                  mediaPlayer.start();
                  seekBar.setMax(mediaPlayer.getDuration()/1000);
                  PlayerActivity.this.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          if(mediaPlayer!=null){
                              int mCurrent=mediaPlayer.getCurrentPosition();
                              seekBar.setProgress(mCurrent/1000);

                          }
                          handler.postDelayed(this,1000);
                      }
                  });
              }
          }
      });
        super.run();
    }
}    ;
playThread.start();
}


    private String formattedTime(int mCurrent) {
        String totalOut="";
        String totalNew="";
        String seconds=String.valueOf(mCurrent%60);
        String minutes=String.valueOf(mCurrent/60);
        totalOut=minutes+":"+seconds;
        totalNew=minutes+":"+"0"+seconds;
        if(seconds.length()==1){
        return totalNew;
        }
        else{
            return totalOut;
        }
    }

    private void getIntentMethod() {

    position=getIntent().getIntExtra("position",-1);
    String sender=getIntent().getStringExtra("sender");
    if (sender!=null&&sender.equals("albumDetails")){
        ListSongs=albumFiles;
    }else {
        ListSongs = mFiles;
    }if(ListSongs!=null){
        play.setImageResource(R.drawable.pause);
        uri=Uri.parse(ListSongs.get(position).getPath());
    }
    if(mediaPlayer!=null){
     mediaPlayer.stop();
     mediaPlayer.release();
     mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
     mediaPlayer.start();
    }
    else{
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
    }
    seekBar.setMax(mediaPlayer.getDuration()/1000);
    metaData(uri);
    }

    private void initViews() {
    songName=findViewById(R.id.song_name);
    songArtist=findViewById(R.id.song_artist);
    totalDuration=findViewById(R.id.duration_total);
    atDuration=findViewById(R.id.duration_Played);
    shuffle=findViewById(R.id.shuffle);
    repeate=findViewById(R.id.repeat);
    next=findViewById(R.id.next);
    previous=findViewById(R.id.previous);

    cover_art=findViewById(R.id.cover_art);
    play=findViewById(R.id.play);
    seekBar=findViewById(R.id.seekbar);
    }

    private void metaData(Uri uri)
    {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duration=Integer.parseInt(ListSongs.get(position).getDuration());
        totalDuration.setText(formattedTime(duration/1000));
        byte[] art=retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if(art!=null){
            bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
            animation(this,cover_art,bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch=palette.getDominantSwatch();
                if(swatch!=null){
                    ImageView imageView=findViewById(R.id.img_view);
                    RelativeLayout mContainer=findViewById(R.id.player);
                    imageView.setBackgroundResource(R.drawable.gradiant_bg);
                    mContainer.setBackgroundResource(R.drawable.main_bg);
                    GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{swatch.getRgb(),0x00000000});
                    imageView.setBackground(gradientDrawable);

                    songArtist.setTextColor(Color.GRAY);
                    songName.setTextColor(Color.WHITE);
                }
                else{
                    ImageView imageView=findViewById(R.id.img_view);
                    RelativeLayout mContainer=findViewById(R.id.player);
                    imageView.setBackgroundResource(R.drawable.gradiant_bg);
                    mContainer.setBackgroundResource(R.drawable.main_bg);
                    GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{0xff000000,0x00000000});
                    imageView.setBackground(gradientDrawable);

                    songArtist.setTextColor(Color.GRAY);
                    songName.setTextColor(Color.WHITE);
                }
                }
            });
        }
        else{
            Glide.with(this).asBitmap()
                    .load(R.drawable.music).into(cover_art);
            ImageView imageView=findViewById(R.id.img_view);
            RelativeLayout mContainer=findViewById(R.id.player);
            imageView.setBackgroundResource(R.drawable.gradiant_bg);
            mContainer.setBackgroundResource(R.drawable.main_bg);
            songArtist.setTextColor(Color.GRAY);
            songName.setTextColor(Color.WHITE);
        }
    }

    public void animation(final Context context, final ImageView imageView, final Bitmap bitmap){
        Animation animaOut= AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        final Animation animaIn= AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animaOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
Glide.with(context).load(bitmap).into(imageView);
animaIn.setAnimationListener(new Animation.AnimationListener() {
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
});
imageView.startAnimation(animaIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animaOut);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % ListSongs.size());
            uri = Uri.parse(ListSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(ListSongs.get(position).getTitle());
            songArtist.setText(ListSongs.get(position).getArtist());
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrent = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(mCurrent / 1000);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            play.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % ListSongs.size());
            uri = Uri.parse(ListSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(ListSongs.get(position).getTitle());
            songArtist.setText(ListSongs.get(position).getArtist());
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrent = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(mCurrent / 1000);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            play.setImageResource(R.drawable.play);
        }
        if (mediaPlayer!=null){
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}
