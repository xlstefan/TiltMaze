package com.azaric.tiltmaze;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.azaric.tiltmaze.Dialog.MainDialogItemLongClick;
import com.azaric.tiltmaze.Dialog.MainDialogItemTmpLoad;

import java.io.File;

public class MainActivity extends Activity
        implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    public static final String NAME_OF_POLYGON = "com.azaric.tiltmaze.MainActivity.NAME_OF_DRAWING";

    //GUI
    ListView listView;
    TextView textView;
    ArrayAdapter<String> adapter;

    String[] namesOfTracks;
    String nameOfTrack = null;
    private String tmpName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Tilt maze MAIN");

        textView = (TextView) findViewById(R.id.textViewMainStatus);
        listView = (ListView) findViewById(R.id.mainActivityListView);
        addTracksToList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_terrain:
                startActivity(new Intent(this, NewTerrainActivity.class));
                return true;
            case R.id.show_stats:
                startActivity(new Intent(this, StatisticsActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Ovo se poziva kada se vrati na ovu aktivnost
     */
    @Override
    protected void onResume() {
        updateListView();
        super.onResume();
    }

    //CODE FOR LIST VIEW
    public void updateListView(){
        //citaj sve fajlove iz files direktorijuma
        namesOfTracks = getApplicationContext().getExternalFilesDir(null).list(new java.io.FilenameFilter() {
            /**
             * Ovo sam napravio iz razloga sto bez ovoga mi izlistava neke sistemske fajlove iz
             * default foldera.
             * @param dir
             * @param filename
             * @return
             */
            @Override
            public boolean accept(File dir, String filename) {
                return !(filename.contains("TEMP"));
            }
        });
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namesOfTracks);
        listView.setAdapter(adapter);
        if(namesOfTracks.length == 0){
            textView.setText(R.string.main_there_are_no_available_polygons);
        } else textView.setText("");
    }

    private void addTracksToList() {
        updateListView();
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }


    public void startGameActivity(String drawingNameToOpen){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(NAME_OF_POLYGON, drawingNameToOpen);
        startActivity(intent);
    }

    /**
     * Ova metoda je potrebna da prepozna kad se klikne na item u listView-u
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String nameOfTrack = (String) parent.getItemAtPosition(position);
        File[] files = getApplicationContext().getExternalFilesDir(null).listFiles();
        for(int i = 0; i < files.length; i++){
            if(files[i].getName().equals("TEMP:" + nameOfTrack + ":tmp")) {
                Log.d("CLICK LOAD TMP", "TEMP:" + nameOfTrack + ":tmp");
                tmpName = nameOfTrack;
                DialogFragment mainDialogItemTmpLoad = new MainDialogItemTmpLoad();
                mainDialogItemTmpLoad.show(getFragmentManager(), "mainDialogItemTmpLoad");
                //startGameActivity("TEMP:" + nameOfTrack + ":tmp");
                return;
            }
        }
        startGameActivity(nameOfTrack);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        nameOfTrack = (String) adapterView.getItemAtPosition(i);
        DialogFragment mainDialogItemLongClick = new MainDialogItemLongClick();
        mainDialogItemLongClick.show(getFragmentManager(), "mainDialogItemLongClick");
        return true;
    }

    public String getPolygonName() {
        return nameOfTrack;
    }

    public void deletePolygon(String polygonName) {
        File[] files = getApplicationContext().getExternalFilesDir(null).listFiles();
        for(int i = 0; i < files.length; i++){
            if(files[i].getName().equals(polygonName))
                files[i].delete();
        }
    }

    public String getTmpName() {
        return tmpName;
    }
}
