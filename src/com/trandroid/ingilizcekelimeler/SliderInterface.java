package com.trandroid.ingilizcekelimeler;

public interface SliderInterface {
	
	 static final int toLeft = 1;
	  static final int toRight = 2;
	  static final int toUp = 3;
	  static final int toDown = 4;
	  
	  public void toggle(int toWhere, boolean isOpen);

}
