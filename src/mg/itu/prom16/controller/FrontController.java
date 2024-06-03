package mg.itu.prom16.controller;

import mg.itu.prom16.util.*;
import mg.itu.prom16.annotation.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class FrontController extends HttpServlet {

   private String basePackage;
   private HashMap<String, Mapping> controllers = new HashMap<>();

   @Override
   public void init(ServletConfig config) throws ServletException {
      super.init(config);
      basePackage = config.getInitParameter("base-package");
      try {
         controllers = ClassScanner.scanClasses(basePackage, Controleur.class, GetMethode.class);
      } catch (Exception e) {
         throw new ServletException("Erreur lors du scan des classes", e);
      }
   }

   protected void processRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
      String fullURI = req.getRequestURI();
      String contextPath = req.getContextPath();
      String urlCourant = fullURI.substring(contextPath.length());

      res.setContentType("text/plain");
      PrintWriter out = res.getWriter();
      out.println("URL courante: " + urlCourant);

      Mapping mapping = controllers.get(urlCourant);
      if (mapping != null) {
         Object result = mapping.executeMethod();
         dispatchResponse(req, res, result, out);
         out.println("Contrôleur trouvé:");
         out.println("Class: " + mapping.getClassName());
         out.println("Exécution du méthode: " + mapping.getMethodName() + "--->" +
         result);
      } else {
         out.println("L'URL spécifiée n'existe pas.");
      }
   }

   protected void dispatchResponse(HttpServletRequest request, HttpServletResponse response, Object model,
         PrintWriter out) throws ServletException, IOException {
      if (model instanceof String) {
         out.println(model);
      } else if (model instanceof ModelView modelView) {
         RequestDispatcher dispatcher = request.getRequestDispatcher(modelView.getUrl());
         HashMap<String, Object> data = modelView.getData();
         for (String varName : data.keySet()) {
            request.setAttribute(varName, data.get(varName));
         }
         dispatcher.forward(request, response);
      } else {
         out.println("return type not found!!");
         throw new ServletException("return type not found!!");
      }
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      try {
         this.processRequest(req, res);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      try {
         this.processRequest(req, res);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public String getServletInfo() {
      return "Short description";
   }
}
