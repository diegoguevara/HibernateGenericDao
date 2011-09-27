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

 `hibernate.cfg.xml` example using MySql:
 
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
        <hibernate-configuration>
                <session-factory>
                        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
                        <property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
                        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/documents</property>
                        <property name="hibernate.connection.username">root</property>
                        <property name="hibernate.connection.password">rootpass</property>
                        
                        <mapping resource="rbx/data/Version.hbm.xml"/>
                        <mapping resource="rbx/data/Document.hbm.xml"/>
                </session-factory>
        </hibernate-configuration>
 
 4. Create Mapping Hbm files and java classes

Example based on configuration file:

        Document.hbm.xml
        Document.java
        Version.hbm.xml
        Version.java
        
Use Netbeans or another tool to generate the mapping files from database tables.


Usage
-----

### Retrieving data

Easy way to get all rows from a database table in a List of hibernate mapped objects:

        List<Object_Data> result;
        
        GenericDao gdao = new GenericDao();
        result = gdao.retrieve( Object_Data.class.getName() );
        
Add query criteria restrictions:

        List<Object_Data> result;
        
        List<Criterion> lscriterion = new ArrayList<Criterion>();
        lscriterion.add( Restrictions.isNull( "param" ) );
        
        GenericDao gdao = new GenericDao();
        result = gdao.retrieve( Object_Data.class.getName(), lscriterion );
        
Add Order in query results

        List<Object_Data> result;
        
        List<Criterion> lscriterion = new ArrayList<Criterion>();
        lscriterion.add( Restrictions.isNull( "param" ) );
        
        List<Order> lorder = new ArrayList<>(Order);
        List<Order> lorder = new ArrayList<Order>();
        lorder.add(Order.asc("param")); // ascending Order with hb order object
            
        GenericDao gdao = new GenericDao();
        result = gdao.retrieve( Object_Data.class.getName(), lscriterion, lorder ); // add criteria and order


TODO: projection session and others in documentation...
        

### Native SQL Query

This method allows you to run native SQL queries using Hibernate.

        GenericDao gdao = new GenericDao();
        
        String sqlquery = "Select * from version";
        
        List result = gdao.sqlQuery(sqlquery);
        
Also, it allows to send parameters using Named Parameters in a Map object.

        GenericDao gdao = new GenericDao();
        
        String sqlquery = "Select * from version where id = :param_data";
        
        Map params = new HashMap();
        params.put("param_data", "4");
        
        List result = gdao.sqlQuery(sqlquery, params);
        


### Saving data

        MappedObjectData objdata = new MappedObjectData();
        objdata.setName("name");
        objdata.setDescription("txt description");
        
        GenericDao gdao = new GenericDao();
        gdao.save(objdata);


TODO : Add save, update, delete etc... in documentation
...