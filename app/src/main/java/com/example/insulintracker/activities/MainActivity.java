package com.example.insulintracker.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.insulintracker.dialogs.LogoutDialog;
import com.example.insulintracker.utilities.DB;
import com.example.insulintracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private BottomNavigationView bnv;
    private NavController navController;
    private NavHostFragment nhf;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        bnv = (BottomNavigationView)findViewById(R.id.bnv);
        bnv.setBackground(null);
        bnv.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LogoutDialog dialog = new LogoutDialog();
                dialog.show(getSupportFragmentManager(),"logout");
                return false;
            }
        });
        nhf = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        navController = nhf.getNavController();
        NavigationUI.setupWithNavController(bnv,navController);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACTIVITY_RECOGNITION},0);
        }
        DB.checkCarbs(navController,getSupportFragmentManager());
    }
}