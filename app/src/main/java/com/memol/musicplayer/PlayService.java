package com.memol.musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

@SuppressWarnings("ALL")
public class PlayService extends Service {


  public static MediaPlayer mediaPlayer;
    String path;
    IBinder binder=new MyBinder();
    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1000,createNotification());
        mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        mediaPlayer.stop();
    }

    public Notification createNotification(){
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel=null;
        notificationChannel=new NotificationChannel("MyNotifiChannel","MemolMusic",NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setName("MemolCode");
        notificationChannel.setDescription("TestCode");
        notificationManager.createNotificationChannel(notificationChannel);

        Notification notification=new NotificationCompat.Builder(this,"MyNotifiChannel")
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Test")
                .setContentText("Test2")
                .setAutoCancel(true)
                .build();
        return notification;
    }

    public class MyBinder extends Binder {

        public PlayService getService(){
            return PlayService.this;
        }

    }
    public void StartMusic(String path){

  if (mediaPlayer!=null){
      mediaPlayer.stop();
      mediaPlayer.release();
      mediaPlayer= MediaPlayer.create(getApplicationContext(), Uri.parse(path));
      mediaPlayer.start();
  }else {
      mediaPlayer= MediaPlayer.create(getApplicationContext(), Uri.parse(path));
      mediaPlayer.start();
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
