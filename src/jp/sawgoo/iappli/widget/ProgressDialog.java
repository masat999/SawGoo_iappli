package jp.sawgoo.iappli.widget;

import jp.sawgoo.iappli.common.Color;
import jp.sawgoo.iappli.common.ImageStore;

import com.docomostar.io.ConnectionException;
import com.docomostar.media.MediaManager;
import com.docomostar.opt.ui.Graphics2;
import com.docomostar.ui.Display;
import com.docomostar.ui.Graphics;

/**
 * プログレスダイアログを表すクラス。
 * @author pcphase
 *
 */
public class ProgressDialog extends Dialog {

	/**
	 * デフォルトコンストラクタ。
	 */
	public ProgressDialog() {
		super();
	}

	/*
	 * (非 Javadoc)
	 * @see pcp.findout.Dialog#draw(com.docomostar.ui.Graphics)
	 * @Override
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

		image = MediaManager.getImage("resource:///progress.gif");
		try {
			image.use();
			((Graphics2)g).drawNthImage(image, cnt, x + padding, y + padding);
			cnt++;
			if (cnt == 8) cnt = 0;
		} catch (ConnectionException e) {
			e.printStackTrace();
		}

		try {
			int msgX = x + (image == null ? 0 : image.getWidth()) + padding * 2;
			int msgY = y + font.getAscent() + padding;
			g.drawString(message, msgX, msgY);

			int btnY = y + font.getBBoxHeight(message) + padding * 3;
			g.drawImage(ImageStore.BTN_CL, getCenter(ImageStore.BTN_CL.getWidth()), btnY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
