/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftpstorage.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dat
 */
public class ReadConfig {
    
    public static HashMap readConfig(String path){
        HashMap readedConfig;
        
        try {
            InputStream is = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(is);
            readedConfig = (HashMap)ois.readObject();
            ois.close();
            is.close();
            return readedConfig;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ReadConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
