package pl.vecoclima.data.entity;



import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.vecoclima.HibernateConnection;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;




@Entity
@Table(name="[Product]")

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter private int id;
    @Column (name="name") @Getter @Setter private String name;
    @Column (name="imageUrl") @Getter @Setter private String imageURL;
    @Column(name="descriptionShort") @Getter @Setter private String descriptionShort;
    @Column (name="descriptionLong") @Getter @Setter private String descriptionLong;
    @Column (name="grossPrice") @Getter @Setter private BigDecimal grossPrice;
    @Column (name="available") @Getter @Setter private boolean available;
    @Column (name="addedByName") @Getter @Setter String addedByName;
    @Column (name="addedDateTime") @Getter @Setter private LocalDateTime addedDateTime;


    public Product(){}

    public void update(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        session.update(this);
        session.beginTransaction().commit();
        session.close();
    }
    public void insert(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        session.save(this);
        session.beginTransaction().commit();
        session.close();
    }
    public void delete(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        session.delete(this);
        session.beginTransaction().commit();
        session.close();
    }


    @SuppressWarnings("unchecked")
    public static List<Product> findAll(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        Query<Product> query = session.createQuery("SELECT p FROM " + Product.class.getSimpleName() + " p");
        List<Product> list = query.list();
        session.close();
        return list;
    }

}


