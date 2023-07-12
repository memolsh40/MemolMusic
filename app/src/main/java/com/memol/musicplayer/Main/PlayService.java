package com.memol.musicplayer.Main;

import static com.memol.musicplayer.G.CHANNEL_ID_1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.memol.musicplayer.R;

@SuppressWarnings("ALL")
public class PlayService extends Service {


  public static MediaPlayer mediaPlayer;
    String path;
    private int position;
    IBinder binder=new MyBinder();
    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Bind","Method");
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        mediaPlayer.stop();
    }

    public void createNotification(){

        Notification notification=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID_1)
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Test")
                .setContentText("Test2")
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(100,notification);
    }

    public class MyBinder extends Binder {

        public PlayService getService(){
            return PlayService.this;
        }

    }

    public  int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void StartMusic(String path){
        try {
            if (mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer= MediaPlayer.create(getApplicationContext(), Uri.parse(path));
                mediaPlayer.start();
                createNotification();
            } else {
                mediaPlayer= MediaPlayer.create(getApplicationContext(), Uri.parse(path));
                mediaPlayer.start();
                createNotification();
            }
        }catch (Exception e){

            if (mediaPlayer==null){
                Toast.makeText(this, "Application can not read this music", Toast.LENGTH_LONG).show();
                mediaPlayer=new MediaPlayer();



            }



        }

    }
    public void StartWhenStop(String path){

  if (!mediaPlayer.isPlaying()){
      mediaPlayer.stop();
      mediaPlayer.release();
      mediaPlayer= MediaPlayer.create(getApplicationContext(), Uri.parse(path));
  }
    }
    public void PauseMusic(){
  if (mediaPlayer!=null){
      mediaPlayer.pause();
  }
}
public void PlayMusic(){
  if (mediaPlayer!=null){
      mediaPlayer.isPlaying();
  }
}



}
