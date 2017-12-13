/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dat
 */
public class Setup {
    
    public static void readUser(){
        HashMap readedConfig;
        String path = "account_info";
        try {
            InputStream is = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(is);
            readedConfig = (HashMap)ois.readObject();
            ois.close();
            is.close();
            readedConfig.forEach((key, value) ->{
                System.out.println(key + " - " + value);
            });
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("1. Set port");
        System.out.println("2. Add user");
        System.out.println("3. Print users info");
        System.out.println("Enter a mode number: ");
        
        int mode = reader.nextInt();
        if(mode == 1){
            System.out.println("Enter port num ber:");
            int port = reader.nextInt();
            if( port > 0 && port <= 65535){
                HashMap config = new HashMap();

                String configPath = "config_file";
                config.put("rmi_port", port);
                config.put("network_speed", 256*1024);
                try {
                    OutputStream os = new FileOutputStream(configPath);
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeObject(config);
                    oos.close();
                    os.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                System.out.println("Port number out of range!");
            }
        } else if(mode == 2){
            HashMap accountInfo = null;
            File file;
            String workingDir = System.getProperty("user.dir");
            String accPath = "account_info";
            try {
                File accFile = new File(accPath);
                if(accFile.exists()){
                    InputStream is = new FileInputStream(accPath);
                    ObjectInputStream ois = new ObjectInputStream(is);
                    accountInfo = (HashMap) ois.readObject();
                    ois.close();
                    is.close();
                }
                if(accountInfo == null){
                    accountInfo = new HashMap();
                }
                
                System.out.println("Enter user's name:");
                String userName = reader.next().trim();
                accountInfo.put(userName, workingDir + "/ServerStorage/"+userName);
                file = new File((String) accountInfo.get(userName));
                if(!file.mkdirs()){
                    System.out.println("Error to add");
                }
                OutputStream os = new FileOutputStream(accPath);
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(accountInfo);
                oos.close();
                os.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else if(mode == 3){
            readUser();
        }
        reader.close();

    }
    
}