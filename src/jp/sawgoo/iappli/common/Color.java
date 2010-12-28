package jp.sawgoo.iappli.common;

import com.docomostar.ui.Graphics;

/**
 * UIの表示色を管理するクラス
 * @author pcphase
 *
 */
public class Color {

	public static final int APP_BACKGROUND = Graphics.getColorOfRGB(64, 64, 64);

	public static final int DIALOG_BACKGROUND = Graphics.getColorOfRGB(51, 51, 51, 153);
	public static final int DIALOG_FOREGROUND = Graphics.getColorOfRGB(64, 64, 64, 239);
	public static final int DIALOG_BORDER = Graphics.getColorOfRGB(239, 239, 239, 239);

	public static final int MENU_BACKGROUND = Graphics.getColorOfRGB(51, 51, 51, 153);
	public static final int MENU_FOCUS = Graphics.getColorOfRGB(0, 128, 255, 239);
	public static final int MENU_DEFAULT = Graphics.getColorOfRGB(64, 64, 64, 239);
	public static final int MENU_BORDER = Graphics.getColorOfRGB(239, 239, 239, 239);
	public static final int MENU_FONT = Graphics.getColorOfRGB(239, 239, 239, 239);
}
