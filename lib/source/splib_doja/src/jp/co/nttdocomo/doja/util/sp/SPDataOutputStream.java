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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>CLDCのDataOutputStreamをラップするクラスです。<br/>
 * このクラスではデータ保存時に自動的にデータ長を書き込むことで
 * 可変長データの保存時にデータ長を意識する必要がない仕組みを提供します。
 * スクラッチパッドライブラリで使用する出力ストリームは全てSPDataOutputStreamとなります。</p>
 * 
 * <p>DataOutputStreamのメソッドのうち、以下のメソッドはデータ長を意識せずに使用することが可能であるため使用が推奨されます。
 * <ul>
 * <li>writeBoolean</li>
 * <li>writeByte</li>
 * <li>writeChar</li>
 * <li>writeDouble</li>
 * <li>writeFloat</li>
 * <li>writeInt</li>
 * <li>writeLong</li>
 * <li>writeShort</li>
 * <li>writeUTF</li>
 * </ul>
 * </p>
 * 
 * @version splib-5.1
 */
public class SPDataOutputStream extends DataOutputStream {
	
	/**
	 * 出力ストリームをラップするコンストラクタ
	 * @param out 出力ストリーム
	 */
	public SPDataOutputStream(OutputStream out) {
		super(out);
	}
	
	/**
	 * バイト列を書き込みます。<br/>
	 * 先頭に4バイトでバイト長を書き込みます。
	 * これにより読み込み時はデータ長を意識せずに読み取りを行うことが可能です。
	 * 
	 * @param data 書き込むバイト列
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public void writeBytes(byte[] data) throws IOException {
		if (data == null) {
			throw new NullPointerException("スクラッチパッドにnull値を保存することは出来ません。");
		}

		writeBytes(data, data.length);
	}
	
	/**
	 * バイト列のうち指定されたバイト長を書き込みます。<br/>
	 * 先頭に4バイトでバイト長を書き込みます。
	 * これにより読み込み時はデータ長を意識せずに読み取りを行うことが可能です。
	 * 
	 * @param data 書き込むバイト列
	 * @param length 書き込むバイト長
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public void writeBytes(byte[] data, int length) throws IOException {
		if (data == null) {
			throw new NullPointerException("スクラッチパッドにnull値を保存することは出来ません。");
		}
		
		super.writeInt(length);
		super.write(data);
	}
	
	/**
	 * シリアライズ可能なオブジェクトを一つ保存します。<br/>
	 * 保存されるデータ構造はSPSerializeインタフェースのtoScratchPadDataメソッドの実装により定義されます。
	 * また、データの一番先頭にはデータを保存する為に必要な領域サイズが保存されます。
	 * 
	 * @param value 保存するシリアライズ可能なオブジェクト
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public void writeSerializable(SPSerializable value) throws IOException {
		if (value == null) {
			throw new NullPointerException("スクラッチパッドにnull値を保存することは出来ません。");
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		SPDataOutputStream bufferOut = new SPDataOutputStream(buffer);
		try {
			// バッファに対してオブジェクトの内容を書き出します。
			// 書き出し内容はSPSerializeインタフェースの実装により定義されます。
			value.toScratchPadData(bufferOut);
			bufferOut.flush();
			
			// データ長を保存します。
			// データ長はSPSerializeインタフェースの実装により定義されます。
			writeInt(buffer.size());
			System.out.println("writeSPSerializable: writeInt, " + buffer.size());
			
			// バッファ内容を書き出します。
			write(buffer.toByteArray());
			System.out.println("writeSPSerializable: write, " + buffer.toByteArray().length);
		} finally {
			bufferOut.close();
		}
	}
	
	
	/**
	 * シリアライズ可能なオブジェクトの配列を保存します。<br/>
	 * 保存されるデータ構造はSPSerializeインタフェースのtoScratchPadDataメソッドの実装により定義されます。
	 * また、データの先頭には配列の要素数、
	 * 各オブジェクトの先頭にはオブジェクトを保存する為に必要な領域サイズが保存されます。
	 * 
	 * @param values 保存するシリアライズ可能なオブジェクトの配列
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public void writeSerializableArray(SPSerializable[] values) throws IOException {
		if (values == null) {
			throw new NullPointerException("スクラッチパッドにnull値を保存することは出来ません。");
		}

		// 配列内のオブジェクト数を保存
		writeInt(values.length);
		System.out.println("save[]: writeInt, " + values.length);
		
		for (int i=0; i<values.length; i++) {
			if (values[i] == null) {
				throw new NullPointerException("スクラッチパッドにnull値を保存することは出来ません。");
			}
			writeSerializable(values[i]);
		}
	}
	
	/**
	 * オブジェクトの型を自動判別して保存します。
	 * 保存可能なオブジェクトはプリミティブラッパークラスかSPSerializableインタフェースを実装したクラスとなります。
	 * 
	 * @param value 保存するオブジェクト
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public void writeObject(Object value) throws IOException {
		if (value == null) {
			throw new NullPointerException("スクラッチパッドにnull値を保存することは出来ません。");
		}
		
		if (value instanceof SPSerializable) {
			writeSerializable((SPSerializable) value);
		} else if (value instanceof String) {
			writeString((String) value);
		} else if (value instanceof Byte) {
			writeByte(((Byte)value).byteValue());
		} else if (value instanceof Short) {
			writeShort(((Short)value).shortValue());
		} else if (value instanceof Integer) {
			writeInt(((Integer)value).intValue());
		} else if (value instanceof Long) {
			writeLong(((Long)value).longValue());
		} else if (value instanceof Float) {
			writeFloat(((Float)value).floatValue());
		} else if (value instanceof Double) {
			writeDouble(((Double)value).doubleValue());
		} else if (value instanceof Character) {
			writeChar(((Character)value).charValue());
		} else if (value instanceof Boolean) {
			writeBoolean(((Boolean)value).booleanValue());
		} else {
			throw new InvalidTypeException("スクラッチパッドに保存できない型が指定されました。");
		}
	}
	
	/**
	 * writeCharsを使用して文字列を保存します。
	 * このメソッドではwriteCharの仕様に基づいて各文字が2バイトとして保存されます。
	 * また、データの先頭には4バイトで文字列を保存するのに必要なバイト数が書き込まれます。
	 * @param value 保存する文字列
	 * @throws IOException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public void writeString(String value) throws IOException {
		if (value == null) {
			throw new NullPointerException("スクラッチパッドにnull値を保存することは出来ません。");
		}

		// バイト数を計算して書き込みます。
		int length = value.length() * 2;	// writeCharsを使用するとCharacterは常に2バイトで保存されます
		writeInt(length);
		
		// データを保存します。
		writeChars(value);
	}
}
