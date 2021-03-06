/*!
 * @(#) DaoException.java
 * 
 * @author diegoguevara
 */


package co.eventt.java.hibernate.exception;

/**
 * Dao Exception class
 * @author Diego Guevara
 */
public class DaoException extends Exception {
    public DaoException(Throwable throwable) {
        super(throwable);
    }

    public DaoException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public DaoException(String string) {
        super(string);
    }

    public DaoException() {
        super();
    }
}
