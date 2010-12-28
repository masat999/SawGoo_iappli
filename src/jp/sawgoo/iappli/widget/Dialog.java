package jp.sawgoo.iappli.widget;

import jp.sawgoo.iappli.common.Color;
import jp.sawgoo.iappli.common.ImageStore;

import com.docomostar.media.MediaImage;
import com.docomostar.ui.Display;
import com.docomostar.ui.Font;
import com.docomostar.ui.Graphics;

/**
 * ダイアログを表すクラス。
 * @author pcphase
 *
 */
public class Dialog {

	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;
	protected int padding = 10;
	protected String title = "";
	protected String message = "";
	protected MediaImage image = null;
	protected Font font = Font.getDefaultFont();
	protected boolean show = false;
	protected boolean setw = false;
	protected int cnt = 0;

	/**
	 * デフォルトコンストラクタ。
	 */
	public Dialog() {
	}

	/**
	 * 描画メソッド
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		if (show == false) return;
		g.setColor(Color.DIALOG_BACKGROUND);
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
		final int R = 5;
		g.setColor(Color.DIALOG_FOREGROUND);
		g.fillArc(x, y, 2 * R, 2 * R, 90, 90);
		g.fillArc(x + width - (2 * R), y, 2 * R, 2 * R, 0, 90);
		g.fillArc(x, y + height - (2 * R), 2 * R, 2 * R, 180, 90);
		g.fillArc(x + width - (2 * R), y + height - (2 * R), 2 * R, 2 * R, 270, 90);
		g.fillRect(x + R, y, width - (2 * R), R);
		g.fillRect(x, y + R, width, height - (2 * R));
		g.fillRect(x + R, y + height - R, width - (2 * R), R);

		g.setColor(Color.DIALOG_BORDER);
		g.drawArc(x, y, 2 * R, 2 * R, 90, 90);
		g.drawArc(x + width - (2 * R), y, 2 * R, 2 * R, 0, 90);
		g.drawArc(x, y + height - (2 * R), 2 * R, 2 * R, 180, 90);
		g.drawArc(x + width - (2 * R), y + height - (2 * R), 2 * R, 2 * R, 270, 90);
		g.drawLine(x, y + R, x, y + height - R);
		g.drawLine(x + R, y, x + width - R, y);
		g.drawLine(x + width, y + R, x + width, y + height - R);
		g.drawLine(x + R, y + height, x + width - R, y + height);

		if (image != null) {
			g.drawImage(image.getImage(), x + padding, y + padding);
		}

		int msgX = x + (image == null ? padding : image.getWidth() + padding * 2);
		int msgY = y + font.getAscent() + padding;
		g.drawString(message, msgX, msgY);

		int btnY = y + font.getBBoxHeight(message) + padding * 3;
		g.drawImage(ImageStore.BTN_OK, getCenter(ImageStore.BTN_OK.getWidth()), btnY);
	}

	/**
	 * ダイアログを表示する。
	 */
	public void show() {
		show = true;
		calculateRect();
	}

	/**
	 * ダイアログを破棄する。（インスタンスは保持される）
	 */
	public void dismiss() {
		show = false;
	}

	/**
	 * ダイアログの幅を設定する。<br/>
	 * このメソッドを呼び出した場合、recalculateRectによる幅の自動計算を上書きするので注意。
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
		this.setw = true;
	}

	/**
	 * ダイアログのタイトルを設定する。
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
		calculateRect();
	}

	/**
	 * ダイアログのメッセージを設定する。
	 * @param message メッセージ
	 */
	public void setMessage(String message) {
		this.message = message;
		calculateRect();
	}

	/**
	 * ダイアログのアイコン画像を設定する。
	 * @param image アイコン画像
	 */
	public void setImage(MediaImage image) {
		this.image = image;
	}

	/**
	 * ダイアログの表示フォントを設定する。
	 * @param font
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * ダイアログの表示幅・高さを再計算する。
	 */
	protected void calculateRect() {
		// 幅・高さの算出
		if (!setw) {
			int titleW = font.getBBoxWidth(title);
			int msgW = font.getBBoxWidth(message);
			width = (titleW + 30 > msgW ? titleW : msgW) + padding * (image == null ? 2 : 3);
		}
		if (Display.getWidth() < width) width = Display.getWidth();
		height = font.getBBoxHeight(title) + font.getBBoxHeight(message) + padding * 2;
		if (true) {
			height += ImageStore.BTN_OK.getHeight() + padding * 2;
		}
		if (Display.getHeight() < height) height = Display.getHeight();

		// x, y の算出（センタリング）
		x = getCenter(width);
		y = getCenter(height);
	}

	/**
	 * 画面中央に表示するためのx座標を算出する。
	 * @param length 表示するコンポーネントの幅
	 * @return 中央表示するためのx座標値
	 */
	protected int getCenter(int length) {
		return (Display.getWidth() - length) / 2;
	}

}