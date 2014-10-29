import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class GaussianImageProcessing {

	public static void main(String[] args) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("pictures/butterfly.jpg"));
		}
		catch (IOException e){
			e.printStackTrace();
		}
		int height = image.getHeight();
		int width = image.getWidth();
		
		HSLColor violetHSLColor = new HSLColor(new Color(120, 105, 130));  //Violet peak in cat visual spectrum
		float violetPeak = violetHSLColor.getHue();
		float violetRange = 360/30;  //this is the wavelength range for one side of the peak
		
		HSLColor blueHSLColor = new HSLColor(new Color(100, 120, 180));  //Blue peak in cat visual spectrum
		float bluePeak = blueHSLColor.getHue();
		float blueRange = 360/30;  //this is the wavelength range for one side of the peak
		
		HSLColor greenHSLColor = new HSLColor(new Color(200, 220, 90));  //Yellow-Green Peak in cat visual spectrum
		float greenPeak = greenHSLColor.getHue();
		float greenRange = 360/12;  //this is the wavelength range for one side of the peak
		
		Color c;
		HSLColor color;
		float percentage;
		float newSaturation;
		
		float[] peaks = new float[3];
		peaks[0] = violetPeak;
		peaks[1] = bluePeak;
		peaks[2] = greenPeak;
		
		float[] ranges = new float[3];
		ranges[0] = violetRange;
		ranges[1] = blueRange;
		ranges[2] = greenRange;
		
		
		for(int i = 0; i< width; i++){  //Converts color of pixel to HSL, checks distance from peak values and returns adjusted color saturation
			for(int j = 0; j < height; j++){
				c = new Color(image.getRGB(i, j));
				color = new HSLColor(c);
				
				
				float nearestPeak = violetPeak;
				float nearestRange = violetRange;
				
				for (int k = 0; k<3; k++){
					if((Math.abs(color.getHue() - peaks[k])) > (Math.abs(color.getHue() - nearestPeak))){
						nearestPeak = peaks[k];
						nearestRange = ranges[k];
					}
				}
				
				
				
				percentage = saturationPercentage(color.getHue(), nearestPeak, nearestRange);
				newSaturation = color.getSaturation()*percentage;
				color = new HSLColor(color.getHue(), newSaturation, color.getLuminance());
				c = color.getRGB();					
				image.setRGB(i, j, c.getRGB());
				
				/*
				else if((color.getHue() > (bluePeak - blueRange)) && (color.getHue() < (bluePeak + blueRange) )){
					percentage = 1 - Math.abs(color.getHue() - bluePeak)/(blueRange);
					newSaturation = color.getSaturation()*percentage;
					color = new HSLColor(color.getHue(), newSaturation, color.getLuminance());
					c = color.getRGB();
					image.setRGB(i, j, c.getRGB());
				}
				else if((color.getHue() > (greenPeak - greenRange)) && (color.getHue() < (greenPeak + greenRange))){
					percentage = 1 - Math.abs(color.getHue() - greenPeak)/(greenRange);
					newSaturation = color.getSaturation()*percentage;
					color = new HSLColor(color.getHue(), newSaturation, color.getLuminance());
					c = color.getRGB();
					image.setRGB(i, j, c.getRGB());
				}
				else{
					newSaturation = 0;
					color = new HSLColor(color.getHue(), newSaturation, color.getLuminance());
					c = color.getRGB();
					image.setRGB(i, j, c.getRGB());
				}
				*/
				
			}
		}
		RenderedImage newImage = (RenderedImage) image;
		File newFile = new File("processedPics/butterflyModified.png");
		try {
			ImageIO.write(newImage, "png", newFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public static float saturationPercentage(float hue, float peak, float range){
		float exponent = (float) (-Math.pow(hue - peak, 2)/(2*Math.pow(range, 2)));
		float percentage = (float) ((1/(range*Math.sqrt(2*Math.PI)))*Math.exp(exponent));
		return 100*percentage;
	}

}
