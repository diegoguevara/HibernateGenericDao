/*!
 * @(#) Hibernate GenericDao v3.0.0
 * https://github.com/diegoguevara/HibernateGenericDao
 * 
 * Hibernate Generic DAO simple implemantation
 * 
 * Copyright 2015, Diego Guevara
 * Released under dual licensed under the MIT or GPL Version 2 licenses.
 *  
 * Creation Date: Jun.2011
 * Modified Date: Sep.2015
 * 
 */

package co.eventt.java.hibernate.dao;

import co.eventt.java.hibernate.exception.DaoException;
import co.eventt.java.hibernate.helper.HibernateUtil;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

/**
 *
 * @author diegoguevara
 */
public class GenericDao {
    
    // retrieve section
    /**
     * retrieve database data with given parameters
     * @param objName_      String with hibernate mapped object name
     * @param criterias_    List of hibernate criterion objects
     * @param orderbys_     List of hibernate order objects
     * @param first_        Integer with first row
     * @param max_          Integer with max rows in result List
     * @param projections_  List of hibernate projection objects
     * @param session_      Hibernate Session object
     * @return List of hbernate database mapped objects
     * @throws DaoException 
     */
    public static List retrieve(String objName_, List<Criterion> criterias_, List<Order> orderbys_, Integer first_, Integer max_, List<Projection> projections_, Session session_) throws DaoException {
        List data = null;
        Transaction tx = null;
        boolean globaltx = true;
        try {
            if (session_ == null) {
                session_ = startSession();
            }
            if (session_.getTransaction() == null || !session_.getTransaction().isActive()) {
                tx = session_.beginTransaction();
                globaltx = false;
            }

            Criteria c;
            c = session_.createCriteria(objName_);

            // add criterias
            if (criterias_ != null) {
                for (int i = 0; i < criterias_.size(); i++) {
                    c.add(criterias_.get(i));
                }
            }

            // add projections
            if (projections_ != null) {
                ProjectionList pl = Projections.projectionList();
                for (int i = 0; i < projections_.size(); i++) {
                    pl.add(projections_.get(i), "p_" + i);
                }
                c.setProjection(pl);
            }

            //add max and first
            if (first_ != null) {
                c.setFirstResult(first_);
            }
            if (max_ != null) {
                c.setMaxResults(max_);
            }


            // add order
            if (orderbys_ != null) {
                for (int i = 0; i < orderbys_.size(); i++) {
                    c.addOrder(orderbys_.get(i));
                }
            }

            data = c.list();

            if (!globaltx) {
                tx.commit();
            }

        } catch (Exception e) {
            //logger.error("Error loading data : " + stacktraceError(e));
            throw new DaoException("Error loading data: " + stacktraceError(e));
        } finally {
            if (!globaltx) {
                if (session_ != null && session_.isOpen()) {
                    if (tx != null && tx.isActive()) {
                        session_.flush();
                    }
                    session_.close();
                    session_ = null;
                }
            }
        }

        return data;
    }

    public static List retrieve(String objName_) throws DaoException {
        return retrieve(objName_, null, null, null, null, null, null);
    }

    public static List retrieve(String objName_, List<Criterion> criterias_) throws DaoException {
        return retrieve(objName_, criterias_, null, null, null, null, null);
    }

    public static List retrieve(String objName_, List<Criterion> criterias_, Session session_) throws DaoException {
        return retrieve(objName_, criterias_, null, null, null, null, session_);
    }
    
    public static List retrieve(String objName_, List<Criterion> criterias_, List<Order> orderbys_) throws DaoException {
        return retrieve(objName_, criterias_, orderbys_, null, null, null, null);
    }

    public static List retrieve(String objName_, List<Criterion> criterias_, List<Order> orderbys_, Session session_) throws DaoException {
        return retrieve(objName_, criterias_, orderbys_, null, null, null, session_);
    }
    
    
    /**
     * Find mapped data object by primary key
     * @param id_       Serializable with mapped object od field
     * @param objName_  String with mapped object name
     * @param session_  Hibernate session object
     * @return          Mapped object with query result
     * @throws DaoException 
     */
    public static Object findByPK(Serializable id_, String objName_, Session session_) throws DaoException {
        Transaction tx = null;
        Object result = null;
        boolean globaltx = true;

        try {
            if (session_ == null) {
                session_ = startSession();
            }
            if (session_.getTransaction() == null || !session_.getTransaction().isActive()) {
                tx = session_.beginTransaction();
                globaltx = false;
            }

            result = session_.load(objName_, id_, LockMode.READ); // load has been deprecated

            if (!globaltx) {
                tx.commit();
            }
        } catch (Exception e) {
            //logger.error("Error retriving by PK: " + stacktraceError(e));
            throw new DaoException("Error retriving by PK: " + stacktraceError(e));
        } finally {
            if (!globaltx) {
                if (session_ != null && session_.isOpen()) {
                    if (tx != null && tx.isActive()) {
                        session_.flush();
                    }
                    session_.close();
                    session_ = null;
                }
            }
        }

        return result;
    }

    /**
     * overwrite findByPK method 
     * @param id_       Serializable of object mapped id field
     * @param objName_  String with mapped object name
     * @return
     * @throws DaoException
     * @see findByPK
     */
    public static Object findByPK(Serializable id_, String objName_) throws DaoException {
        return findByPK(id_, objName_, null);
    }

    /**
     * Save method create a new row in database
     * @param data_     Mapped object with data to save
     * @param session_  Hibernate session object
     * @return          Saved object
     * @throws DaoException 
     */
    public static Object save(Object data_, Session session_) throws DaoException {
        Transaction tx = null;
        Object result = null;
        boolean globaltx = true;

        try {
            if (session_ == null) {
                session_ = startSession();
            }
            if (session_.getTransaction() == null || !session_.getTransaction().isActive()) {
                tx = session_.beginTransaction();
                globaltx = false;
            }

            result = session_.save(data_);

            if (!globaltx) {
                tx.commit();
            }
        } catch (Exception e) {
            //logger.error("Error saving data : " + stacktraceError(e));
            throw new DaoException("Error saving data" + stacktraceError(e));
        } finally {
            if (!globaltx) {
                if (session_ != null && session_.isOpen()) {
                    if (tx != null && tx.isActive()) {
                        session_.flush();
                    }
                    session_.close();
                    session_ = null;
                }
            }
        }

        return result;
    }

    /**
     * overwrite save method
     * @param data_ Mapped object with data to save
     * @return      Saved object
     * @throws DaoException 
     */
    public static Object save(Object data_) throws DaoException {
        return save(data_, null);
    }

    /**
     * Update mapped object
     * @param data_     mapped object with data for update, must include object id
     * @param session_  Hibernate session object
     * @return          updated mapped object
     * @throws DaoException 
     */
    public static Object update(Object data_, Session session_) throws DaoException {
        Transaction tx = null;
        Object result = null;
        boolean globaltx = true;

        try {
            if (session_ == null) {
                session_ = startSession();
            }
            if (session_.getTransaction() == null || !session_.getTransaction().isActive()) {
                tx = session_.beginTransaction();
                globaltx = false;
            }

            session_.update(data_);

            result = data_;

            if (!globaltx) {
                tx.commit();
            }
        } catch (Exception e) {
            tx.rollback();
            //logger.error("Error updating data : " + stacktraceError(e));
            throw new DaoException("Error updating data" + stacktraceError(e));
        } finally {
            if (!globaltx) {
                if (session_ != null && session_.isOpen()) {
                    if (tx != null && tx.isActive()) {
                        session_.flush();
                    }
                    session_.close();
                    session_ = null;
                }
            }
        }

        return result;
    }

    /**
     * overwrite update method
     * @param data_ mapped object with data for update, must include object id
     * @return
     * @throws DaoException 
     */
    public static Object update(Object data_) throws DaoException {
        return update(data_, null);
    }

    /**
     * Delete method
     * @param data_     Mapped object with delete criterias
     * @param session_  Hibernate session object
     * @throws DaoException 
     */
    public static void delete(Object data_, Session session_) throws DaoException {
        Transaction tx = null;
        boolean globaltx = true;

        try {
            if (session_ == null) {
                session_ = startSession();
            }
            if (session_.getTransaction() == null || !session_.getTransaction().isActive()) {
                tx = session_.beginTransaction();
                globaltx = false;
            }

            session_.delete(data_);

            if (!globaltx) {
                tx.commit();
            }
        } catch (Exception e) {
            //logger.error("Error deleting data: " + stacktraceError(e));
            throw new DaoException("Error deleting data: " + stacktraceError(e));
        } finally {
            if (!globaltx) {
                if (session_ != null && session_.isOpen()) {
                    if (tx != null && tx.isActive()) {
                        session_.flush();
                    }
                    session_.close();
                    session_ = null;
                }
            }
        }
    }

    /**
     * overwrite delete method
     * @param data_ Mapped object with delete criterias
     * @throws DaoException 
     */
    public static void delete(Object data_) throws DaoException {
        delete(data_, null);
    }

    /**
     * Tuncate a mapped table with hibernate
     * @param objName_  String with mapped Object
     * @param session_  Hibernate session object
     * @return true if truncate is correct
     * @throws DaoException 
     */
    public static boolean truncate(String objName_, Session session_) throws DaoException {
        Transaction tx = null;
        boolean globaltx = true;
        try {
            if (session_ == null) {
                session_ = startSession();
            }
            if (session_.getTransaction() == null || !session_.getTransaction().isActive()) {
                tx = session_.beginTransaction();
                globaltx = false;
            }

            String hql = String.format("delete from %s", objName_);
            session_.createQuery(hql).executeUpdate();

            if (!globaltx) {
                tx.commit();
            }

        } catch (Exception e) {
            //logger.error("Error deleting data with hql: " + stacktraceError(e));
            throw new DaoException("Error deleting data with hql: " + stacktraceError(e));
        } finally {
            if (!globaltx) {
                if (session_ != null && session_.isOpen()) {
                    if (tx != null && tx.isActive()) {
                        session_.flush();
                    }
                    session_.close();
                    session_ = null;
                }
            }
        }
        return true;
    }

    /**
     * Run a Native SQL Query supporting NamedParameters
     * @param sql_      String with Native SQL Query
     * @param params_   Map with Named Parameters for SQL Query
     * @param session_  Hibernate Session Object
     * @return List of Objects with query result
     * @throws DaoException 
     * 
     */
    public static List sqlQuery(String sql_, Map params_, Session session_) throws DaoException {
        Transaction tx = null;
        boolean globaltx = true;
        List result = null;
        try {
            if (session_ == null) {
                session_ = startSession();
            }
            if (session_.getTransaction() == null || !session_.getTransaction().isActive()) {
                tx = session_.beginTransaction();
                globaltx = false;
            }

            Query query = session_.createSQLQuery(sql_);
            
            if ( params_ != null)
                query.setProperties(params_);
            
            result = query.list();
            
            if (!globaltx) {
                tx.commit();
            }

        } catch (Exception e) {
            //logger.error("Error running sqlquery: " + stacktraceError(e));
            throw new DaoException("Error running sqlquery: " + stacktraceError(e));
        } finally {
            if (!globaltx) {
                if (session_ != null && session_.isOpen()) {
                    if (tx != null && tx.isActive()) {
                        session_.flush();
                    }
                    session_.close();
                    session_ = null;
                }
            }
        }
        return result;
    }
    
    /**
     * Overwrite sqlQuery method
     * @param sql_  String with Native SQL Query
     * @return  List of Objects with query result
     * @throws DaoException 
     */
    public static List sqlQuery(String sql_) throws DaoException {
        return sqlQuery(sql_,null, null);
    }
    
    /**
     * Overwrite sqlQuery method
     * @param sql_      String with Native SQL Query
     * @param session_  Hibernate Session Object
     * @return          List of Objects with query result
     * @throws DaoException 
     */
    public static List sqlQuery(String sql_, Session session_) throws DaoException {
        return sqlQuery(sql_,null, session_);
    }
    
    /**
     * Overwrite sqlQuery method
     * @param sql_      String with Native SQL Query
     * @param params_   Map with Named Parameters for SQL Query
     * @return          List of Objects with query result
     * @throws DaoException 
     */
    public static List sqlQuery(String sql_, Map params_) throws DaoException {
        return sqlQuery(sql_,params_,null);
    }
    
    
    /**
     * Run HQL Queries
     * @param hql_      HQL string
     * @param params_   Map with named parameters
     * @param session_  Hibernate session
     * @return
     * @throws DaoException 
     */
    public static List hqlQuery(String hql_, Map params_, Session session_) throws DaoException {
        Transaction tx = null;
        boolean globaltx = true;
        List result = null;
        try {
            if (session_ == null) {
                session_ = startSession();
            }
            if (session_.getTransaction() == null || !session_.getTransaction().isActive()) {
                tx = session_.beginTransaction();
                globaltx = false;
            }

            Query query = session_.createQuery(hql_);
            
            if ( params_ != null)
                query.setProperties(params_);
            
            result = query.list();
            
            if (!globaltx) {
                tx.commit();
            }

        } catch (Exception e) {
            //logger.error("Error running hqlquery: " + stacktraceError(e));
            throw new DaoException("Error running hqlquery: " + stacktraceError(e));
        } finally {
            if (!globaltx) {
                if (session_ != null && session_.isOpen()) {
                    if (tx != null && tx.isActive()) {
                        session_.flush();
                    }
                    session_.close();
                    session_ = null;
                }
            }
        }
        return result;
    }
    
    /**
     * overwrite hqlQuery method to allow just with hql string
     * @param hql_  HQL String
     * @return      List of objects with result
     * @throws DaoException 
     */
    public static List hqlQuery(String hql_) throws DaoException {
        return hqlQuery(hql_, null, null);
    }
    
    /**
     * overwrite hqlQuery method with hql string and session
     * @param hql_      HQL String
     * @param session_  Hibernate session
     * @return          Listo of objects with result
     * @throws DaoException 
     */
    public static List hqlQuery(String hql_, Session session_) throws DaoException {
        return hqlQuery(hql_, null, session_);
    }
    
    /**
     * overwrite hqlQuery method with hql string and Map parameters
     * @param hql_      HQL String
     * @param params_   Map with Named parameters
     * @return          List of objects with result
     * @throws DaoException 
     */
    public static List hqlQuery(String hql_, Map params_) throws DaoException {
        return hqlQuery(hql_, params_, null);
    }
    
    
    /**
     * Call Hibernate helper session factory and opens a session
     * @return Hibernate Session
     */
    public static Session startSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    /**
     * Generate a string with all stacktrace error
     * @param e Exception object
     * @return String
     */
    private static String stacktraceError(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
