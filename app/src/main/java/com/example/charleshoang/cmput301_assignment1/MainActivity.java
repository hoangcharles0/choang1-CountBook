package com.example.charleshoang.cmput301_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CounterBook counterBook;
    Counter newCounter;
    ArrayList<Counter> arrayBook;
    Button btnAdd;
    ListView counterView;
    Type counterListType;

    SharedPreferences bookSharedPref;
    SharedPreferences newCounterShare;
    SharedPreferences modifyCounterShare;
    SharedPreferences.Editor bookEditor;
    SharedPreferences.Editor newCounterEditor;

    Gson gson;
    String json;

    String[] counterNames;


    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();

    }

    @Override

    protected void onResume(){
        super.onResume();


        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addCounterIntent = new Intent(getApplicationContext(), CreateCounterActivity.class);
                startActivity(addCounterIntent);
            }

        });

        checkBookSize();
        Log.d("MytAg", "Count Value: " + newCounterShare.getInt("value",0));
        if (newCounterShare.getInt("value",0) == (-1)){
            Log.d("MY TAG", "DO NOT MAKE NEW COUNTER");
        }
        else{
            Log.d("MY TAG", "MAKE NEW COUNTER");
            newCounter = new Counter(newCounterShare.getString("name",""),
                    newCounterShare.getInt("value",0),newCounterShare.getString("comment",""));
            newCounterEditor.putInt("value", -1);
            newCounterEditor.putString("comment","");
            newCounterEditor.apply();

            counterBook.getCounterBook().add(newCounter);
            bookEditor.putString("counterBook", new Gson().toJson(counterBook.getCounterBook()));
            bookEditor.commit();

        }


        counterNames = counterBook.listNames();
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(counterView.getContext(),android.R.layout.simple_list_item_1, counterNames);
        counterView.setAdapter(namesAdapter);

    }



    private void initVariables() {

        counterBook = new CounterBook();
        btnAdd = (Button) findViewById(R.id.cb_add_button);
        counterView  = (ListView) findViewById(R.id.cb_counter_listview);

        bookSharedPref = getSharedPreferences("counterBook", Context.MODE_PRIVATE);
        bookEditor = bookSharedPref.edit();
        json = bookSharedPref.getString("counterBook","");


        newCounterShare = getSharedPreferences("newCounter", Context.MODE_PRIVATE);
        newCounterEditor = newCounterShare.edit();

        gson = new Gson();

        counterListType = new TypeToken<ArrayList<Counter>>() {}.getType();

//        ArrayList<Counter> emptyArray = new ArrayList<Counter>();
//
//        bookEditor.putString("counterBook", new Gson().toJson(emptyArray));
//        bookEditor.commit();
//
//
//
//        bookSharedPref = getSharedPreferences("counterBook", Context.MODE_PRIVATE);
//        bookEditor = bookSharedPref.edit();


        arrayBook = (gson.fromJson(bookSharedPref.getString("counterBook",""), counterListType));
        Log.d("MYTAG", "ARRAYBOOK: " +
                bookSharedPref.getString("abc",""));

        counterBook.setCounterBook(arrayBook);
        //No CounterBook is in bookSharedPref. Use Gson to input a new empty ArrayList
        if (counterBook.getCounterBook() == null){
            Log.d("MyTag", "null");
            counterBook.setCounterBook(new ArrayList<Counter>());
            bookEditor.putString("counterBook", new Gson().toJson(counterBook.getCounterBook()));
            bookEditor.commit();

        }
        else{

        }
        Log.d("MYTAG", "READY: ARRAYBOOK SIZE: " + arrayBook.size());
        Log.d("ThisTag","HERE");

//        counterBook.setCounterBook(new ArrayList<Counter>());
//        if (counterBook.getCounterBook().size() == 0 ) {
//            Log.d("MYTAG", "Empty Counter Book. Add 4");
//            counterBook.getCounterBook().add(new Counter("Cars", 6));
//            counterBook.getCounterBook().add(new Counter("Players", 13));
//            counterBook.getCounterBook().add(new Counter("MMR", 9000));
//            counterBook.counterBookList.add(new Counter("XD", 450));
//        }

        bookEditor.putString("counterBook", new Gson().toJson(counterBook.getCounterBook()));
        bookEditor.commit();
        Log.d("ThisTag","HERE1");

        arrayBook = (gson.fromJson(json, counterListType));
        counterBook.setCounterBook(arrayBook);
        Log.d("MYTAG", "READY: coUNTEROBOK SIZE: " + counterBook.getSize());
        Log.d("ThisTag","HERE2");
    }

    private void checkBookSize(){
        if (counterBook.getCounterBook().size() == 0) {
            Log.d("MYTAG", "Book is Empty");
        }

        else {
            Log.d("MyTag", "Size of book is " + counterBook.getCounterBook().size());
        }
    }


    public void sendMessage (View view){
        Intent startCreateCounter = new Intent (this, CreateCounterActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        startCreateCounter.putExtra(EXTRA_MESSAGE, message);
        startActivity(startCreateCounter);

    }

}
