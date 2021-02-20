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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements IAPIResponse {

    private RecyclerView recycler;
    private Button filter;

    List<Data> data = new ArrayList<Data>();
    List<Data> filter_data = new ArrayList<Data>();
    ItemAdapter adapter;
    Spinner grade, subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recycler = (RecyclerView) findViewById(R.id.recycle);
        filter = (Button) findViewById(R.id.filter_button);

        filter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("Tag", "grade=" + grade.getSelectedItem().toString());
                Log.d("Tag", "subject=" + subject.getSelectedItem().toString());
                filterResults(subject.getSelectedItem().toString(), grade.getSelectedItem().toString());
                Log.d("Hello", "TEST BUTTON PRESS");
            }
        });


        grade = (Spinner) findViewById(R.id.gradeSpinner);
        subject = (Spinner) findViewById(R.id.subjectSpinner);

        ArrayAdapter gradesAdapter = ArrayAdapter.createFromResource(this, R.array.GradesFilter, android.R.layout.simple_spinner_item);

        gradesAdapter.setDropDownViewResource(R.layout.spinner_item);
        grade.setAdapter(gradesAdapter);

        ArrayAdapter subjectsAdapter = ArrayAdapter.createFromResource(this, R.array.SubjectsFilter, android.R.layout.simple_spinner_item);

        subjectsAdapter.setDropDownViewResource(R.layout.spinner_item);
        subject.setAdapter(subjectsAdapter);

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

    protected void filterResults(String subject, String grade){
        filter_data.clear();

        for (int i = 0; i < data.size(); i++){
            Log.d("Hello", data.get(i).subject.toString());
            if (data.get(i).subject.equalsIgnoreCase(subject) &&
                    data.get(i).grade.equalsIgnoreCase(grade) ){
      //      if (data.get(i).subject.equalsIgnoreCase(subject)){
                filter_data.add(data.get(i));
            }
        }

        Log.d("Hello", filter_data.toString());
        adapter.notifyDataSetChanged();
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