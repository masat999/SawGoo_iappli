package jp.sawgoo.iappli.widget;

import java.io.IOException;

import jp.co.nttdocomo.star.util.sp.ScratchPad;
import jp.co.nttdocomo.star.util.sp.ScratchPadManager;
import net.oauth.j2me.BadTokenStateException;
import net.oauth.j2me.Consumer;
import net.oauth.j2me.OAuthServiceProviderException;
import net.oauth.j2me.token.AccessToken;
import net.oauth.j2me.token.RequestToken;

import com.docomostar.system.Launcher;

/**
 * ê›íËâÊñ 
 * 
 * @author pcphase
 * 
 */
public class Preference {

	public static final String CONSUMER_KEY = "eJGDSMGlPwCE1RuGXOw";
	public static final String CONSUMER_SECRET = "jsI5ZdBUS6IOriodYj7PUZjiRlsYGMfbyZszLxc";

	public Preference() {
		Consumer consumer = new Consumer(CONSUMER_KEY, CONSUMER_SECRET, "oob");
		consumer.setSignatureMethod("HMAC-SHA1");
		try {
			String token = null;
			//TODO pinÇÃì¸óÕâÊñ ÅAåƒÇ—èoÇµ
			String pin = null;
			if (ScratchPadManager.isSaved(0)) {
				token = ScratchPadManager.loadString(0);
				RequestToken rToken = new RequestToken(token, CONSUMER_SECRET);
				rToken.setAuthorized(true);
				AccessToken aToken = consumer.getAccessToken("https://api.twitter.com/oauth/access_token", rToken);
				System.out.println(aToken.getToken());
			}
			if (token == null || token.equals("")) {
				RequestToken rToken = consumer.getRequestToken("https://api.twitter.com/oauth/request_token");
				ScratchPadManager.save(0, rToken.getToken());
				Launcher.launch(Launcher.LAUNCH_BROWSER, new String[] {"https://api.twitter.com/oauth/authenticate?oauth_token=" + rToken.getToken()});
//				Launcher.launch(Launcher.LAUNCH_BROWSER, new String[] {"https://api.twitter.com/oauth/authorize?oauth_token=" + rToken.getToken()});
			}
		} catch (OAuthServiceProviderException e) {
			e.printStackTrace();
		} catch (BadTokenStateException e) {
			e.printStackTrace();
		} catch (IOException e) { // in case of ADF/SPsize is not modified
			e.printStackTrace();
		}
		
	}
}
