import Server.BusinessLayer.OurSystemServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.io.*;
import java.sql.*;

public class OurSystemServerTest {
    private final ByteArrayOutputStream OS=new ByteArrayOutputStream();
    private final PrintStream PS=System.out;
    OurSystemServer ourSystem;

    @Before
    public void setUp(){
        ourSystem=new OurSystemServer();
        System.setOut(new PrintStream(OS));
    }

    @After
    public void restore(){
        System.setOut(PS);
    }

    @Test
    public void Initialize(){
        File initCheckFile=new File("firstInitCheck");
        if(initCheckFile.exists()) initCheckFile.delete();

        ourSystem.Initialize();
        File errorLogger=new File("error log");
        assertFalse(errorLogger.exists());

        assertEquals("Established connection to Accounty System\r\n" + "Established connection to Federal Tax System\r\n" + "Server started\r\n" + "Initializing database\r\nDatabase initialized\r\n",OS.toString());

        try {
            BufferedReader BR=new BufferedReader(new FileReader(new File("event log")));
            String line="";
            while((line=BR.readLine())!=null){
                line=line.substring(line.indexOf("-")+2);
                assertEquals("System has been initialized",line);
                break;
            }
            BR.close();
        } catch (IOException e) { e.printStackTrace(); }

        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select * from SystemManager");
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                assertEquals("SM1X",RS.getString(1));
                assertEquals("SM1",RS.getString(2));
            }
            con.close();
        } catch (SQLException e) { e.printStackTrace(); }

    }


    private Connection connectToDB(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost;databaseName=DB2020;integratedSecurity=true";
            Connection con = DriverManager.getConnection(connectionUrl);
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        return null;
    }



}