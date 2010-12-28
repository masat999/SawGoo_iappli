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
 * <p>CLDC��DataOutputStream�����b�v����N���X�ł��B<br/>
 * ���̃N���X�ł̓f�[�^�ۑ����Ɏ����I�Ƀf�[�^�����������ނ��Ƃ�
 * �ϒ��f�[�^�̕ۑ����Ƀf�[�^�����ӎ�����K�v���Ȃ��d�g�݂�񋟂��܂��B
 * �X�N���b�`�p�b�h���C�u�����Ŏg�p����o�̓X�g���[���͑S��SPDataOutputStream�ƂȂ�܂��B</p>
 * 
 * <p>DataOutputStream�̃��\�b�h�̂����A�ȉ��̃��\�b�h�̓f�[�^�����ӎ������Ɏg�p���邱�Ƃ��\�ł��邽�ߎg�p����������܂��B
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
	 * �o�̓X�g���[�������b�v����R���X�g���N�^
	 * @param out �o�̓X�g���[��
	 */
	public SPDataOutputStream(OutputStream out) {
		super(out);
	}
	
	/**
	 * �o�C�g����������݂܂��B<br/>
	 * �擪��4�o�C�g�Ńo�C�g�����������݂܂��B
	 * ����ɂ��ǂݍ��ݎ��̓f�[�^�����ӎ������ɓǂݎ����s�����Ƃ��\�ł��B
	 * 
	 * @param data �������ރo�C�g��
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
	 */
	public void writeBytes(byte[] data) throws IOException {
		if (data == null) {
			throw new NullPointerException("�X�N���b�`�p�b�h��null�l��ۑ����邱�Ƃ͏o���܂���B");
		}

		writeBytes(data, data.length);
	}
	
	/**
	 * �o�C�g��̂����w�肳�ꂽ�o�C�g�����������݂܂��B<br/>
	 * �擪��4�o�C�g�Ńo�C�g�����������݂܂��B
	 * ����ɂ��ǂݍ��ݎ��̓f�[�^�����ӎ������ɓǂݎ����s�����Ƃ��\�ł��B
	 * 
	 * @param data �������ރo�C�g��
	 * @param length �������ރo�C�g��
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
	 */
	public void writeBytes(byte[] data, int length) throws IOException {
		if (data == null) {
			throw new NullPointerException("�X�N���b�`�p�b�h��null�l��ۑ����邱�Ƃ͏o���܂���B");
		}
		
		super.writeInt(length);
		super.write(data);
	}
	
	/**
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g����ۑ����܂��B<br/>
	 * �ۑ������f�[�^�\����SPSerialize�C���^�t�F�[�X��toScratchPadData���\�b�h�̎����ɂ���`����܂��B
	 * �܂��A�f�[�^�̈�Ԑ擪�ɂ̓f�[�^��ۑ�����ׂɕK�v�ȗ̈�T�C�Y���ۑ�����܂��B
	 * 
	 * @param value �ۑ�����V���A���C�Y�\�ȃI�u�W�F�N�g
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
	 */
	public void writeSerializable(SPSerializable value) throws IOException {
		if (value == null) {
			throw new NullPointerException("�X�N���b�`�p�b�h��null�l��ۑ����邱�Ƃ͏o���܂���B");
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		SPDataOutputStream bufferOut = new SPDataOutputStream(buffer);
		try {
			// �o�b�t�@�ɑ΂��ăI�u�W�F�N�g�̓��e�������o���܂��B
			// �����o�����e��SPSerialize�C���^�t�F�[�X�̎����ɂ���`����܂��B
			value.toScratchPadData(bufferOut);
			bufferOut.flush();
			
			// �f�[�^����ۑ����܂��B
			// �f�[�^����SPSerialize�C���^�t�F�[�X�̎����ɂ���`����܂��B
			writeInt(buffer.size());
			System.out.println("writeSPSerializable: writeInt, " + buffer.size());
			
			// �o�b�t�@���e�������o���܂��B
			write(buffer.toByteArray());
			System.out.println("writeSPSerializable: write, " + buffer.toByteArray().length);
		} finally {
			bufferOut.close();
		}
	}
	
	
	/**
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g�̔z���ۑ����܂��B<br/>
	 * �ۑ������f�[�^�\����SPSerialize�C���^�t�F�[�X��toScratchPadData���\�b�h�̎����ɂ���`����܂��B
	 * �܂��A�f�[�^�̐擪�ɂ͔z��̗v�f���A
	 * �e�I�u�W�F�N�g�̐擪�ɂ̓I�u�W�F�N�g��ۑ�����ׂɕK�v�ȗ̈�T�C�Y���ۑ�����܂��B
	 * 
	 * @param values �ۑ�����V���A���C�Y�\�ȃI�u�W�F�N�g�̔z��
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
	 */
	public void writeSerializableArray(SPSerializable[] values) throws IOException {
		if (values == null) {
			throw new NullPointerException("�X�N���b�`�p�b�h��null�l��ۑ����邱�Ƃ͏o���܂���B");
		}

		// �z����̃I�u�W�F�N�g����ۑ�
		writeInt(values.length);
		System.out.println("save[]: writeInt, " + values.length);
		
		for (int i=0; i<values.length; i++) {
			if (values[i] == null) {
				throw new NullPointerException("�X�N���b�`�p�b�h��null�l��ۑ����邱�Ƃ͏o���܂���B");
			}
			writeSerializable(values[i]);
		}
	}
	
	/**
	 * �I�u�W�F�N�g�̌^���������ʂ��ĕۑ����܂��B
	 * �ۑ��\�ȃI�u�W�F�N�g�̓v���~�e�B�u���b�p�[�N���X��SPSerializable�C���^�t�F�[�X�����������N���X�ƂȂ�܂��B
	 * 
	 * @param value �ۑ�����I�u�W�F�N�g
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
	 */
	public void writeObject(Object value) throws IOException {
		if (value == null) {
			throw new NullPointerException("�X�N���b�`�p�b�h��null�l��ۑ����邱�Ƃ͏o���܂���B");
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
			throw new InvalidTypeException("�X�N���b�`�p�b�h�ɕۑ��ł��Ȃ��^���w�肳��܂����B");
		}
	}
	
	/**
	 * writeChars���g�p���ĕ������ۑ����܂��B
	 * ���̃��\�b�h�ł�writeChar�̎d�l�Ɋ�Â��Ċe������2�o�C�g�Ƃ��ĕۑ�����܂��B
	 * �܂��A�f�[�^�̐擪�ɂ�4�o�C�g�ŕ������ۑ�����̂ɕK�v�ȃo�C�g�����������܂�܂��B
	 * @param value �ۑ����镶����
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l��ۑ����悤�Ƃ����ꍇ�X���[����܂��B
	 */
	public void writeString(String value) throws IOException {
		if (value == null) {
			throw new NullPointerException("�X�N���b�`�p�b�h��null�l��ۑ����邱�Ƃ͏o���܂���B");
		}

		// �o�C�g�����v�Z���ď������݂܂��B
		int length = value.length() * 2;	// writeChars���g�p�����Character�͏��2�o�C�g�ŕۑ�����܂�
		writeInt(length);
		
		// �f�[�^��ۑ����܂��B
		writeChars(value);
	}
}
