package Entity;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import network.AExternalizedForm;
import network.ANodeImpl;

/**
 *
 * @author Antonius Weinzierl
 */
public class FunctionSymbol implements Serializable {
    
        private static HashMap<String,FunctionSymbol> func_symbols = new HashMap<String,FunctionSymbol>();
        
        private String name;
        
        public static FunctionSymbol getFunctionSymbol(String name) {
            if(func_symbols.containsKey(name))
                return func_symbols.get(name);
            else {
                FunctionSymbol fun = new FunctionSymbol(name);
                func_symbols.put(name, fun);
                return fun;
            }
        }
        
        private FunctionSymbol(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }
    
        public static Iterator<FunctionSymbol> getFunctionSymbolsIterator() {
            return func_symbols.values().iterator();
        }
        
    public Object writeReplace() throws ObjectStreamException {
        return new AExternalizedForm(ANodeImpl.out_mapping.get(this));
    }

}
