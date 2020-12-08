package com.luchuang.fileImport.util;

import com.alibaba.fastjson.JSON;
import com.luchuang.fileImport.pojo.Pics;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileWord {


//    public static String file_top = "file:/";
    public static String file_top = "file:"; //linux
    public static String file_path1 = Thread.currentThread().getContextClassLoader().getResource("").toString();
//    public static String file_path = file_path1.indexOf("file://")==-1?file_path1.replaceAll("file:",""):file_path1.replaceAll(file_top,"");
    public static String file_path = Thread.currentThread().getContextClassLoader().getResource("").toString().replaceAll(file_top,"");
    private static Logger log = Logger.getLogger(FileWord.class);
    private FileWord() {

    }

    public static Boolean delWordFile(String date,String name){
        String path = FileWord.file_path.replaceAll("classes", "cache") + date + "/" + name;
        return FileWord.del(new File(path));
    }

    public static Boolean del(String path){
        return del(new File(path));
    }

    public static Boolean del(File file) {
        boolean flag = false;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            File[] filess = files;
            for(int i = 0; i < files.length; ++i) {
                File fil = filess[i];
                del(fil);
            }
            log.info(file.delete());
        }else{
            flag = file.delete();
        }
        log.info("文件-----" + file.getName() + "-----删除" + (flag ? "成功" : "失败"));
        return flag;
    }

    public static String read(String path){
        if(path==null||path.equals("")){
            path = FileWord.file_path.replaceAll("classes","json") + "mission.json";
        }
        return FileWord.read(new File(path));
    }

    public static String readJson(String name){
        String path = FileWord.file_path.replaceAll("classes","json") + name +".json";
        System.out.println("路径:"+path);
        return FileWord.read(new File(path));
    }

    public static String read(File file) {
        BufferedReader bfr = null;
        String txt = "";
        try{
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
            bfr = new BufferedReader(read);
            String text = "";
            while((text = bfr.readLine())!=null){
                txt += text;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            try {
                bfr.close();
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
        return txt;
    }

    public static boolean writeJson(String name,String text){
        String path = FileWord.file_path.replaceAll("classes","json") + name + ".json";
        return FileWord.write(path,text);
    }

    public static boolean write(String path,String text){
        if(path==null||path.equals("")){
            path = FileWord.file_path.replaceAll("classes","json") + "mission.json";
        }
        return FileWord.write(new File(path),text);
    }

    public static boolean write(File file,String text){
        boolean flag = false;
        PrintWriter pw = null;
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            pw = new PrintWriter(write);
            pw.println(text);
            flag = true;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            if(pw!=null){
                pw.close();
            }
        }
        return flag;
    }

    public static String find(File file, String type) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            File[] var6 = files;
            for(int i = 0; i < files.length; ++i) {
                File fil = var6[i];
                if (fil.getName().contains(type)) {
                    return fil.getName();
                }
            }
        }
        return null;
    }

    public static List fileList(File file){
        List list = new ArrayList();
        File[] files = file.listFiles();

        return list;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();

            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String getCachePath(String path){
        String str = FileWord.file_path.replaceAll("classes","cache");
        if(path!=null){
            str += path;
        }
        log.info("路径："+str);
        return str;
    }

    public static byte[] toByteArray(String filename)throws IOException{
        FileChannel fc = null;
        try{
            fc = new RandomAccessFile(filename,"r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            byte[] result = new byte[(int)fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally{
            try{
                fc.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args){
//        String str = "a,b,c";
//        Pattern pattern = Pattern.compile("a|b");
//        Matcher matcher = pattern.matcher(str);
//        System.out.println(matcher.replaceAll(""));
//    }

    //base64字符串转化成图片
    public static boolean GenerateImage(String imgStr,String name){
        Pattern pattern = Pattern.compile("data:image/jpeg;base64,|data:image/png;base64,");
        // 通过base64来转化图片
        imgStr = pattern.matcher(imgStr).replaceAll("");
        BASE64Decoder decoder = new BASE64Decoder();
        // Base64解码
        byte[] imageByte = null;
        try {
            imageByte = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < imageByte.length; ++i) {
                if (imageByte[i] < 0) {// 调整异常数据
                    imageByte[i] += 256;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 生成文件路径
//        String str = FileWord.file_path.replaceAll("classes","pics");
        String filename = FileWord.file_path.replaceAll("classes", "pics") + name + ".png";
        OutputStream imageStream = null;
        try {
            // 生成文件
            File imageFile = new File(filename);
            imageFile.createNewFile();
            if(!imageFile.exists()){
                imageFile.createNewFile();
            }
            imageStream = new FileOutputStream(filename);
            imageStream.write(imageByte);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(imageStream!=null){
                    imageStream.flush();
                    imageStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
//        System.out.println("123");
        String path = "/Users/pppl/Desktop/其他项目/簏创/";
        File file = new File(path+"pics");
        String jsonName = "pics.json";
        String jsonStr = FileWord.read(path+jsonName);
        List<Pics> pics = JSON.parseArray(jsonStr,Pics.class);
//        if(file.exists()){
//            file.mkdir();
//        }
        Map<String,List<Pics>> map = new HashMap<>();
        for(Pics pic : pics){
            List<Pics> list = map.get(pic.getWid());
            if(list == null){
                list = new LinkedList<>();
            }
            list.add(pic);
            map.put(pic.getWid(),list);
//            String json = JSON.toJSONString(pic);
//            System.out.println(json);
//            FileWord.write(path+"pics/"+pic.getId(),json);
        }
        for(String key : map.keySet()){
            List<Pics> list = map.get(key);
            String json = JSON.toJSONString(list);
            FileWord.write(path+"pics/"+key+".json",json);
        }
    }
}
