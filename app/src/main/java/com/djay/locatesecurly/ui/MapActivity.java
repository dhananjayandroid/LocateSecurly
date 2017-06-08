package com.djay.locatesecurly.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.djay.locatesecurly.R;
import com.djay.locatesecurly.models.Session;
import com.djay.locatesecurly.models.SessionLatLng;
import com.djay.locatesecurly.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Activity class for showing locations on map extends {@link BaseActivity}
 *
 * @author Dhananjay Kumar
 */
public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static void start(Context context, Session session) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(Constants.EXTRA_SESSION, session);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initUi();
    }

    /**
     * initializes UI
     */
    private void initUi() {
        ButterKnife.bind(this);
        setActivityUpEnabled();
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Setups map if not initialized
     */
    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        generatePointsLine();
    }

    /**
     * Draw line connecting all captured locations
     */
    private void generatePointsLine() {
        ArrayList<SessionLatLng> sessionLatLngs = dataManager.getSessionDetails(((Session) getIntent()
                .getSerializableExtra
                        (Constants.EXTRA_SESSION)).getSessionId());
        if (sessionLatLngs != null) {
            Log.i("sessionLatLngs === ", "" + sessionLatLngs.size());
            PolylineOptions rectOptions = new PolylineOptions();
            rectOptions.color(Color.argb(255, 85, 166, 27));
            LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
            for (int i = 0; i < sessionLatLngs.size(); i++) {
                LatLng latlng = new LatLng(sessionLatLngs.get(i).getLat(), sessionLatLngs.get(i).getLng());
                rectOptions.add(latlng);
                latLngBuilder.include(latlng);
                if (i == 0)
                    mMap.addMarker(new MarkerOptions().position(latlng).title(getString(R.string.start)));
                if (i == sessionLatLngs.size() - 1)
                    mMap.addMarker(new MarkerOptions().position(latlng).title(getString(R.string.end)));
            }
            mMap.addPolyline(rectOptions);
            Point displaySize = new Point();
            getWindowManager().getDefaultDisplay().getSize(displaySize);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), (int) (displaySize.x * 0.9),
                    (int) (displaySize.y * 0.8), 0));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
