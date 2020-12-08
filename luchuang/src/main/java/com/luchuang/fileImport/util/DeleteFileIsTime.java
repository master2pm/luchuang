package com.luchuang.fileImport.util;

import com.luchuang.fileImport.pojo.Wltsj;

import java.io.File;
import java.util.concurrent.Callable;

public class DeleteFileIsTime implements Callable<Integer> {

    private File file;

    private Wltsj wltsj;

    public Wltsj getWltsj() {
        return wltsj;
    }

    public void setWltsj(Wltsj wltsj) {
        this.wltsj = wltsj;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public Integer call() throws Exception {

        Thread.sleep(20000);
        System.out.println("删除"+(file.delete()?"成功":"失败"));

        String path = FileWord.file_path.replaceAll("classes", "cache") + wltsj.getDate();

        FileWord.deleteDir(new File(path));

        return 1;

    }
}
