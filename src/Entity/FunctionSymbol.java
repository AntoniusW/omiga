/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author aweinz
 */
public class FunctionSymbol {
    
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
}
