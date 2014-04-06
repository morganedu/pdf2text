
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.PDFTextStream;
import edu.morgan.users.IncompleteStudent;
import edu.morgan.users.IncompleteStudents;
import java.io.File;
import java.io.IOException;
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

    @SuppressWarnings("empty-statement")
    public static void main(String args[]) {
        /*
        final String JSONTYPE = "BAFASE.json";
        final String OUTPUTPATH = "/Users/user/Desktop/Output/";
        final String JSONPath = "/Users/user/Desktop/" + JSONTYPE;
        final String PDFFolder = "/Users/user/Desktop/S";
        */
        
        final String JSONTYPE = "BAFASE_min.json";
        final String OUTPUTPATH = "/Users/pablohpsilva/Desktop/Output/";
        final String JSONPath = "/Users/pablohpsilva/Downloads/" + JSONTYPE;
        final String PDFFolder = "/Users/pablohpsilva/Desktop/PDFFolder";
        ArrayList<IncompleteStudent> studentsProcessed = new ArrayList<>();

        Execute exec = new Execute();
        IncompleteStudents incompletestudents = new IncompleteStudents();
        ArrayList<File> arrayPdf = new ArrayList<>();
        File folder = new File(PDFFolder);

        //Get all PDFs from a folder
        arrayPdf.addAll(Arrays.asList(folder.listFiles()));

        try {
            incompletestudents.utility();
            studentsProcessed = (ArrayList<IncompleteStudent>) incompletestudents.getStudents().clone();
            //Get all the students from JSON file
            incompletestudents.utility(JSONPath);
            //studentsProcessed = (ArrayList<IncompleteStudent>) incompletestudents.getStudents().clone();

            for (IncompleteStudent student : studentsProcessed) {

                //Create Patterns based on LastName.*FirstName and FirstName.*LastName
                Pattern patternLastFirstName = exec.getPatternNames(student.getLastName(), student.getFirstName());
                Pattern patternFirstLastName = exec.getPatternNames(student.getFirstName(), student.getLastName());

                // Create folder path
                String studentFolderPath = OUTPUTPATH + student.getLastName() + "_" + student.getFirstName() + "_";
                if (!student.getId().equals("")) {
                    studentFolderPath += student.getId() + "_ATO/";
                } else {
                    studentFolderPath += "_ATO/";
                }

                File studentFolder = new File(studentFolderPath);
                if (!studentFolder.exists()) {
                    studentFolder.mkdir();
                }

                if (!student.getChecklist().equals("")) {
                    student.setChecklist(student.getChecklist().replaceAll("\\u000b", "::").toLowerCase());
                    String checklist[] = student.getChecklist().split("::");

                    for (File file : arrayPdf) {
                        //Get StudentFolder
                        String[] fileNameAux = file.getPath().split("/");
                        String fileName = fileNameAux[fileNameAux.length - 1];

                        //Get the PDF file and convert it to StringBuffer
                        if (!file.getName().contains("pdf")) {
                            continue;
                        }
                        //System.out.print("Hello, I got here");
                        StringBuffer str = new Main().getPDFText(file);

                        // Convert StringBuffer to String
                        String theString = str.toString();

                        // Create matchers to match the string from PDF file.
                        Matcher matcher = patternLastFirstName.matcher(theString);
                        Matcher mat = patternFirstLastName.matcher(theString);

                        // Find for checklist item title
                        for (String checklistItem : checklist) {
                            theString = theString.toLowerCase().trim().replaceAll("\\s+", " ");
                            checklistItem = checklistItem.toLowerCase();
                            
                            if (checklistItem.contains("sat") && checklistItem.contains("scores")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("sat") && theString.contains("scores"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("act") && checklistItem.contains("scores")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("sat") && theString.contains("scores"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("counselor") && checklistItem.contains("recommendation")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("counselor") && theString.contains("recommendation"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("essay") && checklistItem.contains("personal")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("essay") && theString.contains("personal"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("teacher") && (checklistItem.contains("recommendation"))) {
                                if ((mat.find() || matcher.find()) && (theString.contains("teacher") && theString.contains("recommendation"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("high") && checklistItem.contains("school") && checklistItem.contains("transcript")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("high") && theString.contains("school") && theString.contains("trancript"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("secondary") && checklistItem.contains("school") && checklistItem.contains("certificate")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("secondary") && theString.contains("school") && theString.contains("certificate"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("birth") && checklistItem.contains("certificate")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("birth") && theString.contains("certificate"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("official") && checklistItem.contains("exam")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("sssce") || theString.contains("waec") || theString.contains("cxc") || theString.contains("gde"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("214") && checklistItem.contains("form")) {
                                if ((mat.find() || matcher.find()) && (theString.contains("214") && theString.contains("form"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.contains("resident") && checklistItem.contains("card")) {
                                if ((mat.find() || matcher.find()) && theString.contains("resident") && theString.contains("card")) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            }
                        } /*
                         * READ ME!
                         * Algorithm below is the precisest one.
                         * Some results are not found. 
                         *
                         */ /*
                         int count = 0;
                         String[] tokenList = checklistItem.replaceAll("-", " ").split(" ");
                         for(String token : tokenList)
                         if (mat.find() || matcher.find())
                         if(theString.toLowerCase().contains(token.toLowerCase()))
                         count+=1;
                         if(count == tokenList.length){
                         exec.copyFile(file, studentFolder, fileName);
                         student.setChecklist(student.getChecklist().replace(checklistItem+"::", ""));
                         System.out.println(studentFolderPath+fileName);
                         }
                         */ /*
                         * READ ME!
                         * Algorithm below is the second best.
                         * 
                         */ /*
                         if ((mat.find() || matcher.find()) && theString.toLowerCase().contains(checklistItem.toLowerCase())){
                         exec.copyFile(file, studentFolder, fileName);
                         student.setChecklist(student.getChecklist().replace(checklistItem+"::", ""));
                         System.out.println(studentFolderPath+fileName);
                         }
                         */ /*
                         * READ ME!
                         * Algorithm below is the best one.
                         * 
                         */ ///*





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
