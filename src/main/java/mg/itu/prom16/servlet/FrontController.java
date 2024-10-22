package mg.itu.prom16.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import mg.itu.prom16.annotation.Controller;
import mg.itu.prom16.annotation.Get;
import mg.itu.prom16.annotation.Url;
import mg.itu.prom16.annotation.Post;
import mg.itu.prom16.annotation.RestApi;
import mg.itu.prom16.exception.DuplicateUrlException;
import mg.itu.prom16.exception.InvalidReturnTypeException;
import mg.itu.prom16.exception.PackageNotFoundException;
import mg.itu.prom16.util.ClassScanner;
import mg.itu.prom16.util.JsonParserUtil;
import mg.itu.prom16.util.Mapping;
import mg.itu.prom16.util.ServletUtil;
import mg.itu.prom16.util.ModelView;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB avant d'être écrit sur le disque
    maxFileSize = 1024 * 1024 * 10,       // Taille maximale d'un fichier (10MB)
    maxRequestSize = 1024 * 1024 * 50     // Taille maximale d'une requête (50MB)
)
public class FrontController extends HttpServlet {
    private String basePackage ;
    private HashMap<String , Mapping> listMapping;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Obtenez la valeur du package
        basePackage = config.getInitParameter("basePackageName");
        try {
            initHashMap();
        } 
        catch (PackageNotFoundException | DuplicateUrlException e) {
            e.printStackTrace();
            throw new Error(e.getMessage());
        }
        catch (Exception ex){
            throw new ServletException(ex);
        }
    }

    protected void doRestApi(Object valueFunction, HttpServletResponse response) throws Exception {
        try {
            if (valueFunction instanceof ModelView) {
                ModelView modelView = (ModelView) valueFunction;
                HashMap<String, Object> listKeyAndValue = modelView.getData();
                String dataString = JsonParserUtil.objectToJson(listKeyAndValue);
                response.setContentType("text/json");
                response.getWriter().println(dataString);
            }
            else {
                String dataString = JsonParserUtil.objectToJson(valueFunction);
                response.setContentType("text/json");
                response.getWriter().println(dataString);
            }
        } catch (Exception e) {
            throw new ServletException(e);

    protected void displayListMapping(PrintWriter out) {
        for (Map.Entry<String, Mapping> e : listMapping.entrySet()) {
            String key = e.getKey();
            Mapping value = e.getValue();

            out.println("<ul> URL : " + key + "</ul>");
            out.println("<li> Class name :  "+ value.getClass1().getSimpleName() +" </li> <li> Method name : "+ value.getMethod().getName() +"</li>");

        }
    }

    protected void doRestApi(Object valueFunction, HttpServletResponse response) throws Exception {
        try {
            if (valueFunction instanceof ModelView) {
                ModelView modelView = (ModelView) valueFunction;
                HashMap<String, Object> listKeyAndValue = modelView.getData();
                String dataString = JsonParserUtil.objectToJson(listKeyAndValue);
                response.setContentType("text/json");
                response.getWriter().println(dataString);
            }
            else {
                String dataString = JsonParserUtil.objectToJson(valueFunction);
                response.setContentType("text/json");
                response.getWriter().println(dataString);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void dispatcher (HttpServletRequest request , HttpServletResponse response,  Object valueFunction) throws InvalidReturnTypeException, Exception{
        try{
            PrintWriter out = response.getWriter();
            if (valueFunction instanceof ModelView) {

                ModelView modelAndView = (ModelView)valueFunction;

                String nameView = modelAndView.getViewName();
                HashMap<String, Object> listKeyAndValue = modelAndView.getData();
                
                for (Map.Entry<String, Object> map : listKeyAndValue.entrySet()) {
                    request.setAttribute(map.getKey(),  map.getValue());
                }

                String queryString = request.getQueryString();
                RequestDispatcher dispatcher = request.getRequestDispatcher(nameView +"?" + queryString);
                dispatcher.forward(request, response);
            }
            else if (valueFunction instanceof String) { // si string
                out.println("<ul><li> Valeur de la fonction :  "+ valueFunction.toString() + "</li></ul>");
            }
            else {
                throw new InvalidReturnTypeException(valueFunction.toString());
            }
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
    protected String getVerbForMethod(Method method){
        if(method.getAnnotation(Get.class) != null){
            return "GET";
        }
        else if(method.getAnnotation(Post.class) != null){
            return "POST";
        }
        else{
            return "GET";
        }
    }
    protected void initHashMap() throws DuplicateUrlException, PackageNotFoundException, Exception {
        List<Class<?>> classes = ClassScanner.scanClasses(basePackage, Controller.class);
        listMapping = new HashMap<String, Mapping>();

        for (Class<?> class1 : classes) {
            Method[] methods = class1.getDeclaredMethods();
        
            for (Method method : methods) {
                if (method.isAnnotationPresent(Url.class)) {
                    String urlMethod = method.getAnnotation(Url.class).value();
                    String verb = getVerbForMethod(method);
                    if(!listMapping.containsKey(urlMethod)){
                        HashMap<String, Method> verbMethod = new HashMap<String, Method>();
                        verbMethod.put(verb, method);
                        Mapping map = new Mapping(class1, verbMethod);
                        this.listMapping.put(urlMethod,map);
                    }else{
                        Mapping map = listMapping.get(urlMethod);
                        HashMap<String, Method> verbMethod = map.getVerbMethod();
                        if(!verbMethod.containsKey(verb)){
                            if(class1.getSimpleName().equals(map.getClass1().getSimpleName())){
                                verbMethod.put(verb, method);
                                this.listMapping.put(urlMethod, map);
                            }else{
                                HashMap<String, Method> verbMethod1 = new HashMap<String, Method>();
                                verbMethod.put(verb, method);
                                Mapping map1 = new Mapping(class1, verbMethod1);
                                this.listMapping.put(urlMethod,map1);
                            }
                        }else{
                            throw new DuplicateUrlException(urlMethod, verb);
                        }
                    }
                }
            }
        }
        
    }

    protected boolean isValidVerb(HttpServletRequest request, Method method) throws Exception{
        String verbRequest = request.getMethod();
        String verbMethod = getVerbForMethod(method);
        if(verbRequest.equals(verbMethod)){
            return true;
        }return false;
    }
    protected Method getMethodByVerb(HttpServletRequest request, Mapping map) throws Exception{
        String verbRequest = request.getMethod();
        HashMap<String, Method> verbMethod = map.getVerbMethod();
        Method method = verbMethod.get(verbRequest);
        if(method != null){
            if(isValidVerb(request, method) == true){
                return method;
            }else{
                throw new Exception("excpected"+ verbMethod + "but received" + verbRequest);
            }
        }
        throw new Exception("excpected"+ verbMethod + "but received" + verbRequest);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 
        String relativeURI = request.getServletPath();
        String queryString = request.getQueryString();
        
        System.out.println(relativeURI);
        System.out.println(queryString);


        try {
            boolean isPresent = listMapping.containsKey(relativeURI);
            
            if (!isPresent) { 
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;           
            }
            
            Mapping mapping =  listMapping.get(relativeURI);
            Object instance = mapping.getClass1().getDeclaredConstructor().newInstance()
            Method method = getMethodByVerb(request, mapping);
            List<Object> listArgs = ServletUtil.parseParameters(request, method);

            ServletUtil.putSession(request,  instance);
            Object valueFunction = method.invoke(instance, listArgs.toArray());
            
            RestApi restApi = method.getAnnotation(RestApi.class);
            if (restApi != null) {
                doRestApi(valueFunction, response);
            } else {
                dispatcher(request, response, valueFunction);
            }
        } 
        catch (Exception e) {   
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");  
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
            out.close();      

        }
    }


    

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
