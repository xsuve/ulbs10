/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Aplicanti;
import entity.Posturi;
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
    
    @Transactional
    public void AddAplicant(int id, Serializable cv, Date data_Aplic, String obs, int id_Obs){ 
        //add in database
        logger.info("createPost");

        try {
            Aplicanti  aplicant = new Aplicanti(id, cv, data_Aplic, obs, id_Obs);
            em.persist(aplicant);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }  
    }
    
    
        @SuppressWarnings("unchecked")
    public List<Aplicanti> getAllAplicants() {
        logger.info("getAllPosts");

        List<Aplicanti> aplicanti = null;

        try {
            aplicanti = (List<Aplicanti>) em.createNamedQuery("Aplicanti.findAll").getResultList();
            return aplicanti;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
    
        @Transactional
    public void AddAplicant(Aplicanti aplicantData) {
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
