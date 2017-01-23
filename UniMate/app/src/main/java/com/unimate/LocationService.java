
package com.unimate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Event;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Service.START_STICKY;


public class LocationService extends Service {

    private final int UPDATE_INTERVAL = 60 * 1000;
    private Timer timer = new Timer();
    private static final int NOTIFICATION_EX = 1;
    private NotificationManager notificationManager;

    private GPSTracker gpsTracker;

    private ArrayList<Event> events;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    public LocationService() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        gpsTracker = new GPSTracker(LocationService.this);


        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        events = new ArrayList<>();

        //on first d´startup we are gathering all the event locatios:
        mDatabase.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
;
                    Event e= d.getValue(Event.class);

                    events.add(e);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Code to execute when the service is first created
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        System.out.println("startCommand .. service");

        //notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = android.R.drawable.stat_notify_sync;
        CharSequence tickerText = "Hello";
        long when = System.currentTimeMillis();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // Check if there are updates here and notify if true
                System.out.println("running .. service");

                double latitude;
                double longitude;
                Location myLocation = new Location("");

                //if we can get gps ..
                if(gpsTracker.canGetLocation()){
                    latitude = gpsTracker.getLatitude(); // returns latitude
                    longitude =gpsTracker.getLongitude(); // returns longitude
                    myLocation.set(gpsTracker.getLocation());

                    for(Event e: events){

                        double lat = -1;
                        double lon = -1;

                        lat = e.getLatitude();
                        lon = e.getLongitude();

                        if(lat != -1 && lon != -1) {
                            Location l = new Location("");
                            l.setLatitude(lat);
                            l.setLongitude(lon);

                            if(l.distanceTo(myLocation) <= 20){
                                System.out.println("you are close to: " + e.getName().toString() + " .. service");

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(LocationService.this)
                                                .setSmallIcon(R.drawable.common_full_open_on_phone)
                                                .setContentTitle("There is a Learinggroup around!")
                                                .setContentText(e.getName());

                                // Creates an explicit intent for an Activity in your app
                                // ****** CREATE INTENT *************** //

                                Intent intent = new Intent(LocationService.this, DescriptionActivity.class);
                                String groupNameString = e.getName();
                                String startTimeString = e.getStartHour() + ":" + e.getStartMinute();
                                String endTimeString = e.getEndHour() + ":" + e.getEndMinute();
                                intent.putExtra("startTime", startTimeString);
                                intent.putExtra("endTime", endTimeString);
                                intent.putExtra("groupName", groupNameString);
                                intent.putExtra("groupLocation", e.getLocation());
                                String groupDescriptionString = e.getDescription();
                                intent.putExtra("cameFromActivity", "locationService");


                                // ************************************ //
                                // The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(LocationService.this);
// Adds the back stack for the Intent (but not the Intent itself)
                                stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                                stackBuilder.addNextIntent(intent);
                                PendingIntent resultPendingIntent =
                                        stackBuilder.getPendingIntent(
                                                0,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );
                                mBuilder.setContentIntent(resultPendingIntent);
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                                mNotificationManager.notify(0, mBuilder.build());


                            }
                        }


                    }
                }
                else{
                   // gpsTracker.showSettingsAlert();
                }

            }
        }, 0, UPDATE_INTERVAL);
        return START_STICKY;
    }

    private void stopService() {
        if (timer != null) timer.cancel();
    }
}