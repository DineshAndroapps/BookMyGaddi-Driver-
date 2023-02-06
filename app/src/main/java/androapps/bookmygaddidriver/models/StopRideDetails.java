package androapps.bookmygaddidriver.models;

/**
 * Created by ADMIN on 25-01-2017.
 */
public class StopRideDetails {
    String name,email,mobile,source_latitude,source_longitude,destinaion_latitude,destinaion_longitude,time,distance,fare="0",fare_with_tax="0";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFare() {
        return fare_with_tax;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }
}
