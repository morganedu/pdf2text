
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
        return Pattern.compile(lastName+".*"+firstName, Pattern.CASE_INSENSITIVE);
    }
    
    /*public String[] getChecklist(String stringList){
        String aux = stringList;
        aux = aux.replace("personal essay", "essay");
        aux = aux.replace("sat or", "sat::");
        aux = aux.replace("act scores", "act");
        aux = aux.replace("secondary school cert", "waec::ssce::sce::exc::gde");
        aux = aux.replace("copy of birth certificate.*::","birth certificate");
        aux = aux.replace("oficial exam waec/sce/cxc/gde", "waec::ssce::sce::exc::gde");
        aux = aux.replace("dd 214 form.*::","dd 214");
        aux = aux.replace("unknown high school","");
        aux = aux.replace("proof of citizenship.*::", "");
        aux = aux.replace("needs.*::", "");
        aux = aux.replace("::.*residence card.*::", "::residence card::");
        aux = aux.replace("$35 application fee.*::", "");
        return aux.split("::");
    }
    */
    
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
	if (destination != null && source != null) {
		destination.transferFrom(source, 0, source.size());
	}
	if (source != null) {
		source.close();
	}
	if (destination != null) {
		destination.close();
	}

}
}
