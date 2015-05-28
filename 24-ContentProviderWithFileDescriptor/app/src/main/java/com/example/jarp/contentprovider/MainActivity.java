package com.example.jarp.contentprovider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends Activity {

    String name = "";
    int priority = -1;
    int status = -1;
    String owner = "";
    Uri imageUri ;

    int PICK_IMAGE = 99;

    ContentResolver resolver;
    Uri result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolver = getContentResolver();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();

        }
    }

    public void onClickChoosePhoto(View view){

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);

    }

    public void onSaveData(View view){



        ContentValues values = new ContentValues();



        values.put(TaskProvider.TaskColumns.NAME, getStringFromTextView(R.id.edt_name) );
        values.put(TaskProvider.TaskColumns.PRIORITY, getIntFromTextView(R.id.edt_priority));
        values.put(TaskProvider.TaskColumns.STATUS, getIntFromTextView(R.id.edt_status));
        values.put(TaskProvider.TaskColumns.DATA, FileUtils.getPath(this,imageUri) );
        values.put(TaskProvider.TaskColumns.OWNER, getStringFromTextView(R.id.edt_owner) );

        /*values.put(TaskProvider.TaskColumns.NAME, "JO" );
        values.put(TaskProvider.TaskColumns.PRIORITY, 1);
        values.put(TaskProvider.TaskColumns.STATUS, 1);
        values.put(TaskProvider.TaskColumns.DATA, FileUtils.getPath(this, imageUri));
        values.put(TaskProvider.TaskColumns.OWNER, "a");*/





        final Uri BASE_CONTENT_URI = Uri.parse("content://" + TaskProvider.AUTHORITY);
        final Uri AddAllTaskUri = Uri.withAppendedPath(BASE_CONTENT_URI, "task");


        result = resolver.insert(AddAllTaskUri,values);



    }

    private  String getStringFromTextView(int idView){

        return ((TextView)(findViewById(idView))).getText().toString();

    }

    private Integer getIntFromTextView(int idView){
        String x =  (( (TextView) (findViewById(idView))).getText().toString());
        return  Integer.valueOf(x);
    }

    public void onFetchImage(View view) throws IOException {

       Bitmap bmp = readBitmap();
       ImageView imageResult = (ImageView) findViewById(R.id.img_result);
       imageResult.setImageBitmap(bmp);


    }



    public Bitmap readBitmap() {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; //reduce quality
        ParcelFileDescriptor fileDescriptor =null;
        try {
            fileDescriptor = resolver.openFileDescriptor(result, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(),null,options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }


}
