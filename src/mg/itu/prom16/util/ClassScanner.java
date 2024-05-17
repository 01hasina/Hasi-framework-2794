package mg.itu.prom16.util;
import mg.itu.prom16.annotation.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassScanner {

    public static HashMap<String, Mapping> scanClasses(String packageName) throws Exception {
        HashMap<String, Mapping> mappings = new HashMap<>();
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');

        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url == null) {
            throw new Exception("Package :" + packageName + " non trouvé");
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
                    if (loadedClass.getAnnotation(Controleur.class) != null) {
                        classes.add(loadedClass);

                        // Process methods annotated with @Get
                        Method[] methods = loadedClass.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(Get.class)) {
                                Get getAnnotation = method.getAnnotation(Get.class);
                                String urlValue = getAnnotation.value();
                                mappings.put(urlValue, new Mapping(loadedClass.getName(), method.getName()));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mappings;
    }
}
