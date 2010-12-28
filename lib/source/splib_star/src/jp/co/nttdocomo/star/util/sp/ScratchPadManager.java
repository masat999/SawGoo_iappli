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
 * <p>�ȈՂɃX�N���b�`�p�b�h���g�p���邽�߂̕⏕�N���X�ł��B<br/>
 * �X�N���b�`�p�b�h�}�l�[�W���[���g�p����ƃX�N���b�`�p�b�h�̈���ӎ����邱�Ɩ���
 * �z��`���ɂ��ȈՂȃf�[�^�ۑ����\�ł��B</p>
 * 
 * <p>�X�N���b�`�p�b�h�}�l�[�W���[�̓X�N���b�`�p�b�h�ԍ�0�Ԃ�0�o�C�g����99999�o�C�g�܂ł̗̈���g�p���܂��B<br/>
 * �X�N���b�`�p�b�h�}�l�[�W���[���g�p����ۂ�ADF�ݒ�ɂ�<br/>
 * SPSize : 100000<br/>
 * ���w�肵�Ă��������B</p>
 * 
 * <p>�ۑ����0����99�܂ł�100���g�p���邱�Ƃ��ł��܂��B<br/>
 * ���ꂼ��̕ۑ���̍ő�T�C�Y��1000�o�C�g�ƂȂ�A
 * 1000�o�C�g�𒴂���f�[�^��ۑ������ꍇ��IOException���������܂��B</p>
 * 
 * <p>�X�N���b�`�p�b�h�ԍ��A�̈�T�C�Y�A�A�N�Z�X�J�n�ʒu�A�ۑ��搔�͕ύX�\�ł��B<br/>
 * �ύX�����ەK�v�ƂȂ�ADF�ɂ�����SPSize�ݒ�̒l��
 * (�̈�T�C�Y�~�ۑ��搔)�{�A�N�Z�X�J�n�ʒu�ɂ���Č��܂�܂��B</p>
 * 
 * @see jp.co.nttdocomo.star.util.sp.SPSerializableObject
 * @version splib-1.0
 */
public class ScratchPadManager {

	/** �f�t�H���g�X�N���b�`�p�b�h�ԍ� */
	private static int DEFAULT_SPINDEX = 0;
	/** �X�N���b�`�p�b�h�ԍ� */
	private static int spindex = DEFAULT_SPINDEX;
	/** �e�̈�̃f�t�H���g�̈�T�C�Y */
	private static int DEFAULT_LENGTH = 1000;
	/** �̈�T�C�Y */
	private static int length = DEFAULT_LENGTH;
	/** �ő�ۑ���ԍ� */
	private static int DEFAULT_MANAGER_INDEX_COUNT = 100;
	/** �A�N�Z�X�J�n�ʒu */
	private static int baseOffset = 0;
	/** �ۑ��搔 */
	private static int manager_index_count = DEFAULT_MANAGER_INDEX_COUNT;
	
	
	/**
	 * �f�t�H���g�ł�0�ƂȂ��Ă���X�N���b�`�p�b�h�ԍ���ύX���܂��B<br/>
	 * ���̃��\�b�h�͊����A�v�����g�p���Ă���X�N���b�`�p�b�h�̈�ƏՓ˂��Ă��܂��ꍇ��
	 * �z��`���ۑ��@�\���g�p����̈��ύX����ׂɎg�p�ł��܂��B
	 * ���łɏ������݂��s���Ă���ꍇ�͕ύX�s��Ȃ��ł��������B
	 * �܂��A���̒l��ύX����ꍇ��ADF��SPSize�ݒ��ύX����K�v������܂��B
	 * @param spindexArg�@�X�N���b�`�p�b�h�ԍ�
	 * @throws RuntimeException �ݒ�\�Ȕ͈͊O���w�肵���ꍇ�X���[����܂��B
	 */
	public static synchronized void setIndex(int spindexArg) {
		if (spindexArg < 0 || ScratchPad.MAX_SCRATCHPAD_COUNT <= spindexArg) {
			throw new RuntimeException("�X�N���b�`�p�b�h�ԍ���0����" + (ScratchPad.MAX_SCRATCHPAD_COUNT - 1) + "�܂ł̒l���w�肵�Ă��������B");
		}
		ScratchPadManager.spindex = spindexArg;
	}
	
	/**
	 * �f�t�H���g�ł�1000�ƂȂ��Ă���e�ۑ���̗̈�T�C�Y��ύX���܂��B<br/>
	 * ���̃��\�b�h�͊����A�v�����g�p���Ă���X�N���b�`�p�b�h�̈�ƏՓ˂��Ă��܂��ꍇ��
	 * �z��`���ۑ��@�\���g�p����̈��ύX����ׂɎg�p�ł��܂��B
	 * ���łɏ������݂��s���Ă���ꍇ�͕ύX�s��Ȃ��ł��������B
	 * �܂��A���̒l��ύX����ꍇ��ADF��SPSize�ݒ��ύX����K�v������܂��B
	 * @param length �e�ۑ���̗̈�T�C�Y
	 * @throws RuntimeException �ݒ�\�Ȕ͈͊O���w�肵���ꍇ�X���[����܂��B
	 */
	public static synchronized void setLength(int length) {
		if (length <= 0 || ScratchPad.MAX_SCRATCHPAD_SIZE < length) {
			throw new RuntimeException("�e�ۑ���T�C�Y��1����" + ScratchPad.MAX_SCRATCHPAD_SIZE + "�܂ł̒l���w�肵�Ă��������B");
		}
		ScratchPadManager.length = length;
	}
	
	/**
	 * �f�t�H���g�ł�0�ƂȂ��Ă���ۑ���̃A�N�Z�X�J�n�ʒu��ύX���܂��B<br/>
	 * ���̃��\�b�h�͊����A�v�����g�p���Ă���X�N���b�`�p�b�h�̈�ƏՓ˂��Ă��܂��ꍇ��
	 * �z��`���ۑ��@�\���g�p����̈��ύX����ׂɎg�p�ł��܂��B
	 * ���łɏ������݂��s���Ă���ꍇ�͕ύX�s��Ȃ��ł��������B
	 * �܂��A���̒l��ύX����ꍇ��ADF��SPSize�ݒ��ύX����K�v������܂��B
	 * @param baseOffset �A�N�Z�X�J�n�ʒu
	 * @throws RuntimeException �ݒ�\�Ȕ͈͊O���w�肵���ꍇ�X���[����܂��B
	 */
	public static synchronized void setOffset(int baseOffset) {
		if (baseOffset < 0 || ScratchPad.MAX_SCRATCHPAD_SIZE <= baseOffset) {
			throw new RuntimeException("�A�N�Z�X�J�n�ʒu��0����" + (ScratchPad.MAX_SCRATCHPAD_SIZE - 1) + "�܂ł̒l���w�肵�Ă��������B");
		}
		ScratchPadManager.baseOffset = baseOffset;
	}
	
	/**
	 * �f�t�H���g�ł�100�ƂȂ��Ă���ۑ��搔��ύX���܂��B
	 * ���̒l��ύX����ꍇ��ADF��SPSize�ݒ��ύX����K�v������܂��B
	 * @param manager_index_count�@�ۑ��搔
	 * @throws RuntimeException �ݒ�\�Ȕ͈͊O���w�肵���ꍇ�X���[����܂��B
	 */
	public static synchronized void setManagerIndexCount(int manager_index_count) {
		if (manager_index_count <= 0) {
			throw new RuntimeException("�ۑ��搔��1�ȏ�̒l���w�肵�Ă��������B");
		}
		ScratchPadManager.manager_index_count = manager_index_count;
	}
	
	/**
	 * �w�肵���ۑ���ɃI�u�W�F�N�g���������݂܂��B<br/>
	 * null�l��ۑ����邱�Ƃ͏o���܂���B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @param value �ۑ�����I�u�W�F�N�g
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
	 */
	public static synchronized void save(int managerIndex, Object value) throws IOException {
		if (managerIndex < 0 || manager_index_count <= managerIndex) {
			throw new InvalidKeyException("�X�N���b�`�p�b�h�}�l�[�W���[�ŕۑ��ł���ۑ���ԍ���" + (manager_index_count - 1) + "�܂łł��B");
		}
		if (value == null) {
			throw new NullPointerException("�X�N���b�`�p�b�h��null�l��ۑ����邱�Ƃ͏o���܂���B");
		}
		SPDataOutputStream out = getScratchPad(managerIndex).openDataOutputStream();
		try {
			out.writeObject(value);
		} finally {
			out.close();
		}
	}
	
	/**
	 * �w�肵���ۑ��悩��V���A���C�Y�\�ȃI�u�W�F�N�g��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @param value �ǂݎ�茋�ʂ��󂯎��C���X�^���X
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l�Ɍ��ʂ��󂯎�낤�Ƃ����ꍇ�X���[����܂��B
	 */
	public static synchronized void load(int managerIndex, SPSerializable value) throws IOException {
		getScratchPad(managerIndex).load(value);
	}
	
	/**
	 * �w�肵���ۑ���ɃV���A���C�Y�\�ȃI�u�W�F�N�g���ۑ�����Ă��邩��Ԃ��܂��B<br/>
	 * ���̃��\�b�h�Ńe�X�g�\�Ȃ��Ƃ�SPSerializable�I�u�W�F�N�g���ۑ�����Ă��邩�ǂ����݂̂ƂȂ�܂��B
	 * �v���~�e�B�u���b�p�[�^���ۑ�����Ă��邩�ǂ����̃`�F�b�N���s�����Ƃ͏o���܂���B
	 * @param managerIndex �ۑ���ԍ�
	 * @return ���łɃV���A���C�Y�\�ȃI�u�W�F�N�g���ۑ�����Ă���ꍇtrue, �ۑ�����Ă��Ȃ��ꍇ��false
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
	 */
	public static synchronized boolean isSaved(int managerIndex) throws IOException {
		return loadInt(managerIndex).intValue() != 0;
	}
	
	/**
	 * �w�肵���ۑ�������������܂��B
	 * ���̃��\�b�h���Ăяo�����ۑ���̗̈悪0x00�Ŗ��߂��܂��B
	 * �܂��A���������ꂽ�ۑ���ɂ���isSaved���\�b�h��false��Ԃ��܂��B
	 * @param managerIndex �ۑ���ԍ�
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
	 */
	public static void clear(int managerIndex) throws IOException {
		getScratchPad(managerIndex).clear();
	}
	
	/**
	 * �S�Ă̕ۑ�������������܂��B
	 * ScratchPadManager���g�p����S�Ă̗̈悪0x00�Ŗ��߂��܂��B
	 * �܂��A�S�Ă̕ۑ���ɂ���isSaved���\�b�h��false��Ԃ��܂��B
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
	 */
	public static void clearAll() throws IOException {
		System.out.println("Manager: clearAll " + spindex + "," + baseOffset + "," + (length * manager_index_count));
		new ScratchPad(spindex, baseOffset, length * manager_index_count).clear();
	}

	/**
	 * �w�肵���ۑ��悩�當�����ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �w�肵���ۑ��悩��Byte�^��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �w�肵���ۑ��悩��Short�^��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �w�肵���ۑ��悩��Integer�^��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �w�肵���ۑ��悩��Long�^��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �w�肵���ۑ��悩��Float�^��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �w�肵���ۑ��悩��Double�^��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �w�肵���ۑ��悩��Character�^��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �w�肵���ۑ��悩��Boolean�^��ǂݎ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return�@�ǂݍ��񂾕�����
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
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
	 * �ۑ���ԍ��ɑΉ�����I�t�Z�b�g��Ԃ��܂��B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @return �ۑ���ԍ��ɑΉ�����A�N�Z�X�J�n�I�t�Z�b�g
	 */
	public static int getOffset(int managerIndex) {
		return baseOffset + managerIndex * length;
	}
	
	/**
	 * �X�N���b�`�p�b�h�ԍ���Ԃ��܂��B
	 * @return �X�N���b�`�p�b�h�ԍ�
	 */
	public static int getIndex() {
		return spindex;
	}
	
	/**
	 * �e�ۑ���̗̈�T�C�Y��Ԃ��܂��B
	 * @return �e�ۑ���̗̈�T�C�Y�o�C�g��
	 */
	public static int getLength() {
		return length;
	}

	/**
	 * �ۑ��搔��Ԃ��܂��B
	 * @return �ۑ��搔
	 */
	public static int getManagerIndexCount() {
		return manager_index_count;
	}
	
	/**
	 * �ۑ���ԍ��ɑΉ�����X�N���b�`�p�b�h�̈��Ԃ��܂��B
	 * @param managerIndex �ۑ���ԍ�
	 * @return �ۑ���ԍ��ɑΉ�����X�N���b�`�p�b�h�̈�
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂�
	 */
	public static ScratchPad getScratchPad(int managerIndex) {
		if (managerIndex < 0 || manager_index_count <= managerIndex) {
			throw new InvalidKeyException("�X�N���b�`�p�b�h�}�l�[�W���[�ŕۑ��ł���ۑ���ԍ���" + (manager_index_count - 1) + "�܂łł��B");
		}
		System.out.println("Manager: getScratchPad(" + managerIndex + "), " + spindex + "," + getOffset(managerIndex) + "," + length);
		return new ScratchPad(spindex, getOffset(managerIndex), length);
	}
}
