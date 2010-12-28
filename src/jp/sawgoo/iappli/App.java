package jp.sawgoo.iappli;

import jp.sawgoo.iappli.common.ImageStore;

import com.docomostar.StarApplication;
import com.docomostar.ui.Display;

/**
 * アプリケーションのメインクラス。
 * @author pcphase
 *
 */
public class App extends StarApplication {

	public static final int W = Display.getWidth();
	public static final int H = Display.getHeight();

	/*
	 * (非 Javadoc)
	 * @see com.docomostar.StarApplication#started(int)
	 * @Override
	 */
	public void started(int launchType) {
//		if (launchType == StarApplication.LAUNCHED_AFTER_DOWNLOAD) {
//			System.out.println("LAUNCHED_AFTER_DOWNLOAD");
//		}
		ImageStore.load();
		MainView mainView = new MainView();
		Display.setCurrent(mainView);
		new Thread(mainView).start();
	}
}
