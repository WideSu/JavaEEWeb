package org.hqu.vibsignal_analysis.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @author Ricky Zhang
 * @version 1.0 2019-3-11
 * 将文件通过输入流输出流保存至本地目录
 */

public class FileStoreToLocal {
    public void storeFile(List<MultipartFile> mFileList, String rootPath)throws IOException{
        for(MultipartFile mFile : mFileList){
            //文件传至后端仍有可能为空，因此在循环中进行校验
            if(mFile!=null) {
                OutputStream os = null;
                InputStream is = null;
                String fileName = null;
                try {
                    is = mFile.getInputStream();
                    fileName = mFile.getOriginalFilename();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    //1k的data buffer
                    byte[] buffer = new byte[1024];

                    int len;

                    //输出文件流保存到本地文件
                    File tempFile = new File(rootPath);
                    if (!tempFile.exists()) {
                        tempFile.mkdirs();
                    }
                    os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
                    //开始写入
                    while ((len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //关闭输入输出流
                    try {
                        os.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
