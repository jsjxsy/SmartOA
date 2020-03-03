package com.gx.smart.lib.http.lib.model;

/**
 * @author xiaosy
 * @create 2019-12-20
 * @Describe 七牛上传文件返回信息
 **/
public class UploadImage {
    private String key;//七牛中的key，用于下载
    private String hash;//	文件hash
    private String bucket;//	七牛bucket
    private int fileSize;//	文件大小
    private String path;//	文件路径，可下载查看

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
