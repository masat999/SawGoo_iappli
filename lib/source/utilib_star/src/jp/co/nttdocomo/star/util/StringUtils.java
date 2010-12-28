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
 *  文字列操作ユーティリティークラスです。<BR>
 *  文字列の書式判定・文字列検索、文字列分割を行います。<BR>
 *  @since  utilib-1.0
 */
public class StringUtils {

    /**
    * インスタンス生成防止。
    */
    private StringUtils() {

        // 処理なし

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
     * 文字列がnullか空文字か判定します。
     *
     * @param str 判定する文字列
     * @return 文字列がnullか空文字の場合はtrue
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
     * 指定された文字列の最初と最後の空白を削除します。 <BR>
     * null が渡された場合には null を返します。<BR>
     *
     * @param str 判定する文字列
     * @return トリムされた文字列(または null)
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
     * 指定された文字列が全て小文字か判定します。 <BR>
     * null が指定された場合には false を返します。<BR>
     * 空の文字列が指定された場合には false を返します。 <BR>
     * 全角文字は小文字とは判別されません。
     *
     * @param str 判定する文字列
     * @return 全て小文字の場合はtrue
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
     * 指定された文字列が全て大文字か判定します。 <BR>
     * null が指定された場合には false を返します。<BR>
     * 空の文字列が指定された場合には false を返します。 <BR>
     * 全角文字は大文字とは判別されません。
     *
     * @param str 判定する文字列
     * @return 全て大文字の場合はtrue
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
     * 指定された文字列を指定された区切り文字を使用して配列にします。  <BR>
     * 区切り文字は配列内の文字列には含まれません。 <BR>
     * null が渡された場合には null を返します。<BR>
     *
     * @param str 分割対象文字列
     * @param separator 部分文字列
     * @return 分割された文字列配列(または null)
     */
    public static String[] split(
            String str,
            String separator) {

        // nullまたは空文字の場合
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
     * 指定された文字列内の指定された文字列(searchWord)を指定された文字列(replacement)に変換します。   <BR>
     * null が渡された場合には null を返します。<BR>
     *
     * @param str 入れ替え対象の文字列を含むテキスト
     * @param searchWord 入れ替え対象となる文字列
     * @param replacement 入れ替えられる文字列
     * @return 変換された文字列(または null)
     */
    public static String replaceAll(
            String str,
            String searchWord,
            String replacement) {

        // nullまたは空文字の場合
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
     * 指定された文字列が半角整数値かどうかを判定します。
     * 全角数字は数値とは判別されません。
     *
     * @param str 判定する文字列
     * @return 指定された文字列が半角整数値の場合は true
     */
    public static boolean isDigit(
            final String str) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // 数値以外
            return false;

        }

        // 文字を取得する
        final char[] strChars = str.toCharArray();

        // 全文字分処理をする
        for (int i = 0; i < strChars.length; i++) {
        	
        	// 負の整数(-0を含む)の場合
        	if (strChars.length >= 2 && i == 0 && strChars[i] == '-') {
        		continue;
        	}

            // 数値以外の場合
            if (strChars[i] < '0' || '9' < strChars[i]) {

                // 数値以外
                return false;

            }

        }

        // 数値
        return true;

    }

    /**
     * 指定された文字列が半角アルファベットかどうかを判定します。
     * 全角文字はアルファベットとは判別されません。
     *
     * @param str 判定する文字列
     * @return 指定された文字列が半角アルファベットの場合は true
     */
    public static boolean isAlpha(
            final String str) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // アルファベット以外
            return false;

        }

        // 文字を取得する
        final char[] strChars = str.toCharArray();

        // 全文字分処理をする
        for (int i = 0; i < strChars.length; i++) {

            final char tempChar = strChars[i];

            // アルファベット以外の場合
            if ((tempChar < 'A' || 'Z' < tempChar)
            		&& (tempChar < 'a' || 'z' < tempChar)) {

                // アルファベット以外
                return false;

            }

        }

        // アルファベット
        return true;

    }
}
