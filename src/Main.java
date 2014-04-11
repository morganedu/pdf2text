
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.PDFTextStream;
import edu.morgan.users.IncompleteStudent;
import edu.morgan.users.IncompleteStudents;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    @SuppressWarnings("empty-statement")
    public static void main(String args[]) {
        /*
         final String JSONTYPE = "BAFASE.json";
         final String OUTPUTPATH = "/Users/user/Desktop/Output/";
         final String JSONPath = "/Users/user/Desktop/" + JSONTYPE;
         final String PDFFolder = "/Users/user/Desktop/S";
         */

        final String JSONTYPE = "BAFASE.json";
        final String OUTPUTPATH = "/Users/pablohpsilva/Desktop/Output/";
        final String JSONPath = "/Users/pablohpsilva/Downloads/" + JSONTYPE;
        final String PDFFolder = "/Users/pablohpsilva/Desktop/PDFFolder";

        ArrayList<IncompleteStudent> studentsProcessed = new ArrayList<>();
        HashMap<String, File> setOfFilesFromFolder = new HashMap<>();

        Execute exec = new Execute();
        IncompleteStudents incompletestudents = new IncompleteStudents();
        File folder = new File(PDFFolder);

        try {
            for (File file : Arrays.asList(folder.listFiles()))
                if (file.getName().contains("pdf"))
                    setOfFilesFromFolder.put(new Main().getPDFText(file).toString().toLowerCase().trim().replaceAll("\\s+", " "), file);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            incompletestudents.utility();
            
            int counter = 0;
            for (IncompleteStudent student : incompletestudents.getStudents()) {

                //Create Patterns based on LastName.*FirstName and FirstName.*LastName
                Pattern patternLastFirstName = exec.getPatternNames(student.getLastName(), student.getFirstName());
                Pattern patternFirstLastName = exec.getPatternNames(student.getFirstName(), student.getLastName());

                // Create folder path
                String studentFolderPath = OUTPUTPATH + student.getLastName() + "_" + student.getFirstName() + "_";
                
                if (!student.getId().equals(""))
                    studentFolderPath += student.getId() + "_ATO/";
                else
                    studentFolderPath += "_ATO/";

                File studentFolder = new File(studentFolderPath);
                
                //Debug purposes;
                counter ++;
                System.out.println("Debug purposes: " + student.getLastName() + ", " + student.getFirstName() + " - " + counter);
                
                if (!student.getChecklist().equals("")) {
                    String checklist[] = student.getChecklist().split("::");

                    for (String key : setOfFilesFromFolder.keySet()) {
                        File file = setOfFilesFromFolder.get(key);
                        String[] fileNameAux = file.getPath().split("/");
                        String fileName = fileNameAux[fileNameAux.length - 1];

                        Matcher matcher = patternLastFirstName.matcher(key);
                        Matcher mat = patternFirstLastName.matcher(key);

                        for (String checklistItem : checklist) {
                            checklistItem = checklistItem.toLowerCase();

                            if (checklistItem.contains("scores") && checklistItem.contains("sat") || checklistItem.contains("act")) {
                                if ((mat.find() || matcher.find()) && (key.contains("scores") && (key.contains("sat") || checklistItem.contains("act")))) {
                                    exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                }
                            } else if (checklistItem.contains("recommendation") && checklistItem.contains("counselor") || checklistItem.contains("teacher")) {
                                if ((mat.find() || matcher.find()) && (key.contains("recommendation") && (key.contains("counselor") || key.contains("teacher")))) {
                                    exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                }
                            } else if ((checklistItem.contains("cert") || checklistItem.contains("certificate"))) {
                                if (checklistItem.contains("secondary") && checklistItem.contains("school")) {
                                    if ((mat.find() || matcher.find()) && key.contains("secondary") && key.contains("school")) {
                                        exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                    }
                                } else if (checklistItem.contains("birth")) {
                                    if ((mat.find() || matcher.find()) && key.contains("birth")) {
                                        exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                    }
                                }
                            } else if (checklistItem.contains("essay") && checklistItem.contains("personal")) {
                                if ((mat.find() || matcher.find()) && (key.contains("essay") && key.contains("personal"))) {
                                    exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                }
                            } else if (checklistItem.contains("high") && checklistItem.contains("school") && checklistItem.contains("transcript")) {
                                if ((mat.find() || matcher.find()) && (key.contains("high") && key.contains("school") && key.contains("transcript"))) {
                                    exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                }
                            } else if (checklistItem.contains("official") && checklistItem.contains("exam")) {
                                if ((mat.find() || matcher.find()) && (key.contains("sssce") || key.contains("sce") || key.contains("waec") || key.contains("cxc") || key.contains("gde"))) {
                                    exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                }
                            } else if (checklistItem.contains("214") && checklistItem.contains("form")) {
                                if ((mat.find() || matcher.find()) && (key.contains("214") && key.contains("form"))) {
                                    exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                }
                            } else if (checklistItem.contains("resident") && checklistItem.contains("card")) {
                                if ((mat.find() || matcher.find()) && key.contains("resident") && key.contains("card")) {
                                    exec.copyFileAndChangeChecklist(studentsProcessed, file, studentFolder, studentFolderPath, fileName, checklistItem, student);
                                }
                            }
                        }
                    }
                }

                if (student.getChecklist().equals("")) {
                    student.setChecklist("COMPLETE");
                }
            }

            // Generate new JSONFile
            incompletestudents.generateJSON(incompletestudents.convertToUsers(studentsProcessed), "BAFASE_new_min");
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
