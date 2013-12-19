package Entity;

/**
 *  Store global settings (eg, version number) here.
 * Note: renaming/moving this class requires adaptions to the build script.
 * @author Antonius
 */
public class GlobalSettings {
    public static boolean debugOutput = false;
    public static boolean debugLearning = false;
    public static boolean debugDecision = false;
    public static String inputPredicates = "";
    
    public static boolean debugHelper = false;
    
    public static int decisionCounter = 0;
    public static int maxDecisionLevel = 0;
    
    public static boolean noLearning = false;
    public static boolean didLearn = false;
    public static boolean initialReteBuilding = true;   // indicates whether Rete nodes are added from learning new rules.
    
    static GlobalSettings instance = null;
    
    // below string is replaced by build-script automatically,
    // do not change line breaking
    public static String version="685838df1372e5123c0982c27fbc7b39284d840a";

    /**
     * Parse string of comma-separated debug channels and activate all channels.
     * @param debugChannels 
     */
    public static void setDebugChannels(String debugChannels) {
        String[] channel = debugChannels.split(",");
        for (int i = 0; i < channel.length; i++) {
            if( channel[i].equals("output")) {
                debugOutput = true;
            } else if( channel[i].equals("decision")) {
                debugDecision = true;
            } else if( channel[i].equals("learning")) {
                debugLearning = true;
            } else if( channel[i].equals("helper")) {
                debugHelper = true;
            }
        }
    }
    
}
