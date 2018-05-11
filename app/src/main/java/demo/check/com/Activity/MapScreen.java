package demo.check.com.Activity;

import android.Manifest;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import demo.check.com.Constants;
import demo.check.com.FontChangeCrawler;
import demo.check.com.R;
import butterknife.ButterKnife;
import static android.content.Context.LOCATION_SERVICE;

public class MapScreen extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationSource,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionCallback {
    Polyline routePolyline;
    @BindView(R.id.navigation)
    Button navigation;
    LocationManager locationManager;
    BitmapDescriptor mapCarIcon;
    GoogleApiClient mGoogleApiClient;
    public GoogleMap mMap;
    Location mCurrentLocation;
    Marker currentLocMarker, pickUPrDropMarker;
    private OnLocationChangedListener mMapLocationListener = null;
    LatLng prevLatLng = new LatLng(0, 0);
    float previousBearing = 0;
    public static LatLng destLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        FontChangeCrawler fontChangeCrawler = new FontChangeCrawler(this.getAssets(), getString(R.string.app_font));
        fontChangeCrawler.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        mapFragment.getMapAsync(this);

        mapCarIcon = BitmapDescriptorFactory.fromResource(R.mipmap.compass);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled) {

            GPSTurnOnAlert();
        }
        mGoogleApiClient.connect();
    }

    public void GPSTurnOnAlert() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(Constants.SET_INTERVAL); //5 seconds
        locationRequest.setFastestInterval(Constants.SET_FASTESTINTERVAL); //3 seconds
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapScreen.this, Constants.REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(MapScreen.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapScreen.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                    startLocationUpdates();

                } else {
                    System.out.println("INSIDE request permission");
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
            }
        }
    }

    protected void startLocationUpdates() {
        checkPermission();
        mGoogleApiClient.connect();
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this, Looper.getMainLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng curPos;
        float curPosBearing;

        if (mMapLocationListener != null) {
            mMapLocationListener.onLocationChanged(location);
        }


        if (mCurrentLocation != null) {
            System.out.println("ONLOCATIOn CHANGE bearing" + mCurrentLocation.getBearing());
            curPos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            curPosBearing = mCurrentLocation.getBearing();

        } else {
            curPos = new LatLng(location.getLatitude(), location.getLongitude());
            curPosBearing = location.getBearing();
            System.out.println("location null");
        }
        mCurrentLocation = location;
        if (mMap != null) {
            try {

                System.out.println("Key moved ===>" + mCurrentLocation.getSpeed());

                zoomCameraToPosition(curPos);
                if (destLocation != null) {
                    GoogleDirection.withServerKey(Constants.GoogleDirectionApi)
                            .from(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                            .to(destLocation)
                            .unit(Unit.METRIC)
                            .transportMode(TransportMode.DRIVING)
                            .execute(this);

                }


                if (currentLocMarker == null) {


                    currentLocMarker = mMap.addMarker(new MarkerOptions()
                            .icon(mapCarIcon)
                            .position(curPos)
                            .flat(true));
                    currentLocMarker.setAnchor(0.5f, 0.5f);
                    currentLocMarker.setRotation(curPosBearing);

                } else {

                    if (mCurrentLocation.getBearing() != 0.0)
                        previousBearing = mCurrentLocation.getBearing();

                    if (prevLatLng != new LatLng(0, 0)) {

                        if (!(curPos.equals(prevLatLng))) {

                            double[] startValues = new double[]{prevLatLng.latitude, prevLatLng.longitude};

                            double[] endValues = new double[]{curPos.latitude, curPos.longitude};

                            System.out.println("Start location===>" + startValues[0] + "  " + startValues[1]);
                            System.out.println("end location===>" + endValues[0] + "  " + endValues[1]);

                            System.out.println("inside locationchange bearing" + mCurrentLocation.getBearing());

                            animateMarkerTo(currentLocMarker, startValues, endValues, mCurrentLocation.getBearing());

                        } else {
                            System.out.println("outside locationchange bearing" + mCurrentLocation.getBearing());
                            if (mCurrentLocation.getBearing() == 0.0)
                                currentLocMarker.setRotation(previousBearing);
                            else
                                currentLocMarker.setRotation(mCurrentLocation.getBearing());
                            // currentLocMarker.setRotation(mCurrentLocation.getBearing());
                        }
                    } else {
                        currentLocMarker.setPosition(curPos);
                        currentLocMarker.setRotation(mCurrentLocation.getBearing());
                    }

                    prevLatLng = new LatLng(curPos.latitude, curPos.longitude);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void zoomCameraToPosition(LatLng curPos) {

        boolean contains = mMap.getProjection().getVisibleRegion().latLngBounds.contains(curPos);

        if (!contains) {

            float zoomPosition;
            zoomPosition = Constants.MAP_ZOOM_SIZE;

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(curPos)                              // Sets the center of the map to current location
                    .zoom(zoomPosition)
                    .tilt(0)                                     // Sets the tilt of the camera to 0 degrees
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void animateMarkerTo(final Marker marker, double[] startValues, double[] endValues, final float bearing) {


        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
        latLngAnimator.setDuration(1300);
        latLngAnimator.setInterpolator(new DecelerateInterpolator());
        latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                double[] animatedValue = (double[]) animation.getAnimatedValue();
                marker.setPosition(new LatLng(animatedValue[0], animatedValue[1]));
            }
        });
        latLngAnimator.start();
        // marker.setRotation(bearing);
        //rotateMarker(marker,bearing,mMap);


        //marker.setRotation(360 - rotate + myMap.getCameraPosition().bearing);
        if (mCurrentLocation.getBearing() == 0.0)
            marker.setRotation(previousBearing);
        else
            marker.setRotation(mCurrentLocation.getBearing());
    }

    private class DoubleArrayEvaluator implements TypeEvaluator<double[]> {

        private double[] mArray;

        /**
         * Create a DoubleArrayEvaluator that does not reuse the animated value. Care must be taken
         * when using this option because on every evaluation a new <code>double[]</code> will be
         * allocated.
         *
         * @see #DoubleArrayEvaluator(double[])
         */
        DoubleArrayEvaluator() {
        }

        /**
         * Create a DoubleArrayEvaluator that reuses <code>reuseArray</code> for every evaluate() call.
         * Caution must be taken to ensure that the value returned from
         * {@link ValueAnimator#getAnimatedValue()} is not cached, modified, or
         * used across threads. The value will be modified on each <code>evaluate()</code> call.
         *
         * @param reuseArray The array to modify and return from <code>evaluate</code>.
         */
        DoubleArrayEvaluator(double[] reuseArray) {
            mArray = reuseArray;
        }

        /**
         * Interpolates the value at each index by the fraction. If
         * {@link #DoubleArrayEvaluator(double[])} was used to construct this object,
         * <code>reuseArray</code> will be returned, otherwise a new <code>double[]</code>
         * will be returned.
         *
         * @param fraction   The fraction from the starting to the ending values
         * @param startValue The start value.
         * @param endValue   The end value.
         * @return A <code>double[]</code> where each element is an interpolation between
         * the same index in startValue and endValue.
         */
        @Override
        public double[] evaluate(float fraction, double[] startValue, double[] endValue) {
            double[] array = mArray;
            if (array == null) {
                array = new double[startValue.length];
            }

            for (int i = 0; i < array.length; i++) {
                double start = startValue[i];
                double end = endValue[i];
                array[i] = start + (fraction * (end - start));
            }
            return array;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mMapLocationListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mMapLocationListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //mMap.setMyLocationEnabled(true);
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.maps_style);
        googleMap.setMapStyle(style);
        mMap.getMinZoomLevel();
        mGoogleApiClient.connect();
        checkPermission();
        currentLocation();
        pickUPrDropMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(13.0474876, 80.0685817)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.wedding_location)));


    }

    public void currentLocation() {
        mCurrentLocation = getFusedLocation();
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            System.out.println("INSIDE LOCAION CHANGE" + mCurrentLocation.getLatitude() + mCurrentLocation.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)                              // Sets the center of the map to current location
                    .zoom(Constants.MAP_ZOOM_SIZE)
                    .tilt(0)                                     // Sets the tilt of the camera to 0 degrees
                    .build();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            if (currentLocMarker == null) {

                currentLocMarker = mMap.addMarker(new MarkerOptions()
                        .icon(mapCarIcon)
                        .position(latLng)
                        .flat(true)
                        .anchor(0.5f, 0.5f)
                        .rotation(mCurrentLocation.getBearing()));
            } else {

                currentLocMarker.setPosition(latLng);
            }
        }
    }

    public Location getFusedLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        System.out.println("Location Provoider:" + " Fused Location");

        if (mCurrentLocation == null) {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            System.out.println("Location Provoider:" + " Fused Location Fail: GPS Location");

            if (locationManager != null) {

                //To avoid duplicate listener
                try {
                    locationManager.removeUpdates(this);
                    System.out.print("remove location listener success");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("remove location listener failed");
                }

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        Constants.MIN_TIME_BW_UPDATES,
                        Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (mCurrentLocation == null) {

                    System.out.println("Location Provoider:" + " GPS Location Fail: Network Location");

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            Constants.MIN_TIME_BW_UPDATES,
                            Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        }

        return mCurrentLocation;
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        System.out.println("enter the google direction status" + direction.getStatus());
        if (direction.isOK()) {
            if (mCurrentLocation != null && destLocation != null) {
                onDirectionSuccessMarkerPlacing(direction);
            }
        }

    }

    public void onDirectionSuccessMarkerPlacing(Direction direction) {
        LatLng curPos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
        if (curPos.latitude != 0 && curPos.longitude != 0) {

            zoomCameraToPosition(curPos);
        }

        if (routePolyline != null) {
            routePolyline.setPoints(directionPositionList);

        } else {
            routePolyline = mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.BLUE));
        }

        if (currentLocMarker.getPosition() != null) {
            if (currentLocMarker.getPosition().latitude != 0 && currentLocMarker.getPosition().longitude != 0) {
                if (mCurrentLocation.getBearing() != 0.0) {
                    final CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentLocMarker.getPosition())      // Sets the center of the map to Mountain View
                            //.zoom(Constants.MAP_ZOOM_SIZE_ONTRIP)      // Sets the zoom
                            .bearing(mCurrentLocation.getBearing())                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    CameraUpdate center = CameraUpdateFactory.newLatLng(curPos);
                    mMap.animateCamera(center, 400, null);
                } else {
                    final CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentLocMarker.getPosition())      // Sets the center of the map to Mountain View
                            //.zoom(Constants.MAP_ZOOM_SIZE_ONTRIP)      // Sets the zoom
                            .bearing(previousBearing)                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    CameraUpdate center = CameraUpdateFactory.newLatLng(curPos);
                    mMap.animateCamera(center, 400, null);
                }
            }
        }

        try {
            if (pickUPrDropMarker != null)
                pickUPrDropMarker.remove();

            // before loop:
            System.out.println("destLocation location ===>" + destLocation);
            pickUPrDropMarker = mMap.addMarker(new MarkerOptions().position(destLocation).icon(BitmapDescriptorFactory.fromResource(R.mipmap.wedding_location)));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }

    @OnClick(R.id.navigation)
    public void onViewClicked() {
        destLocation = new LatLng(13.0474876, 80.0685817);
        if (mCurrentLocation != null) {
            GoogleDirection.withServerKey(Constants.GoogleDirectionApi)
                    .from(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .to(destLocation)
                    .unit(Unit.METRIC)
                    .transportMode(TransportMode.DRIVING)
                    .execute(this);
        }


    }
}
