/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 *
 * @author ytxlo
 */
public class DownloadFile {
        private List<String> remoteFilePath;
        private List<String> localFilePath;
    public DownloadFile(List<String> remoteFilePath, List<String> localFilePath) throws Exception{
        System.out.println("[DownloadFile] Initializing download for " + remoteFilePath.size() + " file(s)");
        this.remoteFilePath = remoteFilePath;
        this.localFilePath = localFilePath;
        download();
    }
    
    private void download() throws Exception{
        System.out.println("[DownloadFile] Starting download process...");
        int successCount = 0;
        int skipCount = 0;
        int failCount = 0;
        
        for(int i=0;i<remoteFilePath.size();i++){
            System.out.println("[DownloadFile] ========================================");
            System.out.println("[DownloadFile] Processing file " + (i+1) + "/" + remoteFilePath.size());
            System.out.println("[DownloadFile] Remote URL: " + remoteFilePath.get(i));
            System.out.println("[DownloadFile] Local path: " + localFilePath.get(i));
            
            URL urlfile = null;
            HttpURLConnection httpUrl = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            File f = new File(localFilePath.get(i));  

            try {  
                // Check if file already exists
                if (f.exists()) {
                    long fileSize = f.length();
                    System.out.println("[DownloadFile] File already exists, size: " + fileSize + " bytes");
                    System.out.println("[DownloadFile] Skipping download");
                    skipCount++;
                    continue;  
                }
                
                // Create parent directories if they don't exist
                File parentDir = f.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    System.out.println("[DownloadFile] Creating parent directory: " + parentDir.getAbsolutePath());
                    boolean created = parentDir.mkdirs();
                    System.out.println("[DownloadFile] Parent directory created: " + created);
                }
                
                System.out.println("[DownloadFile] Creating new file: " + f.getAbsolutePath());
                boolean fileCreated = f.createNewFile();
                System.out.println("[DownloadFile] File created: " + fileCreated);
            } catch (Exception e) {
                System.out.println("[DownloadFile] ERROR: Failed to prepare local file");
                System.out.println("[DownloadFile] Exception: " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                failCount++;
                continue;
            }
            
            long bytesDownloaded = 0;
            try {
                System.out.println("[DownloadFile] Connecting to remote URL...");
                urlfile = new URL(remoteFilePath.get(i));
                httpUrl = (HttpURLConnection)urlfile.openConnection();
                httpUrl.setConnectTimeout(10000); // 10 seconds timeout
                httpUrl.setReadTimeout(10000); // 10 seconds timeout
                
                int responseCode = httpUrl.getResponseCode();
                System.out.println("[DownloadFile] HTTP Response Code: " + responseCode);
                
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    System.out.println("[DownloadFile] ERROR: HTTP response code is not OK (200)");
                    failCount++;
                    continue;
                }
                
                System.out.println("[DownloadFile] Connection established, starting download...");
                httpUrl.connect();       
                bis = new BufferedInputStream(httpUrl.getInputStream());
                bos = new BufferedOutputStream(new FileOutputStream(f));
                int len = 2048;
                byte[] b = new byte[len];
                while ((len = bis.read(b)) != -1) {
                    bos.write(b, 0, len);
                    bytesDownloaded += len;
                }
                bos.flush();
                System.out.println("[DownloadFile] Download completed, bytes downloaded: " + bytesDownloaded);
                System.out.println("[DownloadFile] File size on disk: " + f.length() + " bytes");
                successCount++;
            }
            catch (Exception e) {
                System.out.println("[DownloadFile] ERROR: Download failed");
                System.out.println("[DownloadFile] Exception: " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                failCount++;
                
                // Delete partial file on error
                if (f.exists() && f.length() == 0) {
                    boolean deleted = f.delete();
                    System.out.println("[DownloadFile] Deleted partial file: " + deleted);
                }
            }
            finally {
                try {
                    if (bis != null) {
                        bis.close();
                        System.out.println("[DownloadFile] Input stream closed");
                    }
                    if (bos != null) {
                        bos.close();
                        System.out.println("[DownloadFile] Output stream closed");
                    }
                    if (httpUrl != null) {
                        httpUrl.disconnect();
                        System.out.println("[DownloadFile] HTTP connection disconnected");
                    }
                }
                catch (IOException e) {
                    System.out.println("[DownloadFile] WARNING: Error closing streams");
                    e.printStackTrace();
                }
            }
            System.out.println("[DownloadFile] ========================================");
        }
        
        System.out.println("[DownloadFile] ========================================");
        System.out.println("[DownloadFile] Download summary:");
        System.out.println("[DownloadFile]   Total files: " + remoteFilePath.size());
        System.out.println("[DownloadFile]   Successful: " + successCount);
        System.out.println("[DownloadFile]   Skipped (exists): " + skipCount);
        System.out.println("[DownloadFile]   Failed: " + failCount);
        System.out.println("[DownloadFile] ========================================");
    }
}
