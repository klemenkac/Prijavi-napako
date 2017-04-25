package com.example.kac.prijavinapako;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DataAll;
import com.example.Lokacija;
import com.example.LokacijaTag;

import java.io.File;
import java.util.Date;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import static com.example.kac.prijavinapako.R.id.spinnerDom;
import static com.example.kac.prijavinapako.R.id.spinnerTipNapake;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class ActivityLocation extends AppCompatActivity {


    ApplicationMy app;
    ImageView ivSlika;
    EditText edS;
    EditText edO;
    TextView tvDatum;

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

    //ELEKTRO: Žarnica, Vtičnica, Televizija, Drugo
    //OPREMA: Kuhinja, Kopalnica, Garderoba, WC, Soba, Drugo
    //VODOVOD: Kuhinja, Kopalnica, Kopalnica, Drugo
    //OGREVANJE: Kuhinja, Kopalnica, Kopalnica, Drugo
    //INTERNET: WIFI, Kabel, Oboje, Drugo
    //POŽARNE NAPRAVE: Detektor alarma, Gasilni aparat, Drugo
    //DRUGO: opiši, Drugo

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
        //edT = (EditText) findViewById(R.id.txtTip);
        edO = (EditText) findViewById(R.id.txtOpis);
        tvDatum = (TextView) findViewById(R.id.datum);
        stateNew = false;

        permissionGranted = new PermissionGranted(this);

        if (android.os.Build.VERSION.SDK_INT >= 24) {
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
        if (magicalCamera ==null)    magicalCamera =  new MagicalCamera(this,permissionGranted);
        //CALL THIS METHOD EVER IN THIS OVERRIDE FOR ACTIVATE PERMISSIONS
        magicalCamera.permissionGrant(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        magicalCamera.resultPhoto(requestCode, resultCode, data);

        //this is for rotate picture in this method
        //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);

        //with this form you obtain the bitmap (in this example set this bitmap in image view)
        ivSlika.setImageBitmap(magicalCamera.getPhoto());

        //if you need save your bitmap in device use this method and return the path if you need this
        //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.
        String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"myPhotoName",  true);


        if(path != null){
            l = new Lokacija("Študentski dom ", "Soba", app.getAll().getUserMe().getIdUser(),System.currentTimeMillis(),"",path,"Tip napake");
            update(l);
            Toast.makeText(this, "The photo is save in device, please check this path: " + path, Toast.LENGTH_SHORT).show();
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
        //permissionGranted.checkAllMagicalCameraPermission();
        // l = app.getTestLocation();
        // update(l);
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

        domSpinner.setText(l.getDom());
        tipSpinner.setText(l.getTipNapake());

        edS.setText(""+l.getSoba());
        //tvDatum.setText(""+l.getDate());
        tvDatum.setText(DataAll.dt.format(new Date(l.getDate())));

        edO.setText(l.getOpis());

        if (l.hasImage()) {
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
        System.out.println("Po:"+l);
        app.save();
        Toast.makeText(this,"Napaka shranjena", Toast.LENGTH_SHORT).show();
    }

    public void onClickSaveMe(View v) {
        if (stateNew) app.getAll().addLocation(l);
        save();
        finish();
    }
}
