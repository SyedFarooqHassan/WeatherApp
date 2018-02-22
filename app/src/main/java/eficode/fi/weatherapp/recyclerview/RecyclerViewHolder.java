package eficode.fi.weatherapp.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import eficode.fi.weatherapp.R;
import eficode.fi.weatherapp.interfaces.IOnItemClickListener;

public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
    public TextView tvCityName;
    public ImageButton ibDeleteCities;

    public RecyclerViewHolder(@NonNull View view, final IOnItemClickListener iOnItemClickListener) {
        super(view);
        tvCityName = view.findViewById(R.id.tv_city_name);
        ibDeleteCities = view.findViewById(R.id.ib_delete_cities);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        ibDeleteCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        });
    }


}
