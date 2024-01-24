package org.unibl.etf.services;

public interface LogService {
    void info(String message);
    void warning(String message);
    void error(String message);
    void fatal(String message);
}
