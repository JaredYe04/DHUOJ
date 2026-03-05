package client.service;

import client.util.CreateProblemHtml;
import client.util.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import client.util.Control;

public class ProblemURL {
    private String code;
    private List<String> url_net;
    private List<String> url_local;
    private String in = "."; // Default local path
    private String url = null;

    public ProblemURL(String code) throws Exception {
        System.out.println("[ProblemURL] Starting URL processing...");
        this.code = code;
        url_net = new ArrayList<>();
        url_local = new ArrayList<>();

        // Check if XML contains any matching URLs
        boolean hasMatches = containsMatchingUrl(code);
        System.out.println("[ProblemURL] Contains matching URLs: " + hasMatches);
        
        if (hasMatches) {
            System.out.println("[ProblemURL] Processing image URLs...");
            processUrls("image");
            System.out.println("[ProblemURL] Processing file URLs...");
            processUrls("file");
        } else {
            System.out.println("[ProblemURL] No matching URLs found in XML");
        }
        System.out.println("[ProblemURL] URL processing completed");
    }

    private boolean containsMatchingUrl(String code) {
        // Modified regex to support paths with/without leading slash, ./ prefix, and after quotes
        // Matches: /oj/upload/image/..., oj/upload/image/..., ./oj/upload/image/..., src="oj/upload/image/...
        String regex = "(?:\\.?/|^|\")oj/upload/(image|file)/[A-Za-z0-9\\._\\?%&+\\-=/#]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);
        boolean found = matcher.find();
        if (found) {
            System.out.println("[ProblemURL] Regex pattern matched in XML content");
        }
        return found;
    }

    private void processUrls(String type) throws Exception {
        // Create temporary lists for each type to avoid class-level variable accumulation
        List<String> typeUrls = new ArrayList<>();
        List<String> typePaths = new ArrayList<>();
        
        System.out.println("[ProblemURL] Processing type: " + type);
        System.out.println("[ProblemURL] Regex pattern: (?:\\.?/|^|\")oj/upload/(" + type + ")/[A-Za-z0-9\\._\\?%&+\\-=/#]*");
        
        // Modified regex to support paths with/without leading slash, ./ prefix, and after quotes
        // Matches: /oj/upload/image/..., oj/upload/image/..., ./oj/upload/image/..., src="oj/upload/image/...
        String regex = "(?:\\.?/|^|\")oj/upload/(" + type + ")/[A-Za-z0-9\\._\\?%&+\\-=/#]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);
        StringBuffer result = new StringBuffer();
        
        int matchCount = 0;

        while (matcher.find()) {
            matchCount++;
            String urlStr = matcher.group();
            System.out.println("[ProblemURL] Match #" + matchCount + " found: " + urlStr);
            
            // Unified processing: remove ./ prefix or quote if exists, ensure urlStr starts with /oj/upload/ (for building network URL)
            String urlStrForNet = urlStr;
            if (urlStrForNet.startsWith("\"")) {
                urlStrForNet = urlStrForNet.substring(1); // Remove leading quote
                System.out.println("[ProblemURL] Removed leading quote, new path: " + urlStrForNet);
            }
            if (urlStrForNet.startsWith("./")) {
                urlStrForNet = urlStrForNet.substring(2); // Remove "./"
                System.out.println("[ProblemURL] Removed ./ prefix, new path: " + urlStrForNet);
            }
            if (!urlStrForNet.startsWith("/")) {
                urlStrForNet = "/" + urlStrForNet;
                System.out.println("[ProblemURL] Added leading slash, new path: " + urlStrForNet);
            }
            
            String urlPrefix = Control.determineProtocol() + "://" + Control.getIp(); // Get server IP from config file for downloading images
            System.out.println("[ProblemURL] URL prefix: " + urlPrefix);
            String netUrl = urlPrefix + urlStrForNet;
            
            // Map /oj/upload/image/... to ./oj/upload/image/...
            // Map /oj/upload/file/... to ./oj/upload/file/...
            // Keep the /oj/upload/ structure to match existing successful cases
            String localUrl = in + urlStrForNet;

            typeUrls.add(netUrl);
            typePaths.add(localUrl);
            
            System.out.println("[ProblemURL] Original URL in XML: " + urlStr);
            System.out.println("[ProblemURL] Network URL for download: " + netUrl);
            System.out.println("[ProblemURL] Local file path: " + localUrl);
            System.out.println("[ProblemURL] ---");

            // Replace in XML: keep the original quote if it existed, use local path
            // If original had quote, replace with quoted local path; otherwise use local path directly
            String replacement;
            if (urlStr.startsWith("\"")) {
                // Original had quote, keep quote in replacement
                replacement = "\"" + localUrl.replace("\\", "/");
            } else if (urlStr.startsWith("./")) {
                // Original had ./ prefix, keep it
                replacement = localUrl.replace("\\", "/");
            } else {
                // No prefix, use local path directly
                replacement = localUrl.replace("\\", "/");
            }
            System.out.println("[ProblemURL] Replacement string: " + replacement);
            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);
        this.code = result.toString();
        
        System.out.println("[ProblemURL] Total matches for type " + type + ": " + matchCount);
        System.out.println("[ProblemURL] URLs to download: " + typeUrls.size());
        for (int i = 0; i < typeUrls.size(); i++) {
            System.out.println("[ProblemURL]   [" + (i+1) + "] Remote: " + typeUrls.get(i));
            System.out.println("[ProblemURL]   [" + (i+1) + "] Local:  " + typePaths.get(i));
        }

        // Download resources of this type
        if (!typeUrls.isEmpty()) {
            System.out.println("[ProblemURL] Starting download for " + typeUrls.size() + " " + type + " file(s)...");
            new DownloadFile(typeUrls, typePaths);
            System.out.println("[ProblemURL] Download completed for type " + type);
        } else {
            System.out.println("[ProblemURL] No " + type + " files to download");
        }

        new CreateProblemHtml(code);
    }

    public String getCode() {
        return this.code;
    }
}
