package pl.vecoclima.data.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.vecoclima.HibernateConnection;

import java.util.List;

@javax.persistence.Entity
@javax.persistence.Table(name="[Person]")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;
    @Column (name="email") @Getter @Setter private String email;
    @Column (name="firstName") @Getter @Setter private String firstName;
    @Column (name="lastName") @Getter @Setter private String lastName;
    @Column (name="postalCode") @Getter @Setter private String postalCode;
    @Column (name="street") @Getter @Setter private String street;
    @Column (name="buildingNumber") @Getter @Setter private String buildingNumber;
    @Column (name="city") @Getter @Setter private String city;
    @Column (name="isAdmin") @Getter @Setter private boolean admin;
    @Column (name="phone") @Getter @Setter private String phone;

    public Person(){}

    public Person(String email, String firstName, String lastName, String postalCode, String street, String buildingNumber, String city, boolean admin, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.city = city;
        this.admin = admin;
        this.phone = phone;
    }

    public void update(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        session.update(this);
        session.beginTransaction().commit();
        session.close();
    }
    public int insert(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        int id = (Integer) session.save(this);
        session.beginTransaction().commit();
        session.close();
        return id;
    }
    public void delete(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        session.delete(this);
        session.beginTransaction().commit();
        session.close();
    }


    @SuppressWarnings("unchecked")
    public List<Person> findAll(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        Query<Person> query = session.createQuery("from Person order by id desc");
        List<Person> list = query.list();
        session.close();
        return list;
    }
}
