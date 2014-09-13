package io.github.jiayochen.sunshine.app.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;
import android.util.Log;

import io.github.jiayochen.sunshine.app.data.WeatherContract.LocationEntry;
import io.github.jiayochen.sunshine.app.data.WeatherContract.WeatherEntry;
import io.github.jiayochen.sunshine.app.data.WeatherDbHelper;

/**
 * Created by jiayochen on 9/12/2014.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {
        String testName = "North Pole";
        String testLocationSetting = "99705";
        double testLatitude = 64.772;
        double testLongitude = -147.355;

        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_CITY_NAME, testName);
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);

        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        String[] columns = {
            LocationEntry._ID,
            LocationEntry.COLUMN_LOCATION_SETTING,
            LocationEntry.COLUMN_CITY_NAME,
            LocationEntry.COLUMN_COORD_LAT,
            LocationEntry.COLUMN_COORD_LONG
        };

        Cursor cursor = db.query(
                LocationEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            int locationIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);

            int nameIndex = cursor.getColumnIndex(LocationEntry.COLUMN_CITY_NAME);
            String name = cursor.getString(nameIndex);

            int latIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LAT);
            double latitude = cursor.getDouble(latIndex);

            int longIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LONG);
            double longitude = cursor.getDouble(longIndex);

            assertEquals(testName, name);
            assertEquals(testLocationSetting, location);
            assertEquals(testLatitude, latitude);
            assertEquals(testLongitude, longitude);

            //test for weather data

            String testDateText = "20141205";
            double testDegrees = 1.1;
            double testHumidity = 1.2;
            double testPressure = 1.3;
            double testMaxTemp = 75;
            double testMinTemp = 65;
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

            long weatherRowId;
            weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);

            Cursor weatherCursor = db.query(
                    WeatherEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            if (weatherCursor.moveToFirst()) {
                int locFkIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_LOC_KEY);
                long locFk = weatherCursor.getLong(locFkIndex);

                int dateTextIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_DATETEXT);
                String dateText = weatherCursor.getString(dateTextIndex);

                int shortDescIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC);
                String shortDesc = weatherCursor.getString(shortDescIndex);

                int weatherIdIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID);
                int weatherId = weatherCursor.getInt(weatherIdIndex);

                int minTempIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP);
                double minTemp = weatherCursor.getDouble(minTempIndex);

                int maxTempIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP);
                double maxTemp = weatherCursor.getDouble(maxTempIndex);

                int humidityIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY);
                double humidity = weatherCursor.getDouble(humidityIndex);

                int pressureIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_PRESSURE);
                double pressure = weatherCursor.getDouble(pressureIndex);

                int windSpeedIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED);
                double windSpeed = weatherCursor.getDouble(windSpeedIndex);

                int degreesIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_DEGREES);
                double degrees = weatherCursor.getDouble(degreesIndex);

                assertEquals(locFk, locationRowId);
                assertEquals(testDateText, dateText);
                assertEquals(testShortDesc, shortDesc);
                assertEquals(testWeatherId, weatherId);
                assertEquals(testMinTemp, minTemp);
                assertEquals(testMaxTemp, maxTemp);
                assertEquals(testHumidity, humidity);
                assertEquals(testPressure, pressure);
                assertEquals(testWindSpeed, windSpeed);
                assertEquals(testDegrees, degrees);

            }
            else {
                fail("No values returned from weather db table.");
            }

            dbHelper.close();
        }
        else {
            fail("No values returned from location db table. :(");
        }

    }
}
