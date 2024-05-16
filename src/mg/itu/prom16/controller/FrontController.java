// Source code is decompiled from a .class file using FernFlower decompiler.
package mg.itu.prom16.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.util.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class FrontController extends HttpServlet {

   private String basePackage;
   private List<Class<?>> controllers = new ArrayList<>();
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

   protected void processRequest(HttpServletRequest req, HttpServletResponse res) throws Exception{
      StringBuffer url = req.getRequestURL();
      System.out.println(url);
      res.setContentType("text/plain");
      PrintWriter out = res.getWriter();
      for (Class<?> class1 : controllers) {
         out.println("listes des controllers : ");
         out.println("      =>"+class1.getName());
      }
   }

   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      try {
         this.processRequest(req, res);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      try {
         this.processRequest(req, res);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public String getServletInfo() {
      return "Short description";
   }
}

