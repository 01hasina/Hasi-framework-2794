package mg.itu.prom16.controller;

import mg.itu.prom16.util.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class FrontController extends HttpServlet {

   private String basePackage;
   private HashMap<String, Mapping> controllers = new HashMap<>();
   private int test = 0;

   @Override
   public void init(ServletConfig config) throws ServletException {
      super.init(config);
      test += 1;
      basePackage = config.getInitParameter("base-package");
      try {
         controllers = ClassScanner.scanClasses(basePackage);
      } catch (Exception e) {
         throw new ServletException("Erreur lors du scan des classes", e);
      }
   }

   public FrontController() {
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
          out.println("Contrôleur trouvé:");
          out.println("Class: " + mapping.getClassName());
          out.println("Méthode: " + mapping.getMethodName());
      } else {
          out.println("L'URL spécifiée n'existe pas.");
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
