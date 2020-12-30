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
     * @param id
     * @param cv
     * @param data_Aplic
     * @param obs
     * @param id_Obs
     */
    @Transactional
    public void addAplicant(int id, Serializable cv, Date data_Aplic, String obs, int id_Obs){ 
        //add in database
        logger.info("createPost");

        try {
            Aplicanti  aplicant = new Aplicanti(id, cv, data_Aplic, obs, id_Obs);
            em.persist(aplicant);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }  
    }
    
    /**
     * Returneaza o lista cu toti aplicantii din baza de date
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
     * @param aplicantData
     */
    @Transactional
    public void addAplicant(Aplicanti aplicantData) {
         //add in database
        logger.info("createPost");
        try {
            Aplicanti aplicant = aplicantData;
            em.persist(aplicant);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }            
    }
    
}
