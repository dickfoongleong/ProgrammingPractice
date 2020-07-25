package com.hth.images;

import java.net.URL;

public class HTH_Image {
	public static URL getImageURL(String imgName) {
		URL imgPath = HTH_Image.class.getResource(imgName);
		
		return imgPath;
	}
}
