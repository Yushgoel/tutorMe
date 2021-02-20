package io.agora.tutorials1v1vcall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AskQuestion extends AppCompatActivity {

    Spinner grade, subject;
    EditText topic, desc;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        grade = (Spinner) findViewById(R.id.gradeSpinner);
        subject = (Spinner) findViewById(R.id.subjectSpinner);

        topic = (EditText) findViewById(R.id.questionTopicTxt);
        desc = (EditText) findViewById(R.id.questionDescTxt);

        ArrayAdapter gradesAdapter = ArrayAdapter.createFromResource(this, R.array.Grades, android.R.layout.simple_spinner_item);

        gradesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(gradesAdapter);

        ArrayAdapter subjectsAdapter = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.simple_spinner_item);

        subjectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(subjectsAdapter);

        submit = (Button) findViewById(R.id.submitQuestion);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                submitForm(grade.getSelectedItem().toString(), subject.getSelectedItem().toString(), topic.getText().toString(), desc.getText().toString());
            }

        });
    }

    void submitForm(String grade, String subject, String topic, String desc){
        API api = new API(this);

//        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
//        int userId = prefs.getInt(getString(R.string.app_userID), -1);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("Id", -1);



        int qID = api.addQuestion(userId, subject, grade, topic, desc);
        Log.d("API", String.valueOf(qID));
        Intent intent = new Intent(this, MainActivityStudent.class);
        startActivity(intent);
    }
}