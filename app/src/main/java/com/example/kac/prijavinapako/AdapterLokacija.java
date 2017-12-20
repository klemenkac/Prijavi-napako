package com.example.kac.prijavinapako;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.DataAll;
import com.example.Lokacija;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by xklem on 12/03/17.
 */

class AdapterLokacija extends RecyclerView.Adapter<AdapterLokacija.ViewHolder> {
    DataAll all;
    Activity ac;
    Location last;
    public static int UPDATE_DISTANCE_IF_DIFF_IN_M=10;



    public void setLastLocation(Location l) {
        if (last==null) {
            last = l;
            notifyDataSetChanged();
        }
    }

    public AdapterLokacija(DataAll all, Activity ac) {
        super();
        this.all = all;
        this.ac = ac;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtFooter;

        public TextView txtDom;
        public TextView txtSoba;
        public TextView txtOpis;
        public TextView txtTip;
        public TextView txtUser;
        public TextView txtDatum;
        public ImageView iv;

        public ViewHolder(View v) {
            super(v);
            txtDom = (TextView) v.findViewById(R.id.textViewDom);
            txtSoba = (TextView) v.findViewById(R.id.textViewSoba);
            txtOpis = (TextView) v.findViewById(R.id.textViewOpis);
            txtTip = (TextView) v.findViewById(R.id.textViewTip);
            txtUser = (TextView) v.findViewById(R.id.textViewUser);
            // txtFooter = (TextView) v.findViewById(R.id.secondLine);
            txtDatum = (TextView) v.findViewById(R.id.textViewDatum);
            iv = (ImageView)v.findViewById(R.id.icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    private static void startDView(String lokacijaID, Activity ac) {
        //  System.out.println(name+":"+position);
        Intent i = new Intent(ac.getBaseContext(), ActivityLocation.class);
        i.putExtra(DataAll.LOKACIJA_ID,  lokacijaID);
        ac.startActivity(i);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Lokacija trenutni = all.getLocation(position);
        final String soba = trenutni.getSoba();

        //holder.txtHeader.setText(trenutni.getIdUser());


        if (trenutni.hasImage()) {

            byte[] decodedString = Base64.decode(trenutni.getFileName(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.iv.setImageBitmap(decodedByte);

            /*
            File f = new File(trenutni.getFileName()); //
            Picasso.with(ac.getApplicationContext())
                    .load() //URL
                    .placeholder(R.drawable.ic_cloud_download_black_124dp)
                    .error(R.drawable.ic_error_black_124dp)
                    // To fit image into imageView
                    .fit()
                    // To prevent fade animation
                    .noFade()
                    .into(holder.iv);
*/
            //   Picasso.with(ac).load(trenutni.getFileName()).into(holder.iv);
            // holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.ic_airline_seat_recline_extra_black_24dp));
        }
        else {
            holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.tools));
        }


       /* byte[] decodedString = Base64.decode(trenutni.getFileName(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.iv.setImageBitmap(decodedByte);*/

        if (position%2==1) {
            holder.txtOpis.setTextColor(Color.BLUE);
            holder.txtTip.setTextColor(Color.DKGRAY);
            holder.txtDom.setTextColor(Color.DKGRAY);
            holder.txtSoba.setTextColor(Color.DKGRAY);
        }else{
            holder.txtOpis.setTextColor(Color.MAGENTA);
            holder.txtTip.setTextColor(Color.DKGRAY);
            holder.txtDom.setTextColor(Color.DKGRAY);
            holder.txtSoba.setTextColor(Color.DKGRAY);
        }



        holder.txtDom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });
        holder.txtSoba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });
        holder.txtOpis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });
        holder.txtTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });
        holder.txtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });
        holder.txtDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });



//        holder.txtFooter.setText(trenutni.getDom()+", Soba: "+soba);
        holder.txtDom.setText(trenutni.getDom());
        holder.txtSoba.setText(trenutni.getSoba());
        holder.txtOpis.setText(trenutni.getOpis());
        holder.txtTip.setText(trenutni.getTipNapake());
        holder.txtUser.setText(trenutni.getIdUser());
        holder.txtDatum.setText(trenutni.getDate());

        byte[] decodedString = Base64.decode(trenutni.getFileName(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.iv.setImageBitmap(decodedByte);


    }


    @Override
    public int getItemCount() {
        return all.getLocationSize();
    }


}
