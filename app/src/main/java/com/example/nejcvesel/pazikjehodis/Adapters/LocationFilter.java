package com.example.nejcvesel.pazikjehodis.Adapters;

import android.widget.Filter;

import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nejcvesel on 20/02/17.
 */

public class LocationFilter extends Filter {

    private List<Location>  originalList;
    private List<Location>  filteredList;
    private Object adapter;

    protected LocationFilter(Object adapter, List<Location> originalList)
    {
        super();
        this.adapter = adapter;
        this.originalList = new LinkedList<>(originalList);
        this.filteredList = new ArrayList<>();
    }



    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        final FilterResults results = new FilterResults();
        filteredList.clear();


        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            for (final Location loc : originalList) {
                if (anywhereInLocation(loc,filterPattern)) {
                    filteredList.add(loc);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    private boolean anywhereInLocation(Location loc, String filterPattern)
    {
        if (loc.getOwner().toLowerCase().contains(filterPattern))
            return true;
        else if (loc.getText().toLowerCase().contains(filterPattern))
            return true;
        else if (loc.getTitle().toLowerCase().contains(filterPattern))
            return true;
        else if(loc.getAddress().toLowerCase().contains(filterPattern))
            return true;
        else if(loc.getName().toLowerCase().contains(filterPattern))
            return true;
        else
            return false;
    }



    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (adapter instanceof MyPathAddAdapter) {
            ((MyPathAddAdapter) adapter).filteredLocationList.clear();
            ((MyPathAddAdapter) adapter).filteredLocationList.addAll((ArrayList<Location>) results.values);
            ((MyPathAddAdapter) adapter).notifyDataSetChanged();

        }
    }

}
