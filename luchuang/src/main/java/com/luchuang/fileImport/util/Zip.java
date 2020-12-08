package com.luchuang.fileImport.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class Zip {

    private static Logger log = Logger.getLogger(Zip.class);
    public Zip() {
    }

    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        if (!sourceFile.exists()) {
            log.info("待压缩的文件目录：" + sourceFilePath + "不存在.");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    log.info(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (sourceFiles != null && sourceFiles.length >= 1) {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[10240];

                        for(int i = 0; i < sourceFiles.length; ++i) {
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 10240);
                            boolean var14 = false;

                            int read;
                            while((read = bis.read(bufs, 0, 10240)) != -1) {
                                zos.write(bufs, 0, read);
                            }

                            if (bis != null) {
                                bis.close();
                            }
                        }

                        flag = true;
                    } else {
                        log.info("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                    }
                }
            } catch (FileNotFoundException var23) {
                log.error(var23.getMessage());
                throw new RuntimeException(var23);
            } catch (IOException var24) {
                log.error(var24.getMessage());
                throw new RuntimeException(var24);
            } finally {
                try {
                    if (zos != null) {
                        zos.close();
                    }
                } catch (IOException var22) {
                    log.error(var22.getMessage());
                    throw new RuntimeException(var22);
                }

            }
        }

        return flag;
    }

    public static void main(String[] args) {
//        String sourceFilePath = "D:\\testff";
//        String zipFilePath = "D:\\";
//        String fileName = "12700153file";
//        boolean flag = fileToZip(sourceFilePath, zipFilePath, fileName);
//        if (flag) {
//            log.info("文件打包成功!");
//        } else {
//            log.info("文件打包失败!");
//        }

        log.info("lalala");
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        log.info(sdf.format(new Date()));
    }
}