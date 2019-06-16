package com.software.application;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class SpeechToText extends AppCompatActivity {

    private  static  final  String TAG = "logs";
    private EditText editText;
    private EditText editText2;
    private EditText editText3;

    private Button btnStart;
    private Button btnLap;
    private Button btnStop;
    private ScrollView svLap;
    private TextView tvtime;
    private EditText edLap;

    private int mLaps=1;

    private Context mContext;
    private Chronometer mChronometer;
    private Thread mThresdChrono;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_to_text);

        mContext=this;

        btnStart = (Button) findViewById(R.id.button_start);
        btnLap = (Button) findViewById(R.id.button_lap);
        btnStop = (Button) findViewById(R.id.button_stop);
        svLap = (ScrollView) findViewById(R.id.ScrollView);
        tvtime = (TextView) findViewById(R.id.textView_tv_time);
        edLap = (EditText) findViewById(R.id.textView4);

        edLap.setEnabled(false);



        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);

        dbHelper = new DBHelper(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChronometer==null){
                    mChronometer=new Chronometer(mContext);
                    mThresdChrono=new Thread(mChronometer);
                    mThresdChrono.start();
                    mChronometer.start();

                    mLaps=1;
                    edLap.setText("");

                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChronometer!=null){
                    mChronometer.stop();
                    mThresdChrono.interrupt();
                    mThresdChrono=null;
                    mChronometer=null;
                }

            }
        });
        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChronometer==null){
                    return;
                }
                editText.clearComposingText();
                editText.append(mLaps+") "+
                        String.valueOf(tvtime.getText() )+"\n");
                //String.valueOf(editText.getText())+" "+
                mLaps++;
                svLap.post(new Runnable() {
                    @Override
                    public void run() {
                        svLap.smoothScrollTo(0,edLap.getBottom());
                    }
                });

            }
        });


    }
    public void updateTimeText(final String time){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvtime.setText(time);
            }
        });
    }

    public String SelectNameFromDb()
    {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_INTERVIEWER_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();
        int Name = cursor.getColumnIndex(DBHelper.KEY_NAMEINTERVIEWER);
        return cursor.getString(Name);
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    public void getSpeechInput2(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 11);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }
    public void SaveToDB(View view) {
        String name = editText2.getText().toString();
        String notes = editText3.getText().toString();
        Calendar c = Calendar.getInstance();//date
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String date = formattedDate;
        String text = editText.getText().toString();


        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_NOTES, notes);
        contentValues.put(DBHelper.KEY_DATE, date);
        contentValues.put(DBHelper.KEY_DATA, text);

        database.insert(DBHelper.TABLE_INTERVIEW,null,contentValues);
        dbHelper.close();
        Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();


    }

    public void blockNameField(View view)
    {
        if (editText2.isEnabled()==true)
        {
            editText2.setEnabled(false);
        }
        else {
            editText2.setEnabled(true);
        }
    }
    public void blockNotesField(View view)
    {
        if (editText3.isEnabled()==true)
        {
            editText3.setEnabled(false);
        }
        else {
            editText3.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String MainName = SelectNameFromDb();

        switch (requestCode) {
            //
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    editText.append(MainName+": "+result.get(0)+"\n"+"\n");
                }
                break;
            case 11:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(editText2.getText().equals("")) {}
                    else {
                        editText.append(editText2.getText()+" :"+result.get(0)+"\n"+"\n");//editText2.getText()
                    }

                }
                break;
        }
    }
}
