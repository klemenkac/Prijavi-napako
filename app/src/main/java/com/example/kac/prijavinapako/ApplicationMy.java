package com.example.kac.prijavinapako;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.DataAll;
import com.example.Lokacija;
import com.example.TagList;

import android.app.AlertDialog;
import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    ArrayAdapter<String> adapter;
    String address="https://klemenkac.000webhostapp.com/GetNapaka.php";
    InputStream is=null;
    String line=null;
    String result=null;
    String[] data;

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
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getData();
        /*
        if (!load()){
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));


        }*/
    }

    private void getData() {
        //Toast.makeText(getApplicationContext(), "Pridobivam podatke...", Toast.LENGTH_SHORT).show();

        try{
            URL url = new URL(address);
            HttpURLConnection con =(HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is=new BufferedInputStream(con.getInputStream());
        }catch(Exception e){
<<<<<<< HEAD
            Toast.makeText(getApplicationContext(), "Strežnik ne dela, poskusi kasneje!", Toast.LENGTH_SHORT).show();
=======
            Toast.makeText(getApplicationContext(), "Napaka1", Toast.LENGTH_SHORT).show();
>>>>>>> d3a6ded9de8295daac7f66ae6a02c06fd2fd5c49

            e.printStackTrace();
        }

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((line=br.readLine()) != null){
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();
        }catch (Exception e){
<<<<<<< HEAD
            Toast.makeText(getApplicationContext(), "Strežnik ne dela, poskusi kasneje!", Toast.LENGTH_SHORT).show();
=======
            Toast.makeText(getApplicationContext(), "Napaka2", Toast.LENGTH_SHORT).show();
>>>>>>> d3a6ded9de8295daac7f66ae6a02c06fd2fd5c49

            e.printStackTrace();
        }

        //PARSE JSON DATA
        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            String dataId;
            String dataDom;
            String dataSoba;
            String dataOpis;
            String dataUser;
            String dataTip;
            String dataSlika;
            String dataDatum;

            DataAll da = new DataAll();
            Lokacija tmp;
<<<<<<< HEAD
            SharedPreferences sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
            String juzer=sharedpreferences.getString("name",null);
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                if(jo.getString("user").equals(juzer)) {
                    dataId = jo.getString("id");
                    dataDom = jo.getString("dom");
                    dataSoba = jo.getString("soba");
                    dataOpis = jo.getString("opis");
                    dataUser = jo.getString("user");
                    dataTip = jo.getString("tip_napake");
                    dataDatum = jo.getString("datum");
                    dataSlika = jo.getString("slika");

                    tmp = da.addLocation(dataId, dataDom, dataSoba, dataUser, dataDatum, dataOpis, dataSlika, dataTip);
                }
=======

            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                dataId=jo.getString("id");
                dataDom=jo.getString("dom");
                dataSoba=jo.getString("soba");
                dataOpis=jo.getString("opis");
                dataUser=jo.getString("user");
                dataTip=jo.getString("tip_napake");
                dataDatum=jo.getString("datum");
                //dataSlika=jo.getString("slika");

                tmp = da.addLocation(dataId,dataDom, dataSoba,dataUser,dataDatum, dataOpis,null,dataTip);
>>>>>>> d3a6ded9de8295daac7f66ae6a02c06fd2fd5c49
            }
//            Toast.makeText(getApplicationContext(), data[0].toString(), Toast.LENGTH_SHORT).show();
            all = da;
        }catch (Exception e){
<<<<<<< HEAD
            Toast.makeText(getApplicationContext(), "Strežnik ne dela, poskusi kasneje!", Toast.LENGTH_SHORT).show();
=======
            Toast.makeText(getApplicationContext(), "Napaka3", Toast.LENGTH_SHORT).show();
>>>>>>> d3a6ded9de8295daac7f66ae6a02c06fd2fd5c49

            e.printStackTrace();
        }
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

        String ajdi=all.getLokacijaAll().get(adapterPosition).getId().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (!success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationMy.this);
                        builder.setMessage("Napaka pri delete napake na strežnik.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DeleteRequest deleteRequest = new DeleteRequest(ajdi, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ApplicationMy.this);
        queue.add(deleteRequest);
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

                        if (l1.getDate().compareTo(l2.getDate()) > 0) return -1;
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