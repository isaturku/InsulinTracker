package com.example.insulintracker.utilities;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import com.example.insulintracker.dialogs.ExcessiveDialog;
import com.example.insulintracker.models.MealItemModel;
import com.example.insulintracker.adapters.MealLVAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB {
    private static FirebaseFirestore db;

    public static void init(FirebaseApp app){
        db = FirebaseFirestore.getInstance(app);
    }

    public static void insertData(String name, String uid, int cals, int carbs, int protein, int fat,int grams,String size){
        Map<String, Object> data = new HashMap<>();
        data.put("food_name",name);
        data.put("uid",uid);
        data.put("cals",cals);
        data.put("carbs",carbs);
        data.put("protein",protein);
        data.put("fat",fat);
        data.put("timeEaten", FieldValue.serverTimestamp());
        data.put("grams",grams);
        data.put("size",size);
        db.collection("meals").add(data);
    }
    public static void populateMeals(Activity ctx, ListView listView,Date beginDate){
        List<MealItemModel> meals  = new ArrayList<MealItemModel>();
        Query query = db.collection("meals").whereEqualTo("uid",Auth.getUser().getUid());
        if(beginDate == null)
            beginDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        beginDate = cal.getTime();
        int date = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE,date+1);
        Date endDate = cal.getTime();
        Log.v("date",beginDate.toString());
        query = query.whereGreaterThanOrEqualTo("timeEaten",new Timestamp(beginDate));
        query = query.whereLessThanOrEqualTo("timeEaten",new Timestamp(endDate));
        query = query.orderBy("timeEaten", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                meals.clear();
                for (QueryDocumentSnapshot doc : value){
                    String name = doc.get("food_name").toString();
                    int cals = (int) doc.get("cals",Integer.class);
                    MealItemModel meal = new MealItemModel(doc.getId(),name,cals);
                    meals.add(meal);
                }
                MealLVAdapter adapter = new MealLVAdapter(ctx, meals.toArray(new MealItemModel[0]));
                listView.setAdapter(adapter);
            }
        });
    }

    public static void getDailyVals(TextView calTxt, TextView carbTxt, TextView proteinTxt, TextView fatTxt,Date beginDate){
        Query query = db.collection("meals").whereEqualTo("uid",Auth.getUser().getUid());
        if(beginDate == null)
            beginDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        beginDate = cal.getTime();
        int date = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE,date+1);
        Date endDate = cal.getTime();
        query = query.whereGreaterThanOrEqualTo("timeEaten",new Timestamp(beginDate));
        query = query.whereLessThanOrEqualTo("timeEaten",new Timestamp(endDate));
        query = query.orderBy("timeEaten", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int cals = 0;
                int carbs = 0;
                int protein = 0;
                int fat = 0;
                for (QueryDocumentSnapshot doc : value){
                    cals += doc.get("cals",Integer.class);
                    carbs += doc.get("carbs",Integer.class);
                    protein += doc.get("protein",Integer.class);
                    fat += doc.get("fat",Integer.class);
                }
                calTxt.setText(""+cals);
                carbTxt.setText(""+carbs);
                proteinTxt.setText(""+protein);
                fatTxt.setText(""+fat);
            }
        });
    }
    public static void checkCarbs(NavController nav, FragmentManager fragmentManager){
        Query query = db.collection("meals").whereEqualTo("uid",Auth.getUser().getUid());
        Date beginDate = new Date();
        beginDate.setHours(0);
        beginDate.setMinutes(0);
        beginDate.setSeconds(0);
        query = query.whereGreaterThanOrEqualTo("timeEaten",new Timestamp(beginDate));
        query = query.orderBy("timeEaten", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int carbs = 0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        carbs += doc.get("carbs", Integer.class);
                    }
                    if (carbs > 100) {
                        ExcessiveDialog dialog = new ExcessiveDialog(nav);
                        dialog.show(fragmentManager,"");
                    }
                }
            }
        });
    }

    public static void deleteMeal(String id){
        db.collection("meals").document(id).delete();
    }
}
