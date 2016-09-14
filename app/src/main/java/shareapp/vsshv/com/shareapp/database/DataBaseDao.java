package shareapp.vsshv.com.shareapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shareapp.vsshv.com.shareapp.datasets.GmailSet;
import shareapp.vsshv.com.shareapp.datasets.TwitterSet;
import shareapp.vsshv.com.shareapp.datasets.UberDataSet;

import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_BODY;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_DESTINATION;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_DROP;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_ID;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_MESSAGE;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_PCIK;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_PROFILEIMG;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_RIDETYPE;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_SCEDULEDTIME;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_SOURCE;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_STATUS;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_SUBJECT;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_TOMAIL;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_UNIQUE;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_USERMAIL;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_USERNAME;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.TABLE_GMAIL;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.TABLE_TWITTER;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.TABLE_UBER;

public class DataBaseDao {

	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;

	private String[] allColumns_SYNC = { SHARE_COLUMN_ID,
			SHARE_COLUMN_USERNAME,
			SHARE_COLUMN_PROFILEIMG,
			SHARE_COLUMN_MESSAGE,
			SHARE_COLUMN_USERMAIL,
			SHARE_COLUMN_SCEDULEDTIME,
			SHARE_COLUMN_STATUS,
			SHARE_COLUMN_UNIQUE};

	private String[] allColumns_G_SYNC = { SHARE_COLUMN_ID,
			SHARE_COLUMN_SUBJECT,
			SHARE_COLUMN_BODY,
			SHARE_COLUMN_TOMAIL,
			SHARE_COLUMN_SCEDULEDTIME,
			SHARE_COLUMN_STATUS,
			SHARE_COLUMN_UNIQUE};

	private String[] allColumns_U_SYNC = { SHARE_COLUMN_ID,
			SHARE_COLUMN_DESTINATION,
			SHARE_COLUMN_DROP,
			SHARE_COLUMN_SOURCE,
			SHARE_COLUMN_PCIK,
			SHARE_COLUMN_SCEDULEDTIME,
			SHARE_COLUMN_RIDETYPE,
			SHARE_COLUMN_STATUS,
			SHARE_COLUMN_UNIQUE
	};


	public DataBaseDao(Context context) {
		dbHelper = new DataBaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		if (null != dbHelper)
			dbHelper.close();
	}

	public synchronized void insertTwitterMessages(TwitterSet set)
	{
		try {
			open();
			ContentValues contentValues = new ContentValues();
			contentValues.put(SHARE_COLUMN_UNIQUE, set.get_id());
			contentValues.put(SHARE_COLUMN_MESSAGE, set.getMessage());
			contentValues.put(SHARE_COLUMN_SCEDULEDTIME, set.getScheduled());
			contentValues.put(SHARE_COLUMN_STATUS, set.getStatus());
			long row = database.insert(DataBaseHelper.TABLE_TWITTER, null, contentValues);

			Log.d("===============", "Insert:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}
	}

	public synchronized void updateTwitterMessage(int status, int _id)
	{
		try {
			open();
			ContentValues contentValues = new ContentValues();
			contentValues.put(SHARE_COLUMN_STATUS, status);
			long row = database.update(DataBaseHelper.TABLE_TWITTER, contentValues, SHARE_COLUMN_UNIQUE+" = ?",
					new String[]{String.valueOf(_id)});

			Log.d("===============", "Update:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}
	}

	public synchronized long removeTMessage(int _id) {
		long row = -1;
		try {
			open();
			row = database.delete(TABLE_TWITTER, SHARE_COLUMN_UNIQUE+" = ?", new String[]{String.valueOf(_id)});
			Log.d("===============", "Delete:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}

		return row;
	}
	/**
	 * 
	 * @return
	 */
	public List<TwitterSet> getAllTMessages() {
		List<TwitterSet> typeList = new ArrayList<TwitterSet>();

		try {
			open();
			Cursor cursor = database.query(DataBaseHelper.TABLE_TWITTER,
					allColumns_SYNC, null, null, null, null, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					TwitterSet comment = cursorToTypes(cursor);
					typeList.add(comment);
					cursor.moveToNext();
				}

			}
			cursor.close();
		} catch (Exception e) {
			Log.e("Error", "" + e.toString());
			
		}finally{
			close();
		}

		return typeList;
	}

	private TwitterSet cursorToTypes(Cursor cursor) {
		TwitterSet set = new TwitterSet();
		set.set_id(cursor.getInt(cursor.getColumnIndex(SHARE_COLUMN_UNIQUE)));
		set.setMessage(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_MESSAGE)));
		set.setScheduled(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_SCEDULEDTIME)));
		set.setStatus(cursor.getInt(cursor.getColumnIndex(SHARE_COLUMN_STATUS)));
		return set;
	}

	public synchronized void insertGmailMessages(GmailSet set)
	{
		try {
			open();
			ContentValues contentValues = new ContentValues();
			contentValues.put(SHARE_COLUMN_UNIQUE, set.get_id());
			contentValues.put(SHARE_COLUMN_SUBJECT, set.getSubject());
			contentValues.put(SHARE_COLUMN_BODY, set.getBody());
			contentValues.put(SHARE_COLUMN_TOMAIL, set.getToMail());
			contentValues.put(SHARE_COLUMN_SCEDULEDTIME, set.getScheduled());
			contentValues.put(SHARE_COLUMN_STATUS, set.getStatus());
			long row = database.insert(DataBaseHelper.TABLE_GMAIL, null, contentValues);

			Log.d("===============", "Insert:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}
	}

	public synchronized void updateGmailMessage(int status, int _id)
	{
		try {
			open();
			ContentValues contentValues = new ContentValues();
			contentValues.put(SHARE_COLUMN_STATUS, status);
			long row = database.update(DataBaseHelper.TABLE_GMAIL, contentValues, SHARE_COLUMN_UNIQUE+" = ?",
					new String[]{String.valueOf(_id)});

			Log.d("===============", "Update:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}
	}

	public synchronized long removeGMessage(int _id) {
		long row = -1;
		try {
			open();
			row = database.delete(TABLE_GMAIL, SHARE_COLUMN_UNIQUE+" = ?", new String[]{String.valueOf(_id)});
			Log.d("===============", "Delete:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}

		return row;
	}

	private GmailSet gmailCursorToTypes(Cursor cursor) {
		GmailSet set = new GmailSet();
		set.set_id(cursor.getInt(cursor.getColumnIndex(SHARE_COLUMN_UNIQUE)));
		set.setBody(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_BODY)));
		set.setSubject(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_SUBJECT)));
		set.setToMail(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_TOMAIL)));
		set.setScheduled(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_SCEDULEDTIME)));
		set.setStatus(cursor.getInt(cursor.getColumnIndex(SHARE_COLUMN_STATUS)));
		return set;
	}

	/**
	 *
	 * @return
	 */
	public List<GmailSet> getAllGMessages() {
		List<GmailSet> typeList = new ArrayList<GmailSet>();

		try {
			open();
			Cursor cursor = database.query(DataBaseHelper.TABLE_GMAIL,
					allColumns_G_SYNC, null, null, null, null, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					GmailSet comment = gmailCursorToTypes(cursor);
					typeList.add(comment);
					cursor.moveToNext();
				}

			}
			cursor.close();
		} catch (Exception e) {
			Log.e("Error", "" + e.toString());

		}finally{
			close();
		}

		return typeList;
	}

	public synchronized void insertUberMessages(UberDataSet set)
	{
		try {
			open();
			ContentValues contentValues = new ContentValues();
			contentValues.put(SHARE_COLUMN_UNIQUE, set.get_id());
			contentValues.put(SHARE_COLUMN_SOURCE, set.getSourceAddress());
			contentValues.put(SHARE_COLUMN_DESTINATION, set.getDestinationAddress());
			contentValues.put(SHARE_COLUMN_PCIK, set.getPickUpNick());
			contentValues.put(SHARE_COLUMN_DROP, set.getDropOffNick());
			contentValues.put(SHARE_COLUMN_SCEDULEDTIME, set.getScheduled());
			contentValues.put(SHARE_COLUMN_RIDETYPE, set.getProductId());
			contentValues.put(SHARE_COLUMN_STATUS, set.getStatus());
			long row = database.insert(DataBaseHelper.TABLE_UBER, null, contentValues);

			Log.d("===============", "Insert:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}
	}

	public synchronized long removeUMessage(int _id) {
		long row = -1;
		try {
			open();
			row = database.delete(TABLE_UBER, SHARE_COLUMN_UNIQUE+" = ?", new String[]{String.valueOf(_id)});
			Log.d("===============", "Delete:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}

		return row;
	}

	private UberDataSet uberCursorToTypes(Cursor cursor) {
		UberDataSet set = new UberDataSet();
		set.set_id(cursor.getInt(cursor.getColumnIndex(SHARE_COLUMN_UNIQUE)));
		set.setSourceAddress(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_SOURCE)));
		set.setDestinationAddress(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_DESTINATION)));
		set.setPickUpNick(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_PCIK)));
		set.setScheduled(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_SCEDULEDTIME)));
		set.setDropOffNick(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_DROP)));
		set.setProductId(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_RIDETYPE)));
		set.setStatus(cursor.getInt(cursor.getColumnIndex(SHARE_COLUMN_STATUS)));
		return set;
	}

	/**
	 *
	 * @return
	 */
	public List<UberDataSet> getAllUMessages() {
		List<UberDataSet> typeList = new ArrayList<UberDataSet>();

		try {
			open();
			Cursor cursor = database.query(DataBaseHelper.TABLE_UBER,
					allColumns_U_SYNC, null, null, null, null, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					UberDataSet comment = uberCursorToTypes(cursor);
					typeList.add(comment);
					cursor.moveToNext();
				}

			}
			cursor.close();
		} catch (Exception e) {
			Log.e("Error", "" + e.toString());

		}finally{
			close();
		}

		return typeList;
	}

}