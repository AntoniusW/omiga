/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.Serializable;

/**
 *
 * @author aweinz
 */
public class Pair<T1,T2> implements Serializable {
    
    private T1 arg1;
    private T2 arg2;

    public Pair(T1 arg1, T2 arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public T1 getArg1() {
        return arg1;
    }

    public void setArg1(T1 arg1) {
        this.arg1 = arg1;
    }

    public T2 getArg2() {
        return arg2;
    }

    public void setArg2(T2 arg2) {
        this.arg2 = arg2;
    }
    
    
    
}
