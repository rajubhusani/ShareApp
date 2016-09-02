package shareapp.vsshv.com.shareapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PC414506 on 31/08/16.
 */

public class DataBaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "ShareApp.db";

    public static final String SHARE_COLUMN_ID = "id";
    public static final String SHARE_COLUMN_APPNAME = "appname";
    public static final String SHARE_COLUMN_USERNAME = "username";
    public static final String SHARE_COLUMN_USERMAIL = "useremail";
    public static final String SHARE_COLUMN_MESSAGE = "message";
    public static final String SHARE_COLUMN_BODY = "body";
    public static final String SHARE_COLUMN_TOMAIL = "toMail";
    public static final String SHARE_COLUMN_PROFILEIMG = "imageUrl";
    public static final String SHARE_COLUMN_SOURCE = "source";
    public static final String SHARE_COLUMN_DESTINATION = "destination";
    public static final String SHARE_COLUMN_LAT = "lat";
    public static final String SHARE_COLUMN_LNG = "lng";
    public static final String SHARE_COLUMN_RIDETYPE = "rideType";
    public static final String SHARE_COLUMN_SCEDULEDTIME = "scheduled";

    public static final String TABLE_TWITTER = "Twitter";
    public static final String TABLE_UBER = "Uber";
    public static final String TABLE_GMAIL = "Gmail";

    private static final String TABLE_T_CREATE = "create table "+TABLE_TWITTER+"(id integer primary key, username text, useremail text," +
            "message text, imageUrl text, scheduled text)";
    private static final String TABLE_U_CREATE = "create table "+TABLE_UBER+"(id integer primary key, username text, useremail text," +
            "source text, imageUrl text, destination text, lat text, lng text, rideType text)";
    private static final String TABLE_G_CREATE = "create table "+TABLE_GMAIL+"(id integer primary key, username text, useremail text," +
            "message text, imageUrl text)";


    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_T_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
