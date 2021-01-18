/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Aplicanti;
import entity.Posturi;
import entity.Users;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author Razvan
 */
public class UserService {

    private static final Logger logger = Logger.getLogger(
            "ULBS10.services.UserService");
    @PersistenceContext
    private EntityManager em;

    /**
     * Creeaza un user nou cu ajutorul parametriilor si apoi il introduce in
     * baza de date
     *
     * @param inID  ID-ul utilizatorului ce v-a fi salvat in baza de date
     * @param stEmail   Email-ul ce v-a fi salvat in baza de date
     * @param stPassword    Parola ce v-a fi salvata in baza de date
     * @param stFirstName   Numele ce v-a fi salvat in baza de date
     * @param stLastName    Prenumele ce v-a fi salvat in baza de date
     * @param stStatut  Statutul ce v-a fi salvat in baza de date, implicit Viewer
     */
    @Transactional
    public void addUser(int inID, String stEmail, String stPassword, String stFirstName, String stLastName, String stStatut) {
        //add in database
        logger.log(Level.INFO, "Adauga utilizatorul {0} - {1} in baza de date (cu parametrii)", new Object[]{inID, stFirstName});
        try {
            Users user = new Users(inID, stEmail, stPassword, stFirstName, stLastName, stStatut);
            em.persist(user);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Executa un query care cauta in baza de date toti utilizatorii
     *
     * @return O lista cu toti utilizatorii din baza de date
     */
    @SuppressWarnings("unchecked")
    public List<Users> getAllUsers() {
        logger.info("Returneaza o lista cu toti utilizatorii din baza de date");
        try {
            List<Users> players = (List<Users>) em.createNamedQuery("Users.findAll").getResultList();
            return players;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Adauga un user in baza de date cu EntityManager persist
     *
     * @param userData  Utilizatorul ce v-a fi salvat in baza de date
     */
    @Transactional
    public void addUser(Users userData) {
        logger.log(Level.INFO, "Adauga utilizatorul {0} - {1} in baza de date", 
                new Object[]{userData.getId(), userData.getFirstname()});
        try {
            Users user = userData;
            em.persist(user);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Executa un query care cauta in baza de date toate posturile
     *
     * @return O lista cu toate posturile din baza de date
     */
    @SuppressWarnings("unchecked")
    public List<Posturi> getAllPosts() {
        logger.info("Returneaza o lista cu  toate posturile din baza de date");
        try {
            List<Posturi> posturi = (List<Posturi>) em.createNamedQuery("Posturi.findAll").getResultList();
            return posturi;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     *  Actualizeaza un utilizator din baza de date
     *
     * @param id    ID-ul utilizatorului
     * @param email     Email-ul nou ce v-a fi modificat in baza de date
     * @param firstname     Numele nou ce v-a fi modificat in baza de date
     * @param lastname      Prenumele nou ce v-a fi modificat in baza de date  
     * @param statut    Statutul nou ce v-a fi modificat in baza de date
     */
    @Transactional
    public void editUser(int id, String email, String firstname, String lastname, String statut) {
        Users user = em.find(Users.class, id);  
        
        logger.log(Level.INFO, "Editeaza utilizatorul{0} cu datele {1}-{2}-{3}-{4} in : {5}{6}{7}{8}", 
                new Object[]{id, user.getEmail(), user.getFirstname(), user.getLastname(), user.getStatut(), email, firstname, lastname, statut});
        
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setStatut(statut);
    }

    /**
     *  Sterge un utilizator din baza de date dupa id, si sterge posurile aferente utilizatorului
     *
     * @param id    ID-ul dupa care se v-a sterge utilizatorul din baza de date
     */
    @Transactional
    public void removeUser(int id) {        
        logger.log(Level.INFO, "Sterge utilizatorul cu id-ul {0} din baza de date", id);
        try {
            Users user = em.find(Users.class, id);
            List<Posturi> posturi = em.createNamedQuery("Posturi.findByDeschisDe").setParameter("deschisDe", user).getResultList();
             posturi.forEach(em::remove);
            em.remove(user);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     *  Executa un query care cauta in baza de date toti aplicantii
     *
     * @return      O lista cu toti aplicantii din baza de date
     */
    @Transactional
    public List<Aplicanti> getAllAplicants() {
        logger.info("Returneaza o lista cu toti aplicantii din baza de date");
        try {
            List<Aplicanti> aplicanti = (List<Aplicanti>) em.createNamedQuery("Aplicanti.findAll").getResultList();
            return aplicanti;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
}
