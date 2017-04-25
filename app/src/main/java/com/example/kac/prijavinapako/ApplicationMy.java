package com.example.kac.prijavinapako;
import com.example.DataAll;
import com.example.Lokacija;
import com.example.TagList;

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.kac.prijavinapako.eventbus.MessageEventUpdateLocation;
/**
 * Created by xklem on 13. 03. 2017.
 */

public class ApplicationMy extends Application {
    int x;
    DataAll all;
    private static final String DATA_MAP = "napakadatamap";
    private static final String FILE_NAME = "napaka.json";

    private static final int SORT_BY_DATE=1;
    private static final int SORT_BY_DOM=0;
    int sortType = SORT_BY_DATE;

    private TagList tags;
    public static SharedPreferences preferences;
    private Location mLastLocation;
    @Override
    public void onCreate() {
        super.onCreate();
        tags = new TagList(); //also sets default tags
        EventBus.getDefault().register(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mLastLocation=null;
        if (!load())
            all = DataAll.scenarijA();
    }

    @Subscribe
    public void onMessageEvent(MessageEventUpdateLocation event) {
        Log.i("ApplicationMy","MessageEventUpdateLocation ");
        mLastLocation = event.getM();
    }
    @Override
    public void onTerminate() {
        EventBus.getDefault().unregister(this);
        super.onTerminate();

    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }
    public boolean hasLocation() {
        if (mLastLocation==null) return false;
        return true;
    }

    public DataAll getAll() {
        return  all;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Lokacija getTestLocation() {
        return all.getLocation(0);
    }

    public Lokacija getLocationByID(String id) {
        return all.getLocationByID(id);
    }


    public List<Lokacija> getLokacijaAll() {
        return all.getLokacijaAll();
    }

    public boolean save() {
        File file = new File(this.getExternalFilesDir(DATA_MAP), ""
                + FILE_NAME);

        return ApplicationJson.save(all,file);
    }

    public boolean load(){
        File file = new File(this.getExternalFilesDir(DATA_MAP), ""
                + FILE_NAME);
        DataAll tmp = ApplicationJson.load(file);
        if (tmp!=null) all = tmp;
        else return false;
        return true;
    }

    public void removeLocationByPosition(int adapterPosition) {
        all.getLokacijaAll().remove(adapterPosition);
    }

    public void sortUpdate() {
        //sortType= (sortType+1) / 2;
        switch (sortType) {
            case SORT_BY_DATE:{
                Toast.makeText(this,"Sortirano po datumu", Toast.LENGTH_SHORT).show();
                Collections.sort(all.getLokacijaAll(), new Comparator<Lokacija>() {
                    @Override
                    public int compare(Lokacija l1, Lokacija l2) {
                        if (l1.getDate()==l2.getDate()) return 0;
                        if (l1.getDate()>l2.getDate()) return -1;
                        return 1;
                    }
                });
            }
            break;
            case SORT_BY_DOM:{
                Toast.makeText(this,"Sortirano po domu", Toast.LENGTH_SHORT).show();

                Collections.sort(all.getLokacijaAll(), new Comparator<Lokacija>() {
                    @Override
                    public int compare(Lokacija l1, Lokacija l2) {
                        if (l1.getDom()==l2.getDom()) return 0;
                      //  a.compareTo(another_string) <0

                        if (l1.getDom().compareTo(l2.getDom()) > 0) return -1;
                        return 1;
                    }
                });

            }
            break;
        }

    }

    public void sortChangeAndUpdate() {
        sortType= (sortType+1) % 2;
        sortUpdate();
    }
}