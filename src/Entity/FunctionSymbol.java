/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
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
        return new SerializedForm(ANodeImpl.out_mapping.get(this));
    }
        
    private static class SerializedForm implements Serializable {
      
        private Integer value;
        
        public SerializedForm(Integer value) {
            this.value=value;
        }
        
        public Object readResolve() throws ObjectStreamException {
            return ANodeImpl.ser_mapping.get(ANodeImpl.serializingFrom).get(value);
    }
        
    }
}
