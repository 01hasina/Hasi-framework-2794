package mg.itu.prom16.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.net.URL;

import mg.itu.prom16.annotation.*;
import mg.itu.prom16.exception.DuplicateMethodException;
import mg.itu.prom16.exception.PackageNotFoundException;

public class ClassScanner {

    @SuppressWarnings("unchecked")
    public static HashMap<String, Mapping> scanClasses(String packageName, Class annotationClass,
            Class annotationMethod) throws Exception {
        HashMap<String, Mapping> classes = new HashMap<>();
        String path = packageName.replace('.', '/');

        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url == null) {
            throw new PackageNotFoundException(packageName);
        }

        File directory = new File(url.toURI());
        File[] files = directory.listFiles();

        for (File file : files) {
            String fileName = file.getName();
            System.out.println("File : " + fileName);

            if (fileName.endsWith(".class")) {
                String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);

                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    Class<?> loadedClass = classLoader.loadClass(className);
                    if (loadedClass.isAnnotationPresent(annotationClass)) {
                        HashMap<String, Mapping> annotatedMethods = getAnnotatedMethods(loadedClass, annotationMethod);
                        classes.putAll(annotatedMethods);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Mapping> getAnnotatedMethods(Class<?> loadedClass, Class annotationClass) {
        HashMap<String, Mapping> methods = new HashMap<>();

        for (Method method : loadedClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                GetMethode annotation = method.getAnnotation(GetMethode.class);
                String key = annotation.value();

                if (methods.containsKey(key)) {
                    throw new DuplicateMethodException("Duplicate method found for key: " + key);
                }

                Mapping mapping = new Mapping(loadedClass, method);
                methods.put(key, mapping);
            }
        }
        return methods;
    }
}
