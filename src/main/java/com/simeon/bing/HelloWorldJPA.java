package com.simeon.bing;

import com.simeon.bing.domain.Message;
import com.simeon.bing.utils.JPAUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class HelloWorldJPA {

    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BING");
//        EntityManager entityManager = emf.createEntityManager();

        EntityManager entityManager = JPAUtils.getEntityManger();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Message message = new Message();
        message.setText("Hello World");
        entityManager.persist(message);

        transaction.commit();

        message = entityManager.find(Message.class, 1L);
        System.out.println("message="+message.getText());

        entityManager.close();
        JPAUtils.closeEntityManagerFactory();
    }
}
