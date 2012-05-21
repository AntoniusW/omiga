/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.Serializable;

/**
 *
 * @author Minh Dao-Tran
 */
public class MessagesCounter implements Serializable {
    public int backtrack;
    public int pushing;
    public int make_choice;
    public int make_branch;
    public int print_answer;
    public int final_closing;
    public int call_local_node;
    
    public MessagesCounter()
    {
        backtrack = 0;        
        pushing = 0;
        make_choice = 0;
        make_branch = 0;
        print_answer = 0;
        final_closing = 0;
        call_local_node = 0;
    }
    
    public String toString()
    {
        String tmp = "";
        tmp = tmp + "INFO: Backtrack counter       = " + backtrack + "\n";
        tmp = tmp + "INFO: Pushing counter         = " + pushing + "\n";
        tmp = tmp + "INFO: MakeChoice counter      = " + make_choice + "\n";
        tmp = tmp + "INFO: MakeBranch counter      = " + make_branch + "\n";
        tmp = tmp + "INFO: PrintAnswer counter     = " + print_answer + "\n";
        tmp = tmp + "INFO: FinalClosing counter    = " + final_closing + "\n";
        tmp = tmp + "INFO: Call local node counter = " + call_local_node + "\n";

        
        int total = backtrack + pushing + make_choice + make_branch + print_answer + final_closing + call_local_node;
        tmp = tmp + "INFO: Total counters       = " + total + "\n";
        
        return tmp;
    }
}
