package com.happytimes.alisha.vurbalicious;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.happytimes.alisha.helper.AppController;
import com.happytimes.alisha.helper.CardTouchHelper;
import com.happytimes.alisha.helper.JacksonRequest;
import com.happytimes.alisha.model.Card;
import com.happytimes.alisha.model.VurbCard;
import com.happytimes.alisha.model.VurbPlace;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class InitActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = InitActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static final String INPUT_URL = "https://gist.githubusercontent.com/helloandrewpark/0a407d7c681b833d6b49/raw/5f3936dd524d32ed03953f616e19740bba920bcd/gistfile1.js";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private volatile boolean locDataReady, cardDetailsReady;

    RecyclerView cardRecyclerView;
    RecyclerCardAdapter cardAdapter;
    VurbCard vurbCard = new VurbCard();
    protected Handler handler;
    private List<Card> list = new ArrayList<>();

    private RequestQueue mRequestQueue;
    private TextView txtLocation;
    long startTime, endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        setContentView(R.layout.activity_init);

        //To reduce overdraw
        getWindow().setBackgroundDrawable(null);

        txtLocation = (TextView) findViewById(R.id.location);

        handler = new Handler();
        mRequestQueue = AppController.getInstance().getRequestQueue();
        locDataReady = false;
        cardDetailsReady = false;


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*Setting up for Recyclerview*/
        initializeRecyclerView();

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        //Make a request to get JSON
        makeJSONRequest(INPUT_URL);
        startTime = System.currentTimeMillis();
        Log.d(TAG, "StartTime: " + startTime + "onCreate");
    }


    private void makeJSONRequest(String url) {
        JacksonRequest<VurbCard> jacksonRequest = new JacksonRequest<>
                (Method.GET, url, null, VurbCard.class, new Response.Listener<VurbCard>() {
                    @Override
                    public void onResponse(VurbCard response) {
                        parseResponseDetails(response);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add a request (in this example, called jacksonRequest) to your RequestQueue.
        AppController.getInstance().addToRequestQueue(jacksonRequest, TAG);

    }


    private void parseResponseDetails(VurbCard response) {
        vurbCard.setCards(removeDuplicate(response.getCards()));
        cardDetailsReady = true;
        checkToDisplayDetails();
    }


    private void displayCardDetails() {
        list = vurbCard.getCards();

        //Simple adapter when infinite scrolling is not implemented
        //cardAdapter = new RecyclerCardAdapter(vurbCard.getCards());

        //Changing the adapter to accommodate infinite scrolling
        cardAdapter = new RecyclerCardAdapter(list, cardRecyclerView);
        cardRecyclerView.setAdapter(cardAdapter);

        cardAdapter.setOnLoadMoreListener(new RecyclerCardAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add progress item
                list.add(null);
                cardAdapter.notifyItemInserted(list.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //remove progress item
                        list.remove(list.size() - 1);
                        cardAdapter.notifyItemRemoved(list.size());
                        //add items one by one
                        while (list.size() < 60) {
                            for (int i = 0; i < 15; i++) {
                                list.add(new VurbPlace("Restaurant " + (list.size() + 1)));
                                cardAdapter.notifyItemInserted(list.size());
                            }
                        }
                        cardAdapter.setLoaded();
                    }
                }, 2000);
            }
        });

        ItemTouchHelper.Callback callback = new CardTouchHelper(cardAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(cardRecyclerView);

    }

    /**
     * Eliminates duplicates**/
    private List<Card> removeDuplicate( List<Card> cards) {
        List<Card> originalList = cards;
        Set<Card> cardSet = new LinkedHashSet<>(originalList); //Using LinkedHashSet to preserve ordering of inserted elements
        originalList.clear();
        originalList.addAll(cardSet);
        return originalList;
    }

    /**
     * Setup the RecyclerView using the LayoutManager and the Adapter**/
    private void initializeRecyclerView() {
        cardRecyclerView = (RecyclerView) findViewById(R.id.vurbCardsList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardRecyclerView.setLayoutManager(llm);

        cardRecyclerView.setAdapter(new RecyclerCardAdapter());
        cardRecyclerView.setHasFixedSize(true);
    }

    /**
     * Creating the google api client
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleInstance = GoogleApiAvailability.getInstance();
        int resultCode = googleInstance.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleInstance.isUserResolvableError(resultCode)) {
                googleInstance.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        mRequestQueue.cancelAll(TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Method to display the location on UI
     */
    private void displayLocation() {

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
        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            txtLocation.setText(latitude + ", " + longitude);
        } else {
            txtLocation
                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }


    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        locDataReady = true;
        checkToDisplayDetails();


    }

    private void checkToDisplayDetails() {
        if(isLocDataReady() && isCardDetailsReady()) {
            displayLocation();
            displayCardDetails();
            endTime = System.currentTimeMillis();
            Log.d(TAG, "EndTime: " + endTime);
            Log.d(TAG, "Total Execution Time: " + (endTime - startTime) + "ms");
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public boolean isLocDataReady() {
        return locDataReady;
    }

    public void setLocDataReady(boolean locDataReady) {
        this.locDataReady = locDataReady;
    }

    public boolean isCardDetailsReady() {
        return cardDetailsReady;
    }

    public void setCardDetailsReady(boolean cardDetailsReady) {
        this.cardDetailsReady = cardDetailsReady;
    }
}
