package Datastructure.storage;

import Entity.GlobalSettings;
import Entity.Instance;
import Entity.TrackingInstance;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Gerald Weidinger 0526105
 * @version 1.0
 * 
 * This class is our datastructure for storing instances. Basically it's an array of HashMaps with Terms as Keys and
 * HashSets of instances as values, and methods for insertion and removal into this structure. Herby references to instances
 * are saved into each HashMap of the Array, such that when making a selection, we can simply look into the corresponding
 * HashMap and directly get all instances that have a certain term at that position.
 * 
 * 
 */
public class Storage {
    
    public static long debugSelectTime = 0;
    public static int debugSelectCount = 0;
    public static long debugAddInstanceTime = 0;
    public static int debugAddInstanceCount = 0;
    
    public static int debugTotalInstances = 0;
    public static int debugOverallTotalInstances = 0;
    
    public int debugStorageSize;
    
    public int arity;
    
    //HashMap<Term,HashSet<Instance>>[] memory; // this is where we store our stuff
    TreeMap<Term,TreeSet<Instance>>[] memory;
    private HashMap<Integer,ArrayList<Instance>> backtrackInstances;    // stores instances for backtracking

    public HashMap<Integer, ArrayList<Instance>> getBacktrackInstances() {
        return backtrackInstances;
    }
    
    HashMap<Instance,Integer> backtrackMustBeTrue;  // maps mbt-instances to decision level the instance was added
    
    /**
     * Initializes the storage, constructs an memory array of size arity and fills it with HashMaps
     * 
     * @param arity The arity of the predicate this storage is used for
     */
    @SuppressWarnings("unchecked") // AW: workaround for array conversion
    public void initStorage(int arity){
        this.arity = arity;
        if ( arity == 0 ) { // 0-ary predicates need memory for true-false as empty instance
            //memory = new HashMap[1];
            memory = new TreeMap[1];
        } else {
            //this.memory = new HashMap[arity];
            memory = new TreeMap[arity];
        }
        for(int i = 0; i < memory.length;i++){
            //memory[i] = new HashMap<Term,HashSet<Instance>>();
            memory[i] = new TreeMap<Term,TreeSet<Instance>>();
        }
        backtrackInstances = new HashMap<Integer, ArrayList<Instance>>();
        backtrackMustBeTrue = new HashMap<Instance, Integer>();
        debugStorageSize = 0;
    }
    
    /**
     * This method adds an instance to our data structure in such a way, that the instance is inserted into each 
     * HashMap of our memory array, where the key is the corresponding Term of the instance at the position corresponding
     * to the position of the HashMap within the array.
     * 
     * @param instance the instance that shall be added into this data structure
     * 
     */
    public void addInstance(Instance instance){
        debugStorageSize++;
        debugOverallTotalInstances++;
        debugTotalInstances++;
        debugAddInstanceCount++;
        long debugStartTime = System.currentTimeMillis();
        if( GlobalSettings.debugOutput && instance.isMustBeTrue ) {
            System.out.println("MBT instance adding: "+instance);
        }
        registerForBacktracking(instance);
        for (int i = 0; i < instance.getSize(); i++) {

            if (!memory[i].containsKey(instance.get(i))) {
                //memory[i].put(instance.get(i), new HashSet<Instance>());
                memory[i].put(instance.get(i), new TreeSet<Instance>());
            }

            // overwrite old instance with new one
            // HashSet.add leaves old instance, remove it if necessary
            if (!memory[i].get(instance.get(i)).add(instance)) {
                memory[i].get(instance.get(i)).remove(instance);
                memory[i].get(instance.get(i)).add(instance);
            }
        }
        debugAddInstanceTime += (System.currentTimeMillis()-debugStartTime);
        //System.out.println(this + " Added: " + Instance.getInstanceAsString(instance));
    }
    
    
    /**
     * This method removes the instance from all HashMaps of our memory array.
     * 
     * Precondition: the structure for that instance must exist, otherwise we reach a null pointer exception
     * This method should only be called for instances for which we know they are within the data structure
     * 
     * @param instance an instance that is within our data structure memory that should be removed
     */
    
    private void removeInstance(Instance instance){
        debugStorageSize--;
        debugTotalInstances--;
        for(int i = 0; i < instance.getSize();i++){
            memory[i].get(instance.get(i)).remove(instance);
        }
    }
    
    /**
     * checks wether an instance is within our data structure or not
     * 
     * @param instance An instance for which we want to check if it is contained within our memory
     * @return true or false, wether the instance is contained or not
     */
    public boolean containsInstance(Instance instance){   
        //memory[0].get(instance.get(0)) = null
        try{
            return memory[0].get(instance.get(0)).contains(instance);
        }catch(Exception e){
            return false;
        }
    }
    
    /**
     * Checks if instance occurs in memory and is a must-be-true instance
     * @param instance
     * @return 
     */
    public boolean containsMustBeTrueInstance(Instance instance){   
        try{
            for (Iterator<Instance> it = memory[0].get(instance.get(0)).iterator(); it.hasNext();) {
                Instance inst = it.next();
                if( inst.equals(instance)) {
                    return inst.isMustBeTrue;
                }
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }
    
 
    /**
     * 
     * Returns all instances that match the selectionCriterion. 
     * Please be aware that a selection Criterion [X,X] is not treated differently than [X,Y]
     * This selection does not enable you to specify equality over several positions
     * This kind of selection is ensured over the Rete Network
     * 
     * PreCondition: selectionCriterion has to be of equal size to the memory's array size
     * 
     * @param selectionCriterion this array of terms defines what has to be at the positions of the instances that are selected.
     *                           If at position i there is a constant, or a functerm, only instances with that constant or functerm
     *                           at position i are selected. if it is a variable then for this slot all instances are selected.
     *                           The size of the selectionCriterion must be equal to this class memory length
     * @return a List of all Instances contained in the memory, that follow all slots of the selection criteria
     */
    @SuppressWarnings("unchecked") // AW: workaround for array conversion
    public Collection<Instance> select(Term[] selectionCriterion) {
        debugSelectCount++;
        long debugStartTime = System.currentTimeMillis();
        
        ArrayList<Instance> ret = new ArrayList<Instance>();
        
        // check if selectionCriterion only consists of Variables
        boolean selectionOnlyVariables = true;
        for (int i = 0; i < selectionCriterion.length; i++) {
            Term term = selectionCriterion[i];
            if( !(term instanceof Variable)) {
                selectionOnlyVariables = false;
            }
        }
        // if selected is empty, this means the selectionCriterion consisted only of variables
        // This means we have to return everything. So we add all instances of our first HashMap to ret, and return it.
        if (selectionOnlyVariables) {
            for (TreeSet hS : memory[0].values()) {
                ret.addAll(hS);
            }
            //System.out.println("Storage returns everything, as only vars are in selCrit: " + Instance.getInstanceAsString(selectionCriterion));
            return ret;
        }
        
        
        ArrayList<TreeSet<Instance>> selected = new ArrayList<TreeSet<Instance>>(); // used to store the hashsets that are treated by the actual guess
        TreeSet<Instance> smallest;

        // for each term of the selectionCriterion we add the HashSet of that Key to our selected List.
        // If there is no entry for that term in the corresponding HashMap then there is no instance to return
        // and we return an empty list
        // if the term is a Variable we do nothing, since a variable is no restriction on the instances
        for (int i = 0; i < selectionCriterion.length; i++) {
            if (!selectionCriterion[i].getClass().equals(Variable.class)) {
                if (!memory[i].containsKey(selectionCriterion[i])) {
                    // There is not even an entry for this key --> no instances
                    //System.out.println("Storage returns because no entry found!");
                    return ret;
                }
                //System.out.println("KEY: " + memory[i].get(selectionCriterion[i]));
                selected.add(memory[i].get(selectionCriterion[i]));
            }
        }


        // There is a selection Criterion so we return only those instances that are contained in each set.

        // We find the set with least ammount of instances and remove it from selected
        smallest = selected.get(0);
        for (int i = 1; i < selected.size(); i++) { //TODO: Maybe this is useless!
            if (selected.get(i).size() < smallest.size()) {
                smallest = selected.get(i);
            }
        }
        selected.remove(smallest);
        //System.out.println("SMALLEST: " + smallest);
        // for each Instance within smallest we check if the instance is containd in all other sets
        // if so the instance fullfills the selectioncriterion and is added to ret
        for (Instance instance : smallest) {
            boolean doesMatch = true;
            //for(HashSet hS: selected){
            for (int i = 0; i < selected.size(); i++) {
                //To Check: Just go trough the instances of the smallest one, and check their positions
                if (!selected.get(i).contains(instance)) {
                    doesMatch = false;
                    break;
                }
            }
            if (doesMatch) {
                ret.add(instance);
            }
        }

        //System.out.println("Storage asked for: " + Instance.getInstanceAsString(selectionCriterion) + " therefore returning: " + ret);
        debugSelectTime += (System.currentTimeMillis() - debugStartTime);
        return ret;
    }
    
    public boolean prettyPrintAllInstances(String pred_name, boolean densePrint){
        boolean didPrint = false;
        if( arity== 0) {    // dont print if predicate arity is zero
            return false;
        }
        if( densePrint ) {
            for (Set<Instance> hashSet : memory[0].values()) {
                for (Instance instance : hashSet) {
                    if(!didPrint) {
                        int instance_spacing = 16;
                        String pred = pred_name;
                        for (int i = 0; i < instance_spacing-pred_name.length(); i++) {
                            pred+=" ";
                        }
                        System.out.print(pred);
                        didPrint = true;
                    }
                    System.out.print(" "+Term.prettyPrint(instance.getTerms(),true));
                    if (instance.isMustBeTrue) {
                        System.out.print("MBT");
                    }
                }
            }
        } else {
            for (Set<Instance> hashSet : memory[0].values()) {
                for (Instance instance : hashSet) {
                    System.out.print(pred_name+Term.prettyPrint(instance.getTerms(),false));
                    System.out.print("@pL="+instance.propagationLevel+" ");
                    if(instance instanceof TrackingInstance) {
                        System.out.print("DL="+((TrackingInstance)instance).decisionLevel+" ");
                        System.out.println(" by Rule: "+((TrackingInstance)instance).getCreatedByRule().toString());
                    }
                    didPrint = true;
                }
            }
        }
        return didPrint;
    }
    
    /**
     * prints all instances that are contained within this memory to standard out.
     */
    public void printAllInstances(){
        for(Set<Instance> hS: memory[0].values()){
            for(Instance instance: hS){
                System.out.println(instance);
            }
        }
        
    }
    
    public ArrayList<Instance> getAllInstances(){
        ArrayList<Instance> ret = new ArrayList<Instance>();
        //ret.clear();
        for(Term t: this.memory[0].keySet()){
            ret.addAll(this.memory[0].get(t));
        }
        return ret;
    }
    
    
    /**
     * Registers an instance to remove when backtracking.
     * @param instance 
     */
    private void registerForBacktracking(Instance instance) {
        // if instance is MBT, then register as such
        if( instance.isMustBeTrue && !backtrackMustBeTrue.containsKey(instance)) {
            backtrackMustBeTrue.put(instance, instance.decisionLevel);
        }
        // register instance for regular backtracking
        ArrayList<Instance> backtrackList;
        if( backtrackInstances.get(instance.decisionLevel) != null) {
            backtrackList = backtrackInstances.get(instance.decisionLevel);
        } else {
            backtrackList = new ArrayList<Instance>();
            backtrackInstances.put(instance.decisionLevel, backtrackList);
        }
        backtrackList.add(instance);
    }
    
    /**
     * Removes all instances whose decision level is greater than the given one.
     * @param decisionLevel 
     */
    public void backtrackTo(int decisionLevel) {
        for (Iterator<Map.Entry<Integer, ArrayList<Instance>>> it = backtrackInstances.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, ArrayList<Instance>> entry = it.next();
            
            // check if decision level is greater than given one
            if( entry.getKey().intValue() >= decisionLevel ) {
                for (Instance instance : entry.getValue()) {
                    // check if there existed mbt on lower dl
                    if( backtrackMustBeTrue.containsKey(instance) &&
                            backtrackMustBeTrue.get(instance).intValue() < decisionLevel) {
                        // keep instance in memory, but set it again to mbt
                        instance.isMustBeTrue = true;
                    } else {
                        // remove instance from node memory
                        removeInstance(instance);
                    }
                }
                // remove from backtracking memory
                it.remove();
            }
        }
        // backtrack the MustBeTrue map
        for (Iterator<Map.Entry<Instance, Integer>> it = backtrackMustBeTrue.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Instance, Integer> entry = it.next();
            if( entry.getValue() >= decisionLevel ) {
                it.remove();
            }
        }
    }
    
    public boolean containsMustBeTrue() {
        for (Map.Entry<Term, TreeSet<Instance>> entry : memory[0].entrySet()) {
            for (Instance instance : entry.getValue()) {
                if( instance.isMustBeTrue ) {
                    return true;
                }
            }
        }
        return false;
    }
}
