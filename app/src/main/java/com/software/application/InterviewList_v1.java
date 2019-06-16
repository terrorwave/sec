package com.software.application;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class InterviewList_v1 extends AppCompatActivity {
    DBHelper dbHelper;
    private ListView listView;
    //private View btnList;
    private View buttonDelete;
    private View buttonImport;

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    private ArrayList<DataModel_v1> friends;
    private ArrayList<DataModel_v1> checkedList;
    private ListViewAdapter_v1 adapter;

    //public ArrayList<String> ArrayDelete;
    private String[] DeleteArr = new String[500];

    final String DIR_SD = "InterViewApp";

    private static final String TAG = "logs";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interview_list_v1);
        dbHelper = new DBHelper(this);
        listView = (ListView) findViewById(R.id.list_view);
        setListViewHeader();
        setListViewAdapter();
        setAdapterData();
        //btnList.setOnClickListener(gotoSelectedListActivity()); // go to checked list
        buttonDelete.setOnClickListener(DeleteOne());
        buttonImport.setOnClickListener(ImportItems());
    }

    private void setListViewHeader() {
        /*LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(
                R.layout.header_listview_v1, listView, false);
        listView.addHeaderView(header, null, false);
         */
        //btnList = (Button) findViewById(R.id.button);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonImport = (Button) findViewById(R.id.buttonImport);
    }

    private void setListViewAdapter() {
        friends = new ArrayList<DataModel_v1>();
        adapter = new ListViewAdapter_v1(this, R.layout.item_listview_v1, friends);
        listView.setAdapter(adapter);
    }

    private void setAdapterData() {
        adapter.clear();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_INTERVIEW, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int Name = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int notes = cursor.getColumnIndex(DBHelper.KEY_NOTES);
            int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int data = cursor.getColumnIndex(DBHelper.KEY_DATA);
            do {
                friends.add(new DataModel_v1(String.valueOf(cursor.getInt(idIndex)) + " " +
                        cursor.getString(Name) + " " +
                        cursor.getString(notes) + " "
                        + cursor.getString(date) + " ", true));
                Log.d("olo",cursor.getString(data));
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close();

        adapter.notifyDataSetChanged(); // update adapter


    }

    /*private View.OnClickListener gotoSelectedListActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterviewList_v1.this, SelectedActivity_v1.class);
                intent.putParcelableArrayListExtra("Checked List", friends);
                startActivity(intent);
            }
        };
    }*/


    private View.OnClickListener DeleteOne() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                //SQLiteDatabase database = dbHelper.getWritableDatabase();
                checkedList = new ArrayList<DataModel_v1>();
                checkedList = friends;
                if (checkedList.size() == i) {}
                while (checkedList.size() > i) {
                    final DataModel_v1 friend = checkedList.get(i);
                    if (friend.isSelected()) {
                        String ss = friend.getName().substring(0,2);
                        Log.d("YYYYYY", ss);
                        DeleteArr[i]=ss;
                        Log.d("ik",DeleteArr[i]);
                        //ArrayDelete.add(ss);
                        //confirmDialogDelete(ss);
                        //database.delete(DBHelper.TABLE_INTERVIEW, "_id=?", new String[]{ss});
                    }
                    i++;
                }
                confirmDialogDelete("2",DeleteArr);
                setAdapterData();
                //dbHelper.close();

            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You grant write external storage permission. Please click original button again to continue.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void confirmDialogDelete(final String id,final String[] ss) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление записей");
        builder.setMessage("Вы действительно хотите удалить выбранные записи?");
        builder.setCancelable(false);

        //Object[] objectList = ss.toArray();
        //final String[] stringArray =  Arrays.copyOf(objectList,objectList.length,String[].class);

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                for (int i = 0; i < ss.length; i++) {

                    try {
                        database.delete(DBHelper.TABLE_INTERVIEW, "_id=?", new String[]{ss[i]});
                    }
                    catch (SQLException e) {
                        Toast.makeText(InterviewList_v1.this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                    }

                }
                setAdapterData();
                dbHelper.close();
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dsetAdapterData();//Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void importData(String id) {
        if (ExternalStorageUtil.isExternalStorageMounted()) {
            // Check whether this app has write external storage permission or not.
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(InterviewList_v1.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // If do not grant write external storage permission.
            if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(InterviewList_v1.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
            } else {
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                Log.d("lol", id);
                Cursor cursor = database.query(DBHelper.TABLE_INTERVIEW, null, "_id = ?", new String[]{id}, null, null, null);
                cursor.moveToFirst();
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int Name = cursor.getColumnIndex(DBHelper.KEY_NAME);
                int notes = cursor.getColumnIndex(DBHelper.KEY_NOTES);
                int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
                int data = cursor.getColumnIndex(DBHelper.KEY_DATA);

                String FILE_NAME = cursor.getString(Name) + " " + cursor.getString(date) + ".txt";
                String Contetnt = "Имя интервьюримого: "+cursor.getString(Name) + "\n"
                        +"Заметка: " +cursor.getString(notes) + "\n"
                        +"Дата и время: "+ cursor.getString(date) + "\n"
                        +"Текст: "+ cursor.getString(data);

                File sdOwnDir = Environment.getExternalStorageDirectory();
                sdOwnDir = new File(sdOwnDir.getAbsolutePath() + "/" + DIR_SD);
                sdOwnDir.mkdirs();

                String Path = sdOwnDir.getAbsolutePath()+"/"+DIR_SD;
                //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

                try {
                    File newFile = new File(sdOwnDir, FILE_NAME);
                    FileWriter fw = new FileWriter(newFile);
                    fw.write(Contetnt);
                    fw.flush();
                    fw.close();
                    Toast.makeText(this, "Файл сохранен в "+Path, Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        /*finally {
            try {
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }*/

        }
    }

        private View.OnClickListener ImportItems () {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = 0;
                    SQLiteDatabase database = dbHelper.getReadableDatabase();
                    checkedList = new ArrayList<DataModel_v1>();
                    checkedList = friends;
                    if (checkedList.size() == i) {
                    }
                    while (checkedList.size() > i) {
                        final DataModel_v1 friend = checkedList.get(i);
                        if (friend.isSelected()) {
                            String ss = friend.getName().substring(0, 2);
                            importData(ss);
                        }
                        i++;
                    }
                }
            };
        }

}

