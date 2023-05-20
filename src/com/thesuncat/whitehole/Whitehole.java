/*
    © 2012 - 2019 - Whitehole Team

    Whitehole is free software: you can redistribute it and/or modify it under
    the terms of the GNU General Public License as published by the Free
    Software Foundation, either version 3 of the License, or (at your option)
    any later version.

    Whitehole is distributed in the hope that it will be useful, but WITHOUT ANY 
    WARRANTY; See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along 
    with Whitehole. If not, see http:www.gnu.org/licenses/.
*/

package com.thesuncat.whitehole;

import com.thesuncat.whitehole.rendering.cache.RendererCache;
import com.thesuncat.whitehole.rendering.cache.ShaderCache;
import com.thesuncat.whitehole.rendering.cache.TextureCache;
import com.thesuncat.whitehole.smg.BcsvFile;
import com.thesuncat.whitehole.smg.GameArchive;
import com.thesuncat.whitehole.swing.MainFrame;
import com.thesuncat.whitehole.swing.RarcEditorForm;
import com.thesuncat.whitehole.swing.SettingsForm;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Whitehole {
    public static final String NAME = "Whitehole v1.7";
    public static final String WEBURL = "https://discord.gg/TudSfUjHcW";
    public static final String CRASHURL = "https://discord.gg/TudSfUjHcW";
    public static final Image ICON = Toolkit.getDefaultToolkit().createImage(Whitehole.class.getResource("/res/icon.png"));
    
    /**
     * The current game directory.
     */
    public static GameArchive game;
    
    /**
     * The currently open game. <br>
     * Unknown = 0<br>
     * SMG1 = 1<br>
     * SMG2 = 2<br>
     * SMG3 = NaN :(
     */
    public static int gameType;
    
    /**
     * The directory of the currently open game. Updated by MainFrame when opening a game directory.
     * Set to the last saved game dir before MainFrame is launched.
     */
    public static String curGameDir = Preferences.userRoot().get("lastGameDir", null);
    
    public static void main(String[] args) throws IOException {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Whitehole.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(args.length != 0) {
            if(args[0].endsWith(".arc"))
                new RarcEditorForm(args[0]).setVisible(true);
            return;
        }
        
        if (!Charset.isSupported("SJIS")) {
            if (!Preferences.userRoot().getBoolean("charset-alreadyWarned", false)) {
                JOptionPane.showMessageDialog(null, "Shift-JIS encoding isn't supported.\n"
                        + "Whitehole will default to ASCII, which may cause certain strings to look corrupted.\n"
                        + "\n"
                        + "This message appears only once.", 
                        Whitehole.NAME, JOptionPane.WARNING_MESSAGE);
                Preferences.userRoot().putBoolean("charset-alreadyWarned", true);
            }
        }
        
        ODB_THREAD.start();
        
        Settings.init();
        TextureCache.init();
        ShaderCache.init();
        RendererCache.init();
        BcsvFile.populateHashTable();
        
        new MainFrame().setVisible(true);
    }
    
    public static boolean execCommand(String com) {
        try {
            System.out.println("Trying to execute " + com);
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", com);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            String line;
            while((line = in.readLine()) != null)
                System.out.println("cmd.exe> " + line);
            
            
            int exitCode = p.waitFor();
            System.out.println("Exited with exit code " + exitCode);
            p.destroy();
            
            return exitCode == 0;
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "cmd.exe failed with the following error:\n" + ex.getLocalizedMessage());
            Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
    }
    
    /**
     * ObjectDB init thread
     */
    private static final Thread ODB_THREAD = new Thread(() -> {
        ObjectDB.init();
    }, "ObjectDB Loader");
    
    // Rich presence stuff
    public static boolean closing = false;
    public static String currentTask = "Idle";
}
