package com.example.nejcvesel.pazikjehodis.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.R;


/**
 * Created by nejcvesel on 15/02/17.
 */

public class PathLocationsViewHolder1 extends RecyclerView.ViewHolder {

    private TextView title, name, address;
    private ImageView img;
    private CardView card;
    private ImageView icon;

    public PathLocationsViewHolder1(View v) {
        super(v);
        title = (TextView) v.findViewById(R.id.loc_detail_title);
        name = (TextView) v.findViewById(R.id.loc_detail_name);
        address = (TextView) v.findViewById(R.id.locAddress);
        img = (ImageView) v.findViewById(R.id.imageView);
        card = (CardView) v.findViewById(R.id.locationCard);
        icon = (ImageView) v.findViewById(R.id.location_icon);
    }

    public CardView getCard() {return this.card;}

    public void setCard(CardView card) { this.card = card;}

    public TextView getTitle() {
        return this.title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getName() {
        return this.name;
    }

    public void setName(TextView name)
    {
        this.name = name;
    }

    public void setAddress(TextView address)
    {
        this.address = address;
    }

    public TextView getAddress() {
        return this.address;
    }

    public ImageView getImg()
    {
        return this.img;
    }

    public ImageView getIcon() {return this.icon;}

    public void setImg(ImageView img)
    {
        this.img = img;
    }




}