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
     *  Creeaza un Post folosind parametrii, post ce v-a fi adaugat in baza de date
     *
     * @param id    ID-ul post-ului
     * @param denumire  Denumirea postului
     * @param cerinteMinime Cerintele minime ale postului
     * @param cerinteOptionale  Cerintele optionale ale postului
     * @param dataLimAplic  Data limita pentru a aplica la un post
     * @param user  Utilizatorul care a aplicat la post
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
     *  Executa un querry pentru a cauta in baza de date toate posturile
     *
     * @return O lista cu toate posturile din baza de date
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
     * @param postData  Post-ul ce v-a fi adaugat in baza de date
     */
    @Transactional
    public void addPost(Posturi postData) {
        logger.log(Level.INFO, "Adauga postul {0} - {1} in baza de date", new Object[]{postData.getId(), postData.getDenumire()});
        try {
            em.persist(postData);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    /**
     *  Executa un querry pentru a cauta in baza de date aplicantii care au aplicat la un post,
     * apoi sterge fiecare aplicant care a aplicat la acel post si postul.
     *
     * @param post  Postul care v-a fi sters din baza de date, si aplicantii corespunzatori postului
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
     * @param id    ID-ul postului
     * @param denumire  Denumirea noua ce v-a fi salvata in baza de date
     * @param cerinteMinime Cerintele minime noi ce vor fi salvate in baza de date
     * @param cerinteOptionale Cerintele optionale noi ce vor fi salvate in baza de date
     * @param dataLimAplic Data noua limita pentru care se poate aplica la post
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
