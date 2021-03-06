package io.github.jiayochen.sunshine.app.test;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import io.github.jiayochen.sunshine.app.data.WeatherContract;
import io.github.jiayochen.sunshine.app.data.WeatherContract.LocationEntry;
import io.github.jiayochen.sunshine.app.data.WeatherContract.WeatherEntry;
import io.github.jiayochen.sunshine.app.data.WeatherDbHelper;

/**
 * Created by jiayochen on 9/12/2014.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }
    public void testGetType() {
        // content://com.example.android.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String testLocation = "94074";
        // content://com.example.android.sunshine.app/weather/94074
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocation(testLocation));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String testDate = "20140612";
        // content://com.example.android.sunshine.app/weather/94074/20140612
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocationWithDate(testLocation, testDate));
        // vnd.android.cursor.item/com.example.android.sunshine.app/weather
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);

        // content://com.example.android.sunshine.app/location/
        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/location
        assertEquals(LocationEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/location/1
        type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
        // vnd.android.cursor.item/com.example.android.sunshine.app/location
        assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
    }

    public static final String TEST_CITY_NAME = "North Pole";
    public static final String TEST_LOCATION = "99705";
    public static final String TEST_DATE = "20141205";

    private ContentValues getLocationContentValues() {
        String testName = TEST_CITY_NAME;
        String testLocationSetting = TEST_LOCATION;
        double testLatitude = 64.772;
        double testLongitude = -147.355;
        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_CITY_NAME, testName);
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);

        return values;

    }

    private ContentValues getWeatherContentValues(long locationRowId) {
        String testDateText = TEST_DATE;
        double testDegrees = 1.1;
        double testHumidity = 1.2;
        double testPressure = 1.3;
        int testMaxTemp = 75;
        int testMinTemp = 65;
        String testShortDesc = "Asteroids";
        double testWindSpeed = 5.5;
        int testWeatherId = 321;

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, testDateText);
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, testDegrees);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, testHumidity);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, testPressure);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, testMaxTemp);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, testMinTemp);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, testShortDesc);
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, testWindSpeed);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, testWeatherId);

        return weatherValues;
    }

    static void validateCursor(ContentValues expectedValues, Cursor valueCursor) {
        //expectedValues is the original set of values
        //valueCursor contains the result from the DB
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(-1 == idx);
            String expectedValue = entry.getValue().toString();
            String resultFromDb = null;
            try {
                resultFromDb = valueCursor.getString(idx);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            assertEquals(expectedValue, resultFromDb);

        }
    }

    public void testInsertReadProvider() {

        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long locationRowId;

        ContentValues values = getLocationContentValues();

//        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);

        locationRowId = ContentUris.parseId(mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, values));

        Log.d(LOG_TAG, "New row id: " + locationRowId);

        Cursor cursor = mContext.getContentResolver().query( LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

/*
        Cursor cursor = db.query(
                LocationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
*/

        if (cursor.moveToFirst()) {
            validateCursor(values, cursor);

            cursor.close();
            cursor = mContext.getContentResolver().query( LocationEntry.buildLocationUri(locationRowId),
                    null,
                    null,
                    null,
                    null
            );

            if(cursor.moveToFirst()) {
                validateCursor(values, cursor);
            }

            cursor.close();

            //test for weather data
            ContentValues weatherValues = getWeatherContentValues(locationRowId);

            long weatherRowId;
//            weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);

            weatherRowId = ContentUris.parseId(mContext.getContentResolver().insert(WeatherEntry.CONTENT_URI, values));

            Cursor weatherCursor = mContext.getContentResolver().query(WeatherEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
/*
            Cursor weatherCursor = db.query(
                    WeatherEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
*/

            if (weatherCursor.moveToFirst()) {
                validateCursor(weatherValues, weatherCursor);
            }
            else {
                fail("No values returned from weather db table.");
            }

            weatherCursor.close();

            weatherCursor = mContext.getContentResolver().query(WeatherEntry.buildWeatherLocation(TEST_LOCATION),
                    null,
                    null,
                    null,
                    null);

            if (weatherCursor.moveToFirst()) {
                validateCursor(weatherValues, weatherCursor);
            }
            else {
                fail("No values returned from weather db table.");
            }
            weatherCursor.close();

            weatherCursor = mContext.getContentResolver().query(WeatherEntry.buildWeatherLocationWithStartDate(TEST_LOCATION, TEST_DATE),
                    null,
                    null,
                    null,
                    null);

            if (weatherCursor.moveToFirst()) {
                validateCursor(weatherValues, weatherCursor);
            }
            else {
                fail("No values returned from weather db table.");
            }
            weatherCursor.close();

            weatherCursor = mContext.getContentResolver().query(WeatherEntry.buildWeatherLocationWithDate(TEST_LOCATION, TEST_DATE),
                    null,
                    null,
                    null,
                    null);

            if (weatherCursor.moveToFirst()) {
                validateCursor(weatherValues, weatherCursor);
            }
            else {
                fail("No values returned from weather db table.");
            }
            weatherCursor.close();

            dbHelper.close();
        }
        else {
            fail("No values returned from location db table. :(");
        }

    }
}
