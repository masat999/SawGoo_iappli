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
 * <p>簡易にスクラッチパッドを使用するための補助クラスです。<br/>
 * スクラッチパッドマネージャーを使用するとスクラッチパッド領域を意識すること無く
 * 配列形式による簡易なデータ保存が可能です。</p>
 * 
 * <p>スクラッチパッドマネージャーはスクラッチパッド番号0番の0バイトから99999バイトまでの領域を使用します。<br/>
 * スクラッチパッドマネージャーを使用する際はADF設定にて<br/>
 * SPSize : 100000<br/>
 * を指定してください。</p>
 * 
 * <p>保存先は0から99までの100個を使用することができます。<br/>
 * それぞれの保存先の最大サイズは1000バイトとなり、
 * 1000バイトを超えるデータを保存した場合はIOExceptionが発生します。</p>
 * 
 * <p>スクラッチパッド番号、領域サイズ、アクセス開始位置、保存先数は変更可能です。<br/>
 * 変更した際必要となるADFにおけるSPSize設定の値は
 * (領域サイズ×保存先数)＋アクセス開始位置によって決まります。</p>
 * 
 * @see jp.co.nttdocomo.star.util.sp.SPSerializableObject
 * @version splib-1.0
 */
public class ScratchPadManager {

	/** デフォルトスクラッチパッド番号 */
	private static int DEFAULT_SPINDEX = 0;
	/** スクラッチパッド番号 */
	private static int spindex = DEFAULT_SPINDEX;
	/** 各領域のデフォルト領域サイズ */
	private static int DEFAULT_LENGTH = 1000;
	/** 領域サイズ */
	private static int length = DEFAULT_LENGTH;
	/** 最大保存先番号 */
	private static int DEFAULT_MANAGER_INDEX_COUNT = 100;
	/** アクセス開始位置 */
	private static int baseOffset = 0;
	/** 保存先数 */
	private static int manager_index_count = DEFAULT_MANAGER_INDEX_COUNT;
	
	
	/**
	 * デフォルトでは0となっているスクラッチパッド番号を変更します。<br/>
	 * このメソッドは既存アプリが使用しているスクラッチパッド領域と衝突してしまう場合に
	 * 配列形式保存機能が使用する領域を変更する為に使用できます。
	 * すでに書き込みを行っている場合は変更行わないでください。
	 * また、この値を変更する場合はADFのSPSize設定を変更する必要があります。
	 * @param spindexArg　スクラッチパッド番号
	 * @throws RuntimeException 設定可能な範囲外を指定した場合スローされます。
	 */
	public static synchronized void setIndex(int spindexArg) {
		if (spindexArg < 0 || ScratchPad.MAX_SCRATCHPAD_COUNT <= spindexArg) {
			throw new RuntimeException("スクラッチパッド番号は0から" + (ScratchPad.MAX_SCRATCHPAD_COUNT - 1) + "までの値を指定してください。");
		}
		ScratchPadManager.spindex = spindexArg;
	}
	
	/**
	 * デフォルトでは1000となっている各保存先の領域サイズを変更します。<br/>
	 * このメソッドは既存アプリが使用しているスクラッチパッド領域と衝突してしまう場合に
	 * 配列形式保存機能が使用する領域を変更する為に使用できます。
	 * すでに書き込みを行っている場合は変更行わないでください。
	 * また、この値を変更する場合はADFのSPSize設定を変更する必要があります。
	 * @param length 各保存先の領域サイズ
	 * @throws RuntimeException 設定可能な範囲外を指定した場合スローされます。
	 */
	public static synchronized void setLength(int length) {
		if (length <= 0 || ScratchPad.MAX_SCRATCHPAD_SIZE < length) {
			throw new RuntimeException("各保存先サイズは1から" + ScratchPad.MAX_SCRATCHPAD_SIZE + "までの値を指定してください。");
		}
		ScratchPadManager.length = length;
	}
	
	/**
	 * デフォルトでは0となっている保存先のアクセス開始位置を変更します。<br/>
	 * このメソッドは既存アプリが使用しているスクラッチパッド領域と衝突してしまう場合に
	 * 配列形式保存機能が使用する領域を変更する為に使用できます。
	 * すでに書き込みを行っている場合は変更行わないでください。
	 * また、この値を変更する場合はADFのSPSize設定を変更する必要があります。
	 * @param baseOffset アクセス開始位置
	 * @throws RuntimeException 設定可能な範囲外を指定した場合スローされます。
	 */
	public static synchronized void setOffset(int baseOffset) {
		if (baseOffset < 0 || ScratchPad.MAX_SCRATCHPAD_SIZE <= baseOffset) {
			throw new RuntimeException("アクセス開始位置は0から" + (ScratchPad.MAX_SCRATCHPAD_SIZE - 1) + "までの値を指定してください。");
		}
		ScratchPadManager.baseOffset = baseOffset;
	}
	
	/**
	 * デフォルトでは100となっている保存先数を変更します。
	 * この値を変更する場合はADFのSPSize設定を変更する必要があります。
	 * @param manager_index_count　保存先数
	 * @throws RuntimeException 設定可能な範囲外を指定した場合スローされます。
	 */
	public static synchronized void setManagerIndexCount(int manager_index_count) {
		if (manager_index_count <= 0) {
			throw new RuntimeException("保存先数は1以上の値を指定してください。");
		}
		ScratchPadManager.manager_index_count = manager_index_count;
	}
	
	/**
	 * 指定した保存先にオブジェクトを書き込みます。<br/>
	 * null値を保存することは出来ません。
	 * @param managerIndex　保存先番号
	 * @param value 保存するオブジェクト
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 * @throws NullPointerException null値を保存しようとした場合スローされます。
	 */
	public static synchronized void save(int managerIndex, Object value) throws IOException {
		if (managerIndex < 0 || manager_index_count <= managerIndex) {
			throw new InvalidKeyException("スクラッチパッドマネージャーで保存できる保存先番号は" + (manager_index_count - 1) + "までです。");
		}
		if (value == null) {
			throw new NullPointerException("スクラッチパッドにnull値を保存することは出来ません。");
		}
		SPDataOutputStream out = getScratchPad(managerIndex).openDataOutputStream();
		try {
			out.writeObject(value);
		} finally {
			out.close();
		}
	}
	
	/**
	 * 指定した保存先からシリアライズ可能なオブジェクトを読み取ります。
	 * @param managerIndex　保存先番号
	 * @param value 読み取り結果を受け取るインスタンス
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 * @throws NullPointerException null値に結果を受け取ろうとした場合スローされます。
	 */
	public static synchronized void load(int managerIndex, SPSerializable value) throws IOException {
		getScratchPad(managerIndex).load(value);
	}
	
	/**
	 * 指定した保存先にシリアライズ可能なオブジェクトが保存されているかを返します。<br/>
	 * このメソッドでテスト可能なことはSPSerializableオブジェクトが保存されているかどうかのみとなります。
	 * プリミティブラッパー型が保存されているかどうかのチェックを行うことは出来ません。
	 * @param managerIndex 保存先番号
	 * @return すでにシリアライズ可能なオブジェクトが保存されている場合true, 保存されていない場合はfalse
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized boolean isSaved(int managerIndex) throws IOException {
		return loadInt(managerIndex).intValue() != 0;
	}
	
	/**
	 * 指定した保存先を初期化します。
	 * このメソッドが呼び出される保存先の領域が0x00で埋められます。
	 * また、初期化された保存先についてisSavedメソッドがfalseを返します。
	 * @param managerIndex 保存先番号
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static void clear(int managerIndex) throws IOException {
		getScratchPad(managerIndex).clear();
	}
	
	/**
	 * 全ての保存先を初期化します。
	 * ScratchPadManagerが使用する全ての領域が0x00で埋められます。
	 * また、全ての保存先についてisSavedメソッドがfalseを返します。
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える書き込みを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static void clearAll() throws IOException {
		System.out.println("Manager: clearAll " + spindex + "," + baseOffset + "," + (length * manager_index_count));
		new ScratchPad(spindex, baseOffset, length * manager_index_count).clear();
	}

	/**
	 * 指定した保存先から文字列を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized String loadString(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return in.readString();
		} finally {
			in.close();
		}
	}

	/**
	 * 指定した保存先からByte型を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized Byte loadByte(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return new Byte(in.readByte());
		} finally {
			in.close();
		}
	}

	/**
	 * 指定した保存先からShort型を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized Short loadShort(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return new Short(in.readShort());
		} finally {
			in.close();
		}
	}

	/**
	 * 指定した保存先からInteger型を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized Integer loadInt(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return new Integer(in.readInt());
		} finally {
			in.close();
		}
	}

	/**
	 * 指定した保存先からLong型を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized Long loadLong(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return new Long(in.readLong());
		} finally {
			in.close();
		}
	}

	/**
	 * 指定した保存先からFloat型を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized Float loadFloat(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return new Float(in.readFloat());
		} finally {
			in.close();
		}
	}

	/**
	 * 指定した保存先からDouble型を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized Double loadDouble(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return new Double(in.readDouble());
		} finally {
			in.close();
		}
	}

	/**
	 * 指定した保存先からCharacter型を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized Character loadChar(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return new Character(in.readChar());
		} finally {
			in.close();
		}
	}

	/**
	 * 指定した保存先からBoolean型を読み取ります。
	 * @param managerIndex　保存先番号
	 * @return　読み込んだ文字列
	 * @throws IOException　ADF設定によりスクラッチパッド領域が正しく確保されていない場合にスローされます。
	 * @throws ConnectionException 領域を超える読み取りを行った場合スローされます。
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます。
	 */
	public static synchronized Boolean loadBoolean(int managerIndex) throws IOException {
		SPDataInputStream in = getScratchPad(managerIndex).openDataInputStream();
		try {
			return new Boolean(in.readBoolean());
		} finally {
			in.close();
		}
	}
	
	/**
	 * 保存先番号に対応するオフセットを返します。
	 * @param managerIndex　保存先番号
	 * @return 保存先番号に対応するアクセス開始オフセット
	 */
	public static int getOffset(int managerIndex) {
		return baseOffset + managerIndex * length;
	}
	
	/**
	 * スクラッチパッド番号を返します。
	 * @return スクラッチパッド番号
	 */
	public static int getIndex() {
		return spindex;
	}
	
	/**
	 * 各保存先の領域サイズを返します。
	 * @return 各保存先の領域サイズバイト数
	 */
	public static int getLength() {
		return length;
	}

	/**
	 * 保存先数を返します。
	 * @return 保存先数
	 */
	public static int getManagerIndexCount() {
		return manager_index_count;
	}
	
	/**
	 * 保存先番号に対応するスクラッチパッド領域を返します。
	 * @param managerIndex 保存先番号
	 * @return 保存先番号に対応するスクラッチパッド領域
	 * @throws InvalidKeyException 正しくない保存先番号が指定された場合スローされます
	 */
	public static ScratchPad getScratchPad(int managerIndex) {
		if (managerIndex < 0 || manager_index_count <= managerIndex) {
			throw new InvalidKeyException("スクラッチパッドマネージャーで保存できる保存先番号は" + (manager_index_count - 1) + "までです。");
		}
		System.out.println("Manager: getScratchPad(" + managerIndex + "), " + spindex + "," + getOffset(managerIndex) + "," + length);
		return new ScratchPad(spindex, getOffset(managerIndex), length);
	}
}
