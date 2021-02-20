package io.agora.tutorials1v1vcall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivityStudent extends AppCompatActivity implements IAPIResponse {

    private RecyclerView recycler;
    private Button askQuery;

    List<Data> data = new ArrayList<Data>();
    List<Data> filter_data = new ArrayList<Data>();
    ItemAdapter adapter;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);


        recycler = (RecyclerView) findViewById(R.id.recycle);
        askQuery = (Button) findViewById(R.id.askQuestion);

        askQuery.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("Hello", "TEST BUTTON PRESS");
                startActivityFromButton();
                //
            }
        });

        progressdialog = new ProgressDialog(MainActivityStudent.this);
        progressdialog.setMessage("Loading....");
        progressdialog.show();
        fetchData();
//        data.add(new Data(0,"Maths", "Grade: 11", "Topic: Calculus", "Details: Differentiation", 0, 1, 0));
//        data.add(new Data(0, "Physics", "Grade: 5", "Topic: Mechanics", "Details: Pressure", 0, 1, 1));
//        data.add(new Data("English", "Grade: 12", "Topic: Literature", "Details: Shakespeare"));
//        data.add(new Data("Maths", "Grade: 12", "Topic: Calculus", "Details: Integration"));

        Log.d("Trial", "Hello" + String.valueOf(data.size()));

        Iterator<Data> it = data.iterator();
        while (it.hasNext()){
            Data d = it.next();
            Data newD = new Data(d.userID, d.questionID, d.subject, d.grade, d.topic, d.desc, d.status, d.teacherID);
            filter_data.add(newD);
        }

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(filter_data, this);
        recycler.setAdapter(adapter);
    

    }

    protected void fetchData() {
        API api = new API(this, this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("Id", -1);
        SystemClock.sleep(2000);
        api.fetchStudentQuestions(userId);  // TODO: change here
    }

    protected void startActivityFromButton(){
        Intent intent = new Intent(this, AskQuestion.class);

        startActivity(intent);

    }

    @Override
    public void onSuccessData(ArrayList<Data> data1) {
        Log.d("API", " Size of THings are for some reason" + data1.size());
        data = data1;
        if (progressdialog.isShowing())
            progressdialog.dismiss();
        populateList();

    }

    @Override
    public void onSuccessInt(int id) {
        Log.d("API", String.valueOf(id));
        if (progressdialog.isShowing())
            progressdialog.dismiss();
    }

    private void populateList(){

        filter_data.clear();

        for (int i = 0; i < data.size(); i++){
            Log.d("Hello", data.get(i).subject.toString());
            filter_data.add(data.get(i));
        }

        Log.d("Hello", filter_data.toString());
        adapter.notifyDataSetChanged();


    }
}

