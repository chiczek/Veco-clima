package pl.vecoclima.data.entity;


import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.vecoclima.HibernateConnection;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="[Order]")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;
    @Column(name="number") @Getter @Setter private String number;
    @Column (name="createdDateTime") @Getter @Setter private LocalDateTime createdDateTime;
    @Column (name="paymentForm") @Getter @Setter private String paymentForm;
    @Column (name="status") @Getter @Setter private String status;
    @ManyToOne
    @JoinColumn (name="customerId") @Getter @Setter private Person customer;
    @ManyToOne
    @JoinColumn (name="shoppingCartId") @Getter @Setter private ShoppingCart shoppingCart;
    @Column (name="comment") @Getter @Setter private String comment;

    public Order(){}

    public Order(String number, LocalDateTime createdDateTime, String paymentForm, String status, Person customer, ShoppingCart shoppingCart, String comment) {
        this.number = number;
        this.createdDateTime = createdDateTime;
        this.paymentForm = paymentForm;
        this.status = status;
        this.customer = customer;
        this.shoppingCart = shoppingCart;
        this.comment = comment;
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
    public List<Order> findAll(){
        Session session = HibernateConnection.getSessionFactory().openSession();
        Query<Order> query = session.createQuery("from Order order by id desc");
        List<Order> list = query.list();
        session.close();
        return list;
    }

    public static int findLastNumber(){
        int lastId = 238;
        Session session = HibernateConnection.getSessionFactory().openSession();
        Query<Integer> query = session.createQuery("SELECT number from Order order by id desc");
        query.setMaxResults(1);

        System.out.println("ELO: " + query.uniqueResult());
        session.close();

        return 222;
    }

}
