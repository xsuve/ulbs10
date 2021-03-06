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
import utility.gmailSendEmailSSL;

/**
 *
 * @author Razvan
 */
@WebServlet(name = "AplicantServlet", urlPatterns = {"/aplicant"})
public class AplicantServlet extends HttpServlet {

    @Inject
    AplicantService service;

    int lastID;
    List<Aplicanti> aplicanti;
    List<Posturi> posturi;
    Aplicanti aplicant = null;
    String alerta;
    RequestDispatcher dispatcher = null;
    Processing processing;
    String[] alert = new String[2];

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws javax.mail.MessagingException if an Message error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, MessagingException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            HttpSession sesiune = request.getSession();
            Users user = (Users) sesiune.getAttribute("user");
            aplicanti = service.getAllAplicants();
            processing = new Processing(request, response, sesiune, null, null, aplicanti);

            if ("aplica".equals(action)) {
                if ("viewer".equals(user.getStatut())) {
                    int id = Integer.parseInt(request.getParameter("id_post"));
                    Posturi post = service.findPostByID(id);
                    if (processing.processAplicant(post, service.existaAplicantByIdUserIdPost(user, post))) {
                        service.addAplicant(processing.getAplicantData());
                    }
                } else {
                    alert[0] = "Nu ai acces pentru aceasta actiune!";
                    alert[1] = "alert alert-danger";
                    sesiune.setAttribute("appAlert", alert);
                    response.sendRedirect(request.getServletContext() + "./../dashboard.jspx#posturi");
                }
            }

            if ("deleteaplicant".equals(action)) {
                if ("generalDirector".equals(user.getStatut())) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    aplicant = service.findByID(id);
                    service.removeAplicant(id);
                    String mesaj = "Din pacate, " + aplicant.getIdUser().getFirstname() + " " + aplicant.getIdUser().getLastname() + ",\n"
                            + "Oferta dumneavoastra pentru postul " + aplicant.getIdPost().getDenumire() + " a fost refuzata,\n\n"
                            + "O zi buna!";
                    gmailSendEmailSSL sendemail = new gmailSendEmailSSL();
                    sendemail.sendMail(aplicant.getIdUser().getEmail(), "Oferta job", mesaj);
                    alert[0] = "Aplicantul a fost refuzat pentru acest post!";
                    alert[1] = "alert alert-danger";
                    sesiune.setAttribute("appAlert", alert);
                    sesiune.setAttribute("aplicants", service.getAllAplicants());
                    response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
                } else {
                    alert[0] = "Nu ai acces pentru aceasta actiune!";
                    alert[1] = "alert alert-danger";
                    sesiune.setAttribute("appAlert", alert);
                    response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
                }
            }

            if ("acceptAplicant".equals(action)) {
                if ("generalDirector".equals(user.getStatut()) || "administrator".equals(user.getStatut())) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    processing.processAcceptAplicant(service.findByID(id));
                    service.removeAplicant(id);
                    sesiune.setAttribute("aplicants", service.getAllAplicants());
                    response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
                } else {
                    alert[0] = "Nu ai acces pentru aceasta actiune!";
                    alert[1] = "alert alert-danger";
                    sesiune.setAttribute("appAlert", alert);
                    response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
                }
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
