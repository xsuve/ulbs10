/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Aplicanti;
import entity.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "AplicantServlet", urlPatterns = {"/AplicantServlet"})
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
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            aplicanti = service.getAllAplicants();
            Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(request.getParameter("dataAplicarii"));
            Serializable s = request.getParameter("cv");

            if ("newpost".equals(action)) {
                if (aplicanti.isEmpty()) {
                    lastID = 0;
                } else {
                    lastID = aplicanti.get(aplicanti.size() - 1).getIdUser();
                }

                HttpSession sesiune = request.getSession();
                Users u = (Users) sesiune.getAttribute("user");
                aplicant = new Aplicanti(++lastID, s, date1, request.getParameter("obs"), u.getId());
                service.addAplicant(aplicant);

                dispatcher = request.getServletContext().getRequestDispatcher("/newpost.jspx");//todo
                dispatcher.forward(request, response);
            }
        } catch (ParseException ex) {
            Logger.getLogger(AplicantServlet.class.getName()).log(Level.SEVERE, null, ex);
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
