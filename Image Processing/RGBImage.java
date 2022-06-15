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
public class RGBImage implements Image {
    protected RGBPixel [][] Pixel;
    protected int width;
    protected int height;
    protected int colordepth;
    public static final int MAX_COLORDEPTH = 255;
    
    public RGBImage(){}
    
    public RGBImage(int width, int height, int colordepth){
        this.width = width;
        this.height = height;
        this.colordepth = colordepth;
        Pixel = new RGBPixel[height][width];
        
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Pixel[i][j] = new RGBPixel((short)0,(short)0,(short)0);
            }
        }
    }
    
    public RGBImage(RGBImage copyImg){
        height = copyImg.height;
        width = copyImg.width;
        Pixel = new RGBPixel[height][width];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Pixel[i][j] = copyImg.Pixel[i][j];
            }
        }
    }
    
    public RGBImage(YUVImage YUVImg){
        
        height = YUVImg.height;
        width = YUVImg.width;
        Pixel = new RGBPixel[YUVImg.height][YUVImg.width];

        for(int i = 0 ; i < YUVImg.height; i++){
          for(int j = 0; j < YUVImg.width; j++){
            Pixel[i][j] = new RGBPixel(YUVImg.Image[i][j]);
          }
        } 
    }
    
    public int getWidth(){ return width;}

    public int getHeight(){return height;}
    
    public int getColorDepth(){return colordepth;}
    
    public RGBPixel getPixel(int row, int col){
        return(Pixel[row][col]);
    }
    public void setPixel(int row, int col, RGBPixel pixel){
        Pixel[row][col] = pixel;
    }

    public void grayscale(){
        
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Pixel[i][j].setRed((short)(0.3*Pixel[i][j].getRed()+ 0.59*Pixel[i][j].getGreen()+ 0.11*Pixel[i][j].getBlue()));
                Pixel[i][j].setGreen(Pixel[i][j].getRed());
                Pixel[i][j].setBlue(Pixel[i][j].getRed());
            }
        }
    }
    public void doublesize(){
        RGBPixel [][] DoublePixel ;
        DoublePixel = new RGBPixel[2*height][2*width];
        
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                DoublePixel[2*i][2*j] = new RGBPixel(Pixel[i][j]);
                DoublePixel[2*i + 1][2*j] = new RGBPixel(Pixel[i][j]);
                DoublePixel[2*i][2*j + 1] = new RGBPixel(Pixel[i][j]);
                DoublePixel[2*i+1][2*j+1] = new RGBPixel(Pixel[i][j]);
                
            }
        }
        height = 2*height;
        width = 2*width;
        
        Pixel = DoublePixel;
    }
    public void halfsize(){
        RGBPixel [][] HalfPixel;
        short red, green, blue;
        
        HalfPixel = new RGBPixel [height/2][width/2];
        
        for(int i = 0; i < height/2; i++){
            for(int j = 0; j < width/2; j++){
                red = (short)((int)Pixel[2 * i][2 * j].getRed() +
                              (int)Pixel[2 * i][2 * j + 1].getRed() +
                              (int)Pixel[2 * i + 1][2 * j].getRed() +
                              (int)Pixel[2 * i][2 * j].getRed());
                red = (short)((int)red/4);

                green = (short)((int)Pixel[2 * i][2 * j].getGreen()+
                                (int)Pixel[2 * i][2 * j + 1].getGreen() +
                                (int)Pixel[2 * i + 1][2 * j].getGreen() +
                                (int)Pixel[2 * i][2 * j].getGreen());
                green = (short)((int)green/4);

                blue = (short)((int)Pixel[2 * i][2 * j].getBlue() +
                               (int)Pixel[2 * i][2 * j + 1].getBlue() +
                               (int)Pixel[2 * i + 1][2 * j].getBlue() +
                               (int)Pixel[2 * i][2 * j].getBlue());
                blue = (short)((int)blue/4);

                HalfPixel[i][j] = new RGBPixel(red, green, blue);
            }
        }
        height = height/2;
        width = width/2;
        Pixel = HalfPixel;
    }
    
    public void rotateClockwise(){
        int temp;
        RGBPixel [][] RotatePixel = new RGBPixel[width][height];

        for(int i = 0; i < height; i++){
          for(int j = 0; j < width; j++){
            RotatePixel[j][height - 1 - i] = Pixel[i][j];
          }
        }
        temp = height;
        height = width;
        width = temp;
        Pixel = RotatePixel;
    } 
}
