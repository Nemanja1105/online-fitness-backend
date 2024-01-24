package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.entities.LogEntity;
import org.unibl.etf.models.enums.LogLevel;
import org.unibl.etf.repositories.LogRepository;
import org.unibl.etf.services.LogService;

import java.util.Date;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    private void log(String message, LogLevel level){
        LogEntity logEntity=new LogEntity();
        logEntity.setId(null);
        logEntity.setDescription(message);
        logEntity.setLevel(level);
        logEntity.setDate(new Date());
        this.logRepository.saveAndFlush(logEntity);
    }

    @Override
    public void info(String message) {
        this.log(message,LogLevel.INFO);
    }

    @Override
    public void warning(String message) {
        this.log(message,LogLevel.WARNING);
    }

    @Override
    public void error(String message) {
        this.log(message,LogLevel.ERROR);
    }

    @Override
    public void fatal(String message) {
        this.log(message,LogLevel.FATAL);
    }
}
