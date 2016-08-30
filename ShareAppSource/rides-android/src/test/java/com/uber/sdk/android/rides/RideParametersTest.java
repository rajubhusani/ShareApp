/*
 * Copyright (c) 2016 Uber Technologies, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.uber.sdk.android.rides;

import android.os.Parcel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link RideParameters}
 */
public class RideParametersTest extends RobolectricTestBase {

    public static final String PRODUCT_ID = "productId";
    private static final Double PICKUP_LAT = 32.1234;
    private static final Double PICKUP_LONG = -122.3456;
    private static final String PICKUP_NICK = "pickupNick";
    private static final String PICKUP_ADDR = "Pickup Address";
    private static final Double DROPOFF_LAT = 32.5678;
    private static final Double DROPOFF_LONG = -122.6789;
    private static final String DROPOFF_NICK = "pickupNick";
    private static final String DROPOFF_ADDR = "Dropoff Address";

    @Test
    public void onBuildRideParams_whenNothingSet_shouldHaveDefaults() {
        RideParameters rideParameters = new RideParameters.Builder().build();

        assertDefaults(rideParameters);
    }

    @Test
    public void onBuildRideParams_whenAllSet_shouldBeFilled() {
        RideParameters rideParameters = new RideParameters.Builder()
                .setProductId(PRODUCT_ID)
                .setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
                .build();

        assertFalse(rideParameters.isPickupMyLocation());
        assertEquals("Product ID does not match.", PRODUCT_ID, rideParameters.getProductId());

        assertEquals("Pickup latitude does not match.", Double.valueOf(PICKUP_LAT), rideParameters.getPickupLatitude());
        assertEquals("Pickup longitude does not match.", Double.valueOf(PICKUP_LONG),
                rideParameters.getPickupLongitude());
        assertEquals("Pickup nickname does not match.", PICKUP_NICK, rideParameters.getPickupNickname());
        assertEquals("Pickup address does not match.", PICKUP_ADDR, rideParameters.getPickupAddress());

        assertEquals("Dropoff latitude does not match.", Double.valueOf(DROPOFF_LAT),
                rideParameters.getDropoffLatitude());
        assertEquals("Dropoff longitude does not match.", Double.valueOf(DROPOFF_LONG),
                rideParameters.getDropoffLongitude());
        assertEquals("Dropoff nickname does not match.", DROPOFF_NICK, rideParameters.getDropoffNickname());
        assertEquals("Dropoff address does not match.", DROPOFF_ADDR, rideParameters.getDropoffAddress());
    }

    @Test
    public void onBuildRideParams_whenJustSetToMyLocation_shouldEqualDefaults() {
        RideParameters rideParameters = new RideParameters.Builder().setPickupToMyLocation().build();

        assertDefaults(rideParameters);
    }

    @Test
    public void onBuildRideParams_whenSetPickupLocationAndThenPickupToMyLocation_shouldHavePickupAsMyLocation() {
        RideParameters rideParameters = new RideParameters.Builder()
                .setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
                .setPickupToMyLocation()
                .build();

        assertDefaults(rideParameters);
    }

    @Test
    public void onBuildRideParams_whenSetPickupToMyLocationAndThenAPickupLocation_shouldHavePickupAsMyLocation() {
        RideParameters rideParameters = new RideParameters.Builder()
                .setPickupToMyLocation()
                .setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
                .build();

        assertFalse(rideParameters.isPickupMyLocation());
        assertNull(rideParameters.getProductId());

        assertEquals("Pickup latitude does not match.", Double.valueOf(PICKUP_LAT), rideParameters.getPickupLatitude());
        assertEquals("Pickup longitude does not match.", Double.valueOf(PICKUP_LONG),
                rideParameters.getPickupLongitude());
        assertEquals("Pickup nickname does not match.", PICKUP_NICK, rideParameters.getPickupNickname());
        assertEquals("Pickup address does not match.", PICKUP_ADDR, rideParameters.getPickupAddress());

        assertNull(rideParameters.getDropoffLatitude());
        assertNull(rideParameters.getDropoffLongitude());
        assertNull(rideParameters.getDropoffNickname());
        assertNull(rideParameters.getDropoffAddress());
    }

    @Test
    public void writeToParcelAndCreateFromParcel_withFullDetails_shouldWork() {
        Parcel parcel = Parcel.obtain();
        RideParameters rideParameters = new RideParameters.Builder()
                .setProductId(PRODUCT_ID)
                .setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
                .build();

        rideParameters.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        RideParameters rideParametersFromParcel = new RideParameters(parcel);
        assertFalse(rideParametersFromParcel.isPickupMyLocation());
        assertEquals("Product ID does not match.", PRODUCT_ID, rideParametersFromParcel.getProductId());

        assertEquals("Pickup latitude does not match.", Double.valueOf(PICKUP_LAT),
                rideParametersFromParcel.getPickupLatitude());
        assertEquals("Pickup longitude does not match.", Double.valueOf(PICKUP_LONG),
                rideParametersFromParcel.getPickupLongitude());
        assertEquals("Pickup nickname does not match.", PICKUP_NICK, rideParametersFromParcel.getPickupNickname());
        assertEquals("Pickup address does not match.", PICKUP_ADDR, rideParametersFromParcel.getPickupAddress());

        assertEquals("Dropoff latitude does not match.", Double.valueOf(DROPOFF_LAT),
                rideParametersFromParcel.getDropoffLatitude());
        assertEquals("Dropoff longitude does not match.", Double.valueOf(DROPOFF_LONG),
                rideParametersFromParcel.getDropoffLongitude());
        assertEquals("Dropoff nickname does not match.", DROPOFF_NICK, rideParametersFromParcel.getDropoffNickname());
        assertEquals("Dropoff address does not match.", DROPOFF_ADDR, rideParametersFromParcel.getDropoffAddress());
    }

    @Test
    public void writeToParcelAndCreateFromParcel_withDefaults_shouldWork() {
        Parcel parcel = Parcel.obtain();
        RideParameters rideParameters = new RideParameters.Builder().build();

        rideParameters.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        RideParameters rideParametersFromParcel = new RideParameters(parcel);
        assertDefaults(rideParametersFromParcel);
    }

    @Test
    public void whenUserAgentIsPassedAfterBuild_userAgentIsSet() {
        RideParameters rideParameters = new RideParameters.Builder().build();

        String userAgent = "USER_AGENT";
        rideParameters.setUserAgent(userAgent);
        assertEquals(userAgent, rideParameters.getUserAgent());
    }

    private void assertDefaults(RideParameters rideParameters) {
        assertTrue(rideParameters.isPickupMyLocation());
        assertNull(rideParameters.getProductId());

        assertNull(rideParameters.getPickupLatitude());
        assertNull(rideParameters.getPickupLongitude());
        assertNull(rideParameters.getPickupNickname());
        assertNull(rideParameters.getPickupAddress());

        assertNull(rideParameters.getDropoffLatitude());
        assertNull(rideParameters.getDropoffLongitude());
        assertNull(rideParameters.getDropoffNickname());
        assertNull(rideParameters.getDropoffAddress());

        assertNull(rideParameters.getUserAgent());
    }
}
