/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import controller.UserServlet;
import entity.Aplicanti;
import entity.Posturi;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entity.Users;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

/**
 *
 * @author DxGod
 */
public class Processing {

    private RequestDispatcher dispatcher = null;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private List<Users> users;
    private Posturi post = null;
    private List<Posturi> posturi;
    private Users user;
    private Aplicanti aplicant;
    public String[] alert = new String[2];
    private String salt = "ksdf@#$#T3fsd";
    private HttpSession sesiune;

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private final List<Aplicanti> aplicanti;

    //-----------------------------------------------------------------------------------------
    //-----------------------------------Login--Register-Util----------------------------------
    //-----------------------------------------------------------------------------------------
    /**
     * Constructor cu parametrii
     *
     * @param request
     * @param response
     * @param sesiune
     * @param users
     * @param posturi
     * @param aplicanti
     */
    public Processing(HttpServletRequest request, HttpServletResponse response, HttpSession sesiune, List<Users> users, List<Posturi> posturi, List<Aplicanti> aplicanti) {
        this.request = request;
        this.response = response;
        this.sesiune = sesiune;
        this.users = users;
        this.posturi = posturi;
        this.aplicanti = aplicanti;
    }

    /**
     * Procesul de autentificare a unui utilizator din UserServlet s-a mutat
     * aici pentru curatarea codului. Se verifica adresele de email si parolele
     * din baza de date pentru a gasi persoana in baza de date, daca aceasta
     * exista, daca nu se trimit catre jspx mesaje sugestive
     *
     * @param allPosts
     * @param allAplicants
     * @throws ServletException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public void processLogin(List<Posturi> allPosts, List<Aplicanti> allAplicants) throws ServletException, IOException, InvalidKeySpecException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //Verificare daca logarea este corecta
        boolean logare = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email) && verifyUserPassword(password, users.get(i).getPassword(), salt)) {
                sesiune.setAttribute("user", users.get(i));
                logare = true;
                break;
            }
        }
        //Logare incorecta
        if (!logare) {
            alert[0] = "Email sau parola incorecta!";
            alert[1] = "alert alert-danger";
            request.setAttribute("alert", alert);
            dispatcher = request.getServletContext().getRequestDispatcher("/login/login.jspx");
            dispatcher.forward(request, response);
        } else {    //logare corecta
            sesiune.setAttribute("posts", allPosts);
            sesiune.setAttribute("users", users);
            sesiune.setAttribute("aplicants", allAplicants);
            response.sendRedirect(request.getServletContext() + "./../../dashboard.jspx");
        }

    }

    /**
     * Procesul de inregistrare a unui utilizator din UserServlet s-a mutat aici
     * pentru curatarea codului. Se verifica daca adresa de email exista in baza
     * de date si se continua cu inregistrarea daca nu exista, daca aceasta
     * exista se trimite catre jsps un mesaj sugestiv Se verifica daca campurile
     * sunt goale, iar daca sunt se trimite catre jspx un mesaj sugestiv
     *
     * @param type
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public boolean processSignup(String type) throws ServletException, IOException, InvalidKeySpecException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String statut = "viewer";
        int lastID = 0;
        String securedPassword = generateSecurePassword(password, salt);
        password = securedPassword;

        //Preluare din baza de date a ultimului id
        if (!users.isEmpty()) {
            lastID = users.get(users.size() - 1).getId();
        }

        //Verificare daca exista deja in baza de date dupa email
        boolean existInDB = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) {
                existInDB = true;
            }
        }

        //Verificare daca campurile sunt goale
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            alert[0] = "Va rugam sa complectati toate campurile.";
            alert[1] = "alert alert-danger";
            request.setAttribute("alert", alert);
            if ("signup".equals(type)) {
                dispatcher = request.getServletContext().getRequestDispatcher("/login/signup.jspx");
                dispatcher.forward(request, response);
            } else {
                dispatcher = request.getServletContext().getRequestDispatcher("/../../dashboard.jspx#utilizatori");
            }
            return false;

        } else {
            //Daca nu sunt goale, verific daca nu exista deja in baza de date
            if (!existInDB) {
                alert[0] = "Te-ai inregistrat cu succes!";
                alert[1] = "alert alert-success";
                request.setAttribute("alert", alert);

                gmailSendEmailSSL gmail = new gmailSendEmailSSL();
                String mesaj = "Bine ai venit " + firstName + " " + lastName + ",\n" + "V-ati inregistrat cu succes, va dorim "
                        + "mult noroc si o cariera de succes!";
                try {
                    gmail.sendMail(email, "Inregistrare platforma ULBS10", mesaj);
                } catch (MessagingException ex) {
                    Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
                }
                if ("signup".equals(type)) {
                    dispatcher = request.getServletContext().getRequestDispatcher("/login/login.jspx");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect(request.getServletContext() + "/../../dashboard.jspx#utilizatori");
                }
                user = new Users(++lastID, email, password, firstName, lastName, statut);
                return true;

                //Daca exista in baza de date
            } else {
                alert[0] = "Emailul deja exista in baza de date!";
                alert[1] = "alert alert-danger";
                request.setAttribute("alert", alert);
                if ("signup".equals(type)) {
                    dispatcher = request.getServletContext().getRequestDispatcher("/login/signup.jspx");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect(request.getServletContext() + "/../../dashboard.jspx#utilizatori");
                }
                return false;
            }
        }
    }

    /**
     * Se invalideaza sesiunea utilizatorului si se redirectioneaza catre index
     *
     * @see HttpSession
     * @throws ServletException
     * @throws IOException
     */
    public void processLogout() throws ServletException, IOException {
        sesiune.invalidate();
        //dispatcher = request.getServletContext().getRequestDispatcher("/index.jspx");
        //dispatcher.forward(request, response);
        response.sendRedirect(request.getContextPath() + "/index.jspx");
    }

    /**
     * Returneaza utilizatorul care v-a fi adaugat in baza de date
     *
     * @return user
     */
    public Users getUserData() {
        return user;
    }

    //-----------------------------------------------------------------------------------------
    //-----------------------------------Criptare-parola---------------------------------------
    //-----------------------------------------------------------------------------------------
    /**
     * Cripteaza parola cu o cheie salt, algoritm luat de pe internet
     *
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

    //-----------------------------------------------------------------------------------------
    //-----------------------------------Utilizator--------------------------------------------
    //-----------------------------------------------------------------------------------------
    /**
     * Ia locatia servletului(build/web), creeaza un folder numit cv daca nu
     * exista si salveaza fisierul in folderul cv cu numele ID.pdf, ID -> id-ul
     * utilizatorului care a incarcat fisierul
     *
     * @throws IOException
     * @throws ServletException
     */
    public void processCV() throws IOException, ServletException {
        //Ia locatia servletului
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + "cv";

        //Userul curent
        Users u = (Users) sesiune.getAttribute("user");
        Part o = request.getPart("cv");
        InputStream fileInputStream = o.getInputStream();

        //Numele fisierului ( ID.pdf )
        String fileName = u.getId().toString() + ".pdf";

        //Creare folder
        File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }

        //Creare fisier si salvare
        File fileToSave = new File(uploadFilePath + File.separator + fileName);
        Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);

        //Atribuire alerte pentru front-end
        alert[0] = "Ai incarcat cu succes CV-ul!";
        alert[1] = "alert alert-success";
        sesiune.setAttribute("appAlert", alert);
        response.sendRedirect(request.getServletContext() + "./../../dashboard.jspx#profil");
    }

    /**
     * Sterge un utilizator din baza de date, sterge CV-ul utilizatorului,
     * notificare utilizator prin email
     *
     * @param users
     */
    public void processRemoveUser(List<Users> users) {
        try {
            //Ia locatia servletului (build/web)
            String appPath = request.getServletContext().getRealPath("");
            String uploadFilePath = appPath + File.separator + "cv" + File.separator + user.getId() + ".pdf";

            //Verifica daca exista fisierul ID.pdf la locatia (build/web/cv)
            File fileDelete = new File(uploadFilePath);
            if (fileDelete.exists()) {
                fileDelete.delete();
            }

            //Trimitere email pentru a instiinta utilizatorul
            gmailSendEmailSSL email = new gmailSendEmailSSL();
            try {
                email.sendMail(user.getEmail(), "Stergere cont", "Contul dumneavoastra a fost sters de pe platforma ULBS10 Recrutari");
            } catch (MessagingException ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Setare parametrii pentru front-end
            sesiune.setAttribute("users", users);
            alert[0] = "Utilizator sters cu succes!";
            alert[1] = "alert alert-success";
            sesiune.setAttribute("appAlert", alert);
            response.sendRedirect(request.getServletContext() + "./../../dashboard.jspx#utilizatori");
        } catch (IOException ex) {
            Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Editeaza utilizatorul curent daca si-a modificat datele personale
     *
     * @param id
     * @param email
     * @param firstname
     * @param lastname
     * @param statut
     * @param users
     */
    public void processEditUser(int id, String email, String firstname, String lastname, String statut, List<Users> users) {
        user = (Users) sesiune.getAttribute("user");

        //Daca utilizatorul s-a editat pe el insusi
        if (user.getId().equals(id)) {
            user.setEmail(email);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setStatut(statut);
            sesiune.setAttribute("user", user);
        }

        //Setare parametrii pentru front-end
        sesiune.setAttribute("users", users);
        alert[0] = "Utilizator modificat cu succes!";
        alert[1] = "alert alert-success";
        sesiune.setAttribute("appAlert", alert);
        try {
            response.sendRedirect(request.getServletContext() + "./../../dashboard.jspx#utilizatori");
        } catch (IOException ex) {
            Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //-----------------------------------------------------------------------------------------
    //-----------------------------------Posturi-----------------------------------------------
    //-----------------------------------------------------------------------------------------
    /**
     * Returneaza un post care v-a fi ulterior salvat in baza de date si se face
     * redirectionare daca se cere
     *
     * @param allPosts
     * @param type
     * @return
     */
    public Posturi processNewPost(List<Posturi> allPosts, String type) {
        try {
            user = (Users) sesiune.getAttribute("user");
            int lastID = 0;

            //Se ia ultimul ID al posturilor din baza de date
            if (!posturi.isEmpty()) {
                lastID = posturi.get(posturi.size() - 1).getId();
            }
            request.setAttribute("posturi", posturi);

            //Se ia parametrii din formularul pentru un nou Post
            Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(request.getParameter("dataLimita"));
            String cerinteMinime = request.getParameter("cerinteMinime");
            cerinteMinime = cerinteMinime.replaceAll("\n", "<br />");
            String cerinteOptionale = request.getParameter("cerinteOptionale");
            cerinteOptionale = cerinteOptionale.replaceAll("\n", "<br />");

            //Se creeaza postul care v-a fi returnat
            post = new Posturi(++lastID, request.getParameter("denumire"), cerinteMinime, cerinteOptionale, date1, user);

            //Daca se face redirectionare, se trimit parametrii pentru front-end
            if ("redirect".equals(type)) {
                sesiune.setAttribute("posts", allPosts);
                alert[0] = "Post adaugat cu succes!";
                alert[1] = "alert alert-success";
                sesiune.setAttribute("appAlert", alert);
                response.sendRedirect(request.getServletContext() + "/../dashboard.jspx#posturi");
            }
        } catch (ParseException | IOException ex) {
            Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return post;

    }

    //-----------------------------------------------------------------------------------------
    //-----------------------------------Aplicanti---------------------------------------------
    //-----------------------------------------------------------------------------------------
    /**
     * Returneaza true daca s-a creeat cu succes o aplicatie pentru un post sau
     * false daca utilizatorul a aplicat deja pentru post-ul respectiv
     *
     * @param postAplicare
     * @param existInDB
     * @return
     */
    public boolean processAplicant(Posturi postAplicare, boolean existInDB) {
        //Se salveaza data de azi si utilizatorul curent
        Date todayDate = new Date();
        user = (Users) sesiune.getAttribute("user");

        //Se ia ultimul ID al aplicantilor din baza de date
        int idAplicant = 0;
        if (!aplicanti.isEmpty()) {
            idAplicant = aplicanti.get(aplicanti.size() - 1).getId();
        }

        String[] appAlert = new String[2];

        //Daca nu a aplicat deja
        if (!existInDB) {
            try {
                aplicant = new Aplicanti(++idAplicant, user, postAplicare, todayDate);
                appAlert[0] = "Ai aplicat cu succes pentru acest post!";
                appAlert[1] = "alert alert-success";
                sesiune.setAttribute("aplicants", aplicanti);
                sesiune.setAttribute("appAlert", appAlert);

                response.sendRedirect(request.getServletContext() + "./../dashboard.jspx#posturi");
                return true;
            } catch (IOException ex) {
                Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {    //Daca a aplicat deja
            try {
                appAlert[0] = "Ai aplicat deja pentru acest post!";
                appAlert[1] = "alert alert-danger";
                sesiune.setAttribute("appAlert", appAlert);

                response.sendRedirect(request.getServletContext() + "./../dashboard.jspx#posturi");
                return false;
            } catch (IOException ex) {
                Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    /**
     * Accepta un utilizator care a aplicat la un post si se trimite un email
     * catre acel utilizator pentru a-l
     *
     * @param userSearch
     * @param aplicantSearch
     */
    public void processAcceptAplicant(Users userSearch, Aplicanti aplicantSearch) {
        if (userSearch != null) {
            try {
                Users use = userSearch;
                post = aplicantSearch.getIdPost();
                String mesaj = "Felicitari " + userSearch.getFirstname() + " " + userSearch.getLastname() + ",\n"
                        + "Ati fost acceptat pentru postul " + post.getDenumire() + ",\n\n"
                        + "O zi buna!";
                gmailSendEmailSSL sendemail = new gmailSendEmailSSL();
                sendemail.sendMail(use.getEmail(), "Oferta job", mesaj);
                alert[0] = "Aplicantul a fost acceptat pentru acest post!";
                alert[1] = "alert alert-success";
                sesiune.setAttribute("appAlert", alert);
            } catch (MessagingException ex) {
                Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Returneaza aplicantul pentru a-l adauga ulterior in baza de date
     *
     * @return
     */
    public Aplicanti getAplicantData() {
        return aplicant;
    }
}
