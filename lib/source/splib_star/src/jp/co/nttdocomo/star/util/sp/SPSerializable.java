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
 * <p>�I�u�W�F�N�g�̓ǂݎ��E�������ݕ��@���`���邽�߂̃C���^�t�F�[�X�ł��B<br/>
 * ���̃C���^�t�F�[�X�����������N���X��
 * <ol>
 * <li>ScratchPad.save</li>
 * <li>ScratchPadManager.save</li>
 * <li>ScratchPad.load</li>
 * <li>ScratchPadManager.load</li>
 * </ol>
 * �𗘗p���ĊȈՂɓǂݎ��E�������݂��s����悤�ɂȂ�܂��B</p>
 * 
 * <p>���̃C���^�t�F�[�X���p�����Ă���SPSerializableObject���g�p�����
 * �I�u�W�F�N�g�ɑ΂���save���\�b�h��load���\�b�h���ǉ�����邽�߂��e�ՂɎg�p�ł��܂��B</p>
 * 
 * @see jp.co.nttdocomo.star.util.sp.ScratchPad
 * @see jp.co.nttdocomo.star.util.sp.ScratchPadManager
 * @see jp.co.nttdocomo.star.util.sp.SPSerializableObject
 * @version splib-1.0
 */
public interface SPSerializable {
	/**
	 * �X�N���b�`�p�b�h�ւ̃f�[�^�ۑ��菇���`���郁�\�b�h�ł��B<br/>
	 * SPDataOutputStream�ɑ΂���write�n���\�b�h���Ăяo�����Ƃ�
	 * �I�u�W�F�N�g�����v���p�e�B�������ۑ����Ă������Ƃ��o���܂��B
	 * 
	 * @param out �X�N���b�`�p�b�h�̏o�̓X�g���[��
	 * @throws IOException �m�ۂ����̈�𒴂��鏑�����݂��s�����ꍇIOException���X���[����܂��B
	 */
	void toScratchPadData(SPDataOutputStream out) throws IOException;
	
	/**
	 * �X�N���b�`�p�b�h����̃f�[�^���A�菇���`���郁�\�b�h�ł��B<br/>
	 * SPDataInputStream�ɑ΂���read�n���\�b�h���Ăяo�����Ƃ�
	 * �I�u�W�F�N�g�ɑ΂��ăv���p�e�B�������ǂݎ�邱�Ƃ��o���܂��B<br/>
	 * ���̂Ƃ��ۑ����Ɠǂݎ�莞�ɑ��삷��v���p�e�B�̏��Ԃ���v���Ă���K�v������܂��B
	 * 
	 * @param in
	 * @throws IOException
	 */
	void fromScratchPadData(SPDataInputStream in) throws IOException;
}
