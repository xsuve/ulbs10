/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Aplicanti;
import entity.Posturi;
import entity.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import services.AplicantService;
import utility.Processing;

/**
 *
 * @author Razvan
 */
@WebServlet(name = "AplicantServlet", urlPatterns = {"/aplicant"})
public class AplicantServlet extends HttpServlet {

    @Inject
    AplicantService service;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    int lastID;
    List<Aplicanti> aplicanti;
    Aplicanti aplicant = null;
    String alerta;
    RequestDispatcher dispatcher = null;
    Processing processing;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            aplicanti = service.getAllAplicants();

            if ("aplica".equals(action)) {
                HttpSession sesiune = request.getSession();
                Date todayDate = new Date();
                int idAplicant;
                int idPost = Integer.parseInt(request.getParameter("id_post")); 
                 
                Users user =  (Users) sesiune.getAttribute("user");
                List<Posturi> posturi = (List<Posturi>) sesiune.getAttribute("posts");   
                List<Aplicanti> allAplicanti = service.getAllAplicants();
                Posturi post = null;
                for(int i = 0; i < posturi.size(); i++) {
                    if(posturi.get(i).getId().equals(idPost)) {
                       post = posturi.get(i);
                    }
                }            
                               
                if(allAplicanti.isEmpty()){
                    idAplicant = 0;
                } else {
                    idAplicant = allAplicanti.get(allAplicanti.size() - 1).getId();
                }
                boolean existInDB = false;
                for(int i=0; i<allAplicanti.size();i++){
                    if(allAplicanti.get(i).getIdPost().equals(post)  && allAplicanti.get(i).getIdUser().equals(user))
                        existInDB = true;
                }
                
                HttpSession session = request.getSession();
                String[] appAlert = new String[2];

                if(!existInDB){
                    service.addAplicant(++idAplicant, user, post , todayDate);
                    
                    appAlert[0] = "Ai aplicat cu succes pentru acest post!";
                    appAlert[1] = "alert alert-success";
                    session.setAttribute("aplicants", service.getAllAplicants());
                    session.setAttribute("appAlert", appAlert);
                    
                    response.sendRedirect(request.getServletContext() + "./../dashboard.jspx#posturi");
                } else {
                    appAlert[0] = "Ai aplicat deja pentru acest post!";
                    appAlert[1] = "alert alert-danger";
                    session.setAttribute("appAlert", appAlert);
                    
                    response.sendRedirect(request.getServletContext() + "./../dashboard.jspx#posturi");
                }
            }
            
             if ("deleteaplicant".equals(action)) {
                  int id = Integer.parseInt(request.getParameter("id"));
                  List<Aplicanti> aplican = service.getAllAplicants();
                  
                  for(Aplicanti aplic : aplican){
                      if(aplic.getId().equals(id)){
                          service.removeAplicant(id);
                      }
                  }
                HttpSession sesiune = request.getSession();
                sesiune.setAttribute("aplicants", service.getAllAplicants());
                response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
             
             }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
