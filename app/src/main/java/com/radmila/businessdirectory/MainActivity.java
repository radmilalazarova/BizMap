package com.radmila.businessdirectory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.radmila.businessdirectory.R;
import com.radmila.businessdirectory.adapter.CategoryPagerAdapter;
import com.radmila.businessdirectory.location.LocationHelper;
import com.radmila.businessdirectory.model.Company;
import com.radmila.businessdirectory.network.ApiClient;
import com.radmila.businessdirectory.network.ApiService;
import com.radmila.businessdirectory.activity.AddCompanyActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CategoryPagerAdapter pagerAdapter;


    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int LOCATION_PERMISSION_CODE = 101;


    private List<Company> allCompanies = new ArrayList<>();


    private static final String[] TAB_TITLES = {
            "Сервиси", "Забава", "Индустрија", "Едукација"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupToolbar();
        setupViewPagerWithTabs();
        requestLocationPermission();
        fetchAllCompaniesForProximity();
    }


    private void initViews() {
        toolbar   = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCompanyActivity.class);
            startActivity(intent);
        });
    }


    private void setupViewPagerWithTabs() {
        pagerAdapter = new CategoryPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(TAB_TITLES[position])
        ).attach();


        viewPager.setOffscreenPageLimit(1);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            // Отвори го екранот за додавање компанија
            Intent intent = new Intent(this, AddCompanyActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void requestLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            startLocationUpdates();
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this,
                        "GPS дозволата е потребна за близина на компании",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private void startLocationUpdates() {
        locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // Се повикува кога GPS ќе добие нова локација
                checkProximityToCompanies(location);
            }
        };


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,   // минимум 5 секунди меѓу ажурирања
                        1f,     // минимум 1 метар поместување
                        locationListener
                );
            }
        }
    }


    private void checkProximityToCompanies(Location userLocation) {
        for (Company company : allCompanies) {
            if (LocationHelper.isNearby(userLocation, company)) {
                Toast.makeText(this,
                        getString(R.string.nearby_prefix) + company.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void fetchAllCompaniesForProximity() {
        ApiService api = ApiClient.getApiService();


        api.getAllCompanies().enqueue(new Callback<List<Company>>() {
            @Override
            public void onResponse(@NonNull Call<List<Company>> call,
                                   @NonNull Response<List<Company>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allCompanies.clear();
                    allCompanies.addAll(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Company>> call,
                                  @NonNull Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}