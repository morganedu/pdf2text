
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.PDFTextStream;
import java.io.File;
import java.io.IOException;
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
        Pattern pattern = Pattern.compile("Hope.*Powell", Pattern.CASE_INSENSITIVE);
        Pattern pat = Pattern.compile("Powell.*Hope", Pattern.CASE_INSENSITIVE);

        try {
            File file = new File("/Users/user/Desktop/Hope Powell.pdf");
            StringBuffer str = new Main().getPDFText(file);
            String theString = str.toString();
            Matcher matcher = pattern.matcher(theString);
            Matcher mat = pat.matcher(theString);
            if ((mat.find() || matcher.find()) && theString.toLowerCase().contains("Recommendation".toLowerCase())) {
                System.out.println(matcher.group());
            }
            //System.out.println(theString);

        } catch (IOException ex) {
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
