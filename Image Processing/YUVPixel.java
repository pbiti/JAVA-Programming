/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

/**
 *
 * @author USER
 */
public class YUVPixel {
    private int yuv;
    
    public YUVPixel(short Y, short U, short V){
        yuv = Y;
        yuv = (yuv << 8) + U;
        yuv = (yuv << 8) + V;
    }
    
    public YUVPixel(YUVPixel pixel){
        yuv = pixel.yuv;
    }
    
    public YUVPixel(RGBPixel pixel){
        short red = pixel.getRed();
        short green = pixel.getGreen();
        short blue = pixel.getBlue();
        
        yuv = ((66 * red + 129 * green +  25 * blue + 128) >> 8) +  16;
        yuv = (yuv << 8) + ((( -38 * red -  74 * green + 112 * blue + 128) >> 8) + 128);
        yuv = (yuv << 8) + ((( 112 * red -  94 * green -  18 * blue + 128) >> 8) + 128);
    }
    
    public short getY(){
        short y = (short)((yuv >> 16) & 0xFF);
        return(y);
    }
    
    public short getU(){
        short u = (short)((yuv >> 8) & 0xFF);
        return(u);
    }
    
    public short getV(){
        short v = (short)(yuv & 0xFF);
        return(v);
    }
    
    public void setY(short Y){
        int dup = yuv;
        int temp = 0;
        
        temp = Y;
        temp = temp << 16;
        dup = dup & 0xFF00FFFF;
        yuv = dup | temp;
    }
    
    public void setU(short U){
        int dup = yuv;
        int temp = 0;
        
        temp = U;
        temp = temp << 8;
        dup = dup & 0xFFFF00FF;
        yuv = dup | temp;
    }
    
    public void setV(short V){
        int dup = yuv;
        int temp = 0;
        
        temp = V;      
        dup = dup & 0xFFFFFF00;
        yuv = dup | temp;
    }
    
    
}
