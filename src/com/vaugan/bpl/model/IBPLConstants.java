package com.vaugan.bpl.model;

public interface IBPLConstants {
    
	public static final int SETS_TO_WIN_MATCH = 2;
	public static final int MAX_SETS_IN_MATCH = 3;
	public static final int MAX_FRAMES_IN_SET = 7;
	public static final int FRAMES_TO_WIN_SET = 4;
	public static final char[] CSFCodes = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'Z','M' };
	public static final char[] CSFCodesInverse = { 'Z', 'E', 'D', 'C', 'B', 'G', 'F','A', 'M' };

	public static final int SET_ONE = 0;
	public static final int SET_TWO = 1;
	public static final int SET_THREE = 2;
	
	public static final int HOME_PLAYER = 0;
	public static final int AWAY_PLAYER = 1;
}
