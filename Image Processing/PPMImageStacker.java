/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author USER
 */
public class PPMImageStacker {
    PPMImage StackImage;
    List <PPMImage> imglist;
    
    public PPMImageStacker(java.io.File dir) throws UnsupportedFileFormatException, FileNotFoundException{        
        File[] ImagesInDir;
        PPMImage filetoppm;
        
  
        if(!dir.exists()){System.err.println("[ERROR] Directory " + dir.getName() + " does not exist!"); return;}
        if(!dir.isDirectory()){System.err.println("[ERROR] " + dir.getName() + " is not a directory!"); return;}
        
        ImagesInDir = dir.listFiles();
        imglist = new LinkedList<PPMImage>();
        
        for(int i=0; i<ImagesInDir.length; i++){
            try{
                filetoppm = new PPMImage(ImagesInDir[i]);
                imglist.add(filetoppm);   
            }
            catch(UnsupportedFileFormatException e){throw new UnsupportedFileFormatException("not a ppm file");}
            catch(FileNotFoundException ex){throw new FileNotFoundException();}
        }
    }
    
    public void stack(){
        int height = imglist.get(0).height;
        int width = imglist.get(0).width;
        int colordepth = imglist.get(0).colordepth;
        int sumR=0, sumG=0, sumB=0;
                        
        StackImage = new PPMImage(width, height, colordepth);
       
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                
                sumR = 0; sumB = 0; sumG = 0;
                for(int k=0; k<imglist.size(); k++){
                    sumR = sumR + imglist.get(k).Pixel[i][j].getRed();
                }
               
                
                StackImage.Pixel[i][j].setRed((short)(sumR/imglist.size()));
                
                for(int k=0; k<imglist.size(); k++){
                    sumG = sumG + imglist.get(k).Pixel[i][j].getGreen();
                }
                StackImage.Pixel[i][j].setGreen((short)(sumG/imglist.size()));
                
                for(int k=0; k<imglist.size(); k++){
                    sumB = sumB + imglist.get(k).Pixel[i][j].getBlue();
                }
                StackImage.Pixel[i][j].setBlue((short)(sumB/imglist.size()));
            }            
        }            
    }
    
    public PPMImage getStackedImage(){
        return StackImage;
    }
}
