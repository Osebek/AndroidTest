package com.example.nejcvesel.pazikjehodis.Adapters;

/**
 * Created by nejcvesel on 15/02/17.
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
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.Fragments.LocationDetailFragment;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nejcvesel on 19/12/16.
 */

public class MyPathAddAdapter extends RecyclerView.Adapter<MyPathAddAdapter.ViewHolder> implements Filterable {

    List<Location> locationList;
    Context context;
    Filter locationFilter;
    protected final List<Location> filteredLocationList;

    public void showFiltered()
    {
        for (Location loc : filteredLocationList)
        {
            System.out.println(loc.getName());
        }
    }



    public MyPathAddAdapter(Context context) {
        super();
        this.context = context;
        locationList = new ArrayList<Location>();
        filteredLocationList = new ArrayList<>();
    }

    public void addData(Location loc) {
        locationList.add(loc);
        notifyDataSetChanged();
    }

    public void clear() {
        locationList.clear();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(locationFilter == null)
           locationFilter = new LocationFilter(this, locationList);
        return locationFilter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_add_paths, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Location loc = filteredLocationList.get(i);
        viewHolder.text.setText(loc.getText());
        viewHolder.longtitude.setText("latitude: " + loc.getLatitude());
        viewHolder.latitude.setText("longtitude: " + loc.getLongtitude());
        viewHolder.locationID.setText(Integer.toString(loc.getId()));
        viewHolder.name.setText(loc.getName());
        viewHolder.title.setText(loc.getTitle());
        viewHolder.pictureURL.setText(loc.getPicture());
        viewHolder.locAddress.setText(loc.getAddress());

        Context context = viewHolder.picture.getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        MainActivity main = (MainActivity) context;

        if (main.locationsToAddToPath.containsValue(Integer.toString(loc.getId())))
        {
            viewHolder.checkbox.setChecked(true);
        }
        else
        {
            viewHolder.checkbox.setChecked(false);
        }

        Picasso.with(context).load(ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(loc.getPicture()))
                .resize(width-40,(int)(height/2.5f))
                .centerCrop()
                .into(viewHolder.picture);

    }

    @Override
    public int getItemCount() {
        return filteredLocationList.size();
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
        public CheckBox checkbox;



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
            checkbox = (CheckBox) itemView.findViewById(R.id.addToPathCheckbox);

            ImageView icon = (ImageView) itemView.findViewById(R.id.location_icon);
            final MainActivity main = (MainActivity) context;
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loc = "[" + locationID.getText() + "]";
                    main.showLocationOnMap(v,loc);

                }
            });

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkbox.isChecked())
                    {
                        main.locationsToAddToPath.put(locationID.getText().toString(),locationID.getText().toString());
                    }
                    else
                    {
                        main.locationsToAddToPath.remove(locationID.getText().toString());
                    }

                    System.out.println(Arrays.toString(main.locationsToAddToPath.values().toArray()));
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(locationID.getText());

                    Location loc = new Location();
                    loc.setTitle(title.getText().toString());
                    loc.setText(text.getText().toString());
                    loc.setName(name.getText().toString());
                    loc.setPicture(pictureURL.getText().toString());
                    loc.setId(Integer.valueOf(locationID.getText().toString()));
                    loc.setAddress(locAddress.getText().toString());

                    Fragment fragment = LocationDetailFragment.newInstance(loc);
                    FragmentManager fragmentManager = ((FragmentActivity)context).getFragmentManager();
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,fragment,"LocationDetail");
                    fragmentTransaction.addToBackStack("LocationDetail");
                    fragmentTransaction.commit();

                }
            });

        }


    }
}