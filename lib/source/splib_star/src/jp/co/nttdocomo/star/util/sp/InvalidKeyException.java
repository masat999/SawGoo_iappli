/*
 * Copyright 2010, NTT DOCOMO,INC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package jp.co.nttdocomo.star.util.sp;

/**
 * スクラッチパッドライブラリのScrachPadMangerを使用する際、
 * 正しくないキーを指定して読み取り・書き込みを行った場合に
 * スローされる例外です。
 * 
 * @since	splib-1.0
 */
public class InvalidKeyException extends RuntimeException {

	/**
	 * インスタンスを作成します。
	 */
	InvalidKeyException() {
		super();
	}
	
	/**
	 * メッセージを指定してインスタンスを作成します。
	 * @param message 例外メッセージ
	 */
	public InvalidKeyException(String message) {
		super(message);
	}

}
