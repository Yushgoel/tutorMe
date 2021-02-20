package io.agora.tutorials1v1vcall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterUserActivity extends AppCompatActivity {


    EditText name, email;
    Spinner role;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        name = (EditText) findViewById(R.id.editTxtName);
        email = (EditText) findViewById(R.id.editTxtEmail);

        role = (Spinner) findViewById(R.id.roleSpinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        role.setAdapter(adapter);

        submit = (Button) findViewById(R.id.SubmitButton);

        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                addUser(name.getText().toString(), email.getText().toString(), role.getSelectedItem().toString());
                startActivityFromButton(role.getSelectedItem().toString());
            }
        });
    }

    protected void startActivityFromButton(String role){
        Intent intent;
        if (role.equalsIgnoreCase("Student")){
            intent = new Intent(this, MainActivityStudent.class);
        } else{
            intent = new Intent(this, MainActivity.class);

        }
        startActivity(intent);

    }

    protected void addUser(String name, String email, String role){
        API api = new API(this);
        api.addUser(name, email, role);
    }
}