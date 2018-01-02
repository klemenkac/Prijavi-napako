package com.example.kac.prijavinapako;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.DataAll;
import com.example.Lokacija;
import com.example.LokacijaTag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.kac.prijavinapako.R.id.spinnerDom;
import static com.example.kac.prijavinapako.R.id.spinnerTipNapake;
import static com.example.kac.prijavinapako.R.id.txtOpis;
import static com.example.kac.prijavinapako.R.id.txtSoba;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class ActivityLocation extends AppCompatActivity {

    private boolean isUserLoggedIn;
    public String encodedImage;

    ApplicationMy app;
    ImageView ivSlika;
    EditText edS;
    EditText edO;
    TextView tvDatum;
    String path;
    TextView stanovalec;
    Button save;
    Lokacija l;
    LokacijaTag lt;
    String ID;
    double latti=0.0;
    double longi=0.0;

    private FusedLocationProviderClient mFusedLocationClient;

    PermissionGranted permissionGranted;
    MagicalCamera magicalCamera;
    boolean stateNew;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 10;

    static final int REQUEST_LOCATION = 1;



    public static String NEW_LOCATION_ID="NEW_LOCATION";

    String [] DOMOVILIST = {"Dom 1", "Dom 2", "Dom 3", "Dom 4", "Dom 5", "Dom 6",
            "Dom 7", "Dom 8", "Dom 9", "Dom 10", "Dom 11", "Dom 12", "Dom 13"
            , "Dom 14", "Dom 15"};

    String [] TIPLIST = {"Elektro","Oprema","Vodovod","Ogrevanje","Internet","Požarne naprave","Drugo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ArrayAdapter<String> arrayAdapterDom = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,DOMOVILIST);
        MaterialBetterSpinner domSpinner = (MaterialBetterSpinner)findViewById(spinnerDom);
        domSpinner.setAdapter(arrayAdapterDom);

        ArrayAdapter<String> arrayAdapterTip = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,TIPLIST);
        MaterialBetterSpinner tipSpinner = (MaterialBetterSpinner)findViewById(spinnerTipNapake);
        tipSpinner.setAdapter(arrayAdapterTip);

        app = (ApplicationMy) getApplication();
        ivSlika =(ImageView) findViewById(R.id.imageViewmain);
        edS = (EditText) findViewById(R.id.txtSoba);
        edO = (EditText) findViewById(R.id.txtOpis);
        stanovalec = (TextView) findViewById(R.id.stanovalec);
        tvDatum = (TextView) findViewById(R.id.datum);
        stateNew = false;
        stanovalec.setVisibility(View.INVISIBLE);

        permissionGranted = new PermissionGranted(this);

        if (android.os.Build.VERSION.SDK_INT >= 20) {
            permissionGranted.checkAllMagicalCameraPermission();
        }else{
            permissionGranted.checkCameraPermission();
            permissionGranted.checkReadExternalPermission();
            permissionGranted.checkWriteExternalPermission();
            permissionGranted.checkLocationPermission();
        }

        ID="";

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latti=location.getLatitude();
                            longi=location.getLongitude();
                        }
                    }
                });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (magicalCamera ==null){
            magicalCamera =  new MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE,permissionGranted);
        }
        //CALL THIS METHOD EVER IN THIS OVERRIDE FOR ACTIVATE PERMISSIONS
        magicalCamera.permissionGrant(requestCode, permissions, grantResults);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        magicalCamera.resultPhoto(requestCode, resultCode, data);

        ivSlika.setImageBitmap(magicalCamera.getPhoto());

        path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"myPhotoName",  true);

        if(path != null){
            Date cDate = new Date();
            String datum = new SimpleDateFormat("dd. MM. yyyy").format(cDate);

            l = new Lokacija("","", "", app.getAll().getUserMe().getIdUser(),datum,"",path,"",0.0,0.0);
            update(l);

        }else{
            Toast.makeText(this, "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show();
        }
    }

    void setLokacija(String ID) {
        l = app.getLocationByID(ID);
        update(l);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if( (extras !=null) && (!ID.equals(NEW_LOCATION_ID)))
        {
            ID = extras.getString(DataAll.LOKACIJA_ID);
            if (ID.equals(NEW_LOCATION_ID)) {
                stateNew = true;
                addNewLocation();
            }else {
                stateNew = false;
                setLokacija(extras.getString(DataAll.LOKACIJA_ID));
            }
        } else {
            System.out.println("Nič ni v extras!");
        }

    }


    public void addNewLocation() {
        if (magicalCamera ==null) magicalCamera =  new MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE,permissionGranted);
        System.out.println("Klik Save magicalCamera1 method");
        magicalCamera.takePhoto();
        System.out.println("Klik Save magicalCamera2 method");

    }

    public void update(Lokacija l) {

        MaterialBetterSpinner domSpinner = (MaterialBetterSpinner)findViewById(spinnerDom);
        MaterialBetterSpinner tipSpinner = (MaterialBetterSpinner)findViewById(spinnerTipNapake);


        tvDatum.setText(l.getDate());
        domSpinner.setText(l.getDom());
        tipSpinner.setText(l.getTipNapake());

        edS.setText(""+l.getSoba());


        if(!stateNew){
            edO.setText(l.getOpis());
            edO.setEnabled(false);
            edS.setEnabled(false);
            domSpinner.setEnabled(false);
            domSpinner.setFocusable(false);
            tipSpinner.setEnabled(false);
            tipSpinner.setFocusable(false);
            Button posljiButn= (Button) findViewById(R.id.buttonSave);
            posljiButn.setVisibility(View.INVISIBLE);
            stanovalec.setVisibility(View.VISIBLE);
            stanovalec.setText(l.getIdUser());
        }
        else{
            edO.setText(l.getOpis());
        }


        if (l.hasImage()) {

            if(l.getFileName().length()>100){
                byte[] decodedString = Base64.decode(l.getFileName(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivSlika.setImageBitmap(decodedByte);
            }else{
                System.out.println("Picasso: "+l.getFileName());
                File f = new File(l.getFileName()); //
                Picasso.with(ActivityLocation.this.getApplicationContext())
                        .load(f) //URL
                        .placeholder(R.drawable.ic_cloud_download_black_124dp)
                        .error(R.drawable.ic_error_black_124dp)
                        // To fit image into imageView
                        .fit()
                        // To prevent fade animation
                        .noFade()
                        .into(ivSlika);
            }


        }
        else {
            ivSlika.setImageDrawable(this.getDrawable(R.drawable.tools));
        }

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b.get("Vpisna")!=null){
            Toast.makeText(this, b.get("Vpisna").toString(), Toast.LENGTH_SHORT).show();

            if(b.get("Vpisna").equals("E1091722"))
            {
                edS.setText("110");
                domSpinner.setText("Dom 13");
            }
            else if(b.get("Vpisna").equals("E123567"))
            {
                edS.setText("220");
                domSpinner.setText("Dom 6");
            }
        }
    }


    public void save() {


        MaterialBetterSpinner domSpinner = (MaterialBetterSpinner)findViewById(spinnerDom);
        l.setDom(domSpinner.getText().toString());



        MaterialBetterSpinner tipSpinner = (MaterialBetterSpinner)findViewById(spinnerTipNapake);
        l.setTipNapake(tipSpinner.getText().toString());

        l.setOpis(edO.getText().toString());

        l.setSoba(edS.getText().toString());

        SharedPreferences sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String ime=sharedpreferences.getString("name",null);
        l.setIdUser(ime);

        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        l.setFileName(encodedImage);
        // l.setDate(tvDatum.getText().toString());


        if(domSpinner.getText().toString()=="Študentski dom"){
            Toast.makeText(this, "Neveljavni študentski dom!", Toast.LENGTH_SHORT).show();
        }
        else if(tipSpinner.getText().toString()=="Tip napake"){
            Toast.makeText(this, "Neveljavni tip napake!", Toast.LENGTH_SHORT).show();
        }
        else{
            app.save();
            Toast.makeText(this,"Napaka shranjena", Toast.LENGTH_SHORT).show();        }
        System.out.println("Po:"+l);
    }

    public void onClickSaveMe(View v) {

        int a = l.getId().length();
        // Toast.makeText(this,a + "", Toast.LENGTH_SHORT).show();
        MaterialBetterSpinner domSpinner = (MaterialBetterSpinner)findViewById(spinnerDom);
        MaterialBetterSpinner tipSpinner = (MaterialBetterSpinner)findViewById(spinnerTipNapake);
        String ajdi = l.getId().toString();
        String opis = edO.getText().toString();
        String dom = domSpinner.getText().toString();
        String soba = edS.getText().toString();
        String tip_napake = tipSpinner.getText().toString();


        Date cDate = new Date();
        String datum = new SimpleDateFormat("dd. MM. yyyy").format(cDate);

        if (stateNew){
            l.setDate(datum);
            app.getAll().addLocation(l);

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (!success) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLocation.this);
                            builder.setMessage("Napaka pri objavi napake na strežnik.")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            // Toast.makeText(this, app.getAll().getUserMe().getIdUser().toString(), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
            String juzer=sharedpreferences.getString("name",null);


            if(path!=null){
                Bitmap bm = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            }


            NapakaRequest napakaRequest = new NapakaRequest(ajdi, dom, soba, tip_napake, opis, juzer,encodedImage,latti,longi, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ActivityLocation.this);
            queue.add(napakaRequest);
        }


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (!success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLocation.this);
                        builder.setMessage("Napaka pri objavi napake na strežnik.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        NapakaUpdateRequest napakaUpdateRequest = new NapakaUpdateRequest(ajdi, dom,soba,tip_napake,opis, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ActivityLocation.this);
        queue.add(napakaUpdateRequest);

        //Toast.makeText(this, l.getId().toString(), Toast.LENGTH_SHORT).show();

        save();
        finish();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}