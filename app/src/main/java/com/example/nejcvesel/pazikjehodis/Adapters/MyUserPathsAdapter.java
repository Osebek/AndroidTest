package com.example.nejcvesel.pazikjehodis.Adapters;

/**
 * Created by nejcvesel on 17/03/17.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nejcvesel.pazikjehodis.Fragments.EditPathFragment;
import com.example.nejcvesel.pazikjehodis.Fragments.LocationInPathDetailFragment;
import com.example.nejcvesel.pazikjehodis.Fragments.MyPathListFragment;
import com.example.nejcvesel.pazikjehodis.Fragments.PathLocationsFragment;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nejcvesel on 13/02/17.
 */

public class MyUserPathsAdapter extends RecyclerView.Adapter<MyUserPathsAdapter.ViewHolder> implements BackendAPICall.BackendCallback{
    List<Path> mItems;
    Context context;
    BackendAPICall apiCall;


    public MyUserPathsAdapter(Context context) {
        super();
        this.context = context;
        mItems = new ArrayList<Path>();
    }

    public void addData(Path pth) {
        mItems.add(pth);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_user_paths, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        apiCall  = new BackendAPICall(this,"");




        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Path path = mItems.get(i);
        viewHolder.pathOwner.setText(path.getOwner());
        viewHolder.pathName.setText(path.getName());
        viewHolder.pathCity.setText(path.getCity());
        viewHolder.pathDescription.setText(path.getDescription());
        viewHolder.pathID.setText(Integer.toString(path.getId()));
        List<Integer> locs = path.getPathLocations();
        viewHolder.pathLocations.setText(locs.toString());
        viewHolder.numOfPathLocations.setText(String.valueOf(locs.size()));



    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void getAllPathsCallback(List<Path> paths, String message) {

    }

    @Override
    public void getAllLocationsCallback(List<Location> loactions, String status) {

    }

    @Override
    public void getSpecificLocationCallback(Location loaction, String message) {

    }

    @Override
    public void getSpecificUserCallback(User user, String message) {

    }

    @Override
    public void getAllUsersCallback(List<User> user, String message) {

    }

    @Override
    public void getUserLocationCallback(List<Location> location, String message) {

    }

    @Override
    public void getUserProfileCallback(User user, String message) {

    }

    @Override
    public void getRefreshTokeneCallback(BackendToken token, String message) {

    }

    @Override
    public void getConvertTokenCallback(BackendToken token, String message) {

    }

    @Override
    public void getAddMessageCallback(String message, String backendCall)
    {
        FragmentManager fragmentManager = ((FragmentActivity)context).getFragmentManager();
        if (message.equals("OK"))
        {
            Toast msg = Toast.makeText((FragmentActivity)context,"Brisanje poti uspešno",Toast.LENGTH_LONG);
            msg.show();
        }
        if (message.equals("ERROR"))
        {
            Toast msg = Toast.makeText((FragmentActivity)context,"Brisanje ni uspelo. Napaka na strežniku",Toast.LENGTH_LONG);
            msg.show();

        }
        if (message.equals("ERROR_FAIL"))
        {
            Toast msg = Toast.makeText((FragmentActivity)context,"Brisanje ni uspelo. Ni povezave do strežnika",Toast.LENGTH_LONG);
            msg.show();

        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame,new MyPathListFragment(),"MyPathListFragment")
                .addToBackStack("MyPathListFragment")
                .commit();


    }

    @Override
    public void getUserPathsCallback(List<Path> userPaths, String message) {

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pathOwner;
        public TextView pathName;
        public TextView pathCity;
        public TextView pathDescription;
        public TextView pathID;
        public TextView pathLocations;
        public TextView numOfPathLocations;
        public ImageView edit_icon;
        public ImageView delete_icon;


        public ViewHolder(final View itemView) {
            super(itemView);
            pathOwner = (TextView) itemView.findViewById(R.id.path_owner);
            pathName = (TextView) itemView.findViewById(R.id.path_name);
            pathCity = (TextView) itemView.findViewById(R.id.path_city);
            pathDescription = (TextView) itemView.findViewById(R.id.path_description);
            pathID = (TextView) itemView.findViewById(R.id.path_id);
            pathLocations = (TextView) itemView.findViewById(R.id.path_locations);
            numOfPathLocations = (TextView) itemView.findViewById(R.id.path_num_of_locations);
            edit_icon = (ImageView) itemView.findViewById(R.id.edit_path_icon);
            delete_icon = (ImageView) itemView.findViewById(R.id.delete_path_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment fragment = PathLocationsFragment.newInstance(1, MainActivity.stringToStringArray(pathLocations.getText().toString()),
                            pathOwner.getText().toString(),pathCity.getText().toString(),pathName.getText().toString(),
                            pathDescription.getText().toString());
                    FragmentManager fragmentManager = ((FragmentActivity)context).getFragmentManager();
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,fragment,"PathLocationsFragment");
                    fragmentTransaction.addToBackStack("PathLocationsFragment");
                    fragmentTransaction.commit();


                }
            });

            ImageView icon = (ImageView) itemView.findViewById(R.id.path_icon);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity main = (MainActivity) context;
                    main.showLocationOnMap(v,(String)pathLocations.getText());

                }
            });

            edit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Path path = new Path();
                    path.setOwner(pathOwner.getText().toString());
                    path.setName(pathName.getText().toString());
                    path.setCity(pathCity.getText().toString());
                    path.setDescription(pathDescription.getText().toString());
                    path.setId(Integer.valueOf(pathID.getText().toString()));

                    String locationsPath = pathLocations.getText().toString();
                    String[] tmp = locationsPath.trim().split(",");
                    tmp[0] = tmp[0].substring(1,tmp[0].length());
                    tmp[tmp.length-1] = tmp[tmp.length-1].trim().substring(0,tmp[tmp.length-1].length()-2);
                    List<Integer> intPathLocs = new ArrayList<Integer>();
                    for (String s : tmp)
                    {
                        intPathLocs.add(Integer.valueOf(s.trim()));
                    }

                    path.setPathLocations(intPathLocs);
                    Fragment fragment = EditPathFragment.newInstance(path);
                    FragmentManager fragmentManager = ((FragmentActivity) context).getFragmentManager();
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,fragment,"EditPathFragment");
                    fragmentTransaction.addToBackStack("EditPathFragment");
                    fragmentTransaction.commit();
                }
            });

            delete_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Brisanje");
                    alertDialog.setMessage("Ste prepirčani da želite izbrisati to pot?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DA, zbriši",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String path_id = pathID.getText().toString();
                                    apiCall.deletePath(((MainActivity)context).userProfile.getBackendAccessToken(),path_id);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Prekliči",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();



                }
            });

        }
    }
}







