/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
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
import services.UserService;
import utility.Processing;

/**
 *
 * @author Razvan
 */
@WebServlet(name = "SignupServlet", urlPatterns = {"/login/user"})
public class UserServlet extends HttpServlet {

    @Inject
    UserService service;
    int lastID;
    List<Users> users;
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
     * @throws java.sql.SQLException if an SQL error occurs
     * @throws java.security.spec.InvalidKeySpecException if an Key error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, InvalidKeySpecException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String action = request.getParameter("action");
            HttpSession sesiune = request.getSession();

            users = service.getAllUsers();
            processing = new Processing(request, response, sesiune, users, null, null);

            if ("signup".equals(action)) {
                if (processing.processSignup("signup")) {
                    service.addUser(processing.getUserData());
                }
            }

            if ("login".equals(action)) {
                processing.processLogin(service.getAllPosts(), service.getAllAplicants());
            }

            if ("logout".equals(action)) {
                processing.processLogout();
            }
            if ("pdf".equals(action)) {
                processing.processCV();
            }

            if ("edituser".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String email = request.getParameter("email");
                String firstname = request.getParameter("firstName");
                String lastname = request.getParameter("lastName");
                String statut = request.getParameter("statut");

                service.editUser(id, email, firstname, lastname, statut);
                users = service.getAllUsers();
                processing.processEditUser(id, email, firstname, lastname, statut, users);
            }

            if ("deleteuser".equals(action)) {
                Users user = (Users) sesiune.getAttribute("user");
                int id = Integer.parseInt(request.getParameter("id"));
                if (!user.getId().equals(id)) {

                    users = service.getAllUsers();
                    service.removeUser(id);
                    processing.processRemoveUser(users);
                } else {
                    alert[0] = "Utilizatorul nu a fost sters!";
                    alert[1] = "alert alert-danger";
                    sesiune.setAttribute("appAlert", alert);
                    response.sendRedirect(request.getServletContext() + "./../../dashboard.jspx#utilizatori");
                }
            }
            if ("newuser".equals(action)) {
                if (processing.processSignup(action)) {
                    service.addUser(processing.getUserData());
                    List<Users> u = (List<Users>) service.getAllUsers();
                    alert[0] = "Utilizator adaugat cu succes!";
                    alert[1] = "alert alert-success";
                    sesiune.setAttribute("appAlert", alert);
                    sesiune.setAttribute("users", u);
                }
            }
        }
    }

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
        } catch (SQLException | InvalidKeySpecException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException | InvalidKeySpecException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
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
    }
}
