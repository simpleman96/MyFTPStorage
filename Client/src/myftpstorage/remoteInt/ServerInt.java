/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftpstorage.remoteInt;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author dat
 */
public interface ServerInt extends Remote{
    
    public boolean checkLogin(String userName) throws RemoteException;
    public String getUserRootPath(String userName) throws RemoteException;
    public HashMap getDirTree(String userName) throws RemoteException;
    public boolean writeLog(String log) throws RemoteException;
    
    public boolean createDir(String dirPath) throws RemoteException;
    public File getFileProperty(String path) throws RemoteException;
    
    public boolean initUpload(String filePath) throws RemoteException;
    public boolean write(byte[] segment) throws RemoteException;
    public boolean initDownload(String filePath) throws RemoteException;
    public byte[] read() throws RemoteException;
    
    public boolean deleteFile(String path) throws RemoteException;
}
