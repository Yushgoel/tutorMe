package io.agora.tutorials1v1vcall;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class API {
    private Context context;
    RequestQueue queue;
    String baseUrl = "https://tisbhacks.uc.r.appspot.com/";

    Integer userID = 0;
    Integer questionID = 0;

    IAPIResponse callback;

    String role;

    API(Context context, IAPIResponse callback){
        this.context = context;
        this.callback = callback;
    }

    API(Context context){
        this.context = context;
    }

    void fetchQuestions(int teacherID){
        String url = baseUrl + "GetQuestionsForTeacher?teacherId=" + teacherID;
        queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Log.d("API", response);
                callback.onSuccessData(parseDfToData(response));
            }
        },
        new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("API", error.toString());
            }
        }

        );

        queue.add(request);
//        return new ArrayList<Data>();
//      return this.data;
    }

    void fetchStudentQuestions(int userId){
        String url = baseUrl + "GetQuestionsForStudent?userid=" + userId;
        Log.d("API", "STudent Questoin URL   " + url);
        queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Log.d("API", response);
                callback.onSuccessData(parseDfToData(response));
            }
        },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("API", error.toString());
                    }
                }

        );

        queue.add(request);
//        return new ArrayList<Data>();
//        return this.data;
    }

    int addUser(String name, String email, String role) {
//        Add code later
        String url = baseUrl + "AddUser?email=" + email + "&name=" + name + "&role=" + role;

        queue = Volley.newRequestQueue(context);
        this.role = role;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Log.d("API", response);
                setUserID(Integer.parseInt(response));
//                return UserID here.
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("API", error.toString());
            }
        }

        );
        queue.add(request);
        return this.userID.intValue();
//        return 0;
    }

    void saveInSharedPreference(int id){
        Activity contextActivity = (Activity) context;
//        SharedPreferences prefs = contextActivity.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(contextActivity.getString(R.string.app_userID), id);
//        editor.apply();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Id", id);
        editor.putString("role", this.role);
        editor.apply();
    }

    int addQuestion(int userId, String subject, String grade, String topic, String desc){
        String url = baseUrl + "AddQuestion?userID=" + userId + "&subject=" + subject + "&grade=" + grade + "&topic=" + topic + "&desc=" + desc;
        Log.d("API", url);
        queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Log.d("API", response);
                setQuestionID(Integer.parseInt(response));
//                return UserID here.
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("API", error.toString());
            }
        }

        );
        queue.add(request);
        return this.questionID.intValue();
//        return 0;

    }

    void setUserID(Integer userID){
        this.userID = userID;
        saveInSharedPreference(this.userID.intValue());

    }

    void setQuestionID(Integer questionID){
        this.questionID = questionID;

    }

    void questionPicked(int questionID, int teacherID){
        String url = baseUrl + "QuestionPicked?questionID=" + questionID + "&teacherID=" + teacherID;
        Log.d("API", url);
        queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Log.d("API", response);
//                return UserID here.
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("API", error.toString());
            }
        }

        );
        queue.add(request);
    }

    void questionFinished(int questionID){
        String url = baseUrl + "QuestionFinished?questionID=" + questionID;
        Log.d("API", url);
        queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Log.d("API", response);
//                return UserID here.
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("API", error.toString());
            }
        }

        );
        queue.add(request);
    }

    ArrayList<Data> parseDfToData(String df){
        ArrayList<Data> parsedData = new ArrayList<Data>();

        try {
            JSONObject obj = new JSONObject(df);
            Log.d("API", "Length " + String.valueOf(obj.length()));
//            Log.d("API", "Kys available are " + obj.keys().get(0));

            Iterator<String> keys = obj.keys();


            while(keys.hasNext()){
                String key = keys.next();
                JSONObject row = obj.getJSONObject(key);

                int userID = row.getInt("userID");
                int questionID = row.getInt("questionID");
                int status = row.getInt("status");
                int teacherId = row.getInt("teacherID");
                String subject = row.getString("subject");
                String grade = row.getString("grade");
                String topic = row.getString("topic");
                String desc = row.getString("desc");
                parsedData.add(new Data(userID, questionID, subject, grade, topic, desc, status, teacherId));
            }

//            for(int i = 0; i < obj.length(); i++){
//                if (obj.has(String.valueOf(i))){

//                }

//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedData;
    }
}
