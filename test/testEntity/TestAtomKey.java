package testEntity;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import Entity.FuncTerm;
import Interfaces.Term;
import Entity.Atom;
import Entity.Variable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class TestAtomKey {
    
    Atom pxx2;
    Atom pxx;
    Atom pyy;
    Atom pxy;
    Atom pyx;
   
    Atom qxx;
    Atom qyy;
    Atom qxy;
    Atom qyx;
    Atom qxx2;
    
    Atom pfxx;
    Atom pfyy;
    Atom pfxy;
    Atom pfyx;
    Atom pxfx;
    Atom pyfy;
    Atom pxfy;
    Atom pyfx;
    
    public TestAtomKey() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        Term[] termsXX = new Term[2];
        termsXX[0] = Variable.getVariable("X");
        termsXX[1] = Variable.getVariable("X");
        pxx = new Atom("p",2,termsXX);
        pxx2 = new Atom("p",2,termsXX);
        
        Term[] termsYY = new Term[2];
        termsYY[0] = Variable.getVariable("Y");
        termsYY[1] = Variable.getVariable("Y");
        pyy = new Atom("p",2,termsYY);
        
        Term[] termsXY = new Term[2];
        termsXY[0] = Variable.getVariable("X");
        termsXY[1] = Variable.getVariable("Y");
        pxy = new Atom("p",2,termsXY);
        
        Term[] termsYX = new Term[2];
        termsYX[0] = Variable.getVariable("Y");
        termsYX[1] = Variable.getVariable("X");
        pyx = new Atom("p",2,termsYX);
        
        qxx = new Atom("q",2,termsXX);
        qxx2 = new Atom("q",2,termsXX);

        qyy = new Atom("q",2,termsYY);

        qxy = new Atom("q",2,termsXY);
        
        qyx = new Atom("q",2,termsYX);
        
        
        ArrayList<Term> childrenX = new ArrayList<Term>();
        childrenX.add(Variable.getVariable("X"));
        FuncTerm fx = FuncTerm.getFuncTerm("f",childrenX);
        
        ArrayList<Term> childrenY = new ArrayList<Term>();
        childrenY.add(Variable.getVariable("Y"));
        FuncTerm fy = FuncTerm.getFuncTerm("f",childrenY);
        
        Term[] fxx = new Term[2];
        fxx[0] = fx;
        fxx[1] = Variable.getVariable("X");
        
        Term[] fyx = new Term[2];
        fyx[0] = fy;
        fyx[1] = Variable.getVariable("X");
        
        Term[] fxy = new Term[2];
        fxy[0] = fx;
        fxy[1] = Variable.getVariable("Y");
        
        Term[] fyy = new Term[2];
        fyy[0] = fy;
        fyy[1] = Variable.getVariable("Y");
        
        
        pfxx = new Atom("p", 2,fxx);
        pfyy = new Atom("p", 2, fyy);
        pfxy = new Atom("p", 2, fxy);
        pfyx = new Atom("p", 2, fyx);
        
        
        Term[] xfx = new Term[2];
        xfx[1] = fx;
        xfx[0] = Variable.getVariable("X");
        
        Term[] yfx = new Term[2];
        yfx[1] = fy;
        yfx[0] = Variable.getVariable("X");
        
        Term[] xfy = new Term[2];
        xfy[1] = fx;
        xfy[0] = Variable.getVariable("Y");
        
        Term[] yfy = new Term[2];
        yfy[1] = fy;
        yfy[0] = Variable.getVariable("Y");
        
        pxfx = new Atom("p", 2,xfx);
        pyfy = new Atom("p", 2,yfy);
        pxfy = new Atom("p", 2,xfy);
        pyfx = new Atom("p", 2,yfx);
        
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void test_AtomEquality(){
        assert(pxx.equals(pxx2));
        assert(pxx != pxx2);
        
        assert(!pxx.equals(qxx));
        assert(!pxx.equals(pxy));
        assert(!pxx.equals(pyy));
        
        assert(!pxy.equals(pyx));
        assert(!pxy.equals(qxy));
        assert(!pxy.equals(qxy));
        
        assert(qxx.equals(qxx2));
        assert(qxx != qxx2);
        
        assert(qxx.equals(qxx));
        assert(!qxx.equals(qxy));
        assert(!qxx.equals(qyy));
        
        assert(!qxy.equals(qyx));
        assert(qxy.equals(qxy));
        assert(!qxy.equals(qyy));
        
    }
    
    @Test
    public void test_AtomHashCode(){
        assert(pxx.hashCode() == pxx2.hashCode());
        assert(qxx.hashCode() == qxx2.hashCode());
        assert(pxx.hashCode() != qxx.hashCode());
        assert(pfxy.getAtomAsReteKey().hashCode() == pfyx.getAtomAsReteKey().hashCode());
    }
    
    @Test
    public void test_AtomKeyEquality(){
        assert(pxx.getAtomAsReteKey().equals(pxx2.getAtomAsReteKey()));
        assert(qxx.getAtomAsReteKey().equals(qxx2.getAtomAsReteKey()));
        
        assert(pxy.getAtomAsReteKey().equals(pyx.getAtomAsReteKey()));
        assert(qxy.getAtomAsReteKey().equals(qyx.getAtomAsReteKey()));
        
        assert(pxx.getAtomAsReteKey().equals(pyy.getAtomAsReteKey()));
        assert(qxx.getAtomAsReteKey().equals(qyy.getAtomAsReteKey()));
        
        assert(!pxx.getAtomAsReteKey().equals(qxx.getAtomAsReteKey()));
    }
    
    @Test
    public void test_AtomsWithFuncTermEquality(){
        assert(pfxx.equals(pfxx));
        
        assert(pfxx.getAtomAsReteKey().equals(pfyy.getAtomAsReteKey()));
        assert(!pfxx.getAtomAsReteKey().equals(pfxy.getAtomAsReteKey()));
        assert(!pfxx.getAtomAsReteKey().equals(pfyx.getAtomAsReteKey()));
        
        assert(pfxy.getAtomAsReteKey().equals(pfyx.getAtomAsReteKey()));
        assert(pfyx.getAtomAsReteKey().equals(pfxy.getAtomAsReteKey()));
        
        assert(!pfxy.getAtomAsReteKey().equals(pxfy.getAtomAsReteKey()));
        
        assert(!pxfx.getAtomAsReteKey().equals(pfxx.getAtomAsReteKey()));
        assert(!pxx.getAtomAsReteKey().equals(pfxx.getAtomAsReteKey()));
        assert(!pxx.getAtomAsReteKey().equals(pxfx.getAtomAsReteKey()));
    }
}
