package com.loopj.android.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuo on 2016/5/16.
 */
public class FileRequestParams extends RequestParams {

    /**
     * 封装文件，使用文件自己的名字
     * @param key
     * @param files
     * @param contentType
     * @throws FileNotFoundException
     */
    public void put(String key, File files[], String contentType) throws FileNotFoundException {

        if (key != null) {
            List<FileWrapper> fileWrappers = new ArrayList<>();
            for (File file : files) {
                if (file == null || !file.exists()) {
                    throw new FileNotFoundException();
                }
                fileWrappers.add(new FileWrapper(file, contentType, file.getName()));
            }
            fileArrayParams.put(key, fileWrappers);
        }
    }
}
