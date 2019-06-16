package com.software.application;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


public class Settings extends AppCompatActivity {

    private  static  final  String TAG = "logs";
    private  TextView textView1;
    private  TextView editText1;
    DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        dbHelper = new DBHelper(this);

        textView1 = (TextView)findViewById(R.id.textView);
        editText1 = (TextView)findViewById(R.id.editText1);


        SelectFromDb();

    }
    public void SaveNameToDb(View view)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String name="Здесь будут настройки";
        if (editText1.getText().toString().equals(""))
        {
            Toast.makeText(this, "Введите ваше имя", Toast.LENGTH_SHORT).show();
        }
        else {
            name = editText1.getText().toString();
        }

        contentValues.put(DBHelper.KEY_NAMEINTERVIEWER, name);
        try {
            database.update(DBHelper.TABLE_INTERVIEWER_NAME, contentValues, "_id=1", null);
        }
        catch (SQLException e) {
            final String message = e.getMessage();
            Log.d("Log catch",message);
        }
        Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();

        /*Cursor cursor = database.query(DBHelper.TABLE_INTERVIEWER_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_NAME);
        int Name = cursor.getColumnIndex(DBHelper.KEY_NAMEINTERVIEWER);
        Log.d("log","id= "+cursor.getInt(idIndex)+ " name" + cursor.getString(Name));
        dbHelper.close();*/
    }
    public void SelectFromDb()
    {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_INTERVIEWER_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();
        int Name = cursor.getColumnIndex(DBHelper.KEY_NAMEINTERVIEWER);
        editText1.setText(cursor.getString(Name));
    }


}
