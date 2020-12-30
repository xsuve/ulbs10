/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Posturi;
import entity.Users;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author DxGod
 */
public class PosturiService {

    private static final Logger logger = Logger.getLogger(
            "ULBS10.services.PosturiService");
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void AddPost(int id, String denumire, String cerinteMinime, String cerinteOptionale, Date dataLimAplic, Users user) {
        //add in database
        logger.info("createPost");

        try {
            Posturi post = new Posturi(id, denumire, cerinteMinime, cerinteOptionale, dataLimAplic, user);
            em.persist(post);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
//                Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/ULBS10", "ulbs10", "ulbs10");
//        Statement stmt = conn.createStatement();
//        stmt.execute("INSERT INTO USERS (ID, EMAIL, PASSWORD, FIRSTNAME, LASTNAME, STATUT) VALUES ('" +inId+"', '" +stEmail+ "', '" +stPassword+ "', '"  +stFirstName+ "', '" +stLastName +
//                "', '" +stStatut+ "')" );
    }

    /**
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Posturi> getAllPosts() {
        logger.info("getAllPosts");

        List<Posturi> posturi = null;

        try {
            posturi = (List<Posturi>) em.createNamedQuery("Posturi.findAll").getResultList();
            return posturi;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Transactional
    public void AddPost(Posturi postData) {
        //add in database
        logger.info("createPost");
        try {
            Posturi post = postData;
            em.persist(post);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Transactional
    public void removePost(int id) {
        logger.info("removePost");

        try {
            Posturi post = em.find(Posturi.class, id);
            em.remove(post);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
}
