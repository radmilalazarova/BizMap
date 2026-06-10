package com.radmila.businessdirectory.location;

import android.location.Location;
import com.radmila.businessdirectory.model.Company;

public class LocationHelper {

    private static final float PROXIMITY_RADIUS_METERS = 50f;

    private LocationHelper() {}

    /**
     * Пресметај метарска оддалеченост меѓу две GPS точки.
     * Го користи Haversine алгоритамот внатрешно (Android SDK).
     */
    public static float distanceBetween(double lat1, double lon1,
                                        double lat2, double lon2) {
        float[] result = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, result);
        return result[0]; // во метри
    }

    /**
     * Врати true ако корисникот е на помалку од 50м
     * до дадената компанија.
     */
    public static boolean isNearby(Location userLocation, Company company) {
        if (userLocation == null) return false;
        if (company.getLatitude() == 0 && company.getLongitude() == 0) {
            return false;
        }

        float distance = distanceBetween(
                userLocation.getLatitude(),
                userLocation.getLongitude(),
                company.getLatitude(),
                company.getLongitude()
        );

        return distance < PROXIMITY_RADIUS_METERS;
    }
}