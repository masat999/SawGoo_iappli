package jp.sawgoo.iappli.common;

import com.docomostar.io.ConnectionException;
import com.docomostar.media.MediaImage;
import com.docomostar.media.MediaManager;
import com.docomostar.ui.Image;

/**
 * UIÇÃâÊëúÇä«óùÇ∑ÇÈÉNÉâÉX
 * @author pcphase
 *
 */
public class ImageStore {

	public static Image MAN_L = null;
	public static Image MAN_R = null;
	public static Image MAN_AR = null;
	public static Image GAL_L = null;
	public static Image GAL_R = null;
	public static Image GAL_AR = null;
	public static Image PET_L= null;
	public static Image PET_R = null;
	public static Image PET_AR = null;
	public static Image BTN_C = null;
	public static Image BTN_OK = null;
	public static Image BTN_CL = null;
	private static ImageStore instance = null;

	private ImageStore() {
		MediaImage mi = null;
		try {
			mi = MediaManager.getImage("resource:///man_l.gif");
			mi.use();
			MAN_L = mi.getImage();
			mi = MediaManager.getImage("resource:///man_r.gif");
			mi.use();
			MAN_R = mi.getImage();
			mi = MediaManager.getImage("resource:///man_active_r.gif");
			mi.use();
			MAN_AR = mi.getImage();
			mi = MediaManager.getImage("resource:///gal_l.gif");
			mi.use();
			GAL_L = mi.getImage();
			mi = MediaManager.getImage("resource:///gal_r.gif");
			mi.use();
			GAL_R = mi.getImage();
			mi = MediaManager.getImage("resource:///gal_active_r.gif");
			mi.use();
			GAL_AR = mi.getImage();
			mi = MediaManager.getImage("resource:///pet_l.gif");
			mi.use();
			PET_L = mi.getImage();
			mi = MediaManager.getImage("resource:///pet_r.gif");
			mi.use();
			PET_R = mi.getImage();
			mi = MediaManager.getImage("resource:///pet_active_r.gif");
			mi.use();
			PET_AR = mi.getImage();
			mi = MediaManager.getImage("resource:///btn_c.gif");
			mi.use();
			BTN_C = mi.getImage();
			mi = MediaManager.getImage("resource:///btn_ok.gif");
			mi.use();
			BTN_OK = mi.getImage();
			mi = MediaManager.getImage("resource:///btn_cancel.gif");
			mi.use();
			BTN_CL = mi.getImage();
			mi = MediaManager.getImage("resource:///null.gif");
			mi.dispose();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public static void load() {
		if (instance == null) {
			instance = new ImageStore();
		}
	}
}
