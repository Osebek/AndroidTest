package com.example.nejcvesel.pazikjehodis;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by nejcvesel on 15/02/17.
 */

public class PathLocationsViewHolder2 extends RecyclerView.ViewHolder {

    private TextView pathName;

    public TextView getPathCity() {
        return pathCity;
    }

    public void setPathCity(TextView pathCity) {
        this.pathCity = pathCity;
    }

    private TextView pathCity;

    public TextView getPathName() {
        return pathName;
    }

    public void setPathName(TextView pathName) {
        this.pathName = pathName;
    }

    private TextView path_owner;
    private TextView path_description;

    public TextView getPathNumOfLocations() {
        return pathNumOfLocations;
    }

    public void setPathNumOfLocations(TextView pathNumOfLocations) {
        this.pathNumOfLocations = pathNumOfLocations;
    }

    public TextView getPath_owner() {
        return path_owner;
    }

    public void setPath_owner(TextView path_owner) {
        this.path_owner = path_owner;
    }

    public TextView getPath_description() {
        return path_description;
    }

    public void setPath_description(TextView path_description) {
        this.path_description = path_description;
    }

    private TextView pathNumOfLocations;

    public PathLocationsViewHolder2 (View v) {
        super(v);
        pathName = (TextView) v.findViewById(R.id.path_name);
        pathCity = (TextView) v.findViewById(R.id.path_city);
        path_owner = (TextView) v.findViewById(R.id.path_owner);
        path_description = (TextView) v.findViewById(R.id.path_description);
        pathNumOfLocations = (TextView) v.findViewById(R.id.path_num_of_locations);
    }

}