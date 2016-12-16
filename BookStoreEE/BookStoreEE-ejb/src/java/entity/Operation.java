/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Daniele
 */
@Entity
public class Operation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String costumerID;

    private String bookID;

    public Operation() {

    }

    public void setcostumerID(String costumerID) {
        this.costumerID = costumerID;
    }

    public String getcostumerID() {
        return costumerID;
    }

    public void setbookID(String bookID) {
        this.bookID = bookID;
    }

    public String getbookID() {
        return bookID;
    }
}
