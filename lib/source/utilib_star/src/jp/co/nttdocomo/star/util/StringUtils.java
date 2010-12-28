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
package jp.co.nttdocomo.star.util;

import java.util.Vector;

/**
 *  �����񑀍샆�[�e�B���e�B�[�N���X�ł��B<BR>
 *  ������̏�������E�����񌟍��A�����񕪊����s���܂��B<BR>
 *  @since  utilib-1.0
 */
public class StringUtils {

    /**
    * �C���X�^���X�����h�~�B
    */
    private StringUtils() {

        // �����Ȃ�

    }

    /*
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    /**
     * ������null���󕶎������肵�܂��B
     *
     * @param str ���肷�镶����
     * @return ������null���󕶎��̏ꍇ��true
     */
    public static boolean isEmpty(
            String str) {
        return (str == null || str.length() == 0);
    }

    /*
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String, handling <code>null</code> by returning
     * <code>null</code>.</p>
     *
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #strip(String)}.</p>
     *
     * <p>To trim your choice of characters, use the
     * {@link #strip(String, String)} methods.</p>
     *
     * <pre>
     * StringUtils.trim(null)          = null
     * StringUtils.trim("")            = ""
     * StringUtils.trim("     ")       = ""
     * StringUtils.trim("abc")         = "abc"
     * StringUtils.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param str  the String to be trimmed, may be null
     * @return the trimmed string, <code>null</code> if null String input
     */
    /**
     * �w�肳�ꂽ������̍ŏ��ƍŌ�̋󔒂��폜���܂��B <BR>
     * null ���n���ꂽ�ꍇ�ɂ� null ��Ԃ��܂��B<BR>
     *
     * @param str ���肷�镶����
     * @return �g�������ꂽ������(�܂��� null)
     */
    public static String trim(
            String str) {
        return str == null ? null : str.trim();
    }

    /*
     * <p>Checks if the String contains only lowercase characters.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>false</code>.</p>
     *
     * <pre>
     * StringUtils.isAllLowerCase(null)   = false
     * StringUtils.isAllLowerCase("")     = false
     * StringUtils.isAllLowerCase("  ")   = false
     * StringUtils.isAllLowerCase("abc")  = true
     * StringUtils.isAllLowerCase("abC") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains lowercase characters, and is non-null
     * @since 2.5
     */
    /**
     * �w�肳�ꂽ�����񂪑S�ď����������肵�܂��B <BR>
     * null ���w�肳�ꂽ�ꍇ�ɂ� false ��Ԃ��܂��B<BR>
     * ��̕����񂪎w�肳�ꂽ�ꍇ�ɂ� false ��Ԃ��܂��B <BR>
     * �S�p�����͏������Ƃ͔��ʂ���܂���B
     *
     * @param str ���肷�镶����
     * @return �S�ď������̏ꍇ��true
     */
    public static boolean isAllLowerCase(
            String str) {
        if (str == null || isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (str.charAt(i) < 'a' || 'z' < str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /*
     * <p>Checks if the String contains only uppercase characters.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>false</code>.</p>
     *
     * <pre>
     * StringUtils.isAllUpperCase(null)   = false
     * StringUtils.isAllUpperCase("")     = false
     * StringUtils.isAllUpperCase("  ")   = false
     * StringUtils.isAllUpperCase("ABC")  = true
     * StringUtils.isAllUpperCase("aBC") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains uppercase characters, and is non-null
     * @since 2.5
     */
    /**
     * �w�肳�ꂽ�����񂪑S�đ啶�������肵�܂��B <BR>
     * null ���w�肳�ꂽ�ꍇ�ɂ� false ��Ԃ��܂��B<BR>
     * ��̕����񂪎w�肳�ꂽ�ꍇ�ɂ� false ��Ԃ��܂��B <BR>
     * �S�p�����͑啶���Ƃ͔��ʂ���܂���B
     *
     * @param str ���肷�镶����
     * @return �S�đ啶���̏ꍇ��true
     */
    public static boolean isAllUpperCase(
            String str) {
        if (str == null || isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (str.charAt(i) < 'A' || 'Z' < str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * �w�肳�ꂽ��������w�肳�ꂽ��؂蕶�����g�p���Ĕz��ɂ��܂��B  <BR>
     * ��؂蕶���͔z����̕�����ɂ͊܂܂�܂���B <BR>
     * null ���n���ꂽ�ꍇ�ɂ� null ��Ԃ��܂��B<BR>
     *
     * @param str �����Ώە�����
     * @param separator ����������
     * @return �������ꂽ������z��(�܂��� null)
     */
    public static String[] split(
            String str,
            String separator) {

        // null�܂��͋󕶎��̏ꍇ
        if (((str == null) || (str.length() == 0))
            || ((separator == null) || (separator.length() == 0))

        ) {
            return null;
        }

        Vector vec = new Vector();
        int index = 0;

        while (true) {
            int regIndex = str.indexOf(separator, index);
            if (regIndex >= 0) {
                vec.addElement(str.substring(index, regIndex));
                index = regIndex + separator.length();
            } else {
                vec.addElement(str.substring(index));
                break;
            }
        }

        if (vec.size() > 0) {
            String[] ret = new String[vec.size()];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = (String) vec.elementAt(i);
            }
            return ret;
        } else {
            return new String[] {
                str
            };
        }
    }

    /**
     * �w�肳�ꂽ��������̎w�肳�ꂽ������(searchWord)���w�肳�ꂽ������(replacement)�ɕϊ����܂��B   <BR>
     * null ���n���ꂽ�ꍇ�ɂ� null ��Ԃ��܂��B<BR>
     *
     * @param str ����ւ��Ώۂ̕�������܂ރe�L�X�g
     * @param searchWord ����ւ��ΏۂƂȂ镶����
     * @param replacement ����ւ����镶����
     * @return �ϊ����ꂽ������(�܂��� null)
     */
    public static String replaceAll(
            String str,
            String searchWord,
            String replacement) {

        // null�܂��͋󕶎��̏ꍇ
        if (((str == null) || (str.length() == 0))
            || ((searchWord == null) || (searchWord.length() == 0))
            || ((replacement == null) || (replacement.length() == 0))

        ) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        int index = 0;
        while (true) {
            int regIndex = str.indexOf(searchWord, index);
            if (regIndex >= 0) {
                sb.append(str.substring(index, regIndex));
                sb.append(replacement);
                index = regIndex + searchWord.length();
            } else {
                sb.append(str.substring(index));
                break;
            }
        }

        return sb.toString();
    }

    /**
     * �w�肳�ꂽ�����񂪔��p�����l���ǂ����𔻒肵�܂��B
     * �S�p�����͐��l�Ƃ͔��ʂ���܂���B
     *
     * @param str ���肷�镶����
     * @return �w�肳�ꂽ�����񂪔��p�����l�̏ꍇ�� true
     */
    public static boolean isDigit(
            final String str) {

        // null�܂��͋󕶎��̏ꍇ
        if ((str == null) || (str.length() == 0)) {

            // ���l�ȊO
            return false;

        }

        // �������擾����
        final char[] strChars = str.toCharArray();

        // �S����������������
        for (int i = 0; i < strChars.length; i++) {
        	
        	// ���̐���(-0���܂�)�̏ꍇ
        	if (strChars.length >= 2 && i == 0 && strChars[i] == '-') {
        		continue;
        	}

            // ���l�ȊO�̏ꍇ
            if (strChars[i] < '0' || '9' < strChars[i]) {

                // ���l�ȊO
                return false;

            }

        }

        // ���l
        return true;

    }

    /**
     * �w�肳�ꂽ�����񂪔��p�A���t�@�x�b�g���ǂ����𔻒肵�܂��B
     * �S�p�����̓A���t�@�x�b�g�Ƃ͔��ʂ���܂���B
     *
     * @param str ���肷�镶����
     * @return �w�肳�ꂽ�����񂪔��p�A���t�@�x�b�g�̏ꍇ�� true
     */
    public static boolean isAlpha(
            final String str) {

        // null�܂��͋󕶎��̏ꍇ
        if ((str == null) || (str.length() == 0)) {

            // �A���t�@�x�b�g�ȊO
            return false;

        }

        // �������擾����
        final char[] strChars = str.toCharArray();

        // �S����������������
        for (int i = 0; i < strChars.length; i++) {

            final char tempChar = strChars[i];

            // �A���t�@�x�b�g�ȊO�̏ꍇ
            if ((tempChar < 'A' || 'Z' < tempChar)
            		&& (tempChar < 'a' || 'z' < tempChar)) {

                // �A���t�@�x�b�g�ȊO
                return false;

            }

        }

        // �A���t�@�x�b�g
        return true;

    }
}
