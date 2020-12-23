/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import static entity.Users_.users;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entity.Users;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import services.UserService;
/**
 *
 * @author DxGod
 */
public class Processing {

    RequestDispatcher dispatcher = null; 
    HttpServletRequest request; 
    HttpServletResponse response;
    List<Users> users;

    
    public Processing( HttpServletRequest request, HttpServletResponse response, List<Users> users){       
        this.request = request;
        this.response = response;
        this.users = users;
    }
    
    public void processLogin() throws ServletException, IOException{
        String email = request.getParameter("email");
        String password = request.getParameter("password");

         //Verificare daca logarea este corecta
        boolean logare = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)  && users.get(i).getPassword().equals(password)) {
               logare = true;
            }
        }

        if(!logare){
            String alerta = "Email sau parola incorecta!";
            request.setAttribute("alert", alerta);                 
            dispatcher = request.getServletContext().getRequestDispatcher("/login/login.jspx");
            dispatcher.forward(request, response);

        }else{
             HttpSession sesiune = request.getSession();
             sesiune.setAttribute("user", users.get(0));
             dispatcher = request.getServletContext().getRequestDispatcher("/dashboard.jspx");
             dispatcher.forward(request, response);    
        }  
    
    }
    
    public boolean processSignup() throws ServletException, IOException{
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String statut = request.getParameter("statut");
        int lastID;
        String alerta;

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

        if (!existInDB) {           
            alerta = "Te-ai inregistrat cu succes!";
            request.setAttribute("alert", alerta);
            dispatcher = request.getServletContext().getRequestDispatcher("/login/login.jspx");
            dispatcher.forward(request, response);
            return true;

        } else {
            alerta = "Emailul deja exista in baza de date!";
            request.setAttribute("alert", alerta);
            dispatcher = request.getServletContext().getRequestDispatcher("/login/signup.jspx");
            dispatcher.forward(request, response);
            return false;
        }
    }
}
