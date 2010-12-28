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

/**
 * �X�N���b�`�p�b�h���C�u�������g�p���ăI�u�W�F�N�g��ǂݎ��E�������݂���ۂ�
 * �Ή����Ă��Ȃ��^�̃I�u�W�F�N�g�������Ƃ��ēn���ꂽ�ꍇ�ɃX���[������O�ł��B
 * 
 * @since	splib-5.1
 */
public class InvalidTypeException extends RuntimeException {

	/**
	 * �C���X�^���X���쐬���܂��B
	 */
	InvalidTypeException() {
		super();
	}
	
	/**
	 * ���b�Z�[�W���w�肵�ăC���X�^���X���쐬���܂��B
	 * @param message ��O���b�Z�[�W
	 */
	InvalidTypeException(String message) {
		super(message);
	}
	
}
