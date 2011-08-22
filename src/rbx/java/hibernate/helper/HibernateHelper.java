/*!
 * @(#) Hibernate Helper v.1.0.1
 * https://github.com/diegoguevara/
 * 
 * Hibernate Helper implemantation
 * 
 * Copyright 2011, Diego Guevara - Ritbox Ltda.
 * Released under dual licensed under the MIT or GPL Version 2 licenses.
 *  
 * Creation Date: Jun.02.2010
 */
package rbx.java.hibernate.helper;

import org.apache.log4j.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate helper reads hibernate configuration file and manage session factory
 * @author Diego Guevara
 * @version 1.0.1
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
            logger.error("Error loading Hibernate Session Factory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
}
