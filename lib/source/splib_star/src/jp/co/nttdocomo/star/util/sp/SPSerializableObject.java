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
 * <p>�I�u�W�F�N�g�̓ǂݎ��E�������ݕ��@���`���邽�߂̒��ۃN���X�ł��B<br/>
 * ���̃C���^�t�F�[�X���p�������N���X��
 * <ol>
 * <li>ScratchPad.save</li>
 * <li>ScratchPadManager.save</li>
 * <li>ScratchPad.load</li>
 * <li>ScratchPadManager.load</li>
 * </ol>
 * �𗘗p���ĊȈՂɓǂݎ��E�������݂��s����悤�ɂȂ�܂��B<br/>
 * �܂��A�p�������N���X���g��save���\�b�h�Eload���\�b�h���񋟂���܂��B</p>
 * 
 * <p>���̒��ۃN���X���p������Ɠ�d�p���ɂȂ��Ă��܂��ꍇ��
 * SPSerializable�C���^�t�F�[�X���������邱�Ƃœǂݎ��E�������ݕ��@���`���邱�Ƃ��\�ł��B</p>
 * 
 * @see jp.co.nttdocomo.star.util.sp.ScratchPad
 * @see jp.co.nttdocomo.star.util.sp.ScratchPadManager
 * @see jp.co.nttdocomo.star.util.sp.SPSerializable
 * 
 * @version splib-1.0
 */
public abstract class SPSerializableObject implements SPSerializable {

	/**
	 * �X�N���b�`�p�b�h�ւ̃f�[�^�ۑ��菇���`���郁�\�b�h�ł��B<br/>
	 * SPDataOutputStream�ɑ΂���write�n���\�b�h���Ăяo�����Ƃ�
	 * �I�u�W�F�N�g�����v���p�e�B�������ۑ����Ă������Ƃ��o���܂��B
	 * 
	 * @param out �X�N���b�`�p�b�h�̏o�̓X�g���[��
	 * @throws IOException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 */
	abstract public void toScratchPadData(SPDataOutputStream out) throws IOException;

	/**
	 * �X�N���b�`�p�b�h����̃f�[�^���A�菇���`���郁�\�b�h�ł��B<br/>
	 * SPDataInputStream�ɑ΂���read�n���\�b�h���Ăяo�����Ƃ�
	 * �I�u�W�F�N�g�ɑ΂��ăv���p�e�B�������ǂݎ�邱�Ƃ��o���܂��B<br/>
	 * ���̂Ƃ��ۑ����Ɠǂݎ�莞�ɑ��삷��v���p�e�B�̏��Ԃ���v���Ă���K�v������܂��B
	 * 
	 * @param in �X�N���b�`�p�b�h�̓��̓X�g���[��
	 * @throws IOException �̈�𒴂���ǂݍ��݂��s�����ꍇ�X���[����܂��B
	 */
	abstract public void fromScratchPadData(SPDataInputStream in) throws IOException;

	/**
	 * �w�肵���ۑ���ɂ��̃I�u�W�F�N�g��ۑ����܂��B<br/>
	 * ���̃��\�b�h��ScratchPadManager���g�p���邽��
	 * ScratchPadManager���g�p����ۂ̒��ӓ_���Q�Ɖ������B
	 * @param managerIndex �ۑ���ԍ�
	 * @throws IOException �̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
	 * @throws ConnectionException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 */
	public void save(int managerIndex) throws IOException {
		ScratchPadManager.getScratchPad(managerIndex).save(this);
	}
	
	/**
	 * �w�肵���X�N���b�`�p�b�h�̈�ɂ��̃I�u�W�F�N�g��ۑ����܂��B
	 * @param sp�@�X�N���b�`�p�b�h�̈�
	 * @throws IOException �m�ۂ��ꂽ�̈�𒴂��鏑�����݂��s�����ꍇ�X���[����܂��B
	 * @throws ConnectionException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws NullPointerException null�l���n���ꂽ�ꍇ�X���[����܂��B
	 */
	public void save (ScratchPad sp) throws IOException {
		sp.save(this);
	}
	
	/**
	 * �w�肵���ۑ��悩�炱�̃I�u�W�F�N�g�𕜋A���܂��B<br/>
	 * ���̃��\�b�h��ScratchPadManager���g�p���邽��
	 * ScratchPadManager���g�p����ۂ̒��ӓ_���Q�Ɖ������B
	 * @param managerIndex�@�ۑ���ԍ�
	 * @throws IOException �̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws InvalidKeyException �������Ȃ��ۑ���ԍ����w�肳�ꂽ�ꍇ�X���[����܂��B
	 * @throws ConnectionException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 */
	public void load(int managerIndex) throws IOException {
		ScratchPadManager.getScratchPad(managerIndex).load(this);
	}
	
	/**
	 * �w�肵���X�N���b�`�p�b�h�̈悩�炱�̃I�u�W�F�N�g�𕜋A���܂��B
	 * @param sp�@�X�N���b�`�p�b�h�̈�
	 * @throws IOException �m�ۂ��ꂽ�̈�𒴂���ǂݎ����s�����ꍇ�X���[����܂��B
	 * @throws ConnectionException ADF�ݒ�ɂ��X�N���b�`�p�b�h�̈悪�������m�ۂ���Ă��Ȃ��ꍇ�ɃX���[����܂��B
	 * @throws NullPointerException null�l���n���ꂽ�ꍇ�X���[����܂��B
	 */
	public void load(ScratchPad sp) throws IOException {
		sp.load(this);
	}
}
