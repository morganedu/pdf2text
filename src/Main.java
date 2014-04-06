
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
        final String JSONTYPE = "BAFASE.json";
        final String OUTPUTPATH = "/Users/user/Desktop/Output/";
        final String JSONPath = "/Users/user/Desktop/" + JSONTYPE;
        final String PDFFolder = "/Users/user/Desktop/S";
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
                            if (checklistItem.toLowerCase().contains("sat") && checklistItem.toLowerCase().contains("scores")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("sat") && theString.toLowerCase().contains("scores"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("act") && checklistItem.toLowerCase().contains("scores")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("sat") && theString.toLowerCase().contains("scores"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("counselor") && checklistItem.toLowerCase().contains("recommendation")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("counselor") && theString.toLowerCase().contains("recommendation"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("essay") && checklistItem.toLowerCase().contains("personal")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("essay") && theString.toLowerCase().contains("personal"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("teacher") && (checklistItem.toLowerCase().contains("recommendation"))) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("teacher") && theString.toLowerCase().contains("recommendation"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("high") && checklistItem.toLowerCase().contains("school") && checklistItem.toLowerCase().contains("trancript")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("high") && theString.toLowerCase().contains("school") && theString.toLowerCase().contains("trancript"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("secondary") && checklistItem.toLowerCase().contains("school") && checklistItem.toLowerCase().contains("certificate")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("secondary") && theString.toLowerCase().contains("school") && theString.toLowerCase().contains("certificate"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("birth") && checklistItem.toLowerCase().contains("certificate")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("birth") && theString.toLowerCase().contains("certificate"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("official") && checklistItem.toLowerCase().contains("exam")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("sssce") || theString.toLowerCase().contains("waec") || theString.toLowerCase().contains("cxc") || theString.toLowerCase().contains("gde"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("214") && checklistItem.toLowerCase().contains("form")) {
                                if ((mat.find() || matcher.find()) && (theString.toLowerCase().contains("214") && theString.toLowerCase().contains("form"))) {
                                    exec.copyFile(file, studentFolder, fileName);
                                    student.setChecklist(student.getChecklist().replace(checklistItem + "::", ""));
                                    System.out.println(studentFolderPath + fileName);
                                }
                            } else if (checklistItem.toLowerCase().contains("resident") && checklistItem.toLowerCase().contains("card")) {
                                if ((mat.find() || matcher.find()) && theString.toLowerCase().contains("resident") && theString.toLowerCase().contains("card")) {
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
