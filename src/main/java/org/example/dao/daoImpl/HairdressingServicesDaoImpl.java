package org.example.dao.daoImpl;


import org.example.dao.HairdressingServicesDao;
import org.example.model.HairdressingServices;
import org.example.sessionFactory.SessionFactoryImpl;
import org.example.utils.SessionUtils;

import java.util.List;

public class HairdressingServicesDaoImpl implements HairdressingServicesDao {

        @Override
        public boolean addHairdressingServices(HairdressingServices HairdressingServices){
            System.out.println(HairdressingServices+"    HairdressingServicesDaoImpl");

            return SessionUtils.saveEntity(HairdressingServices);
        }

        @Override
        public boolean updateHairdressingServices(HairdressingServices HairdressingServices){

            return SessionUtils.updateEntity(HairdressingServices);
        }

        @Override
        public  boolean deleteHairdressingServices(int id){
            return SessionUtils.deleteEntity(id, HairdressingServices.class);
        }

        @Override
        public List<HairdressingServices> showHairdressingServices(){
            System.out.println((List<HairdressingServices>) SessionFactoryImpl.getSessionFactory()
                    .openSession()
                    .createQuery("FROM HairdressingServices")
                    .list()+" LIST");
            return (List<HairdressingServices>) SessionFactoryImpl.getSessionFactory()
                    .openSession()
                    .createQuery("FROM HairdressingServices")
                    .list();
        }//есть некоторые вопросы к from HairdressingServices

        @Override
        public  HairdressingServices findHairdressingServicesById(int id){
            return SessionUtils.find(HairdressingServices.class, id, "id");
        }
    }

