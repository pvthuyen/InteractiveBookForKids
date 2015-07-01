package com.example.pvthuyen.interactivebookforkids;

import com.example.pvthuyen.interactivebookforkids.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * Created by pvthuyen on 6/29/15.
 */

public final class Global {
    public static final ArrayList<Integer> books = new ArrayList<>(Arrays.asList(R.drawable.book0_cover, R.drawable.book1_cover));

    public static final ArrayList<Integer> book0 = new ArrayList<>(Arrays.asList(R.drawable.book0_page00, R.drawable.book0_page01,
            R.drawable.book0_page02, R.drawable.book0_page03, R.drawable.book0_page04, R.drawable.book0_page05,
            R.drawable.book0_page06, R.drawable.book0_page07, R.drawable.book0_page08, R.drawable.book0_page09));

    public static final ArrayList <Integer> book1 = new ArrayList<>(Arrays.asList(R.drawable.book1_page00, R.drawable.book1_page01,
            R.drawable.book1_page02, R.drawable.book1_page03, R.drawable.book1_page04, R.drawable.book1_page05,
            R.drawable.book1_page06, R.drawable.book1_page07, R.drawable.book1_page08, R.drawable.book1_page09));

    public static final ArrayList <ArrayList<Integer>> bookpages = new ArrayList<>(Arrays.asList(book0, book1));

    public static final ArrayList<Integer> text0 = new ArrayList<>(Arrays.asList(R.raw.book0_page00_txt,
            R.raw.book0_page01_txt, R.raw.book0_page02_txt, R.raw.book0_page03_txt, R.raw.book0_page04_txt,
            R.raw.book0_page05_txt, R.raw.book0_page06_txt, R.raw.book0_page07_txt, R.raw.book0_page08_txt,
            R.raw.book0_page09_txt));

    public static final ArrayList <Integer> text1 = new ArrayList<>(Arrays.asList(R.raw.book1_page00_txt,
            R.raw.book1_page01_txt, R.raw.book1_page02_txt, R.raw.book1_page03_txt, R.raw.book1_page04_txt,
            R.raw.book1_page05_txt, R.raw.book1_page06_txt, R.raw.book1_page07_txt, R.raw.book1_page08_txt,
            R.raw.book1_page09_txt));

    public static final ArrayList <ArrayList<Integer>> texts = new ArrayList<>(Arrays.asList(text0, text1));

    public static final ArrayList <Integer> quiz0 = new ArrayList<>();
    public static final ArrayList <Integer> quiz1 = new ArrayList<>(Arrays.asList(R.raw.book1_quiz0_txt, R.raw.book1_quiz1_txt,
            R.raw.book1_quiz2_txt));

    public static final ArrayList <ArrayList<Integer>> quizzes = new ArrayList<>(Arrays.asList(quiz0, quiz1));

    public static final String DEVELOPER_KEY = "AIzaSyDRJ4Ln0Wu0IyBJyEYEt5OEi_avC3Xj_6U";

    public static final TreeMap<Integer, String> achievements = new TreeMap<>();

    static {
        achievements.put(3, "SbAUzcuvVYc");
    }
}

