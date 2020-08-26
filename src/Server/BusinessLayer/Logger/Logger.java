package Server.BusinessLayer.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger logger=null;
    private static Logger errorLogger=null;

    private Logger(){}

    public static Logger getInstance(){
        if(logger==null)
            logger=new Logger();
        return logger;
    }

    public static Logger getInstanceError(){
        if(errorLogger==null)
            errorLogger=new Logger();
        return errorLogger;
    }

    public void writeNewLine(String line)  {
        if(this!=logger){
            (new Exception()).printStackTrace();
            return;
        }

        toWrite(line,"event log");
    }

    public void writeNewLineError(String line){
        if(this!=errorLogger){
            (new Exception()).printStackTrace();
            return;
        }
        toWrite(line,"error log");
    }

    private void toWrite(String line,String fileToWrite) {
        try {
            File loggerFile=new File(fileToWrite);
            if(!loggerFile.exists())
                loggerFile.createNewFile();
            FileWriter FW=new FileWriter(loggerFile,true);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            FW.write(formatter.format(date)+" - "+line+"\n");
            FW.flush();
            FW.close();
        } catch (IOException e) { }
    }

    public String readLoggerFile(){
        if(this!=logger){
            (new Exception()).printStackTrace();
            return "";
        }
        return toRead("event log");
    }

    public String readLoggerFileError(){
        if(this!=errorLogger){
            (new Exception()).printStackTrace();
            return "";
        }
        return toRead("error log");
    }

    private String toRead(String fileToRead) {
        String string="";
        try {
            File loggerFile=new File(fileToRead);
            BufferedReader BR=new BufferedReader(new FileReader(loggerFile));
            String line="";
            while((line=BR.readLine())!=null){
                string+=line;
                string+="\n";
            }
            BR.close();
        } catch (IOException e) { }
        return string;
    }


}
