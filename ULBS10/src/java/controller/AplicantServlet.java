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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.mail.MessagingException;
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
    List<Posturi> posturi;
    Aplicanti aplicant = null;
    String alerta;
    RequestDispatcher dispatcher = null;
    Processing processing;
    String[] alert = new String[2];

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, MessagingException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            HttpSession sesiune = request.getSession();
            aplicanti = service.getAllAplicants();
            processing = new Processing(request, response, sesiune, null, null, aplicanti);

            if ("aplica".equals(action)) {
                int idPost = Integer.parseInt(request.getParameter("id_post"));
                Users user = (Users) sesiune.getAttribute("user");
                if (processing.processAplicant(service.findPostByID(idPost), service.existaAplicantByIdUser(user, idPost))) {
                    service.addAplicant(processing.getAplicantData());
                }
            }

            if ("deleteaplicant".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                List<Aplicanti> aplican = service.getAllAplicants();

                aplican.stream().filter((aplic) -> (aplic.getId().equals(id))).forEachOrdered((_item) -> {
                    service.removeAplicant(id);
                });
                alert[0] = "Aplicantul a fost refuzat pentru acest post!";
                alert[1] = "alert alert-danger";
                sesiune.setAttribute("appAlert", alert);
                sesiune.setAttribute("aplicants", service.getAllAplicants());
                response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
            }

            if ("acceptAplicant".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                processing.processAcceptAplicant(service.existaUserByAplicantByID(id),service.findByID(id));
                service.removeAplicant(id);
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
        try {
            processRequest(request, response);
        } catch (MessagingException ex) {
            Logger.getLogger(AplicantServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (MessagingException ex) {
            Logger.getLogger(AplicantServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
