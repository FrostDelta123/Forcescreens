package ru.frostdelta.forcescreens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GetHWID {

    private String processorName;
    private String pcName;
    private String username;

    public String getProcessorName(){
        return processorName;
    }

    public String getPcName(){
        return pcName;
    }

    public String getUsername(){
        return username;
    }
    public void dumpHWID(){

        processorName = System.getenv("PROCESSOR_IDENTIFIER").trim();
        pcName = System.getenv("COMPUTERNAME").trim();
        username = System.getProperty("user.name").trim();

    }

    public String getMotherboardSN() {
        String result = "";
        try {
            String line;
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_BaseBoard\") \nFor Each objItem in colItems \n    Wscript.Echo objItem.SerialNumber \n    exit for  ' do the first cpu only! \nNext \n";
            fw.write(vbs);
            fw.close();
            java.lang.Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                result = result + line;
            }
            input.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

}
