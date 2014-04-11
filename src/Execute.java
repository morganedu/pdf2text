
import edu.morgan.users.IncompleteStudent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pablohpsilva
 */
public class Execute {
    
    public Pattern getPatternNames(String lastName, String firstName){
        return Pattern.compile("\\b"+lastName+".*"+firstName+"\\b", Pattern.CASE_INSENSITIVE);
    }
    
    public void copyFile(File sourceFile, File destFile, String fileName) throws IOException {
	if (!sourceFile.exists())
            return;
	if(!destFile.exists())
            destFile.mkdir();
            
        destFile = new File(destFile.getPath()+"/"+fileName);
        if(!destFile.exists())
            destFile.createNewFile();
        
	FileChannel source = null;
	FileChannel destination = null;
	source = new FileInputStream(sourceFile).getChannel();
	destination = new FileOutputStream(destFile).getChannel();
        
	if (destination != null && source != null)
            destination.transferFrom(source, 0, source.size());
        
	if (source != null)
            source.close();
        
	if (destination != null)
            destination.close();

    }
    
    public void copyFileAndChangeChecklist(ArrayList<IncompleteStudent> studentsList, File file, File studentFolder, String studentFolderPath, String fileName, String checklistItem, IncompleteStudent student) throws IOException{
        if (!studentFolder.exists()) {
            studentFolder.mkdir();
        }
        this.copyFile(file, studentFolder, fileName);
        
        String aux = student.getChecklist().replace(checklistItem + "::", "");
        if(student.getChecklist().equals(aux))
            aux = student.getChecklist().replace(checklistItem, "");
        
        student.setChecklist(aux);
        
        if(!studentsList.contains(student))
            studentsList.add(student);
        else
            studentsList.get(studentsList.indexOf(student)).setChecklist(aux);
        
        System.out.println("\t\t" + studentFolderPath + fileName);
    }
}
