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
    
    public static boolean debugHelper = false;
    
    public static int decisionCounter = 0;
    public static int maxDecisionLevel = 0;
    
    public static boolean noLearning = false;
    public static boolean didLearn = false;
    public static boolean initialReteBuilding = true;   // indicates whether Rete nodes are added from learning new rules.
    
    static GlobalSettings instance = null;
    
    // below string is replaced by build-script automatically,
    // do not change line breaking
    public static String version="6c7ae86de70ca8b961f2e08d314634ecc6a88ca4";
    
}
