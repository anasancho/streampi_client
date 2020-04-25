package StreamPiClient;

import javafx.scene.image.Image;

import java.io.*;

public class io {
    String location;
    public io() throws Exception
    {
        if(Main.buildPlatform == Main.platform.android)
        {
            location = "/storage/emulated/0/Android/in.dubbadhar.StreamPiClient/";
        }
        else
        {
            location = "";
        }

        File actionsFolder = new File(location+"actions/");
        if(!actionsFolder.exists())
        {
            if(!actionsFolder.mkdirs()) throw new Exception("Unable to create Actions Folder");
            File detailsFolder = new File(location+"actions/details/");
            if(!detailsFolder.mkdirs()) throw new Exception("Unable to create Details Folder");
            File iconsFolder = new File(location+"actions/icons/");
            if(!iconsFolder.mkdirs()) throw new Exception("Unable to create Icons Folder");


            if(!new File(location+"config").exists())
            {
                if(!new File(location+"config").createNewFile()) throw new Exception("Unable to Create Config");
                writeToFile("800::480::192.168.0.108::23::StreamPi Client::1::1::105::10::","config");
            }
        }
    }

    public String readFileRaw(String fileName) throws Exception
    {
        String toBeReturned;
        BufferedReader bf = new BufferedReader(new FileReader(location+fileName));
        toBeReturned = bf.readLine();
        bf.close();
        return toBeReturned;
    }

    public boolean deleteFile(String loc)
    {
        return new File(location+loc).delete();
    }

    public String[] listFiles(String loc)
    {
        return new File(location+loc).list();
    }

    public Image returnImage(String loc)
    {
        return new Image(new File(location+loc).toURI().toString());
    }

    public String[] readFileArranged(String fileName, String s) throws Exception
    {
        return readFileRaw(fileName).split(s);
    }

    public byte[] returnBytesFromFile(String loc) throws Exception
    {
        FileInputStream fs = new FileInputStream(location+loc);
        byte[] imageB = fs.readAllBytes();
        fs.close();
        return imageB;
    }

    public void writeToFile(String content, String fileName) throws Exception
    {
        BufferedWriter bf = new BufferedWriter(new FileWriter(location+fileName));
        bf.write(content);
        bf.close();
    }

    public void writeToFileRaw(byte[] toWrite, String fileName) throws Exception
    {
        FileOutputStream fs = new FileOutputStream(location+fileName);
        fs.write(toWrite);
        fs.close();
    }
}
