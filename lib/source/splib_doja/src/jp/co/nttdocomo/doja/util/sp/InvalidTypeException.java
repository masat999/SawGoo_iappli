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
package jp.co.nttdocomo.doja.util.sp;

/**
 * スクラッチパッドライブラリを使用してオブジェクトを読み取り・書き込みする際に
 * 対応していない型のオブジェクトが引数として渡された場合にスローされる例外です。
 * 
 * @since	splib-5.1
 */
public class InvalidTypeException extends RuntimeException {

	/**
	 * インスタンスを作成します。
	 */
	InvalidTypeException() {
		super();
	}
	
	/**
	 * メッセージを指定してインスタンスを作成します。
	 * @param message 例外メッセージ
	 */
	InvalidTypeException(String message) {
		super(message);
	}
	
}
