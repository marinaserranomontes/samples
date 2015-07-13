package com.marinas.weemo.android.sample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.rtccloud.sdk.MeetingPoint;
import net.rtccloud.sdk.Rtcc;
import net.rtccloud.sdk.RtccEngine;
import net.rtccloud.sdk.event.RtccEventListener;
import net.rtccloud.sdk.event.call.StatusEvent;
import net.rtccloud.sdk.event.global.AuthenticatedEvent;
import net.rtccloud.sdk.event.global.ConnectedEvent;


public class SampleActivity extends ActionBarActivity {
    private static String LOGTAG = "weemo-sample";

    private static String TOKEN = "";
    private Button callButton = (Button) findViewById(R.id.call_button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finalize() {
        if ( Rtcc.instance() != null ) {
            Rtcc.instance().disconnect();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Rtcc.eventBus().unregister(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        Rtcc.eventBus().register(this);
    }

    public void initialize() {
        Rtcc.initialize("4lcrkkb5f37m", SampleActivity.this);
    }

    public void onCall(View view){
        Log.i(LOGTAG, "onCall");
        Rtcc.instance().createCall("myAndroidCall");
    }

    public void authenticate(Context context, String token) {
        Rtcc.instance().authenticate(context, token, RtccEngine.UserType.INTERNAL);
    }

    public void checkStatus() {
        switch(Rtcc.getEngineStatus()) {
            case NETWORK_LOST:
                // The device lost network connection
                break;

            //case: initializing, disconnecting, authenticating, connected,....
        }
    }

    void createMultiparty(String title, String location, int startTime, int endTime) {
        MeetingPoint.Builder builder = new MeetingPoint.Builder(MeetingPoint.Type.SCHEDULED)
                .title(title)
                .location(location)
                .startTime(startTime)
                .endTime(endTime);
        Rtcc.instance().createMeetingPoint(builder);
    }

    @RtccEventListener
    public void onConnected(ConnectedEvent event) {
        Log.i(LOGTAG, "onConnected - The app is connected");
        if (event.isSuccess()) {
            authenticate(this, TOKEN);
        } else {
            // An error occurred during initilization
        }
    }

    @RtccEventListener
    public void onAuthenticated(final AuthenticatedEvent event) {
        if (event.isSuccess()) {
            // The app is authenticated
            // From here you can set your display name, create a call, ...
            Rtcc.instance().setDisplayName("MyAndroid");
            callButton.setEnabled(true);
        } else {
            // An error occurred during authentication
            checkStatus();
        }
    }


    @RtccEventListener
    public void onStatusEvent(final StatusEvent event) {
        Log.i(LOGTAG, "onStatusEvent");
        switch (event.getStatus()) {
            case PROCEEDING:
                Log.i(LOGTAG, "onStatusEvent - call is ringing on remote's device");
                break;
            case RINGING:
                Log.i(LOGTAG, "onStatusEvent - someone is calling you");
                //pick up call
                break;
            case ACTIVE:
                Log.i(LOGTAG, "onStatusEvent - the call is active, it should be displayed");
                Intent intent = new Intent(SampleActivity.this,
                        CallActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case ENDED:
                Log.i(LOGTAG, "onStatusEvent - the call ended");
                break;

        }
    }


}
