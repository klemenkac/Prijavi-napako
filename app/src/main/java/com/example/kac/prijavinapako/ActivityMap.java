package com.example.kac.prijavinapako;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.DataAll;
import com.example.Lokacija;
import com.example.kac.prijavinapako.Trgovski_potnik.City;
import com.example.kac.prijavinapako.Trgovski_potnik.GA;
import com.example.kac.prijavinapako.Trgovski_potnik.Tour;
import com.example.kac.prijavinapako.Trgovski_potnik.TourManager;
import com.example.kac.prijavinapako.eventbus.MessageEventUpdateLocation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klemen on 27. 12. 2017.
 */

public class ActivityMap extends AppCompatActivity {
    ApplicationMy app;
    MapView mMapView;
    Lokacija l;
    String ID;
    ArrayList<OverlayItem> items;
    private String id_user = "xklemenx@gmail.com";
    private ItemizedOverlayWithFocus<OverlayItem> mMyLocationOverlay;
    Location mLocation;
    City city;


    @Subscribe
    public void onMessageEvent(MessageEventUpdateLocation event) {
        Log.i("ActivityMap","MessageEventUpdateLocation ");
        mLocation = event.getM();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        app = (ApplicationMy) getApplication();

        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mLocation = null;
        items = new ArrayList<OverlayItem>();

        mMyLocationOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        IMapController mapController = mMapView.getController();
                        mapController.setCenter(item.getPoint());
                        mapController.zoomTo(mMapView.getMaxZoomLevel());
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        String lokacijaId = app.getLokacijaIDByIme(item.getTitle());
                        Intent i = new Intent(getBaseContext(), ActivityLocation.class);
                        i.putExtra(DataAll.LOKACIJA_ID, lokacijaId);
                        startActivity(i);
                        return false;
                    }
                }, this);
        mMyLocationOverlay.setFocusItemsOnTap(true);

        mMapView.getOverlays().add(mMyLocationOverlay);


        SharedPreferences sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String potka=sharedpreferences.getString("pot",null);

        //Toast.makeText(this, potka , Toast.LENGTH_LONG).show();
    }


    void setLokacija(String ID) {
        l = app.getLocationByID(ID);
        IMapController mapController = mMapView.getController();
        mapController.setZoom(16);
        GeoPoint startPoint = new GeoPoint(l.getX(), l.getY());

        mMyLocationOverlay.removeAllItems();
        OverlayItem olItem = new OverlayItem(l.getTipNapake(), l.getX() + ";" + l.getY(), startPoint);
        Drawable newMarker = this.getResources().getDrawable(R.drawable.icon48);
        olItem.setMarker(newMarker);
        mMyLocationOverlay.addItem(olItem);
        mapController.setCenter(startPoint);
    }

    public void onResume() {
        super.onResume();

        app = (ApplicationMy) getApplication();
        int stNapak=app.getAll().getLokacijaAll().size();


        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        startService(new Intent(app, GPSTracker.class));//start service
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
            items = new ArrayList<OverlayItem>();

            List<Lokacija> lokacijaList = app.getAll().getLokacijaAll();
            TourManager ta = new TourManager();
            ta.clearCity();
            for(int i=0;i<lokacijaList.size();i++){
                city = new City(lokacijaList.get(i).getX(), lokacijaList.get(i).getY(),lokacijaList.get(i).getDom(),lokacijaList.get(i).getId());
                ta.addCity(city);
            }
        Population pop = new Population(50, true);

        // Evolve population for 100 generations
        pop = GA.evolvePopulation(pop);
        for (int i = 0; i < 100; i++) {
            pop = GA.evolvePopulation(pop);
        }

        Toast.makeText(this, pop.getFittest().toString() , Toast.LENGTH_LONG).show();

            for (int x = 0; x < lokacijaList.size(); x++) {
                OverlayItem olItem = new OverlayItem(lokacijaList.get(x).getTipNapake(), lokacijaList.get(x).getDom(), new GeoPoint(lokacijaList.get(x).getX(), lokacijaList.get(x).getY()));
                Drawable newMarker = this.getResources().getDrawable(R.drawable.marker_default);
                olItem.setMarker(newMarker);
                items.add(olItem);
            }

            GeoPoint startPoint = null;
            mLocation = app.getLastLocation();
            ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
            if (mLocation != null) {
                OverlayItem olItem = new OverlayItem("Tukaj si!", "", new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude()));
                Drawable newMarker = this.getResources().getDrawable(R.drawable.icon48);
                olItem.setMarker(newMarker);
                mItems.add(olItem);
                startPoint = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
            } else {
                startPoint = new GeoPoint(46.559717, 15.636585); //Maribor
            }
            IMapController mapController = mMapView.getController();
            mapController.setZoom(15);
            mapController.setCenter(startPoint);
            mMyLocationOverlay.removeAllItems();
            mMyLocationOverlay.addItems(items);
            mMyLocationOverlay.addItems(mItems);
            mapController.setCenter(startPoint);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        stopService(new Intent(app, GPSTracker.class));
        super.onPause();
    }
}