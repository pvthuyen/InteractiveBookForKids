package com.example.pvthuyen.interactivebookforkids;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class PageActivity extends Activity {

    private int bookID;
    private int pageID;

    private TextToSpeech textToSpeech;
    private String pageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        Button audioButton = (Button)findViewById(R.id.btSpeak);
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(pageContent, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        bookID = getIntent().getIntExtra("bookID", 0);
        pageID = getIntent().getIntExtra("pageID", 0);

        Bitmap image = BitmapFactory.decodeResource(getResources(),
                Global.bookpages.get(bookID).get(pageID));
        image = BitmapScaler.rescale(image, false, false);

        ImageView imageView = (ImageView)findViewById(R.id.ivPageImage);
        imageView.setImageBitmap(image);

        InputStream is = getResources().openRawResource(Global.texts.get(bookID).get(pageID));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((readLine = br.readLine()) != null) {
                stringBuilder.append(readLine);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView)findViewById(R.id.tvContent);
        textView.setText(stringBuilder.toString());
        pageContent = stringBuilder.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_page, menu);
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
    protected void onPause() {
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
}
