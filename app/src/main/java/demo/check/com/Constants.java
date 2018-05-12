package demo.check.com;

public class Constants {

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    // The minimum distance to change Updates in meters
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BW_UPDATES = 1000 * 2; //2 Seconds

    public static final long updateLocationToFBHandlerTime = 1000 * 10; //10 Seconds

    //gps turn on
    public static final int REQUEST_CHECK_SETTINGS = 0x1;

    public static final long SET_INTERVAL = 5000; //5 Seconds
    public static final long SET_FASTESTINTERVAL = 3000; //3 Seconds
    public static int GET_ZOOM_TIME = 4000;

    //Map Zooming Size
    public static final float MAP_ZOOM_SIZE = 14;

    public static final float MAP_ZOOM_SIZE_ONTRIP = 17;


    public static String GoogleDirectionApi = "AIzaSyAUYGW-EfTxrg5d4yYiEuRV8aRdtKSgriA";
}
