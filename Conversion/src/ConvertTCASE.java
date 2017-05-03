import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.jdbc.OraclePreparedStatement; 


class ConvertTCASE { 
	
	static int BATCH_SIZE = 10;
	static long  Counter = 0;
	static long  InsCounter = 0;
	static long  ps_tsubcase_counter = 0;
	static long TPERSON_counter = 0;
	static long TCHARGE_counter = 0;
	static long TADDRESS_counter = 0;
	static long TPERSONIDENTIFIER_counter = 0;
	static long TPARTY_counter = 0;
	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    static Connection src;
    static Connection trg;
    static Statement stmt;
    static Statement stmt2;
    static Statement stmt3;
    static Statement stmt4;
    static int ID;
    static PreparedStatement ps_tsubcase;
    static PreparedStatement ps_tcharge;
    static PreparedStatement ps_tperson;
    static PreparedStatement ps_taddress;
    static PreparedStatement ps_tparty;
    static PreparedStatement ps_tpersonidentifier;
    static ResultSet rs3;
    static ResultSet rs4;
    static ResultSet rs5;
    static ResultSet rs6;
    static ResultSet hibernateRS;
    static int HISTORY_NBR = 0;
    static int PERSON_ID = 0;
    static int SUBCASE_ID = 0;
    static int HIBERNATE_SEQUENCE;
    static int TCASE_ID;
    static int TPERSONIDENTIFIER_ID;
    static int TSUBCASE_ID;
    static int TCHARGE_ID;
    static int TASSOCIATEDPARTY_ID;
    static int TPARTY_ID;
    static int TADDRESS_ID;
    static int TPERSON_ID;
    
   
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	 
public static void main(String args[])
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
{  
	
	Date date = new Date();
	System.out.println("Start time is " + dateFormat.format(date)); 

	
try{  
//step1 load the driver class  
Class.forName("oracle.jdbc.driver.OracleDriver");  
  
//step2 create  the connection object  
 src=DriverManager.getConnection(  
"jdbc:oracle:thin:@bison:1521:TRNLV","court_migrate","cm23456"); 


 trg=DriverManager.getConnection(  
"jdbc:oracle:thin:@bison:1521:TRNLV","vegas_training2","vt123456");  
 trg.setAutoCommit(false);

 
//step3 create the statement object  
 stmt=src.createStatement();  
 stmt2=src.createStatement(); 
 stmt3=trg.createStatement(); 
 stmt4=trg.createStatement();
 
Statement Trgstmt=trg.createStatement(); 


PreparedStatement ps = trg.prepareStatement("insert into TCASE " +
  "(ID , " +
  " CREATEUSERREALNAME, " +     
  " CREATEUSERNAME,     " +     
  " DATECREATED,        " +                // TIMESTAMP(6),
  " LASTUPDATEUSERREALNAME, " +  
  " LASTUPDATEUSERNAME, " +     
  "LASTUPDATED, " +                       // TIMESTAMP(6),
  "MEMO,   " +                 
  "OPTLOCK, " +                           // NOT NULL,
  "SOURCECASENUMBER, " +       
  "CASENAME, " +               
  "CASENUMBER, " +                        // VARCHAR2(255 CHAR)    NOT NULL,
  "CASETYPE, " +                          // VARCHAR2(255 CHAR),
  "FILINGDATE, " +                        // TIMESTAMP(6),
  "STATUS, " +                            // VARCHAR2(255 CHAR),
  "STATUSDATE ) " +                       // TIMESTAMP(6)
" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); 
((OraclePreparedStatement)  ps).setExecuteBatch (BATCH_SIZE);

 ps_tpersonidentifier = trg.prepareStatement("insert into TPERSONIDENTIFIER " +
		  "(ID , "                    +
		  " CREATEUSERREALNAME, "     +     
		  " CREATEUSERNAME,     "     +     
		  " DATECREATED,        "     +                 // TIMESTAMP(6),
		  " LASTUPDATEUSERREALNAME, " +  
		  " LASTUPDATEUSERNAME, "     +     
		  "LASTUPDATED, "             +                       // TIMESTAMP(6),
		  "MEMO,   "                  +                 
		  "PERSONIDENTIFIERNUMBER) "  +                           // NOT NULL,
		" values (?, ?, ?, ?, ?, ?, ?, ?,? )"); 
((OraclePreparedStatement)  ps_tpersonidentifier).setExecuteBatch (BATCH_SIZE);




ps_tsubcase = trg.prepareStatement("insert into TSUBCASE " +
		  "(ID , " +
		  " CREATEUSERREALNAME, " +     
		  " CREATEUSERNAME,     " +     
		  " DATECREATED,        " +                // TIMESTAMP(6),
		  " LASTUPDATEUSERREALNAME, " +  
		  " LASTUPDATEUSERNAME, " +     
		  "LASTUPDATED, " +                       // TIMESTAMP(6),
		  "MEMO,   "           +                 
		  "OPTLOCK, "          +                           // NOT NULL,
		  "SOURCECASENUMBER, " +            	  // VARCHAR2(255 CHAR),
		  "SUBCASENAME, "      +            	  // VARCHAR2(255 CHAR),
		  "SUBCASETYPE,"       +
		  "STATUS, "           +                       
		  "STATUSDATE , "      +                  // TIMESTAMP(6),
		  "VIOLATIONDATE, "    +                  // TIMESTAMP(6),
		  "CASE_ID )"  +                          // NUMBER(19)       NOT NULL,
		" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)"); 
((OraclePreparedStatement) ps_tsubcase).setExecuteBatch (BATCH_SIZE);


ps_tcharge = trg.prepareStatement("insert into TCHARGE " +
		  "(ID , " +
		  " CREATEUSERREALNAME, " +     
		  " CREATEUSERNAME,     " +     
		  " DATECREATED,        " +               
		  " LASTUPDATEUSERREALNAME, " +  
		  " LASTUPDATEUSERNAME, "   +     
		  "LASTUPDATED, "           +                      
		  "MEMO,   "                +                 
		  "OPTLOCK, "               +                          
		  "SOURCECASENUMBER, "      +            	 
		  "STATUS, "                +                       
		  "STATUSDATE , "           +                  
		  "ASSOCIATEDPARTY_ID, "    +            
		  "STATUTE_ID )"            +                          
		" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); 
((OraclePreparedStatement) ps_tcharge).setExecuteBatch (BATCH_SIZE);


ps_tperson = trg.prepareStatement("insert into TPERSON " +
		  "(ID , " +
		  " CREATEUSERREALNAME, "     +     
		  " CREATEUSERNAME,     "     +     
		  " DATECREATED,        "     +               
		  " LASTUPDATEUSERREALNAME, " +  
		  " LASTUPDATEUSERNAME, "    +     
		  "LASTUPDATED, "            +                      
		  "MEMO,   "                 +                 
		  "OPTLOCK, "                +                          
		  "FIRSTNAME, "              +            	 
		  "LASTNAME, "               +                       
		  "MIDDLENAME , "            +    
		  "CMSCASENUMBER , "         +   
		  "HISTORYNUMBER , "         + 
		  "PERSONID , "              +   
		  "PERSONIDENTIFIER_ID) "    +            
		" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ? )"); 
((OraclePreparedStatement) ps_tperson).setExecuteBatch (BATCH_SIZE);


ps_taddress = trg.prepareStatement("insert into TADDRESS " +
		  "(ID , " +
		  " CREATEUSERREALNAME, "     +     
		  " CREATEUSERNAME,     "     +     
		  " DATECREATED,        "     +               
		  " LASTUPDATEUSERREALNAME, " +  
		  " LASTUPDATEUSERNAME, "     +     
		  "LASTUPDATED, "             +                      
		  "MEMO,   "                  +                 
		  "OPTLOCK, "                 +   
		  "SOURCECASENUMBER, "        +            	 
		  "ADDRESS1, "                +                       
		  "ADDRESS2 , "               +    
		  "ADDRESSTYPE , "            +   
		  "CITY , "                   + 
		  "STATE , "                  +   
		  "ZIP,"                      +
		  "ASSOCIATEDPERSON_ID,"      +
 		  "ADDRESSIDENTIFIER_ID) "    +            
		" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"); 
((OraclePreparedStatement) ps_taddress).setExecuteBatch (BATCH_SIZE);

ps_tparty = trg.prepareStatement("insert into TPARTY " +
		  "(ID , " +
		  " CREATEUSERREALNAME, "     +     
		  " CREATEUSERNAME,     "     +     
		  " DATECREATED,        "     +               
		  " LASTUPDATEUSERREALNAME, " +  
		  " LASTUPDATEUSERNAME, "     +     
		  "LASTUPDATED, "             +                      
		  "MEMO,   "                  +                 
		  "OPTLOCK, "                 +   
		  "SOURCECASENUMBER, "        +            	 
		  "CASE_ID,   "               +    // not null                   
		  "PERSON_ID , "              +    // not null
		  "SUBCASE_ID  ) "            +   
		" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"); 
((OraclePreparedStatement) ps_tparty).setExecuteBatch (BATCH_SIZE);

/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
//step4 execute query  MAIN LOOP FOR CASE BUILDING
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
//ResultSet rs=stmt.executeQuery("select  history_nbr from court.cms_case where  rownum < 21 group by history_nbr having history_nbr > 0");

ResultSet rs=stmt.executeQuery("select  incident_nbr, aoc_group, created_dttm, filing_dttm, nbr_of_charges, history_nbr, case_type, case_id from COURT.INCIDENT where  rownum < 21 ");

ResultSet rs1=Trgstmt.executeQuery("select HIBERNATE_SEQUENCE.NEXTVAL from DUAL");

int return_val = 0;

while (rs1.next()) {
	
	HIBERNATE_SEQUENCE   = rs1.getInt(1);
	TCASE_ID             = HIBERNATE_SEQUENCE;
	TPERSONIDENTIFIER_ID = HIBERNATE_SEQUENCE;
    TSUBCASE_ID          = HIBERNATE_SEQUENCE;
    TCHARGE_ID           = HIBERNATE_SEQUENCE;
    TASSOCIATEDPARTY_ID  = HIBERNATE_SEQUENCE;
    TPARTY_ID            = HIBERNATE_SEQUENCE;
    TADDRESS_ID          = HIBERNATE_SEQUENCE;
    TPERSON_ID           = HIBERNATE_SEQUENCE;

}
rs1.close();

while(rs.next())  {
	//System.out.println("Number of records on COURT.CMS_CASE " + rs.getInt(1));
	
	/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
	/* BUILD TCASE */
	/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */		
	InsCounter++;
	
	TCASE_ID++;
	System.out.println("****** START *********  Incident_nbr " + rs.getInt("incident_nbr") + " TCASE_ID " + TCASE_ID + "  ******************");
	ps.setInt(1, TCASE_ID);
	ps.setString(2, "ODI"); 
	ps.setString(3, "ODI"); 
	ps.setDate(4, new java.sql.Date(System.currentTimeMillis())); 
	ps.setString(5, "ODI"); 
	ps.setString(6, "ODI");
	ps.setDate(7, new java.sql.Date(System.currentTimeMillis())); 
	ps.setString(8, " " );                                                        // memo
	ps.setInt(9, 0 );
	ps.setInt(10,       rs.getInt("history_nbr"));                                // sourcecasenumber
	ps.setString(11,    rs.getString("aoc_group"));                               // casename
	ps.setInt(12,       rs.getInt("CASE_ID"));                                    // casenumber    LOOK AT!!!!
	ps.setString(13,    rs.getString("case_type"));                               // Case type     LOOK AT !!!
	ps.setDate(14,     new java.sql.Date(System.currentTimeMillis()));            // Filing Date
	ps.setString(15, "CLOSE");                                                    // LOOK AT !!!!
	ps.setDate(16, new java.sql.Date(System.currentTimeMillis()));             
	return_val=ps.executeUpdate();                                                           //JDBC queues this for later execution               *** INSERT TCASE *****
	System.out.println("TCASE - update result -> " + return_val);
	HISTORY_NBR =       rs.getInt("history_nbr");
	
	Build_TPERSON(HISTORY_NBR, ID);
	Build_SubCase(HISTORY_NBR, ID );

	Counter++;
	//if (Counter > BATCH_SIZE) {
		 ((OraclePreparedStatement)ps).sendBatch();                   // JDBC sends the queued request

		 Counter = 0;
	//}
	
	
}
  
//step5 close the connection object 
trg.commit();
trg.close();
src.close(); 

  System.out.println("Total rows inserted into TPERSONIDENTIFIER " + InsCounter);
  System.out.println("Total rows inserted into TPERSON "     + TPERSON_counter);
  System.out.println("Total rows inserted into TCASE "       + InsCounter);
  System.out.println("Total rows inserted into TSUBCASE "    + ps_tsubcase_counter);
  System.out.println("Total rows inserted into TCHARGE "     + ps_tsubcase_counter);
  System.out.println("Total rows inserted into TADDRESS "    + TADDRESS_counter);
  System.out.println("Total rows inserted into TPARTY "      + TPARTY_counter);
  
	date = new Date();
	System.out.println("End time is " + dateFormat.format(date)); 
  
} catch(Exception e)
    { System.out.println(e.getMessage());}  

  
}


/* BULD ps_tsubcase */
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
static void Build_SubCase(int IN_HISTORY_NBR, int IN_ID)  
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
{
	
	int return_val;
	int ws_counter = 0;
	
	
	try 
	
		{

		//Statement stmt=src.createStatement();
		   rs3=stmt2.executeQuery("select  case_nbr, status, to_char(status_dttm, 'dd-mon-yyyy') status_dttm, to_char(violation_dttm, 'dd-mon-yyyy') viol_dttm, history_nbr, defendant, dob, ssn, veh_lic_nbr,   last_name, first_name, middle_name, dl_nbr, " +
		" viol_original_code, viol_original_desc, case_type from court.cms_case where history_nbr =" + IN_HISTORY_NBR );
		
			while (rs3.next()) {
				ps_tsubcase_counter++;
				
				TSUBCASE_ID++ ;
				ps_tsubcase.setInt(1, TSUBCASE_ID); 
				ps_tsubcase.setString(2, "ODI");
				ps_tsubcase.setString(3, "ODI"); 
				ps_tsubcase.setDate(4, new java.sql.Date(System.currentTimeMillis())); 
				ps_tsubcase.setString(5, "ODI"); 
				ps_tsubcase.setString(6, "ODI");
				ps_tsubcase.setDate(7, new java.sql.Date(System.currentTimeMillis())); 
				ps_tsubcase.setString(8, " " );                                    // memo
				ps_tsubcase.setInt(9, 0 );                                         // optlock
				ps_tsubcase.setString(10,   rs3.getString("case_nbr"));            // sourcecasenumber
				ps_tsubcase.setString(11,   rs3.getString("viol_original_desc"));  // case name
				ps_tsubcase.setString(12,   rs3.getString("case_type"));
				ps_tsubcase.setString(13,   rs3.getString("status"));              // status
				ps_tsubcase.setString(14,   rs3.getString("status_dttm"));         // status Date
     			ps_tsubcase.setString(15,   rs3.getString("viol_dttm"));           // violationdate
				ps_tsubcase.setInt(16,TCASE_ID);                                   // case_id
				return_val = ps_tsubcase.executeUpdate();                          //JDBC queues this for later execution *** INSERT ps_tsubcase *****
				System.out.println("TSUBCASE - update result -> " + return_val);
			    
			    
				/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
				/* BULD TCHARGE */
				/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
			    TCHARGE_counter++;
			    
			    TCHARGE_ID++;
			    TASSOCIATEDPARTY_ID++;
			    
			    ps_tcharge.setInt(1, TCHARGE_ID);
				ps_tcharge.setString(2, "ODI"); 
				ps_tcharge.setString(3, "ODI"); 
				ps_tcharge.setDate(4, new java.sql.Date(System.currentTimeMillis())); 
				ps_tcharge.setString(5, "ODI"); 
				ps_tcharge.setString(6, "ODI");
				ps_tcharge.setDate(7, new java.sql.Date(System.currentTimeMillis())); 
				ps_tcharge.setString(8, " " );                                     // memo
				ps_tcharge.setInt(9, 0 );                                          // optlock
				ps_tcharge.setString(10,   rs3.getString("CASE_NBR"));             // sourcecasenumber
				ps_tcharge.setString(11,   rs3.getString("STATUS"));               // status
				ps_tcharge.setString(12,   rs3.getString("status_dttm"));          // status Date
				ps_tcharge.setInt(13,  TASSOCIATEDPARTY_ID );                   // ASSOCIATEDPARTY_ID
				ps_tcharge.setInt(14, GetStatute(rs3.getString("viol_original_code")));                          // STATUTE_ID
				return_val = ps_tcharge.executeUpdate();                                                         //JDBC queues this for later execution *** INSERT TCHARGE ***
				System.out.println("TCHARGE - update result -> " + return_val);
				/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
				/* BULD TPARTY */
				/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	

		        TPARTY_counter++;
				TPARTY_ID++;
				ps_tparty.setInt(1, TPARTY_ID);
				ps_tparty.setString(2, "ODI"); 
				ps_tparty.setString(3, "ODI"); 
				ps_tparty.setDate(4, new java.sql.Date(System.currentTimeMillis())); 
				ps_tparty.setString(5, "ODI"); 
				ps_tparty.setString(6, "ODI");
				ps_tparty.setDate(7, new java.sql.Date(System.currentTimeMillis())); 
				ps_tparty.setString(8, " " );                                    // memo
				ps_tparty.setInt(9, 0 );                                         // optlock
				ps_tparty.setString(10,   rs3.getString("CASE_NBR"));             // sourcecasenumber
				ps_tparty.setInt(11,  TCASE_ID);                                 // TCASE case_id
				ps_tparty.setInt(12,  TPERSON_ID);                                // PERSON_ID
				ps_tparty.setInt(13,  TSUBCASE_ID);                               // SUBCASE_ID,
				return_val = ps_tparty.executeUpdate();                                                         //JDBC queues this for later execution *** INSERT TCHARGE ***
				
				System.out.println("TPARTY - update result -> " + return_val);
				System.out.println("TCASE_ID -> " + TCASE_ID + " TPERSON_ID -> " + TPERSON_ID + " TSUBCASE_ID -> " + TSUBCASE_ID + " TPARTY_ID -> " + TPARTY_ID);
				
				ws_counter++;
				//if (ws_counter > BATCH_SIZE) {
			  	 ((OraclePreparedStatement)ps_tsubcase).sendBatch();                                                // JDBC sends the queued request
			  	 ((OraclePreparedStatement)ps_tcharge).sendBatch();                                                 // JDBC sends the queued request
			  	 ((OraclePreparedStatement)ps_tparty).sendBatch();                                                  // JDBC sends the queued request
					 ws_counter = 0;
				//}
				
				
				
			} 
	
		} // End Try
	catch (Exception e)  {System.out.println(e.getMessage());}
	 // End-While


}  // End Medthod - Build_SubCase

/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
static int GetStatute(String IN_STATUTE_ID) 
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
{
	int OUT_STATUTE_ID = 0;

	
	if (IN_STATUTE_ID.length() == 0 )
			IN_STATUTE_ID = "0";

	try {
	      rs5=stmt3.executeQuery("select statute_id from  TSTATUTECATEGORY where reporttype = 'CMS' and value = " +  "'" + IN_STATUTE_ID + "'");
	      
	      
	      while (rs5.next()) {
	    	  OUT_STATUTE_ID =  rs5.getInt("statute_id");
	      }
	      
	}
	catch (Exception e) { System.out.println(e.getMessage()); }
		return OUT_STATUTE_ID;

}

/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
static int GetCurrent_Hibernate_Sequence() throws SQLException 
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
{
	int ws_hibernate = 0;
	
	hibernateRS=stmt4.executeQuery("select HIBERNATE_SEQUENCE.CURRVAL HS from DUAL");


	while (hibernateRS.next()) {
		
		ws_hibernate =  hibernateRS.getInt(1);
	}
	hibernateRS.close();

	return ws_hibernate;
	
}

/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
static int Build_TPERSON (int IN_HISTORY_NBR, int IN_ID) 
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
{
	
	int ws_counter = 0;


	
	try {
	      rs4=stmt2.executeQuery("select   first_name,last_name, middle_name, numeric_case_nbr, defendant, dob, ssn, dl_nbr, address1, address2, city, state, zip  from court.cms_case where history_nbr =" + HISTORY_NBR );
	
	      while (rs4.next()) {
	    	  
	    	     int ws_up = 0;
	    		 System.out.println("IN_HISTORY_NBR " + IN_HISTORY_NBR + " TPERSON_ID ==> " + TPERSON_ID );
				TPERSON_counter++;
				TPERSON_ID++;
				ps_tperson.setInt(1, TPERSON_ID);
				ps_tperson.setString(2, "ODI"); 
				ps_tperson.setString(3, "ODI"); 
				ps_tperson.setDate(4, new java.sql.Date(System.currentTimeMillis())); 
				ps_tperson.setString(5, "ODI"); 
				ps_tperson.setString(6, "ODI");
				ps_tperson.setDate(7, new java.sql.Date(System.currentTimeMillis())); 
				ps_tperson.setString(8, " " );                                  // memo
				ps_tperson.setInt(9, 0 );                                       // optlock
				ps_tperson.setString(10,   rs4.getString("first_name"));        // FirstName
				ps_tperson.setString(11,   rs4.getString("last_name"));         // Last name
				ps_tperson.setString(12,   rs4.getString("middle_name"));       // Middle Name
				ps_tperson.setInt(13,      rs4.getInt("numeric_case_nbr"));     // CMSCASENUMBER
				ps_tperson.setInt(14,      IN_HISTORY_NBR);                     // History
				ps_tperson.setInt(15,      TPERSONIDENTIFIER_ID);               // PersonID
				ps_tperson.setInt(16,      rs4.getInt("numeric_case_nbr"));     // case_id
				ws_up=ps_tperson.executeUpdate();                                     // JDBC queues this for later execution 
				System.out.println("TPERSON - update result -> " + ws_up);
				
				/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
				/* BUILD TPERSONIDENTIFIER */
				/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
				
				
				TPERSONIDENTIFIER_counter++;
				TPERSONIDENTIFIER_ID++;
				
				ps_tpersonidentifier.setInt(1, TPERSONIDENTIFIER_ID);
				ps_tpersonidentifier.setString(2, "ODI"); 
				ps_tpersonidentifier.setString(3, "ODI"); 
				ps_tpersonidentifier.setDate(4, new java.sql.Date(System.currentTimeMillis())); 
				ps_tpersonidentifier.setString(5, "ODI"); 
				ps_tpersonidentifier.setString(6, "ODI");
				ps_tpersonidentifier.setDate(7, new java.sql.Date(System.currentTimeMillis())); 
				ps_tpersonidentifier.setString(8, " " );                       // memo
				ps_tpersonidentifier.setInt(9,TPERSON_ID);                     // PersonIdentifierNumber   !!!! LOOK AT  !!!
				ws_up = ps_tpersonidentifier.executeUpdate();
				System.out.println("TPERSONIDENTIFIER - update result -> " + ws_up);

				/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	
				/* BULD TADDRESS */
				/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */		               
				TADDRESS_counter++;
			
				ps_taddress.setInt(1, TADDRESS_ID); 
				ps_taddress.setString(2, "ODI"); 
				ps_taddress.setString(3, "ODI"); 
				ps_taddress.setDate(4, new java.sql.Date(System.currentTimeMillis())); 
				ps_taddress.setString(5, "ODI"); 
				ps_taddress.setString(6, "ODI");
				ps_taddress.setDate(7, new java.sql.Date(System.currentTimeMillis())); 
				ps_taddress.setString(8, " " );                                 // memo
				ps_taddress.setInt(9, 0 );                                      // optlock
				ps_taddress.setInt(10,     HISTORY_NBR);                        // history_nbr
				ps_taddress.setString(11,  rs6.getString("address1"));          // address1
				ps_taddress.setString(12,  rs6.getString("address2"));          // address2
				ps_taddress.setString(13,  "HOME");                             // address type
				ps_taddress.setString(14,  rs6.getString("city"));              // city
				ps_taddress.setString(15,  rs6.getString("state"));             // state
				ps_taddress.setString(16,  rs6.getString("zip"));               // zip
				ps_taddress.setInt(17,     TPERSONIDENTIFIER_ID);                // ASSOCATEDPERSON_ID
				ps_taddress.setString(18,     " ");                              // ADDRESSIDENTIFIER_ID
				ws_up=ps_taddress.executeUpdate();                                    // JDBC queues this for later execution 
				System.out.println("TADDRESS - update result -> " + ws_up);
				
				TADDRESS_ID++;
				
				
				ws_counter++;
				// if (ws_counter > BATCH_SIZE) {
			  	 ((OraclePreparedStatement)ps_tperson).sendBatch();               // JDBC sends the queued request
			  	 ((OraclePreparedStatement)ps_tpersonidentifier).sendBatch();     // JDBC sends the queued request
			  	 ((OraclePreparedStatement)ps_taddress).sendBatch();              // JDBC sends the queued request
					 ws_counter = 0;
				// }
				

	      }  // END-WHILE


	
	} catch (Exception e) { System.out.println( e.getMessage()) ; }
	
	return PERSON_ID;

 }  // Class

   

}
