package com.example.pvthuyen.interactivebookforkids;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class VisualSearchResultActivity extends Activity {

    ArrayList<Integer> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_search_result);

        searchResult = getIntent().getIntegerArrayListExtra("searchResult");

        LinearLayout container = (LinearLayout) VisualSearchResultActivity.this.findViewById(R.id.book_scroll_container);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        for (int i = 0; i < searchResult.size(); ++i) {
            LinearLayout item = new LinearLayout(VisualSearchResultActivity.this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setLayoutParams(layoutParams);
            container.addView(item);

            ImageView imageView = new ImageView(VisualSearchResultActivity.this);
            imageView.setId(searchResult.get(i));
            imageView.setLayoutParams(layoutParams);
            imageView.setBackgroundResource(Global.books.get(searchResult.get(i)));

            AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getBackground();
            frameAnimation.start();

            item.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(VisualSearchResultActivity.this, PageListsActivity.class);
                    intent.putExtra("bookID", view.getId());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visual_search_result, menu);
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
