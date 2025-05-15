package pl.vecoclima.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.vecoclima.HibernateConnection;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="[ShoppingCartLine]")
public class ShoppingCartLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private int id;
     @ManyToOne @JoinColumn (name="productId") @Getter @Setter private Product product;
     @ManyToOne @JoinColumn (name="shoppingCartId") @Getter @Setter private ShoppingCart shoppingCart;
    @Column (name="priceGross") @Getter @Setter private BigDecimal priceGross;
    @Column (name="amount") @Getter @Setter private int amount;

    public ShoppingCartLine(){}

    public ShoppingCartLine(Product product, ShoppingCart shoppingCart, BigDecimal priceGross, int amount) {
        this.product = product;
        this.shoppingCart = shoppingCart;
        this.priceGross = priceGross;
        this.amount = amount;
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
    public List<ShoppingCartLine> findAll(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        Query<ShoppingCartLine> query = session.createQuery("from ShoppingCartLine order by id desc");
        List<ShoppingCartLine> list = query.list();
        session.close();
        return list;
    }
}
