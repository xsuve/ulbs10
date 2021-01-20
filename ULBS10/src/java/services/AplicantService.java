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
     * @param i     ID-ul ce v-a fi salvat in baza de date
     * @param u     Utilizatorul
     * @param p     Postul
     * @param date1 Data aplicarii (data de azi)
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
     *  Executa un query care returneaza o lista cu toti aplicantii din baza de date
     *
     * @return  Lista cu aplicantii din baza de date
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
     * @param aplicantData  Aplicantul care v-a fi adaugat in baza de date
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

    /**
     *  Executa un query pentru a cauta un Aplicant dupa ID, apoi sterge acea
     * inregistrare din baza de date
     *
     * @param id    ID-ul utilizatorului ce v-a fi sters
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
     *  Executa un query pentru a cauta in baza de date un aplicant dupa idPost si idUser
     *
     * @param idUser    Utilizatorul care se cauta in Aplicanti
     * @param idPost    Postul care se cauta in Aplicanti
     * @return      <code>true</code> daca s-a gasit un rezultat in baza de date dupa specificatiile
     *              pe care le-am vrut;
     *              <code>false</code> daca nu s-a gasit.
     */
    @Transactional
    public boolean existaAplicantByIdUserIdPost(Users idUser, Posturi idPost) {
        logger.log(Level.INFO, "Verifica daca utilizatorul {0} - {1} a mai aplicat ", new Object[]{idUser.getId(), idUser.getFirstname()});
        try {
            Aplicanti aplicant = (Aplicanti) em.createNamedQuery("Aplicanti.findByIdPostAndIDUser").
                    setParameter("idUser", idUser).setParameter("idPost", idPost).getSingleResult();

        } catch (Exception ex) {
            //Daca in baza de date, nu avem un aplicant (nu exista un utilizator idUser care a aplicat la post-ul ID)
            return false;
        }

        //returneaza true daca exista, false daca nu
        return true;
    }

    /**
     *  Executa un query pentru a cauta in baza de date un Post dupa un ID
     *
     * @param id    ID-ul dupa care se v-a cauta Postul in baza de date
     * @return      Postul care a avut ID-ul cerut.
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
     *  Executa un query pentru a cauta in baza de date un aplicant dupa ID
     *
     * @param id    ID-ul dupa care se v-a cauta Aplicantul in baza de date
     * @return  Aplicantul care are ID-ul specific;
     *          <code>null</code> daca nu exista.
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
