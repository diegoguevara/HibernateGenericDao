Hibernate GenericDao
====================

Hibernate Generic Dao implementation library. documentation is in development...

Requirements
------------

1. Hibernate 3.x ( http://www.hibernate.org/ )
2. Log4j 1.2.x   ( http://logging.apache.org/log4j/1.2/ )


Installation
------------

1. add HibernateGenericDao.jar into your project libraries.
2. Import library GenericDao class.

        import rbx.java.dao.hibernate.dao.GenericDao
        import rbx.java.hibernate.exception.DaoException

3. create and configure `hibernate.cfg.xml` file

TODO: add hibernate.cfg.xml configuration example...

Usage
-----

Easy way to get all rows from a database table in a List of hibernate mapped objects:

        List<Object_Data> result;
        
        GenericDao gdao = new GenericDao();
        result = gdao.retrieve( Object_Data.class.getName() );
        
Add query criteria restrictions:

        List<Object_Data> result;
        
        List<Criterion> lscriterion;
        lscriterion = new ArrayList<Criterion>();
        lscriterion.add( Restrictions.isNull( "param" ) );
        
        GenericDao gdao = new GenericDao();
        result = gdao.retrieve( Object_Data.class.getName(), lscriterion );
        
TODO: add order, projection session and others in documentation...
...