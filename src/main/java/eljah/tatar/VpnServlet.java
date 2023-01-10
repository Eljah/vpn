package eljah.tatar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;


@WebServlet("/config")
public class VpnServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            File dir = new File("/opt/vpn");
            //File dir = new File("test");

            FileFilter fileFilter = new WildcardFileFilter("*.ovpn");
            File[] files = dir.listFiles(fileFilter);

            String configName
                    = req.getParameter("config");

            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(configName + ".ovpn")) {
                    res.setContentType("text/html; charset=UTF-8");
                    res.setCharacterEncoding("UTF-8");
                    ServletOutputStream out = res.getOutputStream();
                    out.write("Шул конфиг-файл исеме инде алынган, башканы уйлап табыгыз!".getBytes("UTF-8"));
                    out.flush();
                    out.close();
                    return;
                }
            }

            ProcessBuilder processBuilder = new ProcessBuilder();

            // -- Linux --

            processBuilder.command("/opt/vpn/openvpngenerate.sh",configName);

            // -- Windows --

            // Run a command
            //processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\mkyong");

            // Run a bat file
            //processBuilder.command("C:\\Users\\mkyong\\hello.bat");

            try {

                Process process = processBuilder.start();

                StringBuilder output = new StringBuilder();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }

                int exitVal = process.waitFor();
                if (exitVal == 0) {
                    System.out.println("Success!");
                    System.out.println(output);
                } else {
                    //abnormal...
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            File[] filesRenewed = dir.listFiles(fileFilter);

            for (int i = 0; i < files.length; i++) {
                if (filesRenewed[i].getName().equals(configName + ".ovpn")) {
                    res.setContentType("application/x-openvpn-profile");
                    res.addHeader("Content-Disposition", "attachment; filename="+ configName+".ovpn");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    FileUtils.copyFile(filesRenewed[i], baos);
                    res.getOutputStream().write(baos.toByteArray());
                    res.flushBuffer();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setContentType("text/html; charset=UTF-8");
            res.setCharacterEncoding("UTF-8");
            ServletOutputStream out = res.getOutputStream();
            out.write("Хата. tlgrm @min_qcqram языгыз".getBytes("UTF-8"));
            out.flush();
            out.close();
        }

    }
}


