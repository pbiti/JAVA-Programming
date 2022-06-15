/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;
import java.io.*;
/**
 *
 * @author USER
 */
public class Histogram {
    int colors[];
    int NumOfPixels;
    
    public Histogram(YUVImage img){        
        colors = new int[256];
        NumOfPixels = img.width * img.height;
        for(int k=0; k<colors.length; k++){colors[k]=0;}
        
        for(int i=0; i < img.height; i++){
            for(int j=0; j < img.width; j++){
                short y = img.Image[i][j].getY();
                colors[y]++;
            }
        }
    }
    
    public String toString(){
        int temp, tempNum;
        StringBuilder str = new StringBuilder("");
        int count=0;
        for(int i=0; i<colors.length; i++){
            tempNum = colors[i];
            
            while(colors[i] > 0){              
               temp =  colors[i]%10;
                OUTER:
                for (int k = 0; k<temp; k++) {
                    switch (count) {
                        case 0:
                            str.insert(0,'*');
                            break;
                        case 1:
                            str.insert(0,'$');
                            break;
                        case 3:
                            str.insert(0,'#');
                            break;
                        case 2:
                            break OUTER;
                        default:
                            break;
                    }
                }
               count++;
               colors[i]=colors[i]/10;
            }
            str.insert(0,tempNum);
            str.append("\n");
        }
        return(str.toString());
    }
    
    public void toFile(File file){
        try{
            FileWriter yuvFile = new FileWriter(file, false);
            yuvFile.write(toString());
        }
        catch(IOException e){System.out.println("I/O Exception caught");}    
    }
    
    public void equalize(){
        double[] pmf = new double[256];
        double[] cdf = new double[256];
        //int [] maxlum = new int[256];
        
        for(int i=0; i<colors.length; i++){
            pmf[i] = (double)colors[i]/NumOfPixels;
            for(int k=0; k<i; k++){
                cdf[i] += pmf[k];
            }
        }
        
        for(int l=0; l<cdf.length; l++){
            colors[l]=(int)(cdf[l]*230);
        }
    }
    
    public short getEqualizedLuminocity(int luminocity){
        return (short)colors[luminocity];
    }
}
