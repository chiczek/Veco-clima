package pl.vecoclima.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.vecoclima.HibernateConnection;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="dbo.[ShoppingCart]")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;
    @ManyToOne @JoinColumn (name="createdByPersonId") @Getter @Setter private Person createdBy;
    @Column (name="createdDateTime") @Getter @Setter private LocalDateTime createdDateTime;

    @OneToMany(mappedBy="shoppingCart")
    private Set<ShoppingCartLine> items;
    public @Getter @Setter ArrayList<ShoppingCartLine> shoppingCartLines;

    public ShoppingCart(){
        shoppingCartLines = new ArrayList<>();
    }

    public ShoppingCart(Person createdBy, LocalDateTime createdDateTime) {
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
        shoppingCartLines = new ArrayList<>();
    }

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
    public List<ShoppingCart> findAll(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        Query<ShoppingCart> query = session.createQuery("from ShoppingCart order by id desc");
        List<ShoppingCart> list = query.list();
        session.close();
        return list;
    }

    public void add(Product product, int amount) {
        shoppingCartLines.add(new ShoppingCartLine(product, this, product.getGrossPrice(), amount));
    }
}
