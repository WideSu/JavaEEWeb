package org.hqu.vibsignal_analysis.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;

public class FileDownload {
    public ResponseEntity<byte[]> download(String path){
        File f=new File(path);
        InputStream in;
        ResponseEntity<byte[]> response=null;
        String fileName = path.split("/")[path.split("/").length-1];
        try {
            in = new FileInputStream(f);
            byte[] b=new byte[in.available()];
            in.read(b);
            HttpHeaders headers = new HttpHeaders();
            fileName = new String(fileName.getBytes("gbk"),"iso8859-1");
            headers.add("Content-Disposition", "attachment;filename="+fileName);
            HttpStatus statusCode=HttpStatus.OK;
            response = new ResponseEntity<>(b, headers, statusCode);
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
}
