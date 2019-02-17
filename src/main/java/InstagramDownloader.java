import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class InstagramDownloader {

    private Document page;
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";

    /*
    * Checks the media type and calls the appropriate method
    * @param url The link to the instagram post
    * @param targetDirectory The directory to store downloaded media
    * */
    public void downloadMedia(String url, String targetDirectory){
        helper.validateURL(url);
        try{
                page = Jsoup.connect(url).userAgent(USER_AGENT).get();
                String mediaType = page.select("meta[name=medium]").first()
                        .attr("content");

                switch (mediaType) {
                    case "video":
                        downloadVideo(url, targetDirectory);
                        break;
                    case "image":
                        downloadImage(url, targetDirectory);
                        break;
                    default:
                        System.out.println("Unable to download media file.");
                        break;
                }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
    * Downloads the instagram video
    * @param url The link to the instagram post
    * @param targetDirectory The directory to store downloaded video
    * */
    public void downloadVideo(String url, String targetDirectory){
        String videoUrl = "";

        helper.validateURL(url);
        try {
                page = Jsoup.connect(url).userAgent(USER_AGENT).get();
                videoUrl = page.select("meta[property=og:video]").first()
                        .attr("content");

        } catch (IOException e){
            e.printStackTrace();
        }

       download(videoUrl, targetDirectory);
    }

    /*
     * Downloads the instagram image
     * @param url The link to the instagram post
     * @param targetDirectory The directory to store downloaded image
     * */
    public void downloadImage(String url, String targetDirectory){
        String imageUrl = "";

        helper.validateURL(url);
        try {
                page = Jsoup.connect(url).userAgent(USER_AGENT).get();
                imageUrl = page.select("meta[property=og:image]").first()
                        .attr("content");

        } catch (IOException e) {
            e.printStackTrace();
        }

        download(imageUrl, targetDirectory);
    }

    /*
    * Fetch the download link of a media without downloading
    * @url The link to the instagram post
    * @return downloadUrl Returns the download link of the media found
    * */
    public String getDownloadUrl(String url){
        String downloadUrl = "";

        helper.validateURL(url);
        try{
                page = Jsoup.connect(url).userAgent(USER_AGENT).get();
                String mediaType = page.select("meta[name=medium]").first()
                        .attr("content");

                switch (mediaType) {
                    case "video":
                        downloadUrl = page.select("meta[property=og:video]").first()
                                .attr("content");
                        break;
                    case "image":
                        downloadUrl = page.select("meta[property=og:image]").first()
                                .attr("content");
                        break;
                    default:
                        downloadUrl = "No media file found.";
                        break;
                }

        } catch (IOException e){
            e.printStackTrace();
        }

        return downloadUrl;
    }

    /*
     * Downloads the instagram media found
     * @param url The link to the instagram post
     * @param targetDirectory The directory to store downloaded media
     * */
    private void download(String url, String targetDirectory){
        String[] tempName = url.split("/");
        String filename = tempName[tempName.length-1].split("[?]")[0];

        try(InputStream inputStream = URI.create(url).toURL().openStream()){
            HttpURLConnection conn = (HttpURLConnection)URI.create(url).toURL().openConnection();

            Path targetPath = new File(targetDirectory + File.separator + filename).toPath();
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            int BYTES_PER_KB = 1024;
            double fileSize = ((double)conn.getContentLength() / BYTES_PER_KB);

            System.out.println("Media file downloaded successfully.");
            System.out.println(String.format("Media Location: %s", targetPath));
            System.out.println(String.format("Media Name: %s", filename));
            System.out.println(String.format("Media Size: %.2f kb", fileSize));
            System.out.println(String.format("Media Type: %s", helper.mediaType(filename)));

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
