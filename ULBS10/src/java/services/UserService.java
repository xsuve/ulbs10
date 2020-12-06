/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import model.User;
/**
 *
 * @author Razvan
 */
public class UserService {
    public User getId(int id) {
        switch (id) {
            case 0:
                return new User(0, "vizitator@ulbsibiu.ro", "java", "User", "Vizitator", "viewer");
            case 1:
                return new User(1, "razvan.toghe@ulbsibiu.ro", "java", "Toghe", "Razvan", "user");
            case 2:
                return new User(2, "george.baba@ulbsibiu.ro", "java", "Baba", "George", "recruiter");
            case 3:
                return new User(3, "alexandru.cocan@ulbsibiu.ro", "java", "Cocan", "Alexandru", "hr");
            case 4:
                return new User(4, "robert.osan@ulbsibiu.ro", "java", "Osan", "Robert", "dd");
            case 5:
                return new User(5, "eliza.matei@ulbsibiu.ro", "java", "Matei", "Eliza", "gd");
            default:
                return new User(-1,"","","","","");
        }
    }
}
