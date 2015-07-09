package com.example.pvthuyen.interactivebookforkids;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VisualSearchActivity extends Activity {

    ImageView viewImage;
    Button b;

    ArrayList<Integer> searchResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_search);

        b=(Button)findViewById(R.id.btnSelectPhoto);
        viewImage=(ImageView)findViewById(R.id.viewImage);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visual_search, menu);
        return true;
    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(VisualSearchActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        extractAllDrawableFeature();
        Mat img = new Mat();
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    viewImage.setImageBitmap(bitmap);

                    Bitmap tmp = BitmapScaler.rescale(bitmap, false, false);

                    Utils.bitmapToMat(tmp, img);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                viewImage.setImageBitmap(thumbnail);

                Bitmap tmp = BitmapScaler.rescale(thumbnail, false, false);

                Utils.bitmapToMat(tmp, img);
            }
        }
        search(img);
    }

    private void extractAllDrawableFeature() {
        if (Global.bookpages_features != null) return;
        Global.bookpages_features = new ArrayList<>();
        Global.bookpages_features.add(new ArrayList<Mat>());
        Global.bookpages_features.add(new ArrayList<Mat>());
        for (int i = 0; i < Global.bookpages.size(); ++i) {
            for (int j = 0; j < Global.bookpages.get(i).size(); ++j) {
                Mat img = null;
                try {
                    img = Utils.loadResource(this, Global.bookpages.get(i).get(j), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//                    Imgproc.cvtColor(tmp, img, Imgproc.COLOR_RGB2BGRA);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                MatOfKeyPoint keypoints = new MatOfKeyPoint();
                FeatureDetector surf = FeatureDetector.create(FeatureDetector.MSER);
                surf.detect(img, keypoints);

                DescriptorExtractor surfDescriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
                Mat descriptor = new Mat();
                surfDescriptor.compute(img, keypoints, descriptor);

                Global.bookpages_features.get(i).add(descriptor);
            }
        }
    }

    private void search(Mat img) {
        searchResult = new ArrayList<>();

        Mat tmpImg = img.clone();
        Imgproc.cvtColor(tmpImg, img, Imgproc.COLOR_RGBA2GRAY);

        MatOfKeyPoint queryKeypoints = new MatOfKeyPoint();
        FeatureDetector surf = FeatureDetector.create(FeatureDetector.MSER);
        surf.detect(img, queryKeypoints);

        DescriptorExtractor surfDescriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        Mat queryDescriptor = new Mat();
        surfDescriptor.compute(img, queryKeypoints, queryDescriptor);

        for (int i = 0; i < Global.bookpages_features.size(); ++i) {
            for (int j = 0; j < Global.bookpages_features.get(i).size(); ++j) {
                DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_SL2);
                MatOfDMatch matches = new MatOfDMatch();
                matcher.match(queryDescriptor, Global.bookpages_features.get(i).get(j), matches);

                List<DMatch> tmp = matches.toList();

                float minDist = 1000000;

                for (int k = 0; k < tmp.size(); ++k)
                    minDist = Math.min(minDist, tmp.get(k).distance);

                int cnt = 0;
                for (int k = 0; k < tmp.size(); ++k)
                    if (tmp.get(k).distance < Math.max(minDist * 2, 0.02))
                        ++cnt;

                if ((float)cnt / queryDescriptor.rows() > 0.5) {
                    searchResult.add(i);
                }
            }
        }

        Intent intent = new Intent(this, VisualSearchResultActivity.class);
        intent.putIntegerArrayListExtra("searchResult", searchResult);
        startActivity(intent);
    }
}
