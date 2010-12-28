/*
 * Copyright 2007 Sxip Identity Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.oauth.j2me;

import net.oauth.j2me.Util;

public class OAuthParameterEncoder {
	private String unreservedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";

	public OAuthParameterEncoder() {
	}

	public String encode(String s) {
		if (s == null || "".equals(s)) {
			return "";
		}
		StringBuffer sb = new StringBuffer(s.length() * 2);
		for (int i = 0; i < s.length(); i++) {
			if (unreservedCharacters.indexOf(s.charAt(i)) == -1) {
				String t = String.valueOf(s.charAt(i));
				sb.append(Util.urlEncode(t));
			} else {
				sb.append(s.charAt(i));
			}
		}
		return sb.toString();
	}
}
