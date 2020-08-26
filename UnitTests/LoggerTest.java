import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class LoggerTest {
    Logger logger;
    Logger errorLogger;

    @Before
    public void setUp(){
        File[] loggers={new File("event log"),new File("error log")};
        for(File f:loggers)
            if(f.exists())
                f.delete();
    }


    //region UC and Technical Test
    @Test
    public void getInstanceOnePointer() {
        logger=Logger.getInstance();
        assertNotNull(logger);
    }
    @Test
    public void getInstanceTwoPointers() {
        logger= Logger.getInstance();
        Logger logger2=Logger.getInstance();
        assertEquals(logger,logger2);
    }
    @Test
    public void getInstanceErrorOnePointer(){
        errorLogger=Logger.getInstanceError();
        assertNotNull(errorLogger);
    }
    @Test
    public void getInstanceErrorTwoPointers() {
        errorLogger= Logger.getInstanceError();
        Logger errorLogger2=Logger.getInstanceError();
        assertEquals(errorLogger,errorLogger2);
    }

    @Test
    public void writeLineLogger(){
        logger=Logger.getInstance();
        logger.writeNewLine("aaa");
        assertTrue(CheckLoggerLines("aaa","event log"));
    }

    @Test
    public void writeLineErrorLogger(){
        errorLogger=Logger.getInstanceError();
        errorLogger.writeNewLineError("aaa");
        assertTrue(CheckLoggerLines("aaa","error log"));
    }


    @Test
    public void readLoggerFile() {
        logger=Logger.getInstance();
        logger.writeNewLine("aaa");
        String s=logger.readLoggerFile();
        assertEquals("aaa\n",s.substring(s.indexOf('-')+2));
    }

    @Test
    public void readErrorLoggerFile() {
        errorLogger=Logger.getInstanceError();
        errorLogger.writeNewLineError("aaa");
        String s=errorLogger.readLoggerFileError();
        assertEquals("aaa\n",s.substring(s.indexOf('-')+2));
    }
    //endregion

    private boolean CheckLoggerLines(String string,String loggerFile) {
        String line= null;
        try {
            BufferedReader BR=new BufferedReader(new FileReader(new File(loggerFile)));
            line = BR.readLine();
            BR.close();
            if(string.equals(line.substring(line.indexOf('-')+2)))
                return true;
            else
                return false;
        } catch (IOException e) { }
        return false;
    }
}