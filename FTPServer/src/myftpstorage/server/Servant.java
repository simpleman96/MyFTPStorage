/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftpstorage.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftpstorage.gui.ServerUI;
import myftpstorage.remoteInt.ServerInt;
import myftpstorage.util.ReadConfig;

/**
 *
 * @author dat
 */
public class Servant extends UnicastRemoteObject implements ServerInt {

    private static final String USER_INFO_PATH = "account_info";
    private static final String CONFIG_PATH = "config_file";

    private int speed;
    private InputStream inStream;
    private OutputStream outStream;
    private byte[] downBuffer;
    private ServerUI serverUI;

    public Servant(ServerUI serverUI) throws RemoteException {
        HashMap config = ReadConfig.readConfig(CONFIG_PATH);
        this.speed = (int) config.get("network_speed");
        this.serverUI = serverUI;
    }

    @Override
    public boolean initUpload(String filePath) throws RemoteException {
        try {
            this.outStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Servant.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean write(byte[] segment) throws RemoteException {
        try {
            this.outStream.write(segment);
        } catch (IOException ex) {
            Logger.getLogger(Servant.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean initDownload(String filePath) throws RemoteException {
        try {
            this.inStream = new FileInputStream(filePath);
            this.downBuffer = new byte[this.speed];

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Servant.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public byte[] read() throws RemoteException {
        try {
            int readedSize = this.inStream.read(this.downBuffer, 0, this.speed);
            if (readedSize < 0) {
                // done
                return null;
            } else if (readedSize < this.speed) {
                byte[] endSeg = new byte[readedSize];
                System.arraycopy(this.downBuffer, 0, endSeg, 0, readedSize);
                return endSeg;
            } else {
                return this.downBuffer;
            }
        } catch (IOException ex) {
            Logger.getLogger(Servant.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public HashMap getDirTree(String userName) {
        HashMap account_info = ReadConfig.readConfig(USER_INFO_PATH);
        String userRootDir = (String) account_info.get(userName);
        System.out.println(userRootDir);
        HashMap<String, HashMap> dirTree = this.getDirMap(new File(userRootDir));
        return dirTree;
    }

    private HashMap getDirMap(File dir) {
        HashMap<String, HashMap> curDir = new HashMap();
                           
        if(dir.isDirectory()){
            File[] childFiles = dir.listFiles();
            for (File childFile : childFiles) {
                if(childFile.isDirectory()){
                    curDir.put(childFile.getName(), this.getDirMap(childFile));
                }else{
                    Date date = new Date(childFile.lastModified());
                    DateFormat formatter = new SimpleDateFormat("MMMMM.dd - HH:mm");
                    String lastModi = formatter.format(date);
                    curDir.put(childFile.getName() + " (" + childFile.length() / 1024 + "KB - " + lastModi + ")", null);
                }
            }
        }else{
            Date date = new Date(dir.lastModified());
            DateFormat formatter = new SimpleDateFormat("MMMMM.dd - HH:mm");
            String lastModi = formatter.format(date);
            curDir.put(dir.getName() + " (" + dir.length() / 1024 + "KB - " + lastModi + ")", null);
        }
        return curDir;
    }

    @Override
    public boolean writeLog(String log) throws RemoteException {
        if (this.serverUI != null) {
            this.serverUI.getTaLog().append(log);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkLogin(String userName) throws RemoteException {
        HashMap account_info = ReadConfig.readConfig(USER_INFO_PATH);
        if (account_info.containsKey(userName)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getUserRootPath(String userName) throws RemoteException {
        HashMap account_info = ReadConfig.readConfig(USER_INFO_PATH);
        if (account_info.containsKey(userName)) {
            return (String)account_info.get(userName);
        } else {
            return "";
        }
    }

    @Override
    public boolean createDir(String dirPath) throws RemoteException {
        File file = new File(dirPath);
        if(file.isFile()){
            file.renameTo(new File(dirPath + "(1)"));
            File dir = new File(dirPath);
            return dir.mkdir();
        }else{
            return file.mkdir();
        }
    }

    @Override
    public File getFileProperty(String path) throws RemoteException {
        return new File(path);
    }

    @Override
    public boolean deleteFile(String path) throws RemoteException {
        File file = new File(path);
        return file.delete();
    }

}
