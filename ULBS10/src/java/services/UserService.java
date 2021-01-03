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
     * @param inID
     * @param stEmail
     * @param stPassword
     * @param stFirstName
     * @param stLastName
     * @param stStatut
     */
    @Transactional
    public void addUser(int inID, String stEmail, String stPassword, String stFirstName, String stLastName, String stStatut) {
        //add in database
        logger.info("createUser din parametrii");
        try {
            Users user = new Users(inID, stEmail, stPassword, stFirstName, stLastName, stStatut);
            em.persist(user);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Returneaza o lista cu toti utilizatorii din baza de date
     *
     * @return Utilizatorii din baza de date
     */
    @SuppressWarnings("unchecked")
    public List<Users> getAllUsers() {
        logger.info("getAllUsers");
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
     * @param userData
     */
    @Transactional
    public void addUser(Users userData) {
        logger.info("createUser din user");
        try {
            Users user = userData;
            em.persist(user);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Returneaza o lista cu toate posturile din baza de date
     *
     * @return Posturile din baza de date
     */
    @SuppressWarnings("unchecked")
    public List<Posturi> getAllPosts() {
        logger.info("getAllPosts");
        try {
            List<Posturi> posturi = (List<Posturi>) em.createNamedQuery("Posturi.findAll").getResultList();
            return posturi;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Transactional
    public void editUser(int id, String email, String firstname, String lastname, String statut) {
        Users user = em.find(Users.class, id);
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setStatut(statut);
    }

    @Transactional
    public void removeUser(int id) {
        logger.info("removeUser");
        try {
            Users user = em.find(Users.class, id);
            em.remove(user);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
    @Transactional
    public List<Aplicanti> getAllAplicants() {
        try {
            List<Aplicanti> aplicanti = (List<Aplicanti>) em.createNamedQuery("Aplicanti.findAll").getResultList();
            return aplicanti;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
}
