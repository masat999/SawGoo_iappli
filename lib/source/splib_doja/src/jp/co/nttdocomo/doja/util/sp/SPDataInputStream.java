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
 * <p>CLDC��DataInputStream�����b�v����N���X�ł��B<br/>
 * ���̃N���X�ł̓f�[�^�ۑ����ɏ������܂ꂽ�f�[�^���������I�ɓǂݍ����
 * �K�؂Ȓ����Ńf�[�^��ǂݍ��ގd�g�݂�񋟂��܂��B
 * �X�N���b�`�p�b�h���C�u�����Ŏg�p������̓X�g���[���͑S��SPDataInputStream�ƂȂ�܂��B</p>
 * 
 * <p>DataInputStream�̃��\�b�h�̂����A�ȉ��̃��\�b�h�̓f�[�^�����ӎ������Ɏg�p���邱�Ƃ��\�ł��邽�ߎg�p����������܂��B
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
	 * ���̓X�g���[�������b�v����R���X�g���N�^
	 * @param in ���̓X�g���[��
	 */
	public SPDataInputStream(InputStream in) {
		super(in);
	}
	
	/**
	 * �o�C�g���ǂݍ��݂܂��B<br/>
	 * �ۑ����ɂ̓o�C�g�����擪��4�o�C�g�ŏ������܂�邽��
	 * �o�C�g����ǂݍ��񂾏�ł���Ƀo�C�g�����̃f�[�^��ǂݍ��݂܂��B
	 * 
	 * @return �ǂݎ�肳�ꂽ�o�C�g��
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 */
	public byte[] readBytes() throws IOException {
		int length = super.readInt();
		byte[] result = new byte[length];
		super.read(result);
		return result;
	}

	/**
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g����ǂݍ��݂܂��B<br/>
	 * �I�u�W�F�N�g�̕������@��SPSerializable��fromScratchPadData���\�b�h�̎����ɂ���`����܂��B
	 * �܂��A�f�[�^�̐擪�ɂ̓I�u�W�F�N�g��ۑ�����ׂɎg�p����Ă���̈�T�C�Y���ۑ�����Ă��܂��B
	 * 
	 * @param value �ǂݎ�茋�ʂ��󂯎��C���X�^���X
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l�Ɍ��ʂ��󂯎�낤�Ƃ����ꍇ�X���[����܂��B
	 */
	public void readSerializable(SPSerializable value) throws IOException {
		if (value == null) {
			throw new NullPointerException("null�l�ɃX�N���b�`�p�b�h�̓��e�𕜌����邱�Ƃ͂ł��܂���B");
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
	 * �V���A���C�Y�\�ȃI�u�W�F�N�g�̔z���ǂݍ��݂܂��B<br/>
	 * �I�u�W�F�N�g�̕������@��SPSerializable��fromScratchPadData���\�b�h�̎����ɂ���`����܂��B
	 * �܂��A�f�[�^�̐擪�ɂ͔z��̗v�f�����ۑ�����A�e�I�u�W�F�N�g�̐擪�ɂ̓I�u�W�F�N�g��ۑ�����ׂɎg�p����Ă���̈�T�C�Y���ۑ�����Ă��܂��B
	 * 
	 * @param values �ǂݎ�茋�ʂ��󂯎��C���X�^���X�̔z��
	 * @throws IOException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws ConnectionException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws NullPointerException null�l�Ɍ��ʂ��󂯎�낤�Ƃ����ꍇ�X���[����܂��B
	 */
	public void readSerializableArray(SPSerializable[] values) throws IOException {
		if (values == null) {
			throw new NullPointerException("null�l�ɃX�N���b�`�p�b�h�̓��e�𕜌����邱�Ƃ͂ł��܂���B");
		}

		int dataCount = readInt();
		if (dataCount > values.length) {
			throw new IOException("�ۑ�����Ă���I�u�W�F�N�g�̐�" + dataCount + 
					"�ɑ΂��ēǂݍ��ݐ�̃C���X�^���X��" + values.length +
					"������Ȃ����ߐ������ǂݍ��߂܂���B");
		}
		for (int i=0; i<values.length; i++) {
			if (values[i] == null) {
				throw new NullPointerException("null�l�ɃX�N���b�`�p�b�h�̓��e�𕜌����邱�Ƃ͂ł��܂���B");
			}
			readSerializable(values[i]);
		}
	}
	
	/**
	 * �������ǂݍ��݂܂��B
	 * �f�[�^�̐擪�ɂ�4�o�C�g�ŕ������ۑ�����ׂɎg�p����Ă���o�C�g�����ۑ�����Ă��܂��B
	 * @return �ǂݍ��܂ꂽ������
	 */
	public String readString() throws IOException {
		// �o�C�g����ǂݍ��݂܂��B
		int length = readInt();
		int pos = 0;
		
		// readChars�Ɋ�Â���
		StringBuffer buffer = new StringBuffer();
		while (pos < length) {
			buffer.append(readChar());
			pos += 2;
		}
		
		return buffer.toString();
	}
}
