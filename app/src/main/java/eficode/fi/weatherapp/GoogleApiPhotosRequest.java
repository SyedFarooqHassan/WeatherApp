package eficode.fi.weatherapp;


import android.support.annotation.NonNull;

import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import eficode.fi.weatherapp.interfaces.IResponseHelper;

public class GoogleApiPhotosRequest {
    public static void getPhotos(String placeId, final IResponseHelper iResponseHelper) {
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = WeatherApplication.getInstance().getmGeoDataClient().getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                PlacePhotoMetadataResponse photos = task.getResult();
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                Task<PlacePhotoResponse> photoResponse = WeatherApplication.getInstance().getmGeoDataClient().getPhoto(photoMetadata);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        iResponseHelper.getData(photo.getBitmap());
                    }
                });


            }


        });
    }
}
