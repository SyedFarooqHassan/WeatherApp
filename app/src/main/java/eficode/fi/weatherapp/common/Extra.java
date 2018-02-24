package eficode.fi.weatherapp.common;

public abstract class Extra {
    public static final int LOCATION = 1;

    public static final String EFICODE_API_BASE_URL = "https://weatherapp.eficode.fi/";
    public static final String GOOGLE_API_BASE_URL = "http://maps.googleapis.com/maps/";
    public static final String IMAGE_URL = EFICODE_API_BASE_URL + "img/";
    public static final String IMAGE_FORMAT = ".svg";
    public static final String PACKAGE = "package";
    public static final int MIN_TIME = 60000;
    public static final int MIN_DISTANCE = 0;
    public static final String BACK_INTENT_EXTRA = "backIntent";
    public static final int SET_RESULT = 1;
    public static final String LOCATION_PROVIDER = "googelProvider";

    public static final String LOCATION_INFO_LIST = "LOCATION_INFO_LIST";
    public static final String LOCATION_INFO_SELECTED_INDEX = "LOCATION_INFO_SELECTED_INDEX";
    public static final String LOCATION_INFO = "LOCATION_INFO";
}
