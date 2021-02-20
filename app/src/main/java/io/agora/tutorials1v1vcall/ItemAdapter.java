package io.agora.tutorials1v1vcall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements IAPIResponse{
    List<Data> data;
    static Context context;
    public ItemAdapter(List<Data> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.items, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder viewHolder, int i) {

        viewHolder.subjectTextView.setText(data.get(i).subject);
        viewHolder.gradeTextView.setText(data.get(i).grade);
        viewHolder.topicTextView.setText(data.get(i).topic);
        viewHolder.detailsTextView.setText(data.get(i).desc);
        viewHolder.join_button.setTag(data.get(i).questionID);
    }

    @Override
    public int getItemCount() {
        Log.d("TAGG", "getItemCount: " + data.size());
        return data.size();
    }

    @Override
    public void onSuccessData(ArrayList<Data> data1) {

    }

    @Override
    public void onSuccessInt(int id) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;
        TextView gradeTextView;
        TextView topicTextView;
        TextView detailsTextView;
        Button join_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.subjectTextView = itemView.findViewById(R.id.txt_Subject);
            this.gradeTextView = itemView.findViewById(R.id.txt_Grade);
            this.topicTextView = itemView.findViewById(R.id.txt_Topic);
            this.detailsTextView = itemView.findViewById(R.id.txt_Desc);
            this.join_button = itemView.findViewById(R.id.join_button);

            this.join_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int questionId = (int) v.getTag();
                    startMeetingActivity(questionId);
                }
            });
        }
    }

    private static void startMeetingActivity(int questionId){

        IAPIResponse apiResponse = (IAPIResponse) context;
        API api = new API(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int teacherId = preferences.getInt("Id", -1);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("qId", questionId);
        editor.apply();

        api.questionPicked(questionId, teacherId);
        Intent intent = new Intent(context, VideoChatViewActivity.class);
        context.startActivity(intent);
    }

}
