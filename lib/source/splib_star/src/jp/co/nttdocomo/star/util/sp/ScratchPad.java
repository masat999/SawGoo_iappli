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
 * �X�N���b�`�p�b�h�̗̈���m�ۂ��R�l�N�V�����̊Ǘ��Ɗ�{�̓ǂݎ��E�������݂��s���N���X�ł��B<BR/>
 * 
 * @see jp.co.nttdocomo.star.util.sp.SPSerializableObject
 * @see jp.co.nttdocomo.star.util.sp.SPDataInputStream
 * @see jp.co.nttdocomo.star.util.sp.SPDataOutputStream
 * @version splib-1.0
 */
public class ScratchPad {

	/** �X�N���b�`�p�b�h�ԍ� */
	private int index = 0;
	/** �I�t�Z�b�g */
	private int offset = 0;
	/** �̈�T�C�Y */
	private int length = 0;
	/** �X�N���b�`�p�b�h�̍ő�T�C�Y */
	public static int MAX_SCRATCHPAD_SIZE = 2097152;	// 2048KB
	/** �X�N���b�`�p�b�h�ԍ��̍ő�� */
	public static int MAX_SCRATCHPAD_COUNT = 16;		// �ő�16��
	
	/**
	 * �X�N���b�`�p�b�h�̗̈���w�肷��R���X�g���N�^�ł��B<br/>
	 * i�A�v���ł̓X�N���b�`�p�b�h���ő�16�܂ŕ����Ǘ����邱�Ƃ��o���邽��
	 * �X�N���b�`�p�b�h�ԍ��Ƃ��Ďw��ł���l��0����15�܂łƂȂ�܂��B
	 * @param index		�X�N���b�`�p�b�h�ԍ�(0����15�܂�)
	 * @param offset	�w�肳�ꂽ�ԍ��̃X�N���b�`�p�b�h�ɂ�����A�N�Z�X�J�n�I�t�Z�b�g
	 * @param length	�A�N�Z�X�J�n�I�t�Z�b�g�ʒu����̃o�C�g��
	 */
	public ScratchPad(int index, int offset, int length) {
		if (index < 0 || ScratchPad.MAX_SCRATCHPAD_COUNT <= index) {
			throw new RuntimeException("�X�N���b�`�p�b�h�ԍ���0����" + (MAX_SCRATCHPAD_COUNT - 1) + "�܂ł̒l���w�肵�Ă��������B");
		}
		if (length <= 0 || ScratchPad.MAX_SCRATCHPAD_SIZE < length) {
			throw new RuntimeException("�̈�T�C�Y��1����" + MAX_SCRATCHPAD_SIZE + "�܂ł̒l���w�肵�Ă��������B");
		}
		if (offset < 0 || ScratchPad.MAX_SCRATCHPAD_SIZE <= offset) {
			throw new RuntimeException("�A�N�Z�X�J�n�ʒu��0����" + (ScratchPad.MAX_SCRATCHPAD_SIZE - 1) + "�܂ł̒l���w�肵�Ă��������B");
		}
		this.index = index;
		this.offset = offset;
		this.length = length;
	}
	
	/**
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g���w�肳�ꂽ�̈�ɕۑ����܂��B<br/>
	 * save���Ɠ����N���X�������Ƃ���load���\�b�h���Ăяo�����Ƃ�
	 * �X�N���b�`�p�b�h����f�[�^�𕜋A���邱�Ƃ��o���܂��B
	 * �Ⴄ�N���X���w�肵��load���\�b�h���Ăяo�����ꍇ�̓���͕s��ƂȂ�܂��B<br/>
	 * 
	 * @param value �ۑ�����I�u�W�F�N�g�B
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �m�ۂ��ꂽ�̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
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
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g�̔z����w�肳�ꂽ�̈�ɕۑ����܂��B<br/>
	 * saveArray���Ɠ����N���X�������Ƃ���loadArray���\�b�h���Ăяo�����Ƃ�
	 * �X�N���b�`�p�b�h����f�[�^�𕜋A���邱�Ƃ��o���܂��B
	 * �Ⴄ�N���X���w�肵��loadArray���\�b�h���Ăяo�����ꍇ�̓���͕s��ƂȂ�܂��B<br/>
	 * 
	 * @param values �ۑ�����I�u�W�F�N�g�̔z��B
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �m�ۂ��ꂽ�̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
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
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g���w�肳�ꂽ�̈悩��ǂݎ��܂��B<br/>
	 * save���Ɠ����N���X�������Ƃ���load���\�b�h���Ăяo�����Ƃ�
	 * �X�N���b�`�p�b�h����f�[�^�𕜋A���邱�Ƃ��o���܂��B
	 * �Ⴄ�N���X���w�肵��load���\�b�h���Ăяo�����ꍇ�̓���͕s��ƂȂ�܂��B<br/>
	 * �����ɂ͓ǂݍ��ރN���X�̃C���X�^���X�𐶐����ēn���K�v������܂��B
	 * 
	 * @param value�@�ǂݎ�茋�ʂ��󂯎��C���X�^���X
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �m�ۂ��ꂽ�̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l�Ɍ��ʂ��󂯎�낤�Ƃ����ꍇ�X���[����܂��B
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
	 * �ۑ�����Ă���I�u�W�F�N�g�z��̗v�f����Ԃ��܂��B<br/>
	 * loadArray���\�b�h�Ŕz���ǂݍ��ޏꍇ�A
	 * �ۑ�����Ă���I�u�W�F�N�g�̐������C���X�^���X��ێ������z���n���K�v�����邽��
	 * ���O�ɂ��̃��\�b�h���g�p���ėv�f�����擾���܂��B
	 * @return �ۑ�����Ă���I�u�W�F�N�g�z��̗v�f��
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �m�ۂ��ꂽ�̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
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
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g�̔z����w�肳�ꂽ�̈悩��ǂݎ��܂��B<br/>
	 * saveArray���Ɠ����N���X�������Ƃ���loadArray���\�b�h���Ăяo�����Ƃ�
	 * �X�N���b�`�p�b�h����f�[�^�𕜋A���邱�Ƃ��o���܂��B
	 * �Ⴄ�N���X���w�肵��loadArray���\�b�h���Ăяo�����ꍇ�̓���͕s��ƂȂ�܂��B<br/>
	 * �����ɂ͓ǂݍ��ރN���X�̃C���X�^���X��v�f�����������ēn���K�v������܂��B
	 * 
	 * @param values �ǂݎ�茋�ʂ��󂯎��C���X�^���X�̔z�� 
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �m�ۂ��ꂽ�̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l�Ɍ��ʂ��󂯎�낤�Ƃ����ꍇ�X���[����܂��B
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
	 * �m�ۂ��ꂽ�X�N���b�`�p�b�h�̈�����������܂��B
	 * ���̃��\�b�h���Ăяo�����Ɗm�ۂ��ꂽ�X�N���b�`�p�b�h�̈悪0x00�Ŗ��߂��܂��B
	 * �܂��A���������ꂽ�X�N���b�`�p�b�h�̈�ł�isSaved���\�b�h��false��Ԃ��܂��B
	 * @throws IOException�@ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
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
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g�������̓V���A���C�Y�\�ȃI�u�W�F�N�g�̔z�񂪕ۑ�����Ă��邩��Ԃ��܂��B
	 * @return ���łɃV���A���C�Y�\�ȃI�u�W�F�N�g���ۑ�����Ă���ꍇtrue, �ۑ�����Ă��Ȃ��ꍇ��false
	 * @throws IOException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws ConnectionException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
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
	 * �m�ۂ��ꂽ�̈�̃X�N���b�`�p�b�h�ԍ���Ԃ��܂��B
	 * @return �X�N���b�`�p�b�h�ԍ�
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * �m�ۂ��ꂽ�̈�̃A�N�Z�X�J�n�I�t�Z�b�g��Ԃ��܂��B
	 * @return �A�N�Z�X�J�n�I�t�Z�b�g
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * �m�ۂ��ꂽ�̈�̃A�N�Z�X�J�n�ʒu����̃o�C�g����Ԃ��܂��B
	 * @return�@�A�N�Z�X�J�n�ʒu����̃o�C�g��
	 */
	public int getLength() {
		return length;
	}

	/**
	 * �m�ۂ��ꂽ�X�N���b�`�p�b�h�̈�ɑ΂���o�̓X�g���[����Ԃ��܂��B<br/>
	 * ���̃��\�b�h�ɂ��擾�����X�g���[����close����K�v������܂��B
	 * @return �m�ۂ����X�N���b�`�p�b�h�̈�ɑ΂���o�̓X�g���[��
	 * @throws IOException �̈�w�肪�������Ȃ��ꍇ�ɃX���[����܂��B
	 */
	public SPDataOutputStream openDataOutputStream() throws IOException {
		DataOutputStream out = Connector.openDataOutputStream("scratchpad:///" + index + ";pos=" + offset + ",length=" + length);
		System.out.println("openDataOutputStream: " + offset + "," + length);
		return new SPDataOutputStream(out);
	}

	/**
	 * �m�ۂ��ꂽ�X�N���b�`�p�b�h�̈�ɑ΂�����̓X�g���[����Ԃ��܂��B<br/>
	 * ���̃��\�b�h�ɂ��擾�����X�g���[����close����K�v������܂��B
	 * @return �m�ۂ����X�N���b�`�p�b�h�̈�ɑ΂�����̓X�g���[��
	 * @throws IOException �̈�w�肪�������Ȃ��ꍇ�ɃX���[����܂��B
	 */
	public SPDataInputStream openDataInputStream() throws IOException {
		DataInputStream in = Connector.openDataInputStream("scratchpad:///" + index + ";pos=" + offset + ",length=" + length);
		System.out.println("openDataInputStream: " + offset + "," + length);
		return new SPDataInputStream(in);
	}

}
