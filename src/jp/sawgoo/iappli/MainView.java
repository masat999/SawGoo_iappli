package jp.sawgoo.iappli;

import java.io.IOException;

import javax.microedition.io.Connector;


import jp.co.nttdocomo.star.device.location.LocationProvider;
import jp.sawgoo.iappli.common.Color;
import jp.sawgoo.iappli.common.ImageStore;
import jp.sawgoo.iappli.common.Type;
import jp.sawgoo.iappli.widget.Dialog;
import jp.sawgoo.iappli.widget.Preference;
import jp.sawgoo.iappli.widget.ProgressDialog;

import com.docomostar.StarApplication;
import com.docomostar.device.location.Location;
import com.docomostar.device.location.LocationException;
import com.docomostar.io.HttpConnection;
import com.docomostar.lang.UnsupportedOperationException;
import com.docomostar.system.Launcher;
import com.docomostar.ui.Canvas;
import com.docomostar.ui.Display;
import com.docomostar.ui.Font;
import com.docomostar.ui.Graphics;
import com.docomostar.ui.Image;

/**
 * メイン画面
 * @author pcphase
 *
 */
public class MainView extends Canvas implements Runnable {

	private static final String BASE_URL = "http://sawgoo.jp/api/";
	private static final String CREATE_URL = BASE_URL + "create_data.php";
	private static final String[] MAP_URL = new String[]{BASE_URL + "show_map.php"};
	private static Graphics g;
	private static Font font = Font.getDefaultFont();
	private static final int M = 20;

	// モードコントロール
	private static final int MODE_DEFAULT = 0;
	private static final int MODE_MENU = 1;
	private static final int MODE_PROGRESS = 2;
	private static final int MODE_DIALOG = 3;
	private static final int MODE_PREFERENCE = 4;
	private static int mode = MODE_DEFAULT;

	// ボタン関連
	private static int selectedIndex = 0;
	private static Image[] btn = new Image[3];

	// ダイアログ
	private static ProgressDialog pdialog;
	private static Dialog dialog;

	// メニュー関連
	private static final int MENU_W = 160;
	private static final int MENU_H = 24;
	private static final String[] MENU_NAMES = new String[]{"マップを表示", "設定"};
	private static int selectedMenuIndex = 0;
	private static int menuY = App.H;	// for animation

	// 設定関連
	private static int prefX = -App.W;	// for animation

	// 通信関連
	private MyHttpClient client;
	private Thread requestThread = null;

	public MainView() {
		g = getGraphics();
		setSoftLabel(SOFT_KEY_1, "MENU");
		setSoftLabel(SOFT_KEY_2, "終了");
		pdialog = new ProgressDialog();
		pdialog.setWidth(App.W - 40);
		dialog = new Dialog();
	}

	/**
	 * Runnable#run()にて描画処理を行うため、ここでは何もしない。
	 * @see com.docomostar.ui.Canvas#paint(com.docomostar.ui.Graphics)
	 * @Override
	 */
	public void paint(Graphics g) {
	}

	/**
	 * キーパットイベントをハンドルする。イベントのハンドルは全てKEY_PRESSED_EVENTで処理することとする。
	 * @see com.docomostar.ui.Canvas#processEvent(int, int)
	 * @Override
	 */
	public void processEvent(int type, int param) {
		//TODO ソフトキーの制御
		super.processEvent(type, param);
		if (type == Display.KEY_PRESSED_EVENT) {
			switch (mode) {
			/*
			 * 通常表示モード
			 * 上下左右キー：フォーカス移動
			 * 選択キー：送信処理を実行
			 * ソフトキー１：メニュー開
			 * ソフトキー２：アプリ終了
			 */
			case MODE_DEFAULT:
				switch (param) {
				// 左キー
				case Display.KEY_LEFT:
					if (selectedIndex > 0) {
						selectedIndex--;
						updateFocusedButtonStatus();
					}
					break;
				// 上キー
				case Display.KEY_UP:
					if (selectedIndex > 0) {
						selectedIndex--;
						updateFocusedButtonStatus();
					}
					break;
				// 右キー
				case Display.KEY_RIGHT:
					if (selectedIndex < btn.length - 1) {
						selectedIndex++;
						updateFocusedButtonStatus();
					}
					break;
				// 下キー
				case Display.KEY_DOWN:
					if (selectedIndex < btn.length - 1) {
						selectedIndex++;
						updateFocusedButtonStatus();
					}
					break;
				// 選択キー
				case Display.KEY_SELECT:
					client = new MyHttpClient();
					client.setClientListener(new ClientListener() {
						public void gpsStart() {
							pdialog.setMessage("GPS情報を取得しています");
							pdialog.show();
							mode = MODE_PROGRESS;
						}
						public void requestStart() {
							pdialog.setMessage("情報を送信しています");
						}
						public void processEnd(int status) {
							pdialog.dismiss();
							if (status == 200) {
								dialog.setMessage("送信しました");
								System.out.println("sendRequest successful.");
							} else {
								dialog.setMessage("送信に失敗しました");
								System.out.println(status);
							}
							mode = MODE_DIALOG;
							dialog.show();
						}
					});
					requestThread = new Thread(client);
					requestThread.start();
					break;
				// ソフトキー１
				case Display.KEY_SOFT1:
					mode = MODE_MENU;
					break;
				// ソフトキー２
				case Display.KEY_SOFT2:
					StarApplication.getThisStarApplication().terminate();
					break;
				}
				break;

			/*
			 * メニュー表示モード
			 * 上下左右キー：フォーカス移動
			 * 選択キー：選択メニュー実行
			 * ソフトキー１：メニュー閉
			 * ソフトキー２：アプリ終了
			 */
			case MODE_MENU:
				switch (param) {
				// 左キー
				case Display.KEY_LEFT:
					if (selectedMenuIndex > 0) {
						selectedMenuIndex--;
						updateFocusedButtonStatus();
					}
					break;
				// 上キー
				case Display.KEY_UP:
					if (selectedMenuIndex > 0) {
						selectedMenuIndex--;
						updateFocusedButtonStatus();
					}
					break;
				// 右キー
				case Display.KEY_RIGHT:
					if (selectedMenuIndex < MENU_NAMES.length - 1) {
						selectedMenuIndex++;
						updateFocusedButtonStatus();
					}
					break;
				// 下キー
				case Display.KEY_DOWN:
					if (selectedMenuIndex < MENU_NAMES.length - 1) {
						selectedMenuIndex++;
						updateFocusedButtonStatus();
					}
					break;
				// 選択キー
				case Display.KEY_SELECT:
					if (selectedMenuIndex == 0) {
						// マップを表示
						try {
							Launcher.launch(Launcher.LAUNCH_BROWSER_SUSPEND, MAP_URL);
						} catch (UnsupportedOperationException e) {
							Launcher.launch(Launcher.LAUNCH_BROWSER, MAP_URL);
						}
					}
					else if (selectedMenuIndex == 1) {
						mode = MODE_PREFERENCE;
						Preference p = new Preference();
					}
					break;
				// ソフトキー１
				case Display.KEY_SOFT1:
					mode = MODE_DEFAULT;
					break;
				// ソフトキー２
				case Display.KEY_SOFT2:
					StarApplication.getThisStarApplication().terminate();
					break;
				}
				break;

			/*
			 * プログレスダイアログ表示モード
			 * 選択キー：MyHttpClientキャンセル処理→ダイアログ閉→通常モードに戻る。
			 */
			case MODE_PROGRESS:
				if (param == Display.KEY_SELECT) {
					try {
						client.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
					pdialog.dismiss();
					mode = MODE_DEFAULT;
				}
				break;

			/*
			 * ダイアログ表示モード
			 * 選択キー：ダイアログ閉→通常表示モードに戻る。
			 */
			case MODE_DIALOG:
				if (param == Display.KEY_SELECT) {
					dialog.dismiss();
					mode = MODE_DEFAULT;
				}
				break;

			/*
			 * 設定表示モード
			 * ソフトキー１：通常表示モードに戻る。
			 */
			case MODE_PREFERENCE:
				if (param == Display.KEY_SOFT1) {
					mode = MODE_DEFAULT;
				}
				break;
			}
		}
	}

	/*
	 * (非 Javadoc)
	 * @see java.lang.Runnable#run()
	 * @Override
	 */
	public void run() {
		updateFocusedButtonStatus();
		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			g.lock();
			g.setColor(Color.APP_BACKGROUND);
			g.fillRect(0, 0, App.W, App.H);
			drawButton();
			drawMenu();
			drawPreference();
			pdialog.draw(g);
			dialog.draw(g);
			g.unlock(false);
		}
	}

	/**
	 * ボタン描画に関わる処理を記述する。<br/>
	 * ボタンの画像サイズに依存した処理が含まれるので、画像変更時は要注意。
	 */
	private void drawButton() {
		int posX = prefX + App.W;
		g.drawImage(ImageStore.MAN_L, 10 + posX, 10);
		g.drawImage(ImageStore.GAL_L, 10 + posX, 90);
		g.drawImage(ImageStore.PET_L, 10 + posX, 170);
		int cx = 10 + 128;
		int rx = App.W - 10 - 64;
		for (int i = cx; i < rx + 9; i += 10) {
			g.drawImage(ImageStore.BTN_C, i + posX, 10);
			g.drawImage(ImageStore.BTN_C, i + posX, 90);
			g.drawImage(ImageStore.BTN_C, i + posX, 170);
		}
		for (int i = 0; i <  3; i++) {
			g.drawImage(btn[i], rx + posX, 10 + (i * 80));
		}
	}

	private void drawPreference() {
		// アニメーションのための演算処理
		int delta = App.W / 5;
		switch (mode) {
		// 設定を開く挙動
		case MODE_PREFERENCE:
			if (prefX < 0) {
				prefX += delta;
				if (prefX > 0) {
					prefX = 0;
				}
			}
			break;
		// 設定を閉じる挙動
		case MODE_DEFAULT:
			if (prefX > -App.W) {
				prefX -= delta;
				if (prefX < -App.W) {
					prefX = -App.W;
				}
			}
			break;
		}
		// メイン描画処理
		g.setColor(Color.MENU_FONT);
		g.drawString("設定", 5 + prefX, 5 + font.getAscent());
		//TODO 設定画面の描画処理
	}

	/**
	 * メニュー描画に関わる処理を記述する。<br/>
	 * 開く・閉じるアニメーションも実装している。
	 * @param g
	 */
	private void drawMenu() {
		if (mode == MODE_MENU || menuY < App.H) {
			g.setColor(Color.MENU_BACKGROUND);
			g.fillRect(0, 0, App.W, App.H);
			// アニメーションのための演算処理
			switch (mode) {
			// メニューを開く挙動
			case MODE_MENU:
				if (menuY > App.H - MENU_H * MENU_NAMES.length) {
					menuY -= M;
					if (menuY < App.H - MENU_H * MENU_NAMES.length) {
						menuY = App.H - MENU_H * MENU_NAMES.length;
					}
				}
				break;
			// メニューを閉じる挙動
			case MODE_DEFAULT:
				if (menuY < App.H) {
					menuY += M;
					if (menuY > App.H) {
						menuY = App.H;
					}
				}
				break;
			}
			// メイン描画処理
			for (int i = 0; i < MENU_NAMES.length; i++) {
				int baseY = menuY + MENU_H * i;
				if (i == selectedMenuIndex) {
					g.setColor(Color.MENU_FOCUS);
				} else {
					g.setColor(Color.MENU_DEFAULT);
				}
				g.fillRect(0, baseY, MENU_W, MENU_H);
				g.setColor(Color.MENU_BORDER);
				g.drawRect(0, baseY, MENU_W, MENU_H);
				g.setColor(Color.MENU_FONT);
				g.drawString(MENU_NAMES[i], 5, baseY + font.getAscent());
			}
			//setSoftLabel(SOFT_KEY_1, "MENU閉");
		}
	}

	/**
	 * ボタンの状態をリセットする。
	 */
	private void resetButtonStatus() {
		btn[0] = ImageStore.MAN_R;
		btn[1] = ImageStore.GAL_R;
		btn[2] = ImageStore.PET_R;
	}

	/**
	 * ボタンのフォーカス状態を更新する。
	 */
	private void updateFocusedButtonStatus() {
		resetButtonStatus();
		switch (selectedIndex) {
		case 0: btn[selectedIndex] = ImageStore.MAN_AR; break;
		case 1: btn[selectedIndex] = ImageStore.GAL_AR; break;
		case 2: btn[selectedIndex] = ImageStore.PET_AR; break;
		}
	}

	/**
	 * HTTP(S)送信、GPS取得に関わる処理を担当するクラス。<br/>
	 * モードの制御や中断処理に対応するため別スレッド化しているが、
	 * 中断処理はネイティブな実装ではなく、キー処理中のフラグにより判断している。<br/>
	 * 当然のことながら、送信したHTTPリクエストをキャンセルすることなどはできない。
	 * @author mtsuchino
	 *
	 */
	private class MyHttpClient implements Runnable {

		private ClientListener listener;
		private LocationProvider provider = null;
		private HttpConnection conn = null;
		private boolean enable = true;
		
		/*
		 * (非 Javadoc)
		 * @see java.lang.Runnable#run()
		 * @Override
		 */
		public void run() {
			enable = true;
			listener.gpsStart();
			Location location = null;
			int status = 0;
			if (provider == null) {
				provider = LocationProvider.getLocationProvider();
			}
			try {
				location = provider.getLocation();
			} catch (SecurityException e) {
				System.out.println("GPS getLocation has been canceled");
			} catch (LocationException e) {
				e.printStackTrace();
			}
			if (location != null) {
				listener.requestStart();
				try {
					conn = (HttpConnection) Connector.open(getRequestURL(location), Connector.READ, true);
					if (conn != null) {
						conn.setRequestMethod(HttpConnection.GET);
						conn.setRequestProperty("User-Agent", "Docomo/0.0 " + System.getProperty("microedition.platform"));
					}
					if (conn != null && enable) {
						conn.connect();
						status = conn.getResponseCode();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (conn != null)
							conn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (enable) {
				listener.processEnd(status);
			} else {
				// canceled
			}
		}

		/**
		 * 処理を中断する。
		 */
		public synchronized void stop() {
			enable = false;
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * ClientListenerをセットする。
		 * @param listener ClientListener
		 */
		public void setClientListener(ClientListener listener) {
			this.listener = listener;
		}

		/**
		 * HTTPリクエストのためのGETパラメータを生成する。
		 * @param location Location
		 * @return GETパラメータを含んだURL
		 */
		private String getRequestURL(Location location) {
			StringBuffer buf = new StringBuffer();
			buf.append(CREATE_URL);
			buf.append("?type=").append(Type.VALUES[selectedIndex]);
			buf.append("&lat=").append(location.getLatitudeString(Location.PREFIX_SIGN, LocationProvider.UNIT_DEGREE));
			buf.append("&lng=").append(location.getLongitudeString(Location.PREFIX_SIGN, LocationProvider.UNIT_DEGREE));
			//TODO nameの読み込み from SP
			buf.append("&name=").append("hoge");
			//TODO twitter連携→xAuth申請
//			buf.append("&twtr=").append(0);
//			buf.append("&carrier=Docomo");
//			buf.append("&device=").append(System.getProperty("microedition.platform"));
			System.out.println(buf.toString());
			return buf.toString();
		}
	}

	/**
	 * MyHttpClientクラスのためのリスナーインターフェース。
	 * @see MyHttpClient
	 */
	private interface ClientListener {

		/**
		 * GPS通信開始時にトリガーされる。
		 */
		void gpsStart();

		/**
		 * HTTPリクエスト送信開始時にトリガーされる。
		 */
		void requestStart();

		/**
		 * 送信完了時にトリガーされる。
		 * @param status
		 */
		void processEnd(int status);

	}
}
