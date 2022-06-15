/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.*;
import javax.swing.*;

/**
 *
 * @author USER
 */
public class YUVImage {
    public YUVPixel [][] Image;
    protected int width;
    protected int height;
    
    public YUVImage(int width, int height){
        this.width = width;
        this.height = height;
        Image = new YUVPixel[height][width];
        
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                //Image[i][j].setY((short)16);
                //Image[i][j].setU((short)128);
                //Image[i][j].setV((short)128);
                Image[i][j] = new YUVPixel((short) 16, (short) 128, (short) 128);
            }            
        }
    }
    
    public YUVImage(YUVImage copyImg){
        /*Image = copyImg.Image;
        height = copyImg.height;
        width  = copyImg.width;*/
        height = copyImg.height;
        width  = copyImg.width;

        Image = new YUVPixel[height][width];

        for(int i = 0 ; i < height; i++){
          for(int j = 0; j < width; j++){
            Image[i][j] = copyImg.Image[i][j];
          }
        }
    }
    
    public YUVImage(RGBImage RGBImg){
        height = RGBImg.height;
        width = RGBImg.width;
        Image = new YUVPixel[height][width];
        
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Image[i][j] = new YUVPixel(RGBImg.getPixel(i,j));
            }
        }
    }
    
    public YUVImage(java.io.File file) throws UnsupportedFileFormatException, FileNotFoundException{
        BufferedReader input;
        String [] ImageData;    
        String CheckFile;
        int y,u,v;
        
        try{
            input = new BufferedReader(new FileReader(file));
            CheckFile = input.readLine();
            
            if(CheckFile.compareTo("YUV3")!=0 || CheckFile == null){
                throw new UnsupportedFileFormatException("Not YUV Format");
            }
            CheckFile = input.readLine();
            ImageData = CheckFile.split(" ");
            width  = new Integer(ImageData[0]);
            //System.out.println(width);
  	    height = new Integer(ImageData[1]);
            //System.out.println(height);
            Image = new YUVPixel[height][width];
            
            for(int i = 0; i < height; i++){
  		for(int j =0 ; j < width; j++){

                    CheckFile = input.readLine();

                    ImageData = CheckFile.split(" ");

                     y = new Integer(ImageData[0]);
                     //System.out.println(y);
                     u = new Integer(ImageData[1]);
                     //System.out.println(u);
                     v = new Integer(ImageData[2]);
                     //System.out.println(v);

                     Image[i][j] = new YUVPixel( (short)y, (short)u, (short)v);
                }
  	    }

  	}
        /*catch(FileNotFoundException ex){
            throw new FileNotFoundException();
        }*/
        catch(IOException e){
            System.out.println("i/o exception");
        }       
    }
    
    public String toString(){
        StringBuilder str = new StringBuilder ("");

        str.append("YUV3").append("\n").append(width).append(" ").append(height);

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                str.append("\n").append(Image[i][j].getY()).append(" ").append(Image[i][j].getU()).append(" ").append(Image[i][j].getV());
            }
        }
       return(str.toString());
       
    }
    
    public void toFile(java.io.File file){
    
        try(FileWriter yuvFile = new FileWriter(file, false);){
            
            yuvFile.write(toString());
        }
        catch(IOException e){System.out.println("I/O Exception caught");}
    }
    
    public void equalize(){
        YUVPixel [][]EqImg = new YUVPixel[height][width];
        Histogram gram = new Histogram(this);

        gram.equalize();
        for(int i = 0 ; i < height; i++){
          for(int j = 0; j < width; j++){
            EqImg[i][j] = new YUVPixel(gram.getEqualizedLuminocity((int)Image[i][j].getY()), Image[i][j].getU(), Image[i][j].getV());
          }
        }
        Image = EqImg;
    }
    
}
