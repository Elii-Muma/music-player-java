import java.awt.image.BufferedImage;
import java.io.File;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.jaudiotagger.audio.*;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import com.mpatric.mp3agic.Mp3File;

public class Audio {

    String title, artist, songLen = " ";
    int time = 0;
    int min = 0;
    int sec = 0;
    int check = 0;
    double actualSongTime;
    String songPath;
    Mp3File mp3File;
    double msPerMin, numOfFrames, test;
    BufferedImage artwork = null;

    Audio(String path){
        this.songPath = path;
        try{

            mp3File = new Mp3File(songPath);
            msPerMin = (double)mp3File.getFrameCount()/mp3File.getLengthInMilliseconds();
            numOfFrames =  mp3File.getFrameCount();

           // test = 0.073 * (500/sPerMin);
            System.out.println("frames  " + mp3File.getLengthInMilliseconds());
            //get audioFile
            AudioFile songTags = AudioFileIO.read(new File(path));
            
            if(songTags.getFile() != null) System.out.println("theres no error in tags at first");
            else System.err.println("theres a problem in the tags");

            Artwork artworkTag = songTags.getTag().getFirstArtwork();
            //read metadata of audio file
            Tag tags = songTags.getTag();            
            if(tags != null){
                title = tags.getFirst(FieldKey.TITLE);
                artist = tags.getFirst(FieldKey.ARTIST);
                if (artworkTag != null) {
                    artwork = ImageIO.read(new ByteArrayInputStream(artworkTag.getBinaryData()));
                }

                int time = (int)mp3File.getLengthInSeconds();
                convertTime(time);
                String wordTime  = " ";
                if(sec < 10){
                    wordTime = String.format("%d:0%d", min, sec);
                }else
                    wordTime = String.format("%d:%d", min, sec);

                songLen = wordTime;

                actualSongTime = mp3File.getLengthInSeconds();
                System.out.println("seconds: " + actualSongTime);
            }
            
            if(!checkForChar(title)){
                title = mp3File.getFilename();
                title = removePath(title);
            }
            if(!checkForChar(artist)){
                artist = "¯\\_('_')_/¯";
            }

            if(title.length() > 32){
                title = title.substring(0, 29) + "...";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void convertTime(int t){
                check = t;
                if(t > 60){
                    do{
                        check = check - 60;
                        min++;
                    }while(check>=60);
                    sec = check;
                }else
                    sec = t;
    }

    public String removePath(String word){
        String newWord = word;
        System.out.println("old word: " + word);

            for(int i=0; i<newWord.length(); i++){

                if(word.charAt(i) == '\\'){
                    newWord = word.substring(i+1, word.length());
                }

            }
        System.out.println("new word" + newWord);
        return newWord;
    }

    public boolean checkForChar(String word){

        System.out.println("checked");
        char[] cWord = word.toCharArray();
        for(char c : cWord){
           System.out.println("characters: " + c + " --size> " + cWord.length);        
        }


        for(int i=0; i< cWord.length; i++){

            if(word.charAt(i) >= 'a' && word.charAt(i) <= 'z'){
                System.out.println("wow, letters");
                return true;
            }
        }
        System.out.println("no chars");
        return false;

    }

    //GETTERS
     
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getSongLen() {
        return songLen;
    }
    public String getSongPath() {
        return songPath;
    }

    public Mp3File getMp3File() {
        return mp3File;
    }

    public double getMsPerMin() {
        return msPerMin;
    }

    public double getActualSongTime() {
        return actualSongTime;
    }

    public double getTest() {
        return test;
    }

    public double getNumOfFrames() {
        return numOfFrames;
    }

    public BufferedImage getArtwork() {
        return artwork;
    }

    public int getTime() {
        return time;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    public int getCheck() {
        return check;
    } 
}
