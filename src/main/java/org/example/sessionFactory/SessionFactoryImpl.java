package org.example.sessionFactory;


import lombok.NoArgsConstructor;
import org.example.Hendlers.*;
import org.example.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor
public class SessionFactoryImpl {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {

                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);

                configuration.addAnnotatedClass(Category.class);

                configuration.addAnnotatedClass(HairdressingServices.class);
                configuration.addAnnotatedClass(UpdateHairdressingHandler.class);
                configuration.addAnnotatedClass(DeleteHairdressingHendler.class);

                configuration.addAnnotatedClass(Order.class);
                configuration.addAnnotatedClass(Person.class);
                configuration.addAnnotatedClass(Review.class);
                configuration.addAnnotatedClass(SetOrderHendler.class);
                configuration.addAnnotatedClass(FindPersonHandler.class);
                configuration.addAnnotatedClass(AddReviewHendler.class);


                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}
