package eficode.fi.weatherapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eficode.fi.weatherapp.R;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.IOnItemClickListener;
import eficode.fi.weatherapp.recyclerview.RecyclerViewHolder;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private Context context;
    private List<LocationInfo> locationInfoList;
    private IOnItemClickListener iOnItemClickListener;

    public RecyclerViewAdapter(final Context context, final IOnItemClickListener iOnItemClickListener) {
        this.context = context;

        this.locationInfoList = new ArrayList<>();
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return locationInfoList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        final LocationInfo locationInfo = locationInfoList.get(position);
        if (locationInfo.getLocationId() == null) {
            holder.ibDeleteCities.setVisibility(View.INVISIBLE);
        } else {
            holder.ibDeleteCities.setVisibility(View.VISIBLE);
        }
        holder.tvCityName.setText(locationInfo.getLocationName());
    }

    /*Creates the view for recycler view*/
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mViewGroup = (ViewGroup) layoutInflater.inflate(
                R.layout.cities_recyclerview_item, viewGroup, false);

        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(mViewGroup, iOnItemClickListener);

        recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemClick(v, locationInfoList.get(recyclerViewHolder.getAdapterPosition()));
            }
        });

        recyclerViewHolder.ibDeleteCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemClick(v, locationInfoList.get(recyclerViewHolder.getAdapterPosition()));
            }
        });

        return recyclerViewHolder;
    }

    public void add(final LocationInfo locationInfo) {
        locationInfoList.add(locationInfo);
        notifyDataSetChanged();
    }

    public void addAll(final List<LocationInfo> locationInfoList) {
        this.locationInfoList.addAll(locationInfoList);
        notifyDataSetChanged();
    }

    public void remove(final LocationInfo locationInfo) {
        locationInfoList.remove(locationInfo);
        notifyDataSetChanged();
    }

    public List<LocationInfo> getLocationInfoList() {
        return locationInfoList;
    }
}
