package com.example.pvthuyen.interactivebookforkids;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class PageListsActivity extends Activity {

    private int bookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_lists);

        Button quizButton = (Button)findViewById(R.id.btQuizz);

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageListsActivity.this, QuizzActivity.class);
                intent.putExtra("bookID", bookID);
                startActivity(intent);
            }
        });

        bookID = getIntent().getIntExtra("bookID", 0);

        LinearLayout container = (LinearLayout) PageListsActivity.this.findViewById(R.id.page_scroll_container);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        ArrayList <Integer> thisBook = (ArrayList <Integer>)Global.bookpages.get(bookID);

        for (int i = 0; i < thisBook.size(); ++i) {
            LinearLayout item = new LinearLayout(PageListsActivity.this);
            item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            item.setOrientation(LinearLayout.HORIZONTAL);
            container.addView(item);

            Bitmap image = BitmapFactory.decodeResource(getResources(),
                    thisBook.get(i));
            image = BitmapScaler.rescale(image, false, false);

            ImageView imageView = new ImageView(PageListsActivity.this);
            imageView.setId(i);
            imageView.setImageBitmap(image);
            imageView.setLayoutParams(layoutParams);
            item.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PageListsActivity.this, PageActivity.class);
                    intent.putExtra("bookID", bookID);
                    intent.putExtra("pageID", view.getId());
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_page_lists, menu);
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
}
