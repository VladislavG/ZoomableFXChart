package solrTest;

/**
 * Place for shared information among client and server. Typically identifiers for models, attributes and actions.
 */
public class ApplicationConstants {
    public static final String PM_APP = unique("APP");
    public static final String COMMAND_ID = unique("DO");
    public static final String STATE = "state";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String QUERY = "query";
    public static final String PRICE = "price";
    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String DATE = "date";
    public static final String SPIKE = "spike";
    public static final String SERIES = "series";
    public static final String DATEENTRY = "dateEntry";
    public static final String DISABLED = "disabled";


    /**
     * Unify the identifier with the class name prefix.
     */
    private static String unique(String key) {
        return ApplicationConstants.class.getName() + "." + key;
    }

}
