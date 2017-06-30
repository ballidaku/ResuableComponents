package ballidaku.resuablecomponents.myMaps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ballidaku.resuablecomponents.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
          GoogleApiClient.ConnectionCallbacks,
          GoogleApiClient.OnConnectionFailedListener,
          LocationListener
{
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    boolean isMarkerRotating = false;


    Context context;

    ArrayList<LatLng> list = new ArrayList<>();

    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this;


        list.add(new LatLng(30.708254, 76.691812));
        list.add(new LatLng(30.708457, 76.691627));
        list.add(new LatLng(30.708962, 76.691185));
        list.add(new LatLng(30.709075, 76.691099));
        list.add(new LatLng(30.709054, 76.690975));
        list.add(new LatLng(30.708939, 76.690788));
        list.add(new LatLng(30.709054, 76.690975));
        list.add(new LatLng(30.708457, 76.691627));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onPause()
    {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            else
            {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else
        {
            // buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        smoothScroll();


        // Add a marker in Sydney, Australia, and move the camera.
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }


    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                  .addConnectionCallbacks(this)
                  .addOnConnectionFailedListener(this)
                  .addApi(LocationServices.API)
                  .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                  Manifest.permission.ACCESS_FINE_LOCATION)
                  == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        /*if (mCurrLocationMarker != null)
        {
            mCurrLocationMarker.remove();
        }*/

 /*
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));*/


        /*LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());


        running(currentLatLng);*/


    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                      Manifest.permission.ACCESS_FINE_LOCATION))
            {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                          .setTitle("Location Permission Needed")
                          .setMessage("This app needs the Location permission, please accept to use location functionality")
                          .setPositiveButton("OK", new DialogInterface.OnClickListener()
                          {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i)
                              {
                                  //Prompt the user once explanation has been shown
                                  ActivityCompat.requestPermissions(MapActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_LOCATION);
                              }
                          })
                          .create()
                          .show();


            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                          MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                          && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                              Manifest.permission.ACCESS_FINE_LOCATION)
                              == PackageManager.PERMISSION_GRANTED)
                    {

                        if (mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                }
                else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void refreshMapUI(LatLng currentLatLng)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                  .target(mMap.getCameraPosition().target)
                  .zoom(17)
                  //.bearing(30)
                  //.tilt(270)
                  .build()));

        mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                  .position(currentLatLng)
                  .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_c))
                  .anchor(0.5f, 0.5f)
//                  .flat(true)
                  //.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(context,R.drawable.ic_sedan_car_model)))

                  /*.title("Hello world")*/);
    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId)
    {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                  drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    /*public void running(final LatLng currentLatLng)
    {
        final LatLng endLatLng = new LatLng(30.708858, 76.690625);

        refreshMapUI(currentLatLng);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker arg0)
            {
                rotateMarker(mCurrLocationMarker, (float) bearingBetweenLocations(currentLatLng, endLatLng));

                return true;
            }
        });


    }*/
    LatLng previousLatLong;

    public void smoothScroll()
    {


        // final LatLng SomePos = new LatLng(12.7796354, 77.4159606);


        final LatLng currentLatLng = new LatLng(30.708200, 76.691737);

        // final LatLng endLatLng = new LatLng(30.708858, 76.690625);

        refreshMapUI(currentLatLng);


        previousLatLong = currentLatLng;

        try
        {
          /*  if (mMap == null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                }
            }
            mMap.setMapType(mMap.MAP_TYPE_NORMAL);*/
            //mMap.setMyLocationEnabled(true);
            /*mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);*/
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(SomePos));
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
//                      .target(mMap.getCameraPosition().target)
//                      .zoom(17)
//                      .bearing(30)
//                      .tilt(45)
//                      .build()));
//
//            mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
//                      .position(SomePos)
//                      .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
//                      .title("Hello world"));


            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {
                @Override
                public boolean onMarkerClick(Marker arg0)
                {

                    final LatLng startPosition = mCurrLocationMarker.getPosition();
                    //final LatLng finalPosition = new LatLng(12.7801569, 77.4148528);
                    final Handler handler = new Handler();
                    final long start = SystemClock.uptimeMillis();
                    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
                    final float durationInMs = 3000;
                    final boolean hideMarker = false;

                    count++;


                    float bearing = getLocation(previousLatLong).bearingTo(getLocation(list.get(count)));

                    //float bearing = prevLoc.bearingTo(newLoc) ;
                    mCurrLocationMarker.setRotation(bearing);


                    previousLatLong = list.get(count);


                    handler.post(new Runnable()
                    {
                        long elapsed;
                        float t;
                        float v;

                        @Override
                        public void run()
                        {
                            // Calculate progress using interpolator
                            elapsed = SystemClock.uptimeMillis() - start;
                            t = elapsed / durationInMs;


                            LatLng currentPosition = new LatLng(
                                      startPosition.latitude * (1 - t) + list.get(count).latitude * t,
                                      startPosition.longitude * (1 - t) + list.get(count).longitude * t);


                            mCurrLocationMarker.setPosition(currentPosition);


                            //rotateMarker(mCurrLocationMarker, (float) bearingBetweenLocations(currentLatLng, list.get(count)));


                            // running(endLatLng);
                            // Repeat till progress is complete.
                            if (t < 1)
                            {
                                // Post again 16ms later.
                                handler.postDelayed(this, 16);
                            }
                            else
                            {
                                if (hideMarker)
                                {
                                    mCurrLocationMarker.setVisible(false);
                                }
                                else
                                {
                                    mCurrLocationMarker.setVisible(true);
                                }
                            }
                        }
                    });

                    return true;

                }

            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public Location getLocation(LatLng latLng)
    {

        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(latLng.latitude);
        temp.setLongitude(latLng.longitude);
        return temp;
    }


    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2)
    {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                  * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation)
    {
        if (!isMarkerRotating)
        {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    Log.e("rot", "" + rot);

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0)
                    {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                    else
                    {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }


}
