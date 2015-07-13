package com.marinas.weemo.android.sample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.marinas.weemo.android.sample.R;

import net.rtccloud.sdk.Contact;
import net.rtccloud.sdk.Rtcc;
import net.rtccloud.sdk.view.VideoInFrame;
import net.rtccloud.sdk.view.VideoOutPreviewFrame;

public class CallActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call, menu);
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

    public void displayVideo(){
        VideoOutPreviewFrame videoOutFrame = (VideoOutPreviewFrame) findViewById(R.id.localView);
        Rtcc.instance().getCurrentCall().setVideoOut(videoOutFrame);

        VideoInFrame videoInFrame = (VideoInFrame) findViewById(R.id.remoteView);
        Rtcc.instance().getCurrentCall().setVideoIn(videoInFrame, Contact.DEFAULT_CONTACT_ID);
    }
}
