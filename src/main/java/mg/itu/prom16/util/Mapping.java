package mg.itu.prom16.util;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Mapping {
    
    Class<?> class1;
    HashMap<String, Method> verbMethod;

    
    public Mapping(Class<?> class1, HashMap<String, Method> verbMethod) {
        this.class1 = class1;
        this.verbMethod = verbMethod;
    }


    public HashMap<String, Method> getVerbMethod() {
        return verbMethod;
    }


    public void setVerbMethod(HashMap<String, Method> verbMethod) {
        this.verbMethod = verbMethod;
    }


    public Class<?> getClass1() {
        return class1;
    }

    public void setClass1(Class<?> class1) {
        this.class1 = class1;
    }
}
