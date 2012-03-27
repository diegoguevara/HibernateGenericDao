/*!
 * @(#) Hibernate Helper v2.0.0
 * https://github.com/diegoguevara/HibernateGenericDao
 * 
 * Hibernate Helper implemantation
 * 
 * Copyright 2012, Diego Guevara
 * Released under dual licensed under the MIT or GPL Version 2 licenses.
 *  
 * Creation Date: Jun.02.2010
 */
package sncode.java.hibernate.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate helper reads hibernate configuration file and manage session factory
 * @author Diego Guevara
 * @version 1.0.2
 */
public class HibernateHelper {
    private static final SessionFactory sessionFactory;
    
    private static String CONFIG_FILE_LOCATION = "hibernate.cfg.xml";
    
    private static final Configuration cfg = new Configuration();
    
    private static Logger logger = Logger.getLogger(HibernateHelper.class.getName());
    
    static {
        try {
            cfg.configure(CONFIG_FILE_LOCATION);
            sessionFactory = cfg.buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Error loading Hibernate Session Factory: " + stacktraceError(ex));
            throw new ExceptionInInitializerError(stacktraceError(ex));
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /**
     * Generate a string with all stacktrace error
     * @param e Exception object
     * @return String
     */
    private static String stacktraceError(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
    
}
