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
 * ���C�����
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

	// ���[�h�R���g���[��
	private static final int MODE_DEFAULT = 0;
	private static final int MODE_MENU = 1;
	private static final int MODE_PROGRESS = 2;
	private static final int MODE_DIALOG = 3;
	private static final int MODE_PREFERENCE = 4;
	private static int mode = MODE_DEFAULT;

	// �{�^���֘A
	private static int selectedIndex = 0;
	private static Image[] btn = new Image[3];

	// �_�C�A���O
	private static ProgressDialog pdialog;
	private static Dialog dialog;

	// ���j���[�֘A
	private static final int MENU_W = 160;
	private static final int MENU_H = 24;
	private static final String[] MENU_NAMES = new String[]{"�}�b�v��\��", "�ݒ�"};
	private static int selectedMenuIndex = 0;
	private static int menuY = App.H;	// for animation

	// �ݒ�֘A
	private static int prefX = -App.W;	// for animation

	// �ʐM�֘A
	private MyHttpClient client;
	private Thread requestThread = null;

	public MainView() {
		g = getGraphics();
		setSoftLabel(SOFT_KEY_1, "MENU");
		setSoftLabel(SOFT_KEY_2, "�I��");
		pdialog = new ProgressDialog();
		pdialog.setWidth(App.W - 40);
		dialog = new Dialog();
	}

	/**
	 * Runnable#run()�ɂĕ`�揈�����s�����߁A�����ł͉������Ȃ��B
	 * @see com.docomostar.ui.Canvas#paint(com.docomostar.ui.Graphics)
	 * @Override
	 */
	public void paint(Graphics g) {
	}

	/**
	 * �L�[�p�b�g�C�x���g���n���h������B�C�x���g�̃n���h���͑S��KEY_PRESSED_EVENT�ŏ������邱�ƂƂ���B
	 * @see com.docomostar.ui.Canvas#processEvent(int, int)
	 * @Override
	 */
	public void processEvent(int type, int param) {
		//TODO �\�t�g�L�[�̐���
		super.processEvent(type, param);
		if (type == Display.KEY_PRESSED_EVENT) {
			switch (mode) {
			/*
			 * �ʏ�\�����[�h
			 * �㉺���E�L�[�F�t�H�[�J�X�ړ�
			 * �I���L�[�F���M���������s
			 * �\�t�g�L�[�P�F���j���[�J
			 * �\�t�g�L�[�Q�F�A�v���I��
			 */
			case MODE_DEFAULT:
				switch (param) {
				// ���L�[
				case Display.KEY_LEFT:
					if (selectedIndex > 0) {
						selectedIndex--;
						updateFocusedButtonStatus();
					}
					break;
				// ��L�[
				case Display.KEY_UP:
					if (selectedIndex > 0) {
						selectedIndex--;
						updateFocusedButtonStatus();
					}
					break;
				// �E�L�[
				case Display.KEY_RIGHT:
					if (selectedIndex < btn.length - 1) {
						selectedIndex++;
						updateFocusedButtonStatus();
					}
					break;
				// ���L�[
				case Display.KEY_DOWN:
					if (selectedIndex < btn.length - 1) {
						selectedIndex++;
						updateFocusedButtonStatus();
					}
					break;
				// �I���L�[
				case Display.KEY_SELECT:
					client = new MyHttpClient();
					client.setClientListener(new ClientListener() {
						public void gpsStart() {
							pdialog.setMessage("GPS�����擾���Ă��܂�");
							pdialog.show();
							mode = MODE_PROGRESS;
						}
						public void requestStart() {
							pdialog.setMessage("���𑗐M���Ă��܂�");
						}
						public void processEnd(int status) {
							pdialog.dismiss();
							if (status == 200) {
								dialog.setMessage("���M���܂���");
								System.out.println("sendRequest successful.");
							} else {
								dialog.setMessage("���M�Ɏ��s���܂���");
								System.out.println(status);
							}
							mode = MODE_DIALOG;
							dialog.show();
						}
					});
					requestThread = new Thread(client);
					requestThread.start();
					break;
				// �\�t�g�L�[�P
				case Display.KEY_SOFT1:
					mode = MODE_MENU;
					break;
				// �\�t�g�L�[�Q
				case Display.KEY_SOFT2:
					StarApplication.getThisStarApplication().terminate();
					break;
				}
				break;

			/*
			 * ���j���[�\�����[�h
			 * �㉺���E�L�[�F�t�H�[�J�X�ړ�
			 * �I���L�[�F�I�����j���[���s
			 * �\�t�g�L�[�P�F���j���[��
			 * �\�t�g�L�[�Q�F�A�v���I��
			 */
			case MODE_MENU:
				switch (param) {
				// ���L�[
				case Display.KEY_LEFT:
					if (selectedMenuIndex > 0) {
						selectedMenuIndex--;
						updateFocusedButtonStatus();
					}
					break;
				// ��L�[
				case Display.KEY_UP:
					if (selectedMenuIndex > 0) {
						selectedMenuIndex--;
						updateFocusedButtonStatus();
					}
					break;
				// �E�L�[
				case Display.KEY_RIGHT:
					if (selectedMenuIndex < MENU_NAMES.length - 1) {
						selectedMenuIndex++;
						updateFocusedButtonStatus();
					}
					break;
				// ���L�[
				case Display.KEY_DOWN:
					if (selectedMenuIndex < MENU_NAMES.length - 1) {
						selectedMenuIndex++;
						updateFocusedButtonStatus();
					}
					break;
				// �I���L�[
				case Display.KEY_SELECT:
					if (selectedMenuIndex == 0) {
						// �}�b�v��\��
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
				// �\�t�g�L�[�P
				case Display.KEY_SOFT1:
					mode = MODE_DEFAULT;
					break;
				// �\�t�g�L�[�Q
				case Display.KEY_SOFT2:
					StarApplication.getThisStarApplication().terminate();
					break;
				}
				break;

			/*
			 * �v���O���X�_�C�A���O�\�����[�h
			 * �I���L�[�FMyHttpClient�L�����Z���������_�C�A���O���ʏ탂�[�h�ɖ߂�B
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
			 * �_�C�A���O�\�����[�h
			 * �I���L�[�F�_�C�A���O���ʏ�\�����[�h�ɖ߂�B
			 */
			case MODE_DIALOG:
				if (param == Display.KEY_SELECT) {
					dialog.dismiss();
					mode = MODE_DEFAULT;
				}
				break;

			/*
			 * �ݒ�\�����[�h
			 * �\�t�g�L�[�P�F�ʏ�\�����[�h�ɖ߂�B
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
	 * (�� Javadoc)
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
	 * �{�^���`��Ɋւ�鏈�����L�q����B<br/>
	 * �{�^���̉摜�T�C�Y�Ɉˑ������������܂܂��̂ŁA�摜�ύX���͗v���ӁB
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
		// �A�j���[�V�����̂��߂̉��Z����
		int delta = App.W / 5;
		switch (mode) {
		// �ݒ���J������
		case MODE_PREFERENCE:
			if (prefX < 0) {
				prefX += delta;
				if (prefX > 0) {
					prefX = 0;
				}
			}
			break;
		// �ݒ����鋓��
		case MODE_DEFAULT:
			if (prefX > -App.W) {
				prefX -= delta;
				if (prefX < -App.W) {
					prefX = -App.W;
				}
			}
			break;
		}
		// ���C���`�揈��
		g.setColor(Color.MENU_FONT);
		g.drawString("�ݒ�", 5 + prefX, 5 + font.getAscent());
		//TODO �ݒ��ʂ̕`�揈��
	}

	/**
	 * ���j���[�`��Ɋւ�鏈�����L�q����B<br/>
	 * �J���E����A�j���[�V�������������Ă���B
	 * @param g
	 */
	private void drawMenu() {
		if (mode == MODE_MENU || menuY < App.H) {
			g.setColor(Color.MENU_BACKGROUND);
			g.fillRect(0, 0, App.W, App.H);
			// �A�j���[�V�����̂��߂̉��Z����
			switch (mode) {
			// ���j���[���J������
			case MODE_MENU:
				if (menuY > App.H - MENU_H * MENU_NAMES.length) {
					menuY -= M;
					if (menuY < App.H - MENU_H * MENU_NAMES.length) {
						menuY = App.H - MENU_H * MENU_NAMES.length;
					}
				}
				break;
			// ���j���[����鋓��
			case MODE_DEFAULT:
				if (menuY < App.H) {
					menuY += M;
					if (menuY > App.H) {
						menuY = App.H;
					}
				}
				break;
			}
			// ���C���`�揈��
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
			//setSoftLabel(SOFT_KEY_1, "MENU��");
		}
	}

	/**
	 * �{�^���̏�Ԃ����Z�b�g����B
	 */
	private void resetButtonStatus() {
		btn[0] = ImageStore.MAN_R;
		btn[1] = ImageStore.GAL_R;
		btn[2] = ImageStore.PET_R;
	}

	/**
	 * �{�^���̃t�H�[�J�X��Ԃ��X�V����B
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
	 * HTTP(S)���M�AGPS�擾�Ɋւ�鏈����S������N���X�B<br/>
	 * ���[�h�̐���⒆�f�����ɑΉ����邽�ߕʃX���b�h�����Ă��邪�A
	 * ���f�����̓l�C�e�B�u�Ȏ����ł͂Ȃ��A�L�[�������̃t���O�ɂ�蔻�f���Ă���B<br/>
	 * ���R�̂��ƂȂ���A���M����HTTP���N�G�X�g���L�����Z�����邱�ƂȂǂ͂ł��Ȃ��B
	 * @author mtsuchino
	 *
	 */
	private class MyHttpClient implements Runnable {

		private ClientListener listener;
		private LocationProvider provider = null;
		private HttpConnection conn = null;
		private boolean enable = true;
		
		/*
		 * (�� Javadoc)
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
		 * �����𒆒f����B
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
		 * ClientListener���Z�b�g����B
		 * @param listener ClientListener
		 */
		public void setClientListener(ClientListener listener) {
			this.listener = listener;
		}

		/**
		 * HTTP���N�G�X�g�̂��߂�GET�p�����[�^�𐶐�����B
		 * @param location Location
		 * @return GET�p�����[�^���܂�URL
		 */
		private String getRequestURL(Location location) {
			StringBuffer buf = new StringBuffer();
			buf.append(CREATE_URL);
			buf.append("?type=").append(Type.VALUES[selectedIndex]);
			buf.append("&lat=").append(location.getLatitudeString(Location.PREFIX_SIGN, LocationProvider.UNIT_DEGREE));
			buf.append("&lng=").append(location.getLongitudeString(Location.PREFIX_SIGN, LocationProvider.UNIT_DEGREE));
			//TODO name�̓ǂݍ��� from SP
			buf.append("&name=").append("hoge");
			//TODO twitter�A�g��xAuth�\��
//			buf.append("&twtr=").append(0);
//			buf.append("&carrier=Docomo");
//			buf.append("&device=").append(System.getProperty("microedition.platform"));
			System.out.println(buf.toString());
			return buf.toString();
		}
	}

	/**
	 * MyHttpClient�N���X�̂��߂̃��X�i�[�C���^�[�t�F�[�X�B
	 * @see MyHttpClient
	 */
	private interface ClientListener {

		/**
		 * GPS�ʐM�J�n���Ƀg���K�[�����B
		 */
		void gpsStart();

		/**
		 * HTTP���N�G�X�g���M�J�n���Ƀg���K�[�����B
		 */
		void requestStart();

		/**
		 * ���M�������Ƀg���K�[�����B
		 * @param status
		 */
		void processEnd(int status);

	}
}
