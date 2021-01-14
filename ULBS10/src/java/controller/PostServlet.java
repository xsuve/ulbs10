/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Posturi;
import entity.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
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
import services.PosturiService;
import utility.Processing;

/**
 *
 * @author DxGod
 */
@WebServlet(name = "PostServlet", urlPatterns = {"/post"})
public class PostServlet extends HttpServlet {

    @Inject
    PosturiService service;
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
    List<Posturi> posturi;
    Posturi post = null;
    String alerta;
    RequestDispatcher dispatcher = null;
    Processing processing;
    String[] alert = new String[2];

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession sesiune = request.getSession();
            Users u = (Users) sesiune.getAttribute("user");
            String action = request.getParameter("action");
            posturi = service.getAllPosts();
            processing = new Processing(request, response, sesiune, null, posturi, null);

            if ("newpost".equals(action)) {
                service.addPost(processing.processNewPost(service.getAllPosts(), ""));
                processing.processNewPost(service.getAllPosts(), "redirect");
            }

            if ("deletepost".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                posturi = (List<Posturi>) sesiune.getAttribute("posts");
                posturi.stream().filter((item) -> (item.getId() == id)).forEachOrdered((item) -> {
                    service.removePost(item);
                });
                sesiune.setAttribute("posts", service.getAllPosts());
                alert[0] = "Post sters cu succes!";
                alert[1] = "alert alert-success";
                sesiune.setAttribute("appAlert", alert);
                response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
            }

            if ("editpost".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = originalFormat.parse(request.getParameter("dataLimita"));

                String cerinteMinime = request.getParameter("cerinteMinime");
                cerinteMinime = cerinteMinime.replaceAll("\n", "<br />");
                String cerinteOptionale = request.getParameter("cerinteOptionale");
                cerinteOptionale = cerinteOptionale.replaceAll("\n", "<br />");

                service.editPost(id, request.getParameter("denumire"), cerinteMinime, cerinteOptionale, date1);
                alert[0] = "Post modificat cu succes!";
                alert[1] = "alert alert-success";
                sesiune.setAttribute("appAlert", alert);
                sesiune.setAttribute("posts", service.getAllPosts());
                response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
            }

            if ("getAllPosts".equals(action)) {
                List<Posturi> postsAvailable = service.getAllPosts();
                sesiune.setAttribute("posts", postsAvailable);
                dispatcher = request.getServletContext().getRequestDispatcher("/index-mask.jspx");
                dispatcher.forward(request, response);
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
        } catch (ParseException ex) {
            Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (ParseException ex) {
            Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
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
