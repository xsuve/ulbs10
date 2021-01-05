/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Users;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import services.UserService;
import utility.Processing;

/**
 *
 * @author Razvan
 */
@WebServlet(name = "SignupServlet", urlPatterns = {"/login/user"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
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
    List<Users> users;
    String alerta;
    RequestDispatcher dispatcher = null;
    Processing processing;
    String[] alert = new String[2];
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, InvalidKeySpecException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String action = request.getParameter("action");

            users = service.getAllUsers();
            processing = new Processing(request, response, users);

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
                String applicationPath = request.getServletContext().getRealPath("");
                String uploadFilePath = applicationPath + File.separator + "cv";
                HttpSession session = request.getSession();
                Users u = (Users) session.getAttribute("user");
                Part o = request.getPart("cv");
                InputStream fileInputStream = o.getInputStream();
                String fileName = u.getId().toString() + ".pdf";
                
                File fileSaveDir = new File(uploadFilePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }
                
                File fileToSave = new File(uploadFilePath + File.separator + fileName);
                Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                alert[0] = "Ai incarcat cu succes CV-ul!";
                alert[1] = "alert alert-success";
                session.setAttribute("appAlert", alert);
                response.sendRedirect(request.getServletContext() + "./../../dashboard.jspx#profil");
            }

            if ("edituser".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String email = request.getParameter("email");
                String firstname = request.getParameter("firstName");
                String lastname = request.getParameter("lastName");
                String statut = request.getParameter("statut");

                service.editUser(id, email, firstname, lastname, statut);
                HttpSession sesiune = request.getSession();
                Users user = (Users) sesiune.getAttribute("user");
                if (user.getId().equals(id)) {
                    user.setEmail(email);
                    user.setFirstname(firstname);
                    user.setLastname(lastname);
                    user.setStatut(statut);
                    sesiune.setAttribute("user", user);
                }
                users = service.getAllUsers();
                sesiune.setAttribute("users", users);
                alert[0] = "Utilizator modificat cu succes!";
                alert[1] = "alert alert-success";
                sesiune.setAttribute("appAlert", alert);
                response.sendRedirect(request.getServletContext() + "./../../dashboard.jspx#utilizatori");                              
            }

            if ("deleteuser".equals(action)) {
                HttpSession sesiune = request.getSession();
                Users user = (Users) sesiune.getAttribute("user");
                int id = Integer.parseInt(request.getParameter("id"));
                if (!user.getId().equals(id)) {
                    service.removeUser(id);
                    users = service.getAllUsers();
                    sesiune.setAttribute("users", users);
                    alert[0] = "Utilizator sters cu succes!";
                    alert[1] = "alert alert-success";
                    sesiune.setAttribute("appAlert", alert);
                    response.sendRedirect(request.getServletContext() + "./../../dashboard.jspx#utilizatori");
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
                    HttpSession session = request.getSession();
                    List<Users> u = (List<Users>) service.getAllUsers();
                    alert[0] = "Utilizator adaugat cu succes!";
                    alert[1] = "alert alert-success";
                    session.setAttribute("appAlert", alert);
                    session.setAttribute("users", u);
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
