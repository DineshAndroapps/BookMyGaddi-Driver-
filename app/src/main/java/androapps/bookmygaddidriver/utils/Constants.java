package androapps.bookmygaddidriver.utils;

/**
 * Created by ADMIN on 26-12-2016.
 */
public class Constants {

    public static int Twentyfive_seconds = 25000;

    public static final String tokenheader = "$6$rounds=10000$jxTkX87qFnpaNt7d$3JFq352IbmjPzqneFJRM2d.psZ/GEVdCNfVBNt8bysiA5/dDdZ2cfaYTf4dZ8plWJw01w9.Iv5X.l8UBgXkqr/";


    public class APICodes {
        public static final int LOGIN=1001;
        public static final int FORGOT_PASSWORD=1002;
        public static final int MOBILE_VERIFICATION=1003;
        public static final int GET_RIDES=1004;
        public static final int CANCELED_RIDE=1005;
        public static final int COMPLETED_RIDE=1006;
        public static final int START_RIDE=1007;
        public static final int STOP_RIDE=1008;
        public static final int SAVE_GCM=1009;
        public static final int UPDATE_LATLAN=1010;
        public static final int MY_PROFILE=1011;
        public static final int TERMS=1012;

    }

    public class Urls{

//        public static final String BASE_URL="http://139.59.24.177/book_my_gaddiv2/webservices/driver/";
//        public static final String BASE_URL="http://139.59.24.177/book_my_gaddiv3/webservices/driver/";

//        public static final String BASE_URL="http://139.59.24.177/book_my_gaddi/webservices/driver/";

//        public static final String BASE_URL="http://139.59.24.177/book_my_gaddi9march/webservices/driver/";
//        http://159.65.154.4/book_my_gaddi9march/webservices/driver/login.php

//        public static final String BASE_URL="http://159.65.154.4/book_my_gaddi9march/webservices/driver/login.php";
        public static final String BASE_URL="http://159.65.154.4/bookmygaddiapi/webservices/driver/";


        public static final String LOGIN=BASE_URL+"login.php";
        public static final String FORGOT_PASSWORD=BASE_URL+"forget_password.php";
        public static final String MOBILE_VERIFICATION=BASE_URL+"mobile_verification.php";
        public static final String GET_RIDES=BASE_URL+"getLatestBooking.php";
        public static final String CANCELED_RIDE=BASE_URL+"cancel_booking.php";
        public static final String COMPLETED_RIDE=BASE_URL+"completed_booking.php";
        public static final String START_RIDE=BASE_URL+"startRide.php";
        public static final String STOP_RIDE=BASE_URL+"stopRide.php";
        public static final String SAVE_GCM=BASE_URL+"save_gcm_number.php";
        public static final String UPDATE_LATLAN=BASE_URL+"updatedriver_latlong.php";
        public static final String MY_PROFILE=BASE_URL+"viewprofile.php";
        public static final String TERMS=BASE_URL+"term_conditions.php";
        public static final String CONTACT_US=BASE_URL+"contact_us.php";
        public static final String Booking_details=BASE_URL+"booking_detail.php";
        public static final String Driver_availability=BASE_URL+"driver_availability.php";
        public static final String Driver_unAvailability=BASE_URL+"driver_unavailability.php";
        public static final String mobileverifivation=BASE_URL+"mobile_verification.php";
        public static final String forgotpassword=BASE_URL+"forget_password.php";

    }


    public class APIKeys{
        public static final String IS_SUCCESS="isSuccess";
        public static final String CODE="code";
        public static final String MESSAGE="message";
        public static final String sender_id = "296951689887";
    }

    public class Codes{

    }

}
