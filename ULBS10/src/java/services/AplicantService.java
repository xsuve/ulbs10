/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Aplicanti;
import java.io.Serializable;
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
     * @param i0
     * @param i1
     * @param date1
     */
    @Transactional
    public void addAplicant(int i, int i0, int i1, Date date1) {
        //add in database
        logger.info("createPost");

        try {
            Aplicanti aplicant = new Aplicanti(i, i0, i1, date1);
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
    public List<Aplicanti> getAplicantsPost(int id) {
        try {
            List<Aplicanti> aplicanti = (List<Aplicanti>) em.createNamedQuery("Aplicanti.findByIdPost").getResultList();
            return aplicanti;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

}
