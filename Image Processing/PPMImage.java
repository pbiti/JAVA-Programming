/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author USER
 */
public class PPMImage extends RGBImage{
    
    public PPMImage(int width, int height, int colordepth){
        super(width, height, colordepth);
    }
    
    public PPMImage(java.io.File file) throws UnsupportedFileFormatException,FileNotFoundException {
        BufferedReader input;
        String [] ImageData;    
        
        short r,g,b;
        
        
        try(Scanner CheckFile = new Scanner(file)){
                             
            if(!CheckFile.hasNext() || !CheckFile.next().equals("P3")){
                throw new UnsupportedFileFormatException("Wrong type of file!");
            }  
            
            width = CheckFile.nextInt();
            height = CheckFile.nextInt();
            colordepth = CheckFile.nextInt();
            
           Pixel = new RGBPixel[height][width];
            
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    r = CheckFile.nextShort();
                    g = CheckFile.nextShort();
                    b = CheckFile.nextShort();
                    Pixel[i][j] = new RGBPixel(r, g, b);
                }
            }  	    
        }
        catch(FileNotFoundException ex){
            throw new FileNotFoundException();
        }        
    }
    
    public PPMImage(RGBImage img){
        super(img);
    }
    
    public PPMImage(YUVImage img){
        super(img);
    }
    
   
    public String toString(){ 
        StringBuilder str = new StringBuilder ("");

        str.append("P3").append("\n").append(width).append(" ").append(height).append("\n").append("255");

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                str.append("\n").append(Pixel[i][j].getRed()).append("\n").append(Pixel[i][j].getGreen()).append("\n").append(Pixel[i][j].getBlue());
            }
        }
       return(str.toString());
    }
    
    public void toFile(java.io.File file){
        try(FileWriter ppmFile = new FileWriter(file, false);){
            
            ppmFile.write(toString());
        }
        catch(IOException e){System.out.println("I/O Exception caught");}
    }
    
}
