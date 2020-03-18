package StreamPiClient;

import javafx.scene.image.Image;

import java.io.*;

public class io {
    public io() throws Exception
    {

        File actionsFolder = new File("actions/");
        if(!actionsFolder.exists())
        {
            actionsFolder.mkdirs();
            File detailsFolder = new File("actions/details/");
            detailsFolder.mkdir();
            File iconsFolder = new File("actions/icons/");
            iconsFolder.mkdir();

            new File("config").createNewFile();
            writeToFile("800::480::192.168.0.108::23::test1::1::1::105::10::","config");
        }
    }

    public String readFileRaw(String fileName) throws Exception
    {
        String toBeReturned;
        BufferedReader bf = new BufferedReader(new FileReader(fileName));
        toBeReturned = bf.readLine();
        bf.close();
        return toBeReturned;
    }

    public boolean deleteFile(String loc)
    {
        return new File(loc).delete();
    }

    public String[] listFiles(String loc)
    {
        return new File(loc).list();
    }

    public Image returnImage(String loc)
    {
        return new Image(new File(loc).toURI().toString());
    }

    public String[] readFileArranged(String fileName, String s) throws Exception
    {
        return readFileRaw(fileName).split(s);
    }

    public byte[] returnBytesFromFile(String loc) throws Exception
    {
        FileInputStream fs = new FileInputStream(loc);
        byte[] imageB = fs.readAllBytes();
        fs.close();
        return imageB;
    }

    public void writeToFile(String content, String fileName) throws Exception
    {
        BufferedWriter bf = new BufferedWriter(new FileWriter(fileName));
        bf.write(content);
        bf.close();
    }

    public void writeToFileRaw(byte[] toWrite, String fileName) throws Exception
    {
        FileOutputStream fs = new FileOutputStream(fileName);
        fs.write(toWrite);
        fs.close();
    }
}