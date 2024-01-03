package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.util.Listener;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class studyScheduleUploadFragment extends Fragment {

    private TextView subject;
    private TextView description;
    private TextView Venue;
    private TimePicker timePicker;
    private CalendarView calendarView;
    private String StudyMode= null;
    private String CurrentUserEmail;
    private Button SubmitSchedule;
    private TextView chooseDate;
    private TextView chooseTime;
    private Button DateStudySchedule;
    private Button TimeStudySchedule;
    FirebaseFirestore Firestore;
    RadioGroup radioGroup;
    RadioButton radioButtonPhysical;
    RadioButton radioButtonOnline;
    Listener listenerSchedule;


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_study_schedule_upload, container, false);

        Firestore = FirebaseFirestore.getInstance();
        CurrentUserEmail = MainActivity.getEmailString();


        subject = (TextView)view.findViewById(R.id.editTextSubject);
        description= (TextView) view.findViewById(R.id.editTextStudyDescription);
        Venue= (TextView) view.findViewById(R.id.editTextVenue);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        SubmitSchedule = (Button)view.findViewById(R.id.SubmitSchedule);

        //FIRDAUS ADDED CODE
        chooseDate = view.findViewById(R.id.chooseDate);
        chooseTime = view.findViewById(R.id.chooseTime);

        //DateStudySchedule = view.findViewById(R.id.DateStudySchedule);
        //TimeStudySchedule = view.findViewById(R.id.TimeStudySchedule);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = formatDate(year, month, dayOfMonth);
                chooseDate.setText(selectedDate);
            }
        });

        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the calendar view when the button is clicked
                calendarView.setVisibility(View.VISIBLE);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String selectedTime = formatTime(hourOfDay, minute);
                chooseTime.setText("Selected time: " + selectedTime);
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the calendar view when the button is clicked
                calendarView.setVisibility(View.VISIBLE);
            }
        });






        SubmitSchedule.setOnClickListener(v->{
            SendToFireStore();
        });


        //RADIO BUTTON AND GROUP SETUP
        radioGroup = (RadioGroup) view.findViewById(R.id.RadioGroupStudySchedule);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Use the lambda parameters (group and checkedId) instead of the class-level variable selectedRadioButtonId
            if (checkedId == R.id.radioButtonOnline) {
                StudyMode = "Online";
            } else if (checkedId == R.id.radioButtonPhysical) {
                StudyMode = "Physical";
            }
        });

        if(ValidateForm()){
            UploadHashmapToDatabase((Map<String, Object>) listenerSchedule);

        }else{
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    //FIRDAUS ADDED CODE
    private String formatDate(int year, int month, int dayOfMonth) {
        // The month is 0-based, so add 1 to it
        month += 1;
        return String.format("%02d/%02d/%04d", dayOfMonth, month, year);
    }

    private String formatTime(int hourOfDay, int minute) {
        String amPm;
        if (hourOfDay >= 12) {
            amPm = "PM";
            if (hourOfDay > 12) {
                hourOfDay -= 12;
            }
        } else {
            amPm = "AM";
            if (hourOfDay == 0) {
                hourOfDay = 12;
            }
        }

        return String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm);
    }




    public interface LoadIntoHashmapCallback{void OnLoadIntoHashmap(Map<String, Object> hashmap);}
    public void LoadIntoHashmap(LoadIntoHashmapCallback callback) {
        Map<String, Object> hashmap = new HashMap<>();


        //SETTING THE RADIOBUTTON INTO TEXT


        // Assuming subject, description, Venue, and StudyMode are TextViews or similar
        String subjectText = subject.getText().toString().trim();
        String descriptionText = description.getText().toString().trim();
        String venueText = Venue.getText().toString().trim();

        // GET TIME OF SCHEDULE
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        //GET DATE OF SCHEDULE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendarView.getDate()); // Use the selected date from the calendarView

        // Set the selected hour and minute to the calendar
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // Convert calendar to Date and then to a timestamp
        Date date = calendar.getTime();
        Timestamp timestamp = new Timestamp(date);

        //GET DATE OF SCHEDULE
        hashmap.put("Subject", subjectText);
        hashmap.put("Description", descriptionText);
        hashmap.put("Venue", venueText);
        hashmap.put("timestampField", timestamp);
        hashmap.put("Study-mode", StudyMode);
        hashmap.put("Author", CurrentUserEmail);

        callback.OnLoadIntoHashmap(hashmap);

        //TODO:Fix timestamp on schedule page
    }


    public void UploadHashmapToDatabase(Map <String, Object> map){

        Firestore.collection("ScheduleList")
                .document()
                .set(map)
                .addOnSuccessListener(v->{
                    Log.d("Study-Schedule","Upload Successful "+CurrentUserEmail);
                    Toast.makeText(getContext(), "Successfully scheduled", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e->{
                    Log.e("Study-Schedule","Error in uploading Studying schedule", e);
                });
    }

    public void SendToFireStore(){
        LoadIntoHashmap(new LoadIntoHashmapCallback() {
            @Override
            public void OnLoadIntoHashmap(Map<String, Object> hashmap) {
                UploadHashmapToDatabase(hashmap);
            }
        });
    }

    public boolean ValidateForm(){

        if (subject.getText().toString().trim().isEmpty() ||
                description.getText().toString().trim().isEmpty() ||
                Venue.getText().toString().trim().isEmpty() ||
                calendarView.getDate() == 0 ||  // Assuming 0 represents an invalid date
                (radioButtonOnline.isChecked() && radioButtonPhysical.isChecked())) {
            return false;
        }else{

            return true;
        }
    }








}