package com.example.antitextbook;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean lessonOrNo(){
        boolean couples = false;
        String folderName = "temp/ATB/settings", fileName = "checkedBox.txt";
        String path = "/" + folderName + "/" + fileName;
        String couplesAfterFile = readFile(path);

        Toast.makeText(this, couplesAfterFile, Toast.LENGTH_SHORT).show();
        if(couplesAfterFile == "1"){
            couples = true;
        }
        else if(couplesAfterFile == "2"){
            couples = false;
        }
        else{
            Toast.makeText(this, "Error: " + "ошибка в значении в файле с обозначением пар или уроков. Выставлены уроки.", Toast.LENGTH_SHORT).show();
            couples = false;
        }
        return couples;
    }

    public String readFile (String path){
        File sdcard = Environment.getExternalStorageDirectory();
        //получает текстовый файл
        File file = new File(sdcard,"/" + path);
        //читаем текстовый файл
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String resultText = "";

            while ((line = br.readLine()) != null) {
                resultText += line;
                return resultText;
            }
            br.close();
        }
        catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // создаем новый фрагмент
        Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();

        if (id == R.id.library) {
        fragmentClass = Library.class;
        }
        else if (id == R.id.settings) {
        fragmentClass = Settings.class;
        }
        else if (id == R.id.server) {
        fragmentClass = Server.class;
        }
        else if (id == R.id.cloud) {
            fragmentClass = Cloud.class;
        }
        /*
        else if (id == R.id.nav_share) {

        }
        */
        else if (id == R.id.nav_send) {
        fragmentClass = Send.class;
        }
        else if (id == R.id.home) {
            fragmentClass = Home.class;
        }
        else if (id == R.id.schedule && lessonOrNo()) {
            fragmentClass = Schedule2.class;
        }
        else if(id == R.id.schedule && !lessonOrNo()){
            fragmentClass = Schedule.class;
        }

        else {
            fragmentClass = Home.class;
        }

        // ловим ошибки
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вставляем фрагмент, заменяя текущий фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
