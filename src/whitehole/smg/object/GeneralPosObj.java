/*
    © 2012 - 2016 - Whitehole Team

    Whitehole is free software: you can redistribute it and/or modify it under
    the terms of the GNU General Public License as published by the Free
    Software Foundation, either version 3 of the License, or (at your option)
    any later version.

    Whitehole is distributed in the hope that it will be useful, but WITHOUT ANY 
    WARRANTY; See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along 
    with Whitehole. If not, see http://www.gnu.org/licenses/.
*/

package whitehole.smg.object;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import whitehole.PropertyGrid;
import whitehole.Whitehole;
import whitehole.smg.Bcsv;
import whitehole.smg.LevelObject;
import whitehole.smg.ZoneArchive;
import whitehole.vectors.Vector3;

public class GeneralPosObj extends LevelObject {
    
    public GeneralPosObj(ZoneArchive zone, String filepath, Bcsv.Entry entry) {
        this.zone = zone;
        String[] stuff = filepath.split("/");
        directory = stuff[0];
        layer = stuff[1].toLowerCase();
        file = stuff[2];
        
        data = entry;
        
        name = (String)data.get("name");
        loadDBInfo();
        renderer = null;
        
        uniqueID = -1;
        
        position = new Vector3((float)data.get("pos_x"), (float)data.get("pos_y"), (float)data.get("pos_z"));
        rotation = new Vector3((float)data.get("dir_x"), (float)data.get("dir_y"), (float)data.get("dir_z"));
        scale = new Vector3(1,1,1);
    }
    
    public GeneralPosObj(ZoneArchive zone, String filepath, int game, Vector3 pos) {
        this.zone = zone;
        String[] stuff = filepath.split("/");
        directory = stuff[0];
        layer = stuff[1].toLowerCase();
        file = stuff[2];
        
        data = new Bcsv.Entry();
        
        name = "GeneralPos";
        loadDBInfo();
        renderer = null;
        
        uniqueID = -1;
        
        position = pos;
        rotation = new Vector3(0f, 0f, 0f);
        scale = new Vector3(1f, 1f, 1f);
        
        data.put("name", name);
        data.put("pos_x", position.x); data.put("pos_y", position.y); data.put("pos_z", position.z);
        data.put("dir_x", rotation.x); data.put("dir_y", rotation.y); data.put("dir_z", rotation.z);
        data.put("PosName", "null");
        data.put("Obj_ID", (short)-1);
        if (ZoneArchive.gameMask == 1)
            data.put("ChildObjId", (short)-1);
    }
    
    public void generalPosNames() {
        choicesGeneralPosNames = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Whitehole.class.getResourceAsStream("/Resources/GeneralPos.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                choicesGeneralPosNames.add(line);
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    @Override
    public void save() {
        data.put("name", name);
        data.put("pos_x", position.x); data.put("pos_y", position.y); data.put("pos_z", position.z);
        data.put("dir_x", rotation.x); data.put("dir_y", rotation.y); data.put("dir_z", rotation.z);
    }

    @Override
    public void getProperties(PropertyGrid panel) {
        generalPosNames();
        panel.addCategory("obj_position", "Position");
        panel.addField("pos_x", "X position", "float", null, position.x, "Default");
        panel.addField("pos_y", "Y position", "float", null, position.y, "Default");
        panel.addField("pos_z", "Z position", "float", null, position.z, "Default");
        panel.addField("dir_x", "X rotation", "float", null, rotation.x, "Default");
        panel.addField("dir_y", "Y rotation", "float", null, rotation.y, "Default");
        panel.addField("dir_z", "Z rotation", "float", null, rotation.z, "Default");
        
        panel.addCategory("obj_objinfo", "Other");
        panel.addField("PosName", "PosName", "textlist", choicesGeneralPosNames, data.get("PosName"), "Default"); 
        panel.addField("Obj_ID", "Obj_ID", "int", null, data.get("Obj_ID"), "Default");
        if (ZoneArchive.gameMask == 1)
            panel.addField("ChildObjId", "ChildObjId", "int", null, data.get("ChildObjId"), "Default"); 
    }
    
    @Override
    public String toString() {
        String l = layer.equals("common") ? "Common" : "Layer"+layer.substring(5).toUpperCase();
        return dbInfo.name + " ( " + data.get("PosName") + " ) " + "[" + l + "]";
    }
    
    private List<String> choicesGeneralPosNames;
}
