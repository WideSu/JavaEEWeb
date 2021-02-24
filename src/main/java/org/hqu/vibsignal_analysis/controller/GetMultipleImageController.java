package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.ExpResultMapper;
import org.hqu.vibsignal_analysis.mapper.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Yyb
 * @version 1.0 2019-3-20
 */
@Controller
public class GetMultipleImageController {
	
	@Autowired
	private ExpResultMapper expResultMapper;
	
	//通过expId获取到expResult内的相对应的图片地址，并将其返回到picTable中
	@ResponseBody
    @RequestMapping("/getMultipleImgURL")
    public List<Image> getMulImgURL(@RequestParam("expId")String expId,HttpServletRequest request, HttpServletResponse response) throws Exception{
		//通过expId获取路径
		List<Image> MultipleImage = new ArrayList<Image>();
		String directory = expResultMapper.findURLbyexpId(expId);
    	if(directory==null || "".equals(directory) ){
    		return MultipleImage;
    	}else{
    		//将路径传入readFile判断该路径是目录还是文件 如果是文件直接返回，如果是目录迭代读取该目录下所有文件 返回url
    		List<String> paths = GetMultipleImageController.readFile(directory);
        	
    		//将url存入VO类Image中
        	for(String path:paths){
        		MultipleImage.add(new Image(path));
        	}
            //返回List<url>
        	return MultipleImage;
    	}
    }
	
	//picTable将url传送至次controller 输出picture
	@RequestMapping("/getSingleImg")
    public String getSingleImg(@RequestParam("path") String path,HttpServletRequest request, HttpServletResponse response) throws Exception{
       // System.out.println(path);
		if(path!=null){
            File file = new File(path);
            if(file.isFile() && file.exists()){
                ServletOutputStream os = null;
                FileInputStream is = null;
                try{
                    String imgPath = path;
                    is = new FileInputStream(new File(imgPath));
                    response.setContentType("multipart/form-data");
                    os = response.getOutputStream();
                    //读取文件流
                    int len = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((len = is.read(buffer)) != -1){
                        os.write(buffer,0,len);
                    }
                    os.flush();
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    is.close();
                    os.close();
                }
            }
        }
        return null;
    }
	
	//用于迭代判断某个路径下的文件和文件夹，输出所有文件夹的全路径
	public static List<String> readFile(String path){
		File file = new File(path);
		List<String> url = new ArrayList<String>();
		if(file.isDirectory()){
            System.out.println("是文件夹");
            File []files = file.listFiles();
            for(File fileIndex:files){
            	if(fileIndex.isDirectory()){
            		url.addAll(readFile(fileIndex.getPath().replace("\\", "/")+"/"));
            	}else{
            		url.add(path+fileIndex.getName());
            	}
            }
            return url;
        }else if(file.isFile()){
            System.out.println("是文件");
            url.add(path);
            return url;
        }else {
			return null;
		}
		
	}
}
