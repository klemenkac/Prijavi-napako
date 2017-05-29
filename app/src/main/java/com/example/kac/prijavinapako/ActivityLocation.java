package com.example.kac.prijavinapako;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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

    // private DropboxAPI dropboxAPI;
    private boolean isUserLoggedIn;
    public String encodedImage;

    ApplicationMy app;
    ImageView ivSlika;
    EditText edS;
    EditText edO;
    TextView tvDatum;
    String path;
    Button save;
    Lokacija l;
    LokacijaTag lt;
    String ID;
    PermissionGranted permissionGranted;
    MagicalCamera magicalCamera;
    boolean stateNew;
    public static String NEW_LOCATION_ID="NEW_LOCATION";

    String [] DOMOVILIST = {"Dom 1", "Dom 2", "Dom 3", "Dom 4", "Dom 5", "Dom 6",
            "Dom 7", "Dom 8", "Dom 9", "Dom 10", "Dom 11", "Dom 12", "Dom 13"
            , "Dom 14", "Dom 15"};

    String [] TIPLIST = {"Elektro","Oprema","Vodovod","Ogrevanje","Internet","Požarne naprave","Drugo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);



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
        domSpinner.setHintTextColor(Color.BLUE);
        edO = (EditText) findViewById(R.id.txtOpis);
        tvDatum = (TextView) findViewById(R.id.datum);
        stateNew = false;

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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (magicalCamera ==null){
            magicalCamera =  new MagicalCamera(this,permissionGranted);
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



            l = new Lokacija("","Študentski dom ", "Soba", app.getAll().getUserMe().getIdUser(),datum,"",path,"Tip napake");
            update(l);
            //Toast.makeText(this, "The photo is save in device, please check this path: " + path, Toast.LENGTH_SHORT).show();
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
        if (magicalCamera ==null) magicalCamera =  new MagicalCamera(this,permissionGranted);
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


        edO.setText(l.getOpis());

        if (l.hasImage()) {

           /* byte[] decodedString = Base64.decode(l.getFileName(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivSlika.setImageBitmap(decodedByte);*/

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

            //   Picasso.with(ac).load(trenutni.getFileName()).into(holder.iv);
            // holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.ic_airline_seat_recline_extra_black_24dp));
        }
        else {
            ivSlika.setImageDrawable(this.getDrawable(R.drawable.tools));
        }



    }


    public void save() {


        MaterialBetterSpinner domSpinner = (MaterialBetterSpinner)findViewById(spinnerDom);
        l.setDom(domSpinner.getText().toString());



        MaterialBetterSpinner tipSpinner = (MaterialBetterSpinner)findViewById(spinnerTipNapake);
        l.setTipNapake(tipSpinner.getText().toString());

        l.setOpis(edO.getText().toString());

        l.setSoba(edS.getText().toString());
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
            String juzer=app.getAll().getUserMe().getIdUser().toString();


            if(path!=null){



              /*  Bitmap bm = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
*/
            }

            NapakaRequest napakaRequest = new NapakaRequest(ajdi, dom, soba, tip_napake, opis, juzer,"", responseListener);
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
