/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Posturi;
import entity.Users;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import model.User;
/**
 *
 * @author Razvan
 */
public class UserService {
    private static final Logger logger = Logger.getLogger(
                "ULBS10.services.UserService");
    @PersistenceContext
    private EntityManager em;
    
    public User getId(int id) {
        switch (id) {
            case 0:
                return new User(0, "vizitator@ulbsibiu.ro", "java", "User", "Vizitator", "viewer");
            case 1:
                return new User(1, "razvan.toghe@ulbsibiu.ro", "java", "Toghe", "Razvan", "user");
            case 2:
                return new User(2, "george.baba@ulbsibiu.ro", "java", "Baba", "George", "recruiter");
            case 3:
                return new User(3, "alexandru.cocan@ulbsibiu.ro", "java", "Cocan", "Alexandru", "humanResources");
            case 4:
                return new User(4, "robert.osan@ulbsibiu.ro", "java", "Osan", "Robert", "directorDepartament");
            case 5:
                return new User(5, "eliza.matei@ulbsibiu.ro", "java", "Matei", "Eliza", "generalDirector");
            default:
                return new User(-1,"","","","","");
        }
    }
    @Transactional
    public void AddUser(int inID, String stEmail, String stPassword, String stFirstName, String stLastName, String stStatut){ 
        //add in database
        logger.info("createUser");

        try {
            Users user = new Users(inID, stEmail, stPassword, stFirstName, stLastName, stStatut);
            em.persist(user);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }            
//                Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/ULBS10", "ulbs10", "ulbs10");
//        Statement stmt = conn.createStatement();
//        stmt.execute("INSERT INTO USERS (ID, EMAIL, PASSWORD, FIRSTNAME, LASTNAME, STATUT) VALUES ('" +inId+"', '" +stEmail+ "', '" +stPassword+ "', '"  +stFirstName+ "', '" +stLastName +
//                "', '" +stStatut+ "')" );
    }
    
    @SuppressWarnings("unchecked")
    public List<Users> getAllPlayers() {
        logger.info("getAllPlayers");

        List<Users> players = null;

        try {
            players = (List<Users>) em.createNamedQuery("Users.findAll").getResultList();
            return players;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
    
    private List<Users> copyPlayersToDetails(List<Users> players) {
         List<Users> detailsList = new ArrayList<Users>();
        Iterator<Users> i = players.iterator();

        while (i.hasNext()) {
            Users users = (Users) i.next();
            Users usersDetails = new Users(
                        users.getId(),
                        users.getEmail(),
                        users.getPassword(),
                        users.getFirstname(),
                        users.getLastname(),
                        users.getStatut());
            detailsList.add(usersDetails);
        }

        return detailsList;
    }

    @Transactional
    public void AddUser(Users userData) {
         //add in database
        logger.info("createUser");

        try {
            Users user = userData;
            em.persist(user);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }            
    }       
}
