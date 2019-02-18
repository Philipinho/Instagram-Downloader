public class Main {
    public static void main(String[] args) {

        String instaImage = "https://www.instagram.com/p/Bt2ECtVg4jh/";
        String instaVideo = "https://www.instagram.com/p/BtT2UomAH9l/";
        String directory = "C:\\Users\\Lite\\Desktop";

        InstagramDownloader instagram = new InstagramDownloader();
       
		
	      instagram.downloadImage(instaImage,directory);
       
	      instagram.downloadVideo(instaVideo,directory);
       
	      instagram.downloadMedia(instaImage,directory);
       
	      System.out.println(instagram.getDownloadUrl(instaVideo));
    }
}

