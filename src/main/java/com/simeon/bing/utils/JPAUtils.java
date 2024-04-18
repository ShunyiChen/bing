package com.simeon.bing.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtils {
    /**
     * 同一个应用中，应该保证只有一个实例工厂
     */
    public static EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

    /**
     * 获得实体管理工厂
     *
     */
    private static EntityManagerFactory createEntityManagerFactory(){
        return Persistence.createEntityManagerFactory("BING");
    }

    /**
     * 获得实体管理类对象
     *
     */
    public static EntityManager getEntityManger(){
        return entityManagerFactory.createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }
}
