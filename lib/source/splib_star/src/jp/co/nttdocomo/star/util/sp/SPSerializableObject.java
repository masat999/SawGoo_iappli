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

import java.io.IOException;

/**
 * <p>オブジェクトの読み取り・書き込み方法を定義するための抽象クラスです。<br/>
 * このインタフェースを継承したクラスは
 * <ol>
 * <li>ScratchPad.save</li>
 * <li>ScratchPadManager.save</li>
 * <li>ScratchPad.load</li>
 * <li>ScratchPadManager.load</li>
 * </ol>
 * を利用して簡易に読み取り・書き込みが行えるようになります。<br/>
 * また、継承したクラス自身にsaveメソッド・loadメソッドが提供されます。</p>
 * 
 * <p>この抽象クラスを継承すると二重継承になってしまう場合は
 * SPSerializableインタフェースを実装することで読み取り・書き込み方法を定義することが可能です。</p>
 * 
 * @see jp.co.nttdocomo.star.util.sp.ScratchPad
 * @see jp.co.nttdocomo.star.util.sp.ScratchPadManager
 * @see jp.co.nttdocomo.star.util.sp.SPSerializable
 * 
 * @version splib-1.0
 */
public abstract class SPSerializableObject implements SPSerializable {

	/**
	 * スクラッチパッドへのデータ保存手順を定義するメソッドです。<br/>
	 * SPDataOutputStreamに対してwrite系メソッドを呼び出すことで
	 * オブジェクトが持つプロパティを順次保存していくことが出来ます。
	 * 
	 * @param out スクラッチパッドの出力ストリーム
	 * @throws IOException 領域を超える書き込みを行った場合スローされます。
	 */
	abstract public void toScratchPadData(SPDataOutputStream out) throws IOException;

	/**
	 * スクラッチパッドからのデータ復帰手順を定義するメソッドです。<br/>
	 * SPDataInputStreamに対してread系メソッドを呼び出すことで
	 * オブジェクトに対してプロパティを順次読み取ることが出来ます。<br/>
	 * このとき保存時と読み取り時に操作するプロパティの順番が一致している必要があります。
	 * 
	 * @param in スクラッチパッドの入力ストリーム
	 * @throws IOException 領域を超える読み込みを行った場合スローされます。
	 */
	abstract public void fromScratchPadData(SPDataInputStream in) throws IOException;

	/**
	 * 指定した保存先にこのオブジェクトを保存します。<br/>
	 * このメソッドはScratchPadManagerを使用するため
	 * ScratchPadManagerを使用する際の注意点を参照下さい。
	 * @param managerIndex 保存先番号
	 * @throws IOException 領域を超える書き込みを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 * @throws ConnectionException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 */
	public void save(int managerIndex) throws IOException {
		ScratchPadManager.getScratchPad(managerIndex).save(this);
	}
	
	/**
	 * 指定したスクラッチパッド領域にこのオブジェクトを保存します。
	 * @param sp　スクラッチパッド領域
	 * @throws IOException 確保された領域を超える書き込みを行った場合スローされます。
	 * @throws ConnectionException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws NullPointerException null値が渡された場合スローされます。
	 */
	public void save (ScratchPad sp) throws IOException {
		sp.save(this);
	}
	
	/**
	 * 指定した保存先からこのオブジェクトを復帰します。<br/>
	 * このメソッドはScratchPadManagerを使用するため
	 * ScratchPadManagerを使用する際の注意点を参照下さい。
	 * @param managerIndex　保存先番号
	 * @throws IOException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 * @throws ConnectionException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 */
	public void load(int managerIndex) throws IOException {
		ScratchPadManager.getScratchPad(managerIndex).load(this);
	}
	
	/**
	 * 指定したスクラッチパッド領域からこのオブジェクトを復帰します。
	 * @param sp　スクラッチパッド領域
	 * @throws IOException 確保された領域を超える読み取りを行った場合スローされます。
	 * @throws ConnectionException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws NullPointerException null値が渡された場合スローされます。
	 */
	public void load(ScratchPad sp) throws IOException {
		sp.load(this);
	}
}
