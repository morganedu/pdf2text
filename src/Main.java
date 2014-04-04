
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.PDFTextStream;
import edu.morgan.users.IncompleteStudent;
import edu.morgan.users.IncompleteStudents;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class Main {        
    public static void main(String args[]) {
        final String JSONTYPE = "BAFASE.json";
        final String JSONTYPEMIN = "BAFASE_min.json";
        final String OUTPUTPATH = "/Users/pablohpsilva/Desktop/Output/";
        final String JSONPath = "/Users/pablohpsilva/Downloads/"+JSONTYPE;
        final String JSONPathMin = "/Users/pablohpsilva/Downloads/"+JSONTYPEMIN;
        final String PDFFolder = "/Users/pablohpsilva/Desktop/PDFFolder";
        
        Execute exec = new Execute();
        IncompleteStudents incompletestudents = new IncompleteStudents();
        ArrayList<File> arrayPdf = new ArrayList<>();
        File folder = new File(PDFFolder);
        
        //Get all PDFs from a folder
        arrayPdf.addAll(Arrays.asList(folder.listFiles()));
        
        try {
            //Get all the students from JSON file
            incompletestudents.utility(JSONPathMin);
            
            for(IncompleteStudent student : incompletestudents.getStudents()){
                
                //Create Patterns based on LastName.*FirstName and FirstName.*LastName
                Pattern patternLastFirstName = exec.getPatternNames(student.getLastName(),student.getFirstName());
                Pattern patternFirstLastName = exec.getPatternNames(student.getFirstName(),student.getLastName());
                
                // Create folder path
                String studentFolderPath = OUTPUTPATH + student.getLastName() + "_" + student.getFirstName() + "_";
                if(!student.getId().equals(""))
                    studentFolderPath += student.getId() + "_ATO/";
                else
                    studentFolderPath += "_ATO/";
                
                if(!student.getChecklist().equals("")){
                    String[] formattedChecklist = exec.getChecklist(student.getChecklist());
                    
                    for(File file : arrayPdf){
                        //Get StudentFolder
                        String[] fileNameAux = file.getPath().split("/");
                        String fileName = fileNameAux[fileNameAux.length-1];
                        File studentFolder = new File(studentFolderPath);
                        
                        //Get the PDF file and convert it to StringBuffer
                        StringBuffer str = new Main().getPDFText(file);

                        // Convert StringBuffer to String
                        String theString = str.toString();

                        // Create matchers to match the string from PDF file.
                        Matcher matcher = patternLastFirstName.matcher(theString);
                        Matcher mat = patternFirstLastName.matcher(theString);

                        // Find for checklist item title
                        for(String checklistItem : formattedChecklist){
                            if ((mat.find() || matcher.find()) && theString.toLowerCase().contains(checklistItem.toLowerCase())) {
                                //System.out.println(matcher.group() + "\t document probably is: " + checklistItem);
                                exec.copyFile(file, studentFolder, fileName);
                                System.out.println(studentFolderPath+fileName);
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StringBuffer getPDFText(File pdfFile) throws IOException {
        PDFTextStream stream = new PDFTextStream(pdfFile);
        StringBuffer sb = new StringBuffer(1024);
        // get OutputTarget configured to pipe text to the provided StringBuffer
        OutputTarget tgt = OutputTarget.forBuffer(sb);
        stream.pipe(tgt);
        stream.close();
        return sb;
    }

}
