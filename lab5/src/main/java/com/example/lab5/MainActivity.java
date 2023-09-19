package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    Button mBtnAdd, mBtnRead, mBtnClear;
    EditText mEdtName, mEdtEmail;
    ListView mListView;
    DBHelper mDbHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnAdd = (Button) findViewById(R.id.buttonAdd);
        mBtnAdd.setOnClickListener(this:: onClick);
        mBtnRead = (Button) findViewById(R.id.buttonRead);
        mBtnRead.setOnClickListener(this :: onClick);
        mBtnClear = (Button) findViewById(R.id.buttonClear);
        mBtnClear.setOnClickListener(this :: onClick);
        mEdtName = (EditText) findViewById(R.id.editTextName);
        mEdtEmail = (EditText) findViewById(R.id.editTextEmail);
        mListView = (ListView) findViewById(R.id.listView);
        mDbHelper = new DBHelper(this);
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // получаем данные из полей ввода
        String name = mEdtName.getText().toString();
        String email = mEdtEmail.getText().toString();
        // подключаемся к БД
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (v.getId() == R.id.buttonAdd){
            Log.d(LOG_TAG, "--- Insert in mytable: ---");
            // подготовим данные для вставки в виде пар: наименование столбца -
            // значение
            cv.put("name", name);
            cv.put("email", email);
            cv.put("dateTime", LocalDateTime.now().toString());
            // вставляем запись и получаем ее ID
            long rowID = db.insert("mytable", null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        }
        if (v.getId() == R.id.buttonRead){
            Log.d(LOG_TAG, "--- Rows in mytable: ---");
            // делаем запрос всех данных из таблицы mytable, получаем Cursor
            Cursor c = db.query("mytable", null, null, null, null, null, null);
            // ставим позицию курсора на первую строку выборки
            // если в выборке нет строк, вернется false
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                int emailColIndex = c.getColumnIndex("email");
                int dateTimeColIndex = c.getColumnIndex("dateTime");
                List<String> rows = new ArrayList<>();
                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    String string = "ID = " + c.getInt(idColIndex) + ", name = "
                            + c.getString(nameColIndex) + ", email = "
                            + c.getString(emailColIndex) + ", dateTime = "
                            + c.getString(dateTimeColIndex);
                    rows.add(string);
                    Log.d(LOG_TAG, string);

                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false -
                    // выходим из цикла
                } while (c.moveToNext());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
                mListView.setAdapter(adapter);
            } else
                Log.d(LOG_TAG, "0 rows");
            c.close();
        }
        if (v.getId() == R.id.buttonClear){
            Log.d(LOG_TAG, "--- Clear mytable: ---");
            // удаляем все записи
            int clearCount = db.delete("mytable", null, null);
            Log.d(LOG_TAG, "deleted rows count = " + clearCount);
        }
        // закрываем подключение к БД
        mDbHelper.close();
    }
    class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 2);
        }
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text"
                    + ");");
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.d(LOG_TAG, "--- onUpdate database ---");
            db.execSQL("create table mytable (" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "email text,"
                    + "dateTime text);");
        }
    }
}