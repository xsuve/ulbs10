/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Aplicanti;
import entity.Posturi;
import entity.Users;
import java.util.Date;
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
public class AplicantService {

    private static final Logger logger = Logger.getLogger(
            "ULBS10.services.PosturiService");
    @PersistenceContext
    private EntityManager em;

    /**
     * Creeaza un aplicant cu parametrii primiti si il adauga in baza de date
     *
     * @param i
     * @param u
     * @param p
     * @param date1
     */
    @Transactional
    public void addAplicant(int i, Users u, Posturi p, Date date1) {
        //add in database
        logger.log(Level.INFO, "Adauga aplicantul {0} - {1} in baza de date",
                new Object[]{u.getId(), u.getFirstname()});

        try {
            Aplicanti aplicant = new Aplicanti(i, u, p, date1);
            em.persist(aplicant);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Returneaza o lista cu toti aplicantii din baza de date
     *
     * @return Aplicantii din baza de date
     */
    @SuppressWarnings("unchecked")
    public List<Aplicanti> getAllAplicants() {
        logger.info("Returneaza o lista cu toti aplicantii din baza de date");
        try {
            List<Aplicanti> aplicanti = (List<Aplicanti>) em.createNamedQuery("Aplicanti.findAll").getResultList();
            return aplicanti;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Adauga un aplicant dat ca parametru in baza de date
     *
     * @param aplicantData
     */
    @Transactional
    public void addAplicant(Aplicanti aplicantData) {
        logger.log(Level.INFO, "Adauga un aplicantul {0} - {1} in baza de date",
                new Object[]{aplicantData.getIdUser().getId(), aplicantData.getIdUser().getFirstname()});
        try {
            em.persist(aplicantData);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /*
     *  Sterge aplicantul din baza de date care are id-ul trimis ca parametru
     *
     * @param id
     */
    @Transactional
    public void removeAplicant(int id) {
        if (id != -1) {
            logger.log(Level.INFO, "Sterge un aplicant din baza de date dupa ID = {0}", id);
            try {
                Aplicanti aplicant = em.find(Aplicanti.class, id);
                em.remove(aplicant);
            } catch (Exception ex) {
                throw new EJBException(ex);
            }
        }
    }

    /**
     * Verifica daca utilizatorul a mai aplicat la acel post
     *
     * @param idUser
     * @param id
     * @return
     */
    @Transactional
    public boolean existaAplicantByIdUser(Users idUser, int id) {
        logger.log(Level.INFO, "Verifica daca utilizatorul {0} - {1} a mai aplicat ", new Object[]{idUser.getId(), idUser.getFirstname()});
        try {
            Posturi idPost = em.find(Posturi.class, id);
            Aplicanti aplicant_user = (Aplicanti) em.createNamedQuery("Aplicanti.findByIdUser").setParameter("idUser", idUser).getSingleResult();
            Aplicanti aplicant_post = (Aplicanti) em.createNamedQuery("Aplicanti.findByIdPost").setParameter("idPost", idPost).getSingleResult();

        } catch (Exception ex) {
            return false;
        }

        //returneaza true daca exista, false daca nu
        return true;
    }

    /**
     * Returneaza postul din baza de date care are id-ul trimis ca parametru
     *
     * @param id
     * @return
     */
    @Transactional
    public Posturi findPostByID(int id) {
        logger.log(Level.INFO, "Cauta postul cu id-ul {0}", id);
        Posturi post;
        try {
            post = em.find(Posturi.class, id);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        return post;
    }

    /**
     * Verifica daca exista un utilizator care a aplicat la un post, si
     * returneaza acel utilizator
     *
     * @param id
     * @return
     */
    @Transactional
    public Users existaUserByAplicantByID(int id) {
        logger.log(Level.INFO, "Verifica daca exista un utilizatorator dupa id-ul {0} din aplicantii bazei de date",
                new Object[]{id});
        Users user;
        try {
            Aplicanti aplicant = em.find(Aplicanti.class, id);
            user = (Users) em.createNamedQuery("Users.findById").setParameter("id", aplicant.getIdUser().getId()).getSingleResult();
        } catch (Exception ex) {
            return null;
        }
        //returneaza user daca exista, null daca nu
        return user;
    }

    /**
     * Cauta un aplicant in baza de date dupa ID
     *
     * @param id
     * @return
     */
    @Transactional
    public Aplicanti findByID(int id) {
        logger.log(Level.INFO, "Cauta aplicantul care are id-ul {0} ", new Object[]{id});
        Aplicanti aplicant;
        try {
            aplicant = em.find(Aplicanti.class, id);
        } catch (Exception ex) {
            return null;
        }

        //returneaza true daca exista, false daca nu
        return aplicant;
    }

}
