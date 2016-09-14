package shareapp.vsshv.com.shareapp.datasets;

import java.io.Serializable;

/**
 * Created by PC414506 on 12/09/16.
 */

public class UberDataSet implements Serializable{
    private int mHour = 0;
    private int mMinute = 0;
    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;
    private String sourceAddress = null;
    private String destinationAddress = null;
    private double sourceLat;
    private double sourceLng;
    private double destLat;
    private double destLng;
    private String productId = null;
    private String pickUpNick;
    private String dropOffNick;
    private String scheduled;
    private int _id;
    private int status;

    public int getmHour() {
        return mHour;
    }

    public int getmMinute() {
        return mMinute;
    }

    public int getmYear() {
        return mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getProductId() {
        return productId;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public void setmMinute(int mMinute) {
        this.mMinute = mMinute;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getSourceLat() {
        return sourceLat;
    }

    public double getSourceLng() {
        return sourceLng;
    }

    public double getDestLat() {
        return destLat;
    }

    public double getDestLng() {
        return destLng;
    }

    public void setSourceLat(double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public void setSourceLng(double sourceLng) {
        this.sourceLng = sourceLng;
    }

    public void setDestLat(double destLat) {
        this.destLat = destLat;
    }

    public void setDestLng(double destLng) {
        this.destLng = destLng;
    }

    public String getPickUpNick() {
        return pickUpNick;
    }

    public String getDropOffNick() {
        return dropOffNick;
    }

    public void setPickUpNick(String pickUpNick) {
        this.pickUpNick = pickUpNick;
    }

    public void setDropOffNick(String dropOffNick) {
        this.dropOffNick = dropOffNick;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
