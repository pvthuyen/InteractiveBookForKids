package com.example.pvthuyen.interactivebookforkids;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class QuizzActivity extends Activity {

    private int bookID;
    private int quizID = 0;
    private int correctAnswer;
    private int quizScore = 0;

    ArrayList<String> answerList = new ArrayList<>();

    private StringBuilder recordedResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        bookID = getIntent().getIntExtra("bookID", 0);
        quizID = getIntent().getIntExtra("quizID", 0);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        quizScore = sharedPref.getInt("quizScore", 0);
        String tmp = sharedPref.getString("recordedResult" + bookID, "");

        TextView quizScoreTextView = (TextView)findViewById(R.id.tvQuizScore);
        quizScoreTextView.setText("Your Score: " + quizScore);;

        if (tmp == "" || tmp.length() < Global.quizzes.get(bookID).size()) {
            tmp = "";
            for (int i = 0; i < Global.quizzes.get(bookID).size(); ++i)
                tmp = tmp + "0";
        }

        recordedResult = new StringBuilder(tmp);

        InputStream is = getResources().openRawResource(Global.quizzes.get(bookID).get(quizID));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;

        try {
            readLine = br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView)findViewById(R.id.tvQuiz);
        textView.setText(readLine);

        try {
            readLine = br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        int nAnswer = Integer.parseInt(readLine);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rgAnswers);
        radioGroup.clearCheck();
        radioGroup.removeAllViews();
        answerList.clear();

        for (int i = 0; i < nAnswer; ++i) {
//            if (quizID == 1) continue;
            RadioButton radioButton = new RadioButton(this);

            try {
                readLine = br.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            answerList.add(readLine);

            radioButton.setId(i);
            radioButton.setText(readLine);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recordedResult.setCharAt(quizID, '1');
                    if (view.getId() == correctAnswer) {
                        ++quizScore;
                        AlertDialog alertDialog = new AlertDialog.Builder(QuizzActivity.this).create();
                        alertDialog.setTitle("CORRECT!!!");
                        alertDialog.setMessage("Congratulation! The correct answer is " + answerList.get(correctAnswer)
                                + ". Your score is now " + quizScore);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        checkAchievement();
                                    }
                                });
                        alertDialog.show();

                        TextView textView1 = (TextView)findViewById(R.id.tvQuizScore);
                        textView1.setText("Your Score: " + quizScore);
                    }
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(QuizzActivity.this).create();
                        alertDialog.setTitle("WRONG ANSWER!!!");
                        alertDialog.setMessage("Sorry! Your answer is not correct.\nThe correct answer is " + answerList.get(correctAnswer));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            });

            radioGroup.addView(radioButton);
        }
        try {
            readLine = br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        correctAnswer = Integer.parseInt(readLine);

        Button buttonNextQuiz = (Button)findViewById(R.id.btNextQuiz);
        buttonNextQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizID == Global.quizzes.get(bookID).size() - 1) {
                    AlertDialog alertDialog = new AlertDialog.Builder(QuizzActivity.this).create();
                    alertDialog.setTitle("This is already the last quiz!!!");
                    alertDialog.setMessage("You have already reached the last quiz of this book. Come back next time!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    ++quizID;
                    getIntent().putExtra("quizID", quizID);

                    RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rgAnswers);
                    radioGroup.clearCheck();
                    radioGroup.removeAllViews();

                    recreate();
                }
            }
        });

        if (quizID < recordedResult.length() && recordedResult.charAt(quizID) == '1') {
            RadioButton radioButton = (RadioButton)findViewById(correctAnswer);
            radioButton.setChecked(true);
            radioGroup.setEnabled(false);

            for (int i = 0; i < nAnswer; ++i) {
                RadioButton radioButton1 = (RadioButton)findViewById(i);
                radioButton1.setEnabled(false);
            }
        }
    }

    private void checkAchievement() {
        if (Global.achievements.containsKey(quizScore)) {
            Intent intent = new Intent(QuizzActivity.this, AchievementActivity.class);
            intent.putExtra("videoCode", Global.achievements.get(quizScore));
            intent.putExtra("quizScore", quizScore);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quizz, menu);
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
        super.onPause();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("quizScore", quizScore);
        editor.putString("recordedResult" + bookID, recordedResult.toString());
//        editor.putInt("quizScore", 0);
//        editor.putString("recordedResult" + bookID, "");
        editor.commit();
    }
}
