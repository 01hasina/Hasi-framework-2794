package mg.itu.prom16.util;

import java.lang.reflect.Method;

public class Mapping {
    private Class<?> className;
    private Method methodName;

    public Mapping(Class<?> nameClass, Method method) {
        className = nameClass;
        methodName = method;
    }

    public Class<?> getClassName() {
        return className;
    }

    public Method getMethodName() {
        return methodName;
    }

    public void setClassName(Class<?> name) {
        className = name;
    }

    public void setMethodName(Method name) {
        methodName = name;
    }

    public Object executeMethod() throws Exception {
        Object instance = className.getDeclaredConstructor().newInstance();
        return methodName.invoke(instance);
    }
}
