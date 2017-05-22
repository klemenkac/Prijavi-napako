package com.example.kac.prijavinapako;

/**
 * Created by xklem on 20. 03. 2017.
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.Toast;

import com.example.DataAll;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.example.kac.prijavinapako.eventbus.MessageEventUpdateLocation;
import java.util.List;

public class ActivityListMain extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ApplicationMy app;
    private FloatingActionButton fab;
    private Location mLocation;
    private GoogleApiClient googleApiClient;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(this,ActivityMySettings.class));
                return true;

            case R.id.action_sort:
                //app.sortUpdate();
                app.sortChangeAndUpdate();
                mAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventUpdateLocation event) {
        Log.i("ActivityZacetna","MessageEventUpdateLocation ");
        mLocation = event.getM();
        //mAdapter.setLastLocation(mLocation);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (ApplicationMy) getApplication();
        setContentView(R.layout.activity_list_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.myrecycleview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        app = (ApplicationMy) getApplication();
        mAdapter = new AdapterLokacija(app.getAll(), this);
        mRecyclerView.setAdapter(mAdapter);
        // setSpinner();

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        //TODO Show V7
        setDeleteOnSwipe(mRecyclerView);


        mLocation=null;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(getResources().getColor(R.color.colorGray));
        //fab.set
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
              /*  if (mLocation==null) {
                    //                   Snackbar.make(view, getResources().getText(R.string.add_new_location_no_location), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Snackbar.make(view, getResources().getText(R.string.add_new_location_no_location), Snackbar.LENGTH_LONG).show();
                }else {*/
                Intent i = new Intent(getBaseContext(), ActivityLocation.class);
                i.putExtra(DataAll.LOKACIJA_ID, ActivityLocation.NEW_LOCATION_ID);
                startActivity(i);
                // }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void setDeleteOnSwipe(final RecyclerView mRecyclerView) {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                app.removeLocationByPosition(viewHolder.getAdapterPosition());
                                app.save();
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;
                        }
                        // mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListMain.this);
                builder.setTitle("Izbriši napako");
                builder.setMessage("Ali si prepričan?").setPositiveButton("Da", dialogClickListener)
                        .setNegativeButton("Ne", dialogClickListener)
                ;


                /*builder.setMessage("Are you sure?");

                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No", dialogClickListener);
*/
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                // nbutton.setBackgroundColor(Color.BLUE);
                nbutton.setTextColor(Color.BLUE);

                Button nbutton2 = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                //nbutton2.setBackgroundColor(Color.MAGENTA);
                nbutton2.setTextColor(Color.MAGENTA);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void getPermissions() {
        MultiplePermissionsListener my  = new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    new android.app.AlertDialog.Builder(ActivityListMain.this)
                            .setTitle(getString(R.string.permission_must_title))
                            .setMessage(getString(R.string.permission_must))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ActivityListMain.this.finish(); //end app!
                                }
                            })
                            .setIcon(R.drawable.trash_icon)
                            .show();
                }}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA
                ).withListener(my).check();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPermissions();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();
            app.getAll().getUserMe().setIdUser(account.getDisplayName().toString());
           /* nameTextView.setText(account.getDisplayName());
            emailTextView.setText(account.getEmail());
            idTextView.setText(account.getId());*/

           // Glide.with(this).load(account.getPhotoUrl()).into(photoImageView);

        } else {
           // goLogInScreen();
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Napača", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void revoke(View view) {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Napaka", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        // stopService(new Intent(ActivityZacetna.this, GPSTracker.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        
    }
}

