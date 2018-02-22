package eficode.fi.weatherapp.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eficode.fi.weatherapp.R;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.IOnItemClickListener;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private ArrayList<LocationInfo> locationInfoArrayList;
    private int[] listItemBackground;
    private Context context;
    private IOnItemClickListener iOnItemClickListener;

    public RecyclerViewAdapter(Context context, ArrayList<LocationInfo> locationDataArrayList, IOnItemClickListener iOnItemClickListener) {
        this.locationInfoArrayList = locationDataArrayList;
        this.context = context;
        this.iOnItemClickListener = iOnItemClickListener;
        listItemBackground = new int[]{R.drawable.dark_list_background,
                R.drawable.light_list_background};
    }


    @Override
    public int getItemCount() {
        return locationInfoArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        int listItemBackgroundPosition = position % listItemBackground.length;
        Drawable drawable;
        if (listItemBackgroundPosition == 0) {
            drawable = context.getResources().getDrawable(R.drawable.dark_list_background);
        } else {
            drawable = context.getResources().getDrawable(R.drawable.light_list_background);
        }
        holder.itemView.setBackground(drawable);

        holder.tvCityName.setText(locationInfoArrayList.get(position).getLocationName());

    }

    /*Creates the view for recycler view*/
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mViewGroup = (ViewGroup) layoutInflater.inflate(
                R.layout.cities_recyclerview_item, viewGroup, false);
        return new RecyclerViewHolder(mViewGroup, iOnItemClickListener);
    }

}
