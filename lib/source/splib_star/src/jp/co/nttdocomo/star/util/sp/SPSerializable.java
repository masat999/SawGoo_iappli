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
 * <p>オブジェクトの読み取り・書き込み方法を定義するためのインタフェースです。<br/>
 * このインタフェースを実装したクラスは
 * <ol>
 * <li>ScratchPad.save</li>
 * <li>ScratchPadManager.save</li>
 * <li>ScratchPad.load</li>
 * <li>ScratchPadManager.load</li>
 * </ol>
 * を利用して簡易に読み取り・書き込みが行えるようになります。</p>
 * 
 * <p>このインタフェースを継承しているSPSerializableObjectを使用すると
 * オブジェクトに対してsaveメソッドとloadメソッドが追加されるためより容易に使用できます。</p>
 * 
 * @see jp.co.nttdocomo.star.util.sp.ScratchPad
 * @see jp.co.nttdocomo.star.util.sp.ScratchPadManager
 * @see jp.co.nttdocomo.star.util.sp.SPSerializableObject
 * @version splib-1.0
 */
public interface SPSerializable {
	/**
	 * スクラッチパッドへのデータ保存手順を定義するメソッドです。<br/>
	 * SPDataOutputStreamに対してwrite系メソッドを呼び出すことで
	 * オブジェクトが持つプロパティを順次保存していくことが出来ます。
	 * 
	 * @param out スクラッチパッドの出力ストリーム
	 * @throws IOException 確保した領域を超える書き込みを行った場合IOExceptionがスローされます。
	 */
	void toScratchPadData(SPDataOutputStream out) throws IOException;
	
	/**
	 * スクラッチパッドからのデータ復帰手順を定義するメソッドです。<br/>
	 * SPDataInputStreamに対してread系メソッドを呼び出すことで
	 * オブジェクトに対してプロパティを順次読み取ることが出来ます。<br/>
	 * このとき保存時と読み取り時に操作するプロパティの順番が一致している必要があります。
	 * 
	 * @param in
	 * @throws IOException
	 */
	void fromScratchPadData(SPDataInputStream in) throws IOException;
}
