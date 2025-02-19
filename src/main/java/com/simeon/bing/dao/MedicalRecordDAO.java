package com.simeon.bing.dao;

import com.simeon.bing.domain.MedicalRecord;
import com.simeon.bing.utils.JPAUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class MedicalRecordDAO {

    public void batchInsert(List<MedicalRecord> medicalRecords) {
        EntityManager entityManager = JPAUtils.getEntityManger();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            int batchSize = 30; // 每批插入的记录数
            for (int i = 0; i < medicalRecords.size(); i++) {
                entityManager.persist(medicalRecords.get(i));

                // 每插入 batchSize 条记录后，提交事务并清空缓存
                if (i > 0 && i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                    transaction.commit();
                    transaction.begin();
                }
            }

            // 提交剩余的记录
            entityManager.flush();
            entityManager.clear();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}