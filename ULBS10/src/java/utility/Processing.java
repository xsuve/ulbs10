/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entity.Users;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import model.User;
import org.apache.jasper.tagplugins.jstl.ForEach;
/**
 *
 * @author DxGod
 */
public class Processing {
    private RequestDispatcher dispatcher = null; 
    private HttpServletRequest request; 
    private HttpServletResponse response;
    private List<Users> users;
    private Users user;
    String[] alert = new String[2];
    private String salt = "ksdf@#$#T3fsd";
    
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    
    public Processing( HttpServletRequest request, HttpServletResponse response, List<Users> users){       
        this.request = request;
        this.response = response;
        this.users = users;
    }
    
    public void processLogin() throws ServletException, IOException, InvalidKeySpecException{
        String email = request.getParameter("email");
        String password = request.getParameter("password");

         //Verificare daca logarea este corecta
        boolean logare = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email) && verifyUserPassword(password, users.get(i).getPassword(), salt)) {
                HttpSession sesiune = request.getSession();
                sesiune.setAttribute("user", users.get(i));
                logare = true;
                break;
            }
        }
        
        if(!logare){
            alert[0] = "Email sau parola incorecta!";
            alert[1] = "alert alert-danger"; 
            request.setAttribute("alert", alert);                 
            dispatcher = request.getServletContext().getRequestDispatcher("/login/login.jspx");
            dispatcher.forward(request, response);
        }else{
             dispatcher = request.getServletContext().getRequestDispatcher("/newpost.jspx");
             dispatcher.forward(request, response);    
        }  
    
    }
    
    public boolean processSignup() throws ServletException, IOException, InvalidKeySpecException{
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String statut = request.getParameter("statut");
        int lastID;
        String securedPassword = generateSecurePassword(password, salt);
        password = securedPassword;
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
        if(email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ){
            alert[0] = "Va rugam sa complectati toate campurile.";
            alert[1] = "alert alert-danger"; 
            request.setAttribute("alert", alert);
            dispatcher = request.getServletContext().getRequestDispatcher("/login/signup.jspx");
            dispatcher.forward(request, response);
            return false;   
            
        } else {
            if (!existInDB) {   
                alert[0] = "Te-ai inregistrat cu succes!";
                alert[1] = "alert alert-success"; 
                request.setAttribute("alert", alert);
                dispatcher = request.getServletContext().getRequestDispatcher("/login/login.jspx");
                dispatcher.forward(request, response);
                user = new Users(++lastID, email, password, firstName, lastName, statut);
                return true;

            } else {
                alert[0] = "Emailul deja exista in baza de date!";
                alert[1]="alert alert-danger"; 
                request.setAttribute("alert", alert);
                dispatcher = request.getServletContext().getRequestDispatcher("/login/signup.jspx");
                dispatcher.forward(request, response);
                return false;
            }
        }    
    }
    
    public String getSalt() {
       
        return salt;
    }
    
    public static byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }
    
    public static String generateSecurePassword(String password, String salt) throws InvalidKeySpecException {
        String returnValue = null;
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
 
        returnValue = Base64.getEncoder().encodeToString(securePassword);
 
        return returnValue;
    }
    
    public static boolean verifyUserPassword(String providedPassword,
            String securedPassword, String salt) throws InvalidKeySpecException
    {
        boolean returnValue = false;
        
        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);
        
        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);
        
        return returnValue;
    }
    
    public Users getUserData(){
        return user;
    }
}
