package com.useek.sdk;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Activity activity;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    public LocationTracker(Activity currentActivity) {
        this.activity = currentActivity;
        connectToApi();
    }

    public String getCoordinates() {
        refreshLocation();
        if(mLastLocation == null) {
            return "";
        } else {
            return mLastLocation.getLongitude() + "," + mLastLocation.getLatitude();
        }
    }

    public Location getLocation() {
        refreshLocation();
        return mLastLocation;
    }

    private void refreshLocation() {
        if(mGoogleApiClient != null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(activity);

        if(code == ConnectionResult.SUCCESS) {
            return true;
        } else {
            if(api.isUserResolvableError(code)) {
                api.showErrorDialogFragment(activity, code, 1000);
            } else {
                Toast.makeText(activity, api.getErrorString(code), Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    private void connectToApi() {
        if(checkPlayServices()) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        refreshLocation();
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("LocationTracker", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
}
