package Entity;

import Interfaces.Term;
import java.util.Comparator;

/**
 * Comparator class for Terms, used for TreeMaps of Terms. It allows comparison
 * with a null term.
 *
 * @author Antonius Weinzierl
 */
public class TermComparator implements Comparator<Term> {
    
    private static TermComparator singleton = null;
    
    private TermComparator() {
    }
    
    public static TermComparator getInstance() {
        if( singleton == null ) {
            singleton = new TermComparator();
        }
        return singleton;
    }

    @Override
    /**
     * We order the subclasses of Term as follows: Constant < Variable < FuncTerm
     */
    public int compare(Term t, Term t1) {
        // null (represents 0-ary terms) is lowest
        if( t==null && t1==null ) {
            return 0;
        }
        else if (t == null ) {
            return -1;
        } else if( t1 == null ) {
            return 1;
        }
        // if both Terms are of the same type, compare them
        if (t1.getClass() == t.getClass()) {
            return t1.hashCode() - t.hashCode();   // use order imposed by hashCode, this is important for values ignored by hashCode
        }
        // rank according to classes of objects
        if (t1.getClass() == Constant.class) {
            // Constant is lowest, and other Term is of other type
            return -1;
        }
        if (t1.getClass() == FuncTerm.class) {
            // FuncTerm is highest, and other Term is of other type
            return 1;
        }
        if (t1.getClass() == Variable.class) {
            // check if other Term is Constant
            if (t.getClass() == Constant.class) {
                return 1;
            } else {    // other term is FuncTerm
                return -1;
            }
        }
        throw new RuntimeException("BUG: Term.compareTo finds no matching class types.");
    }
    
}
