package noodnik.anaplan;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

public class SergeyPairingFun {

    @Test
    public void funTestCase() {
        
        // head:[tail]
        Function<Boolean, Object> l = cons(1, null);
        Function<Boolean, Object> l2 = cons(2, l);
        log("originalPrint()");
        originalPrint(l2);
        // 2 1

        log("printUsingFold0()");
        printUsingFold0(l2);
        // 2 1 
        
        log("sumUsingFold0()");
        log(sumUsingFold0(l2));
        // 3
    }
    
    static Function<Boolean, Object> cons(int head, Object tail) {
        return headRequested -> headRequested ? head : tail;
    }       
    
    static void originalPrint(Object printable) {

        if (!(printable instanceof Function)) {
            log(printable);
            return;
        }

        Function listF = (Function) printable;
        log(listF.apply(true));
        originalPrint(listF.apply(false));

    }
    
    static void printUsingFold0(Object printable) {
        if (!(printable instanceof Function)) {
            log(printable);
            return;
        }
        Function listF = (Function) printable;
        fold0(0, listF, (a, b) -> { log(b); return b; });
    }
    
    static int sumUsingFold0(Function<Boolean, Object> listF) {
        return fold0(0, listF, (a, b) -> a + b);
    }
    
    static int fold0(
        int initial, 
        Function<Boolean, Object> listF, 
        BiFunction<Integer, Integer, Integer> folderF
    ) {
        int currentValue = (int) listF.apply(true);
        int foldedValue = folderF.apply(initial,  currentValue);
        Function nextFn = (Function) listF.apply(false);
        if (nextFn == null) {
            return foldedValue;
        }
        return fold0(foldedValue, nextFn, folderF);
    }
    
    static void log(Object o) {
        if (o != null) {
            System.out.println("" + o);
        }
    }
    
}
