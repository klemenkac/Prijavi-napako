package com.example.kac.prijavinapako;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.DataAll;
import com.example.Lokacija;
import com.example.TagList;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewGroup;
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

import com.example.kac.prijavinapako.Trgovski_potnik.City;
import com.example.kac.prijavinapako.Trgovski_potnik.GA;
import com.example.kac.prijavinapako.Trgovski_potnik.TourManager;
import com.example.kac.prijavinapako.eventbus.MessageEventUpdateLocation;
/**
 * Created by xklem on 13. 03. 2017.
 */

public class ApplicationMy extends Application{
    int x;
    DataAll all;
    private static final String DATA_MAP = "napakadatamap";
    private static final String FILE_NAME = "napaka.json";

    private static final int SORT_BY_DATE=1;
    private static final int SORT_BY_DOM=0;
    int sortType = SORT_BY_DATE;

    String address="https://klemenkac.000webhostapp.com/GetNapaka.php";
    InputStream is=null;
    String line=null;
    String result=null;
    private Location mLastLocation;
    Integer vse;
    private TagList tags;
    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        tags = new TagList(); //also sets default tags
        EventBus.getDefault().register(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        mLastLocation=null;
    }

    public void getData() {

        class DownloadFilesTask extends AsyncTask<Void, Void, DataAll> {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                /**
                 * show dialog
                 */
                super.onPreExecute();
                System.out.println("Tukaj1");
            }

            @Override
            protected DataAll doInBackground(Void... params) {
                System.out.println("Tukaj2");
                try{
                    URL url = new URL(address);
                    HttpURLConnection con =(HttpURLConnection) url.openConnection();

                    con.setRequestMethod("GET");

                    is=new BufferedInputStream(con.getInputStream());
                }catch(Exception e){
                    System.out.println("Napaka1");
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
                    System.out.println("Napaka2");

                    e.printStackTrace();
                }

                //PARSE JSON DATA
                try{
                    JSONArray ja = new JSONArray(result);
                    JSONObject jo = null;

                    DataAll da = new DataAll();
                    SharedPreferences sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                    String ime=sharedpreferences.getString("name",null);

                    if(ime.equals("v") || ime.equals("vzdrzevalec"))
                        vse=1;
                    else
                        vse=0;

                    Integer st=0;
                    for(int i=0;i<ja.length();i++){
                        jo = ja.getJSONObject(i);
                        if(vse==0){

                            if(jo.getString("user").equals(ime)) {

                                String dataId = jo.getString("id");
                                String dataDom = jo.getString("dom");
                                String dataSoba = jo.getString("soba");
                                String dataOpis = jo.getString("opis");
                                String dataUser = jo.getString("user");
                                String dataTip = jo.getString("tip_napake");
                                String dataDatum = jo.getString("datum");
                                String dataSlika = jo.getString("slika");
                                Double smerX = jo.getDouble("x");
                                Double smerY = jo.getDouble("y");
                                String konc = jo.getString("koncano");

                                da.addLocation(dataId, dataDom, dataSoba, dataUser, dataDatum, dataOpis, dataSlika, dataTip, smerX, smerY, konc);
                                st++;
                            }
                        }
                        else{
                            if(jo.getString("koncano").equals("0")){
                                String dataId = jo.getString("id");
                                String dataDom = jo.getString("dom");
                                String dataSoba = jo.getString("soba");
                                String dataOpis = jo.getString("opis");
                                String dataUser = jo.getString("user");
                                String dataTip = jo.getString("tip_napake");
                                String dataDatum = jo.getString("datum");
                                String dataSlika = jo.getString("slika");
                                Double smerX = jo.getDouble("x");
                                Double smerY = jo.getDouble("y");
                                String konc = jo.getString("koncano");

                                da.addLocation(dataId, dataDom, dataSoba, dataUser, dataDatum, dataOpis, dataSlika, dataTip, smerX, smerY, konc);
                                st++;
                            }
                        }
                    }
                    System.out.println("Dodanih "+st);

                    all = da;
                    return da;
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Napaka8");
                }
                return null;
            }

            @Override
            protected void onPostExecute(DataAll result) {
                // TODO Auto-generated method stub
                /**
                 * update ui thread and remove dialog
                 */
                if(vse==0){
                    Intent i = new Intent(getBaseContext(), ActivityListMain.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(getBaseContext(), VActivityListMain.class);
                    startActivity(i);
                }

                super.onPostExecute(result);
            }
        }

        DownloadFilesTask gi = new DownloadFilesTask();
        gi.execute();
    }

    @Subscribe
    public void onMessageEvent(MessageEventUpdateLocation event) {
        Log.i("ApplicationMy","MessageEventUpdateLocation ");
        mLastLocation = event.getM();
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }
    public boolean hasLocation() {
        if (mLastLocation==null) return false;
        return true;
    }

    @Override
    public void onTerminate() {
        EventBus.getDefault().unregister(this);
        super.onTerminate();

    }



    public DataAll getAll() {
        return  all;
    }

    public String getLokacijaIDByIme(String title) {
        List<Lokacija> list= all.getLokacijaAll();
        String id="";
        for(int x=0;x<list.size();x++){
            if(list.get(x).getTipNapake().equals(title)){
                id= list.get(x).getId();
                break;
            }
        }
        return id;
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

    public void setLocationKoncano(int adapterPosition) {

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

        NapakaKoncanoRequest napakaKoncanoRequest = new NapakaKoncanoRequest(ajdi, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ApplicationMy.this);
        queue.add(napakaKoncanoRequest);

        all.getLokacijaAll().remove(adapterPosition);
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
                Toast.makeText(this,"Sortirano po datumu naraščajoče", Toast.LENGTH_SHORT).show();
                Collections.sort(all.getLokacijaAll(), new Comparator<Lokacija>() {
                    @Override
                    public int compare(Lokacija l1, Lokacija l2) {
                        if (l1.getDate()==l2.getDate()) return 0;

                        if (l1.getDate().compareTo(l2.getDate()) < 0) return -1;
                        return 1;
                    }
                });
            }
            break;
            case SORT_BY_DOM:{
                Toast.makeText(this,"Sortirano po datumu padajoče", Toast.LENGTH_SHORT).show();
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
        }

    }

    public void sortChangeAndUpdate() {
        sortType= (sortType+1) % 2;
        sortUpdate();
    }
}