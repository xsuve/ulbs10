/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Users;
import java.io.IOException;
import java.io.PrintWriter;
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
import model.User;
import services.UserService;

/**
 *
 * @author Razvan
 */
@WebServlet(name = "SignupServlet", urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {

    @Inject
    UserService service;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String action = request.getParameter("action");
            if ("signup".equals(action)) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String statut = request.getParameter("statut");
                //User u = new User(id++, email, password, firstName, lastName, statut);
                //request.setAttribute("user", u);
                //service.AddUser(id++, email,password,firstName,lastName,statut);
                List<Users> users;
                users = service.getAllPlayers();
                
                //Preluare din baza de date a ultimului id
                if (users.isEmpty()) {
                    lastID = 0;
                } else {
                    lastID = users.get(users.size() - 1).getId();
                }
                
                //Verificare daca exista deja in baza de date dupa email
                boolean existInDB = false;

                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getEmail().equals(email)) {
                        existInDB = true;
                    }
                }
                
                String alerta;
                RequestDispatcher dispatcher = null;
            
                if (!existInDB) {
                    service.AddUser(++lastID, email, password, firstName, lastName, statut);
                    
                    alerta = "Te-ai inregistrat cu succes!";
                    request.setAttribute("alert", alerta);
                    request.getServletContext().getRequestDispatcher("/../../web/login/login.jspx");
                    dispatcher.forward(request, response);
                    //response.sendRedirect("./login/login.jspx");
                    
                } else {
                    alerta = "Emailul deja exista in baza de date!";
                    request.setAttribute("alert", alerta);
                    request.getServletContext().getRequestDispatcher("/../../web/login/signup.jspx");
                    dispatcher.forward(request, response);
                    //response.sendRedirect("./login/signup.jspx");
                }
                                      
                

            } else if ("login".equals(action)) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                //todo
                //response.sendRedirect("./login/signup.jspx");
            }
            //RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("login/login.jspx");
            //dispatcher.forward(request, response);
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
        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
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
