package io.agora.tutorials1v1vcall;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements IAPIResponse {

    private RecyclerView recycler;
    private Button filter;

    List<Data> data = new ArrayList<Data>();
    List<Data> filter_data = new ArrayList<Data>();
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recycler = (RecyclerView) findViewById(R.id.recycle);
        filter = (Button) findViewById(R.id.filter_button);

        filter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                filterResults("maths");
                Log.d("Hello", "TEST BUTTON PRESS");
            }
        });


        fetchData();
//        data.add(new Data("Maths", "Grade: 11", "Topic: Calculus", "Details: Differentiation"));
//        data.add(new Data("Physics", "Grade: 5", "Topic: Mechanics", "Details: Pressure"));
//        data.add(new Data("English", "Grade: 12", "Topic: Literature", "Details: Shakespeare"));
//        data.add(new Data("Maths", "Grade: 12", "Topic: Calculus", "Details: Integration"));


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
        int teacherId = preferences.getInt("Id", -1);

        api.fetchQuestions(teacherId);
    }

    protected void filterResults(String subject){
        filter_data.clear();

        for (int i = 0; i < data.size(); i++){
            Log.d("Hello", data.get(i).subject.toString());
            if (data.get(i).subject.equalsIgnoreCase(subject)){
                filter_data.add(data.get(i));
            }
        }

        Log.d("Hello", filter_data.toString());
        adapter.notifyDataSetChanged();
    }

    protected void callAPI(){
        API api = new API(this);
//        api.callApi();
    }

    @Override
    public void onSuccessData(ArrayList<Data> data1) {
        Log.d("API", String.valueOf(data1.size()));
        data = data1;
        populateList();
    }

    @Override
    public void onSuccessInt(int id) {
        Log.d("API", String.valueOf(id));
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