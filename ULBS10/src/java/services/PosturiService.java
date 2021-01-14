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
import java.util.Objects;
import java.util.logging.Level;
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

    /**
     * Creeaza un post cu parametrii primiri si il adauga in baza de date
     *
     * @param id
     * @param denumire
     * @param cerinteMinime
     * @param cerinteOptionale
     * @param dataLimAplic
     * @param user
     */
    @Transactional
    public void AddPost(int id, String denumire, String cerinteMinime, String cerinteOptionale, Date dataLimAplic, Users user) {
        //add in database
        logger.log(Level.INFO, "Adauga postul {0} - {1} in baza de date (cu parametrii)", new Object[]{id, denumire});

        try {
            Posturi post = new Posturi(id, denumire, cerinteMinime, cerinteOptionale, dataLimAplic, user);
            em.persist(post);
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
        logger.info("Returneaza o lista cu toate posturile din baza de date");
        try {
            List<Posturi> posturi = (List<Posturi>) em.createNamedQuery("Posturi.findAll").getResultList();
            return posturi;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Adauga un post in baza de date avant ca parametru un post
     *
     * @param postData
     */
    @Transactional
    public void addPost(Posturi postData) {
        logger.log(Level.INFO, "Adauga postul {0} - {1} in baza de date", new Object[]{postData.getId(), postData.getDenumire()});
        try {
            Posturi post = postData;
            em.persist(post);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Sterge din baza de date postul cu id-ul dat ca parametru
     *
     * @param post
     */
    @Transactional
    public void removePost(Posturi post) {
        logger.log(Level.INFO, "Sterge postul {0} - {1} din baza de date", new Object[]{post.getId(), post.getDenumire()});
        try {
            List<Aplicanti> aplicanti = (List<Aplicanti>) em.createNamedQuery("Aplicanti.findByIdPost").setParameter("idPost", post).getResultList();
            aplicanti.stream().filter((aplicant) -> (Objects.equals(aplicant.getIdPost().getId(), post.getId()))).forEachOrdered(em::remove);

            Posturi removePost = em.find(Posturi.class, post.getId());
            em.remove(removePost);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     * Modifica un post cu datele din parametrii, update-ul in baza de date se
     * face cu metodele set ale entitatii
     *
     * @see Posturi
     * @param id
     * @param denumire
     * @param cerinteMinime
     * @param cerinteOptionale
     * @param dataLimAplic
     */
    @Transactional
    public void editPost(int id, String denumire, String cerinteMinime, String cerinteOptionale, Date dataLimAplic) {
        Posturi post = em.find(Posturi.class, id);
        logger.log(Level.INFO, "Editeaza utilizatorul{0} cu datele {1}-{2}-{3}-{4} in : {5}{6}{7}{8}",
                new Object[]{id, post.getDenumire(), post.getCerinteMinime(), post.getCerinteOptionale(), 
                            post.getDataLimAplic(), denumire, cerinteMinime, cerinteOptionale, dataLimAplic});

        post.setDenumire(denumire);
        post.setCerinteMinime(cerinteMinime);
        post.setCerinteOptionale(cerinteOptionale);
        post.setDataLimAplic(dataLimAplic);
    }
}
