package org.example.service.serviceImpl;

import org.example.dao.HairdressingServicesDao;
import org.example.dao.daoImpl.HairdressingServicesDaoImpl;
import org.example.model.HairdressingServices;
import org.example.service.HairdressingServicesServise;
import org.hibernate.HibernateError;

import java.util.List;

public class HairdressingServicesServiseImpl implements HairdressingServicesServise {
    HairdressingServicesDao hairdressingServicesDao=new HairdressingServicesDaoImpl();
    @Override
    public boolean addHairdressingServices(HairdressingServices hairdressingServices) {
        System.out.println(hairdressingServices+"HairdressingServicesServiseImpl");
        boolean isAdded = false;
        try {
            if (hairdressingServicesDao.addHairdressingServices(hairdressingServices))
                isAdded = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isAdded;
    }

    @Override
    public boolean updateHairdressingServices(HairdressingServices hairdressingServices) {
        boolean isUpdated = false;
        try {
            if (hairdressingServicesDao.updateHairdressingServices(hairdressingServices))
                isUpdated = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteHairdressingServices(int id) {
        boolean isDeleted = false;
        try {
            if (hairdressingServicesDao.deleteHairdressingServices(id))
                isDeleted = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isDeleted;
    }

    @Override
    public List<HairdressingServices> showHairdressingServices() {
        List<HairdressingServices> hairdressingServices = null;
        try {
            hairdressingServices = hairdressingServicesDao.showHairdressingServices();
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        System.out.println("@Override\n" +
                "    public List<hairdressingServices> "+ hairdressingServices);
        return hairdressingServices;
    }

    @Override
    public HairdressingServices findHairdressingServicesById(int id) {
        HairdressingServices hairdressingServices = null;
        try {
            hairdressingServices = hairdressingServicesDao.findHairdressingServicesById(id);
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return hairdressingServices;
    }
}
