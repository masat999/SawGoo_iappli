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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;

/**
 * スクラッチパッドの領域を確保しコネクションの管理と基本の読み取り・書き込みを行うクラスです。<BR/>
 * 
 * @see jp.co.nttdocomo.star.util.sp.SPSerializableObject
 * @see jp.co.nttdocomo.star.util.sp.SPDataInputStream
 * @see jp.co.nttdocomo.star.util.sp.SPDataOutputStream
 * @version splib-1.0
 */
public class ScratchPad {

	/** スクラッチパッド番号 */
	private int index = 0;
	/** オフセット */
	private int offset = 0;
	/** 領域サイズ */
	private int length = 0;
	/** スクラッチパッドの最大サイズ */
	public static int MAX_SCRATCHPAD_SIZE = 2097152;	// 2048KB
	/** スクラッチパッド番号の最大個数 */
	public static int MAX_SCRATCHPAD_COUNT = 16;		// 最大16個
	
	/**
	 * スクラッチパッドの領域を指定するコンストラクタです。<br/>
	 * iアプリではスクラッチパッドを最大16個まで分割管理することが出来るため
	 * スクラッチパッド番号として指定できる値は0から15までとなります。
	 * @param index		スクラッチパッド番号(0から15まで)
	 * @param offset	指定された番号のスクラッチパッドにおけるアクセス開始オフセット
	 * @param length	アクセス開始オフセット位置からのバイト数
	 */
	public ScratchPad(int index, int offset, int length) {
		if (index < 0 || ScratchPad.MAX_SCRATCHPAD_COUNT <= index) {
			throw new RuntimeException("スクラッチパッド番号は0から" + (MAX_SCRATCHPAD_COUNT - 1) + "までの値を指定してください。");
		}
		if (length <= 0 || ScratchPad.MAX_SCRATCHPAD_SIZE < length) {
			throw new RuntimeException("領域サイズは1から" + MAX_SCRATCHPAD_SIZE + "までの値を指定してください。");
		}
		if (offset < 0 || ScratchPad.MAX_SCRATCHPAD_SIZE <= offset) {
			throw new RuntimeException("アクセス開始位置は0から" + (ScratchPad.MAX_SCRATCHPAD_SIZE - 1) + "までの値を指定してください。");
		}
		this.index = index;
		this.offset = offset;
		this.length = length;
	}
	
	/**
	 * シリアライズ可能なオブジェクトを指定された領域に保存します。<br/>
	 * save時と同じクラスを引数としてloadメソッドを呼び出すことで
	 * スクラッチパッドからデータを復帰することが出来ます。
	 * 違うクラスを指定してloadメソッドを呼び出した場合の動作は不定となります。<br/>
	 * 
	 * @param value 保存するオブジェクト。
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 確保された領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public void save(SPSerializable value) throws IOException {
		SPDataOutputStream spOut = openDataOutputStream();
		try {
			spOut.writeObject(value);
		} finally {
			spOut.close();
		}
	}
	
	/**
	 * シリアライズ可能なオブジェクトの配列を指定された領域に保存します。<br/>
	 * saveArray時と同じクラスを引数としてloadArrayメソッドを呼び出すことで
	 * スクラッチパッドからデータを復帰することが出来ます。
	 * 違うクラスを指定してloadArrayメソッドを呼び出した場合の動作は不定となります。<br/>
	 * 
	 * @param values 保存するオブジェクトの配列。
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 確保された領域を超える書き込みを行った場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public void saveArray(SPSerializable[] values) throws IOException {
		SPDataOutputStream spOut = openDataOutputStream();
		try {
			spOut.writeSerializableArray(values);
		} finally {
			spOut.close();
		}
	}

	/**
	 * シリアライズ可能なオブジェクトを指定された領域から読み取ります。<br/>
	 * save時と同じクラスを引数としてloadメソッドを呼び出すことで
	 * スクラッチパッドからデータを復帰することが出来ます。
	 * 違うクラスを指定してloadメソッドを呼び出した場合の動作は不定となります。<br/>
	 * 引数には読み込むクラスのインスタンスを生成して渡す必要があります。
	 * 
	 * @param value　読み取り結果を受け取るインスタンス
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 確保された領域を超える読み取りを行った場合スローされます。
	 * @throws NullPointerException null値に結果を受け取ろうとした場合スローされます。
	 */
	public void load(SPSerializable value) throws IOException {
		SPDataInputStream spIn = openDataInputStream();
		try {
			spIn.readSerializable(value);
		} finally {
			spIn.close();
		}
	}
	
	/**
	 * 保存されているオブジェクト配列の要素数を返します。<br/>
	 * loadArrayメソッドで配列を読み込む場合、
	 * 保存されているオブジェクトの数だけインスタンスを保持した配列を渡す必要があるため
	 * 事前にこのメソッドを使用して要素数を取得します。
	 * @return 保存されているオブジェクト配列の要素数
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 確保された領域を超える読み取りを行った場合スローされます。
	 */
	public int loadCount() throws IOException {
		SPDataInputStream spIn = openDataInputStream();
		try {
			int length = spIn.readInt();
			System.out.println("loadCount: readInt, " + length);
			return length;
		} finally {
			spIn.close();
		}
	}
	
	/**
	 * シリアライズ可能なオブジェクトの配列を指定された領域から読み取ります。<br/>
	 * saveArray時と同じクラスを引数としてloadArrayメソッドを呼び出すことで
	 * スクラッチパッドからデータを復帰することが出来ます。
	 * 違うクラスを指定してloadArrayメソッドを呼び出した場合の動作は不定となります。<br/>
	 * 引数には読み込むクラスのインスタンスを要素数分生成して渡す必要があります。
	 * 
	 * @param values 読み取り結果を受け取るインスタンスの配列 
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 確保された領域を超える読み取りを行った場合スローされます。
	 * @throws NullPointerException null値に結果を受け取ろうとした場合スローされます。
	 */
	public void loadArray(SPSerializable[] values) throws IOException {
		SPDataInputStream spIn = openDataInputStream();
		try {
			spIn.readSerializableArray(values);
		} finally {
			spIn.close();
		}
	}
	
	/**
	 * 確保されたスクラッチパッド領域を初期化します。
	 * このメソッドが呼び出されると確保されたスクラッチパッド領域が0x00で埋められます。
	 * また、初期化されたスクラッチパッド領域ではisSavedメソッドがfalseを返します。
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 */
	public void clear() throws IOException {
		SPDataOutputStream spOut = openDataOutputStream();
		try {
			byte[] zerofill = new byte[length];
			spOut.write(zerofill);
		} finally {
			spOut.close();
		}
	}
	
	/**
	 * シリアライズ可能なオブジェクトもしくはシリアライズ可能なオブジェクトの配列が保存されているかを返します。
	 * @return すでにシリアライズ可能なオブジェクトが保存されている場合true, 保存されていない場合はfalse
	 * @throws IOException 領域を超える読み取りを行った場合スローされます。
	 * @throws ConnectionException ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 */
	public boolean isSaved() throws IOException {
		SPDataInputStream spIn = openDataInputStream();
		try {
			return spIn.readInt() != 0;
		} finally {
			spIn.close();
		}
	}

	/**
	 * 確保された領域のスクラッチパッド番号を返します。
	 * @return スクラッチパッド番号
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * 確保された領域のアクセス開始オフセットを返します。
	 * @return アクセス開始オフセット
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * 確保された領域のアクセス開始位置からのバイト数を返します。
	 * @return　アクセス開始位置からのバイト数
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 確保されたスクラッチパッド領域に対する出力ストリームを返します。<br/>
	 * このメソッドにより取得したストリームはcloseする必要があります。
	 * @return 確保したスクラッチパッド領域に対する出力ストリーム
	 * @throws IOException 領域指定が正しくない場合にスローされます。
	 */
	public SPDataOutputStream openDataOutputStream() throws IOException {
		DataOutputStream out = Connector.openDataOutputStream("scratchpad:///" + index + ";pos=" + offset + ",length=" + length);
		System.out.println("openDataOutputStream: " + offset + "," + length);
		return new SPDataOutputStream(out);
	}

	/**
	 * 確保されたスクラッチパッド領域に対する入力ストリームを返します。<br/>
	 * このメソッドにより取得したストリームはcloseする必要があります。
	 * @return 確保したスクラッチパッド領域に対する入力ストリーム
	 * @throws IOException 領域指定が正しくない場合にスローされます。
	 */
	public SPDataInputStream openDataInputStream() throws IOException {
		DataInputStream in = Connector.openDataInputStream("scratchpad:///" + index + ";pos=" + offset + ",length=" + length);
		System.out.println("openDataInputStream: " + offset + "," + length);
		return new SPDataInputStream(in);
	}

}
