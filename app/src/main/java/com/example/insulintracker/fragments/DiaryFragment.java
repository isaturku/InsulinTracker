package com.example.insulintracker.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.insulintracker.R;
import com.example.insulintracker.activities.SearchFoodActivity;
import com.example.insulintracker.utilities.DB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends Fragment {
    private FloatingActionButton addFab;
    private FloatingActionButton calendarFab;
    private  TextView calTxt;
    private  TextView carbsTxt;
    private  TextView proteinTxt;
    private  TextView fatTxt;
    private DatePickerDialog datePickerDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryFragment newInstance(String param1, String param2) {
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        addFab = (FloatingActionButton) view.findViewById(R.id.addFab);
        calendarFab = (FloatingActionButton) view.findViewById(R.id.calendarFab);
        calTxt = view.findViewById(R.id.calsNumber);
        carbsTxt = view.findViewById(R.id.carbsNumber);
        fatTxt = view.findViewById(R.id.fatNumber);
        proteinTxt = view.findViewById(R.id.proteinNumber);
        DB.getDailyVals(calTxt,carbsTxt,proteinTxt,fatTxt,null);
        ListView mealsList = view.findViewById(R.id.mealsList);
        DB.populateMeals(getActivity(),mealsList,null);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(year,month,dayOfMonth);
                DB.populateMeals(getActivity(),mealsList, cal.getTime());
                DB.getDailyVals(calTxt,carbsTxt,proteinTxt,fatTxt,cal.getTime());
            }
        };
        datePickerDialog = new DatePickerDialog(getActivity(),dateSetListener,year,month,day);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchFoodActivity.class);
                startActivity(intent);
            }
        });
        calendarFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        return view;
    }
}