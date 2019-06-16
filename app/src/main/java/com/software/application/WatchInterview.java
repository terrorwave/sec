package com.software.application;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;


public class WatchInterview extends AppCompatActivity {
    DBHelper dbHelper;
    private  static  final  String TAG = "logs";
    private EditText editTextNameInterviewerWatcher;
    private EditText editTextInterwiewWatcher;
    private EditText editTextNoteWatcher;
    private Button buttonWatcherSave;

    private EditText editTextFinder;
    private  Button buttonSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_interview_v1);
        dbHelper = new DBHelper(this);

        editTextNameInterviewerWatcher = (EditText) findViewById(R.id.editTextNameInterviewerWatcher);
        editTextInterwiewWatcher = (EditText) findViewById(R.id.editTextInterwiewWatcher);
        editTextNoteWatcher = (EditText) findViewById(R.id.editTextNoteWatcher);
        buttonWatcherSave = (Button)findViewById(R.id.buttonWatcherSave);

        editTextFinder = (EditText) findViewById(R.id.editTextFinder);
        buttonSearch = (Button)findViewById(R.id.buttonSearch);
        //!!!!

        Bundle arguments = getIntent().getExtras();
        String id = arguments.get("_id").toString();
        GetData(id);
        buttonWatcherSave.setOnClickListener(ClickUpdate(id));
        buttonSearch.setOnClickListener(FindText());


        /*editTextFinder.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = editTextInterwiewWatcher.getText().toString();
                Log.d("Position",String.valueOf(text.indexOf(s.toString())));
                int position = text.indexOf(s.toString());
                if (position != -1){
                    Log.d("olo",getCurrentFocus().toString());
                    if (getCurrentFocus()==editTextFinder){}
                    else{
                        setPos(position);
                    }
                }
                else {}
            }
        });

        editTextFinder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){ }
            }
        });*/



    }

    private View.OnClickListener FindText () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editTextFinder.getText().toString();
                String text = editTextInterwiewWatcher.getText().toString();
                Log.d("Position",String.valueOf(text.indexOf(s)));
                int position = text.indexOf(s);
                if (position != -1){
                    //Log.d("olo",getCurrentFocus().toString());
                    setPos(position);
                }
                else {}
            }
        };
    }
    public void setPos(int pos){

        editTextInterwiewWatcher.requestFocus();
        editTextInterwiewWatcher.setSelection(pos);
    }


    public void GetData(String id)
    {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Log.d("lol",id);
        Cursor cursor = database.query(DBHelper.TABLE_INTERVIEW,null,"_id = ?",new String[]{id},null,null,null);
        cursor.moveToFirst();
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int Name = cursor.getColumnIndex(DBHelper.KEY_NAME);
        int notes = cursor.getColumnIndex(DBHelper.KEY_NOTES);
        int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
        int data = cursor.getColumnIndex(DBHelper.KEY_DATA);
        editTextNameInterviewerWatcher.setText(cursor.getString(Name));
        editTextNoteWatcher.setText(cursor.getString(notes));
        editTextInterwiewWatcher.setText(cursor.getString(data));
        cursor.close();
        dbHelper.close();
    }
    public void UpdateData(String id){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, editTextNameInterviewerWatcher.getText().toString());
        contentValues.put(DBHelper.KEY_NOTES, editTextNoteWatcher.getText().toString());
        contentValues.put(DBHelper.KEY_DATA, editTextInterwiewWatcher.getText().toString());

        String s ="";
        if (editTextInterwiewWatcher.getText().equals(s)){
            Toast.makeText(this, "Заполните поле текста интервью", Toast.LENGTH_SHORT).show();
        }
        else{ if(editTextNoteWatcher.getText().equals(s)) Toast.makeText(this, "Заполните заметку интервью", Toast.LENGTH_SHORT).show();
            else { if(editTextNameInterviewerWatcher.getText().equals(s))  Toast.makeText(this, "Заполните имя интервьюримого", Toast.LENGTH_SHORT).show();
            else {
                try{
                    database.update(DBHelper.TABLE_INTERVIEW,contentValues,"_id = ?", new String[]{id});
                }
                catch (SQLException e){
                    Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private View.OnClickListener ClickUpdate (final String id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData(id);
            }
        };
    }























}
