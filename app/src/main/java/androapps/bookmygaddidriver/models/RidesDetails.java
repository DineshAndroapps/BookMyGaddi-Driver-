package androapps.bookmygaddidriver.models;

import java.io.Serializable;

/**
 * Created by ADMIN on 17-01-2017.
 */
public class RidesDetails implements Serializable{
    String name;
    String user_id;
    String source_latitude;
    String source_longitude;
    String destinaion_latitude;
    String destinaion_longitude;
    String source;
    String destination;
    String mobile;
    String vehicle_category;
    String ride_status;
    String ride_type;
    String airport_type;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    String booking_id;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }



    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    String added_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSource_latitude() {
        return source_latitude;
    }

    public void setSource_latitude(String source_latitude) {
        this.source_latitude = source_latitude;
    }

    public String getSource_longitude() {
        return source_longitude;
    }

    public void setSource_longitude(String source_longitude) {
        this.source_longitude = source_longitude;
    }

    public String getDestinaion_latitude() {
        return destinaion_latitude;
    }

    public void setDestinaion_latitude(String destinaion_latitude) {
        this.destinaion_latitude = destinaion_latitude;
    }

    public String getDestinaion_longitude() {
        return destinaion_longitude;
    }

    public void setDestinaion_longitude(String destinaion_longitude) {
        this.destinaion_longitude = destinaion_longitude;
    }

    public String getVehicle_category() {
        return vehicle_category;
    }

    public void setVehicle_category(String vehicle_category) {
        this.vehicle_category = vehicle_category;
    }

    public String getRide_status() {
        return ride_status;
    }

    public void setRide_status(String ride_status) {
        this.ride_status = ride_status;
    }

    public String getRide_type() {
        return ride_type;
    }

    public String getAirport_type() {
        return airport_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
    }
}
