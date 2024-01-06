package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class exploreFragment extends Fragment {

    private FirebaseFirestore Firestore;
    private ListView StudySchedulelistView;
    Button EditScheduleButton;
    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_explore, container, false);
        Firestore=FirebaseFirestore.getInstance();

        //BUTTON OBJECTS

        EditScheduleButton = view.findViewById(R.id.editScheduleButton9);

        //OPEN EDIT SCHEDULE BUTTON
        EditScheduleButton.setOnClickListener(v->{
            HomePage activity = (HomePage) getActivity();
            if(activity!=null){
                activity.replaceFragment(new studyScheduleUploadFragment());
            }
        });








        StudySchedulelistView = (ListView) view.findViewById(R.id.ScheduleListView1);

        return view;
    }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);
//            StudySchedulelistView = (ListView) view.findViewById(R.id.ScheduleListView1);
            handleQueryResults(new HandleQueryResultsCallback() {
                @Override
                public void onHandleQueryResults(List<Map<String, Object>> dataList) {
                    String[] from = {"Subject", "Description", "Venue", "timestampField", "Author", "Study-Mode"};
                    int[] to = {R.id.schedule_title, R.id.schedule_decription, R.id.schedule_venue, R.id.schedule_date, R.id.schedule_author, R.id.study_mode};
                    SimpleAdapter adapter = new SimpleAdapter(
                            getContext(),
                            dataList,
                            R.layout.schedule_list_item,
                            from,
                            to
                    );


                    ;
                    Log.d("Study-Schedule", "Successfully ran handleQueryResults()");
                    Log.d("Study-Schedule", "Hashmap:" + dataList);

                    StudySchedulelistView.setAdapter(adapter);
                }
            });

        }





    interface HandleQueryResultsCallback { void onHandleQueryResults(List<Map<String, Object>> dataList);}
    private void handleQueryResults(HandleQueryResultsCallback handleQueryResultsCallback) {
        Query query = Firestore.collection("ScheduleList").orderBy("timestampField", Query.Direction.DESCENDING).limit(5);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Map<String, Object>> dataList = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    try {
                        dataList.add(ChangeDocumentDate(data));


                    } catch (Exception e) {
                        Log.e("Study-Schedule", "Can't add hashmap to list", e);
                    }
                }
                // Call the callback with the result
                handleQueryResultsCallback.onHandleQueryResults(dataList);
                Log.d("Study-Schedule", "Successfully sent hashamp to handleQueryResults()");
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("Study-Schedule", "Error in handleQueryResults", exception);
                }
            }
        });
    }
    //RETURN EMAIL FROM DATABASE


    public interface OnEmailFoundListener {
        void onEmailFound(String email);
    }








    public Map<String, Object> ChangeDocumentDate(Map <String,Object> map){

        for(Map.Entry<String,Object> iterate: map.entrySet()){
            String keyName= iterate.getKey();

            if(keyName.equals("timestampField")){
                Object keyValues = iterate.getValue();

                if (keyValues instanceof Timestamp) {
                    Timestamp timestamp = (Timestamp) keyValues;
                    Date date = timestamp.toDate();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    String formattedDate = dateFormat.format(date);
                    map.replace("timestampField",formattedDate);
                    return map;
                }
            }
        }
        return map;
    }
}
