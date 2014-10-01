package io.github.jiayochen.sunshine.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by jiayochen on 9/29/2014.
 */
public class WeatherProvider extends ContentProvider {

    public static final int WEATHER = 100;
    public static final int WEATHER_WITH_LOCATION = 101;
    public static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    public static final int LOCATION = 300;
    public static final int LOCATION_ID = 301;

    static UriMatcher sUriMatcher = buildUriMatcher();

    private WeatherDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.BASE_CONTENT_URI + "/" + WeatherContract.PATH_WEATHER, WEATHER);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.BASE_CONTENT_URI + "/" + WeatherContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.BASE_CONTENT_URI + "/" + WeatherContract.PATH_WEATHER + "/*/*", WEATHER_WITH_LOCATION_AND_DATE);

        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.BASE_CONTENT_URI + "/" + WeatherContract.PATH_LOCATION, LOCATION);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.BASE_CONTENT_URI + "/" + WeatherContract.PATH_LOCATION + "/*", LOCATION_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER_WITH_LOCATION_AND_DATE:
                return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_LOCATION:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case WEATHER:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case LOCATION:
                return WeatherContract.LocationEntry.CONTENT_TYPE;
            case LOCATION_ID:
                return WeatherContract.LocationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}