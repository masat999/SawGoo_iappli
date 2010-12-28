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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>CLDCのDataInputStreamをラップするクラスです。<br/>
 * このクラスではデータ保存時に書き込まれたデータ長を自動的に読み込んで
 * 適切な長さでデータを読み込む仕組みを提供します。
 * スクラッチパッドライブラリで使用する入力ストリームは全てSPDataInputStreamとなります。</p>
 * 
 * <p>DataInputStreamのメソッドのうち、以下のメソッドはデータ長を意識せずに使用することが可能であるため使用が推奨されます。
 * <ul>
 * <li>readBoolean</li>
 * <li>readByte</li>
 * <li>readChar</li>
 * <li>readDouble</li>
 * <li>readFloat</li>
 * <li>readInt</li>
 * <li>readLong</li>
 * <li>readShort</li>
 * <li>readUTF</li>
 * </ul>
 * </p>
 * 
 * @version splib-5.1
 */
public class SPDataInputStream extends DataInputStream {

	/**
	 * 入力ストリームをラップするコンストラクタ
	 * @param in 入力ストリーム
	 */
	public SPDataInputStream(InputStream in) {
		super(in);
	}
	
	/**
	 * バイト列を読み込みます。<br/>
	 * 保存時にはバイト長が先頭に4バイトで書き込まれるため
	 * バイト長を読み込んだ上でさらにバイト長分のデータを読み込みます。
	 * 
	 * @return 読み取りされたバイト列
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 */
	public byte[] readBytes() throws IOException {
		int length = super.readInt();
		byte[] result = new byte[length];
		super.read(result);
		return result;
	}

	/**
	 * シリアライズ可能なオブジェクトを一つ読み込みます。<br/>
	 * オブジェクトの復元方法はSPSerializableのfromScratchPadDataメソッドの実装により定義されます。
	 * また、データの先頭にはオブジェクトを保存する為に使用されている領域サイズが保存されています。
	 * 
	 * @param value 読み取り結果を受け取るインスタンス
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値に結果を受け取ろうとした場合スローされます。
	 */
	public void readSerializable(SPSerializable value) throws IOException {
		if (value == null) {
			throw new NullPointerException("null値にスクラッチパッドの内容を復元することはできません。");
		}

		int length = readInt();
		System.out.println("loadSPSerializable: readInt, " + length);
		byte[] data = new byte[length];
		in.read(data);
		System.out.println("loadSPSerializable: read, " + data.length);
		SPDataInputStream bufferIn = new SPDataInputStream(new ByteArrayInputStream(data));
		try {
			value.fromScratchPadData(bufferIn);
		} finally {
			bufferIn.close();
		}
	}
	
	/**
	 * シリアライズ可能なオブジェクトの配列を読み込みます。<br/>
	 * オブジェクトの復元方法はSPSerializableのfromScratchPadDataメソッドの実装により定義されます。
	 * また、データの先頭には配列の要素数が保存され、各オブジェクトの先頭にはオブジェクトを保存する為に使用されている領域サイズが保存されています。
	 * 
	 * @param values 読み取り結果を受け取るインスタンスの配列
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値に結果を受け取ろうとした場合スローされます。
	 */
	public void readSerializableArray(SPSerializable[] values) throws IOException {
		if (values == null) {
			throw new NullPointerException("null値にスクラッチパッドの内容を復元することはできません。");
		}

		int dataCount = readInt();
		if (dataCount > values.length) {
			throw new IOException("保存されているオブジェクトの数" + dataCount + 
					"に対して読み込み先のインスタンス数" + values.length +
					"が足りないため正しく読み込めません。");
		}
		for (int i=0; i<values.length; i++) {
			if (values[i] == null) {
				throw new NullPointerException("null値にスクラッチパッドの内容を復元することはできません。");
			}
			readSerializable(values[i]);
		}
	}
	
	/**
	 * 文字列を読み込みます。
	 * データの先頭には4バイトで文字列を保存する為に使用されているバイト数が保存されています。
	 * @return 読み込まれた文字列
	 */
	public String readString() throws IOException {
		// バイト数を読み込みます。
		int length = readInt();
		int pos = 0;
		
		// readCharsに基づいて
		StringBuffer buffer = new StringBuffer();
		while (pos < length) {
			buffer.append(readChar());
			pos += 2;
		}
		
		return buffer.toString();
	}
}
