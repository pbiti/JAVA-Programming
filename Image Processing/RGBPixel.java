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
public class RGBPixel {
    private int rgb;
    
    public RGBPixel(short red, short green, short blue){
        rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
    }
    
    public RGBPixel(RGBPixel pixel){
        rgb = pixel.rgb;
    }
    
    public int clip(int value){
        if(value < 0){value = 0;}
        else if(value > 255){value = 255;}
        return (value);
    }
    
    public RGBPixel(YUVPixel pixel){
        int c = pixel.getY() - 16;
        int d = pixel.getU() - 128;
        int e = pixel.getV() - 128;
        
        rgb = clip((298 * c + 409 * e + 128) >> 8);
        rgb = (rgb << 8) + clip((298 * c - 100 * d - 208 * e + 128) >> 8);
        rgb = (rgb << 8) + clip((298 * c + 516 * d + 128) >> 8);
    }

    public short getRed(){
        short red = (short)((rgb >> 16) & 0xFF);
        return(red);
        
    }
    
    public short getGreen(){
        short green = (short)((rgb >> 8) & 0xFF);
        return(green);
    }
    
    public short getBlue(){
        short blue = (short)(rgb & 0xFF);
        return(blue);
    }
    
    public void setRed(short red){
        int dup = rgb;
        int temp = 0;
        
        temp = red;
        temp = temp << 16;
        dup = dup & 0xFF00FFFF;
        rgb = dup | temp;
    }
    
    public void setGreen(short green){
        int dup = rgb;
        int temp = 0;
        
        temp = green;
        temp = temp << 8;
        dup = dup & 0xFFFF00FF;
        rgb = dup | temp;
    }
    
    public void setBlue(short blue){
        int dup = rgb;
        int temp = 0;
        
        temp = blue;      
        dup = dup & 0xFFFFFF00;
        rgb = dup | temp;
    }
    
    public int getRGB(){
        return(rgb);
    }
    
    public void setRGB(int value){
        short red,green,blue;
        
        red = (short)((rgb >> 16) & 0xFF);
        green = (short)((rgb >> 8) & 0xFF);
        blue = (short)(rgb & 0xFF);
        
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }
    
    public final void setRGB(short red, short green, short blue){
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }
    
    public String toString(){
       short red, green, blue;
       StringBuilder str = new StringBuilder("");
       
       red = getRed();
       green = getGreen();
       blue = getBlue();
       
      str.append(red).append(" ").append(green).append(" ").append(blue);
      return(str.toString());
    }
}
