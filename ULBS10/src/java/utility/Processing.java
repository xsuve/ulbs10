/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import entity.Posturi;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

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


    /**
     * Constructor cu parametrii
     *
     * @param request
     * @param response
     * @param users
     */
    public Processing(HttpServletRequest request, HttpServletResponse response, List<Users> users) {
        this.request = request;
        this.response = response;
        this.users = users;
    }

    /**
     * Procesul de autentificare a unui utilizator din UserServlet s-a mutat aici pentru curatarea codului.
     * Se verifica adresele de email si parolele din baza de date pentru a gasi persoana in baza de date, daca aceasta exista, daca nu
     * se trimit catre jspx mesaje sugestive
     * @param allPosts
     * @throws ServletException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public void processLogin(List<Posturi> allPosts) throws ServletException, IOException, InvalidKeySpecException{
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

        if (!logare) {
            alert[0] = "Email sau parola incorecta!";
            alert[1] = "alert alert-danger";
            request.setAttribute("alert", alert);
            dispatcher = request.getServletContext().getRequestDispatcher("/login/login.jspx");
            dispatcher.forward(request, response);
        } else {
            gmailSendEmailSSL mail = new gmailSendEmailSSL();
            try {
                mail.sendMail("ulbs10.recrutari@gmail.com", "elena.raicu@ulbsibiu.ro", "ULBS10", "O facuram si pe astaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            } catch (MessagingException ex) {
                Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
            }

            HttpSession sesiune = request.getSession();
            sesiune.setAttribute("posts", allPosts);
            sesiune.setAttribute("users", users);
            response.sendRedirect(request.getServletContext() + "./../../newjsp.jspx");
            //dispatcher = request.getServletContext().getRequestDispatcher("/dashboard.jspx");
            //dispatcher.forward(request, response);
        }

    }

    /**
     * Procesul de inregistrare a unui utilizator din UserServlet s-a mutat aici
     * pentru curatarea codului. Se verifica daca adresa de email exista in baza
     * de date si se continua cu inregistrarea daca nu exista, daca aceasta
     * exista se trimite catre jsps un mesaj sugestiv Se verifica daca campurile
     * sunt goale, iar daca sunt se trimite catre jspx un mesaj sugestiv
     *
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public boolean processSignup() throws ServletException, IOException, InvalidKeySpecException {
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
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
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
                alert[1] = "alert alert-danger";
                request.setAttribute("alert", alert);
                dispatcher = request.getServletContext().getRequestDispatcher("/login/signup.jspx");
                dispatcher.forward(request, response);
                return false;
            }
        }
    }

    /**
     * Se invalideaza sesiunea utilizatorului si se redirectioneaza catre index
     * @see HttpSession
     * @throws ServletException
     * @throws IOException
     */
    public void processLogout() throws ServletException, IOException {
        HttpSession sesiune = request.getSession();
        sesiune.invalidate();
        //dispatcher = request.getServletContext().getRequestDispatcher("/index.jspx");
        //dispatcher.forward(request, response);
        response.sendRedirect(request.getContextPath() + "/index.jspx");
    }

    /**
     * Cripteaza parola cu o cheie salt
     * @param password
     * @param salt
     * @return Sir binar ce contine parola criptata
     * @throws InvalidKeySpecException
     */
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

    /**
     * Converteste sirul de biti criptat al parolei intr-un string pentru a
     * putea fi stocat in baza de date
     *
     * @param password
     * @param salt
     * @return Parola care se v-a salva in baza de date
     * @throws InvalidKeySpecException
     */
    public static String generateSecurePassword(String password, String salt) throws InvalidKeySpecException {
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        String returnValue = Base64.getEncoder().encodeToString(securePassword);
        return returnValue;
    }

    /**
     * Cripteaza parola din formular si o verifica cu cea din baza de date
     *
     * @param providedPassword
     * @param securedPassword
     * @param salt
     * @return true/false
     * @throws InvalidKeySpecException
     */
    public static boolean verifyUserPassword(String providedPassword,
            String securedPassword, String salt) throws InvalidKeySpecException {

        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);

        // Check if two passwords are equal
        boolean returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }

    /**
     * Returneaza utilizatorul care v-a fi adaugat in baza de date
     *
     * @return user
     */
    public Users getUserData() {
        return user;
    }

}
