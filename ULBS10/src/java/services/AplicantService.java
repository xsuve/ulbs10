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
        logger.info("createPost");

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
        logger.info("getAllPosts");
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
        logger.info("createPost");
        try {
            Aplicanti aplicant = aplicantData;
            em.persist(aplicant);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Transactional
    public void removeAplicant(int id) {
         logger.info("removeAplicant");
         try {
            Aplicanti aplicant = em.find(Aplicanti.class,id);
            em.remove(aplicant);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }


}
