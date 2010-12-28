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

/**
 * �X�N���b�`�p�b�h���C�u������ScrachPadManger���g�p����ہA
 * �������Ȃ��L�[���w�肵�ēǂݎ��E�������݂��s�����ꍇ��
 * �X���[������O�ł��B
 * 
 * @since	splib-1.0
 */
public class InvalidKeyException extends RuntimeException {

	/**
	 * �C���X�^���X���쐬���܂��B
	 */
	InvalidKeyException() {
		super();
	}
	
	/**
	 * ���b�Z�[�W���w�肵�ăC���X�^���X���쐬���܂��B
	 * @param message ��O���b�Z�[�W
	 */
	public InvalidKeyException(String message) {
		super(message);
	}

}
