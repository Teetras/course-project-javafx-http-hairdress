package org.example.dao;

import org.example.model.HairdressingServices;

import java.util.List;

public interface HairdressingServicesDao {
    boolean addHairdressingServices(HairdressingServices hairdressingServices);

    boolean updateHairdressingServices(HairdressingServices hairdressingServices);

    boolean deleteHairdressingServices(int id);

    List<HairdressingServices> showHairdressingServices();

    HairdressingServices findHairdressingServicesById(int id);}
