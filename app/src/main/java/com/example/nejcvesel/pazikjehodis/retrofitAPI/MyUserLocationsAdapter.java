package com.example.nejcvesel.pazikjehodis.retrofitAPI;

/**
 * Created by nejcvesel on 07/03/17.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.LocationDetailFragment;
import com.example.nejcvesel.pazikjehodis.LocationFormFragment;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nejcvesel on 19/12/16.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.LocationDetailFragment;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyUserLocationsAdapter extends RecyclerView.Adapter<MyUserLocationsAdapter.ViewHolder> {
    List<Location> mItems;
    Context context;


    public MyUserLocationsAdapter(Context context) {
        super();
        this.context = context;
        mItems = new ArrayList<Location>();
    }

    public void addData(Location loc) {
        mItems.add(loc);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_user_loc, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        System.out.println("OnCreateViewHolder " + i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Location loc = mItems.get(i);
        System.out.println(loc.getId());
        viewHolder.text.setText(loc.getText());
        viewHolder.longtitude.setText("latitude: " + loc.getLatitude());
        viewHolder.latitude.setText("longtitude: " + loc.getLongtitude());
        viewHolder.locationID.setText(Integer.toString(loc.getId()));
        viewHolder.name.setText(loc.getName());
        viewHolder.title.setText(loc.getTitle());
        viewHolder.pictureURL.setText(loc.getPicture());
        viewHolder.locAddress.setText(loc.getAddress());
        viewHolder.owner.setText(loc.getOwner());

        Context context = viewHolder.picture.getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Picasso.with(context).load(ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(loc.getPicture()))
                .resize(width-40,(int)(height/2.5f))
                .centerCrop()
                .into(viewHolder.picture);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView longtitude;
        public TextView latitude;
        public ImageView picture;
        public TextView locationID;
        public TextView title;
        public TextView name;
        public TextView pictureURL;
        public TextView locAddress;
        public TextView owner;
        public ImageView editIcon;




        public ViewHolder(final View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.locationText);
            longtitude = (TextView) itemView.findViewById(R.id.longtitude);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            picture = (ImageView) itemView.findViewById(R.id.imageView);
            locationID = (TextView) itemView.findViewById(R.id.locIdentifier);
            title = (TextView) itemView.findViewById(R.id.loc_detail_title);
            name = (TextView) itemView.findViewById(R.id.loc_detail_name);
            pictureURL = (TextView) itemView.findViewById(R.id.picture_url);
            locAddress = (TextView) itemView.findViewById(R.id.locAddress);
            owner = (TextView) itemView.findViewById(R.id.loc_owner);
            editIcon = (ImageView) itemView.findViewById(R.id.edit_icon);




            ImageView icon = (ImageView) itemView.findViewById(R.id.location_icon);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity main = (MainActivity) context;
                    String loc = "[" + locationID.getText() + "]";
                    main.showLocationOnMap(v,loc);

                }
            });

            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Location loc = new Location();
                    loc.setTitle(title.getText().toString());
                    loc.setText(text.getText().toString());
                    loc.setName(name.getText().toString());
                    loc.setPicture(pictureURL.getText().toString());
                    loc.setId(Integer.valueOf(locationID.getId()));
                    loc.setAddress(locAddress.getText().toString());
                    loc.setOwner(owner.getText().toString());

                    Fragment fragment = LocationFormFragment.newInstance(loc);
                    FragmentManager fragmentManager = ((FragmentActivity)context).getFragmentManager();
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,fragment,"LocationFormFragment");
                    fragmentTransaction.addToBackStack("LocationFormFragment");
                    fragmentTransaction.commit();


                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Location loc = new Location();
                    loc.setTitle(title.getText().toString());
                    loc.setText(text.getText().toString());
                    loc.setName(name.getText().toString());
                    loc.setPicture(pictureURL.getText().toString());
                    loc.setId(Integer.valueOf(locationID.getId()));
                    loc.setAddress(locAddress.getText().toString());
                    loc.setOwner(owner.getText().toString());
                    goToLocationDetail(loc,0);




                }
            });

        }


    }

    void goToLocationDetail(Location loc, int mode)
    {
        Fragment fragment = LocationDetailFragment.newInstance(loc);
        FragmentManager fragmentManager = ((FragmentActivity)context).getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment,"LocationDetail");
        fragmentTransaction.addToBackStack("LocationDetail");
        fragmentTransaction.commit();

    }
}