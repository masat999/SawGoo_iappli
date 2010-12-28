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

import java.util.Calendar;
import java.util.Date;

/**
 *	日付情報操作ユーティリティークラスです。<BR>
 *  日付加減算、Dateから文字列、文字列からDateの操作・変換を行います。<BR>
 *	@since	utilib-1.0
 */
public class DateUtils {

    /**
    * 日付操作種別 : 年
    */
    public static final int         YEAR                                = Calendar.YEAR;

    /**
     * 日付操作種別 : 月
     */
    public static final int         MONTH                               = Calendar.MONTH;

    /**
     * 日付操作種別 : 日
     */
    public static final int         DATE                                = Calendar.DATE;

    /**
     * 日付操作種別 : 時 (12時間制)
     */
    public static final int         HOUR                                = Calendar.HOUR;

    /**
     * 日付操作種別 : 時 (24時間制)
     */
    public static final int         HOUR_OF_DAY                         = Calendar.HOUR_OF_DAY;

    /**
     * 日付操作種別 : 分
     */
    public static final int         MINUTE                              = Calendar.MINUTE;

    /**
     * 日付操作種別 : 秒
     */
    public static final int         SECOND                              = Calendar.SECOND;

    /**
     * 日付操作種別 : ミリ秒
     */
    public static final int         MILLISECOND                         = Calendar.MILLISECOND;

    /**
     * ロケーション : 英語
     */
    public static final int         ENGLISH                             = 0;

    /**
     * ロケーション : 日本語
     */
    public static final int         JAPANESE                            = 1;


    /**
     * 変換用定数 : ミリ秒 → 秒 または 秒 → ミリ秒
     */
    private static final int        CONVERT_MILLIS_TO_SECONDS           = 1000;

    /**
     * 変換用定数 : 分 → 秒 または 秒 → 分
     */
    private static final int        CONVERT_SECONDS_TO_MINUTES          = 60;

    /**
     * 変換用定数 : 分 → 時 または 時 → 分
     */
    private static final int        CONVERT_MINUTES_TO_HOURS            = 60;

    /**
     * 変換用定数 : 秒 → 時 または 時 → 秒
     */
    private static final int        CONVERT_SECONDS_TO_HOURS            = CONVERT_MINUTES_TO_HOURS
                                                                          * CONVERT_SECONDS_TO_MINUTES;

    /**
     * タイムゾーン変換用定数
     */
    private static final int        CONVERT_TIMEZONE                    = 100;

    /**
     * タイムゾーンオフセットの時間変換用定数
     * (+0900 のように数値部分が表示されるよう変換する)
     */
    private static final int        CONVERT_TIMEZONE_RAW_OFFSET_TO_HOUR = CONVERT_SECONDS_TO_HOURS
                                                                          * CONVERT_MILLIS_TO_SECONDS
                                                                          / CONVERT_TIMEZONE;


    /**
     * 日付フォーマットトークン
     */
    private static final char[]     FORMAT_DATE_TIMES                   = {

            // 年 (3文字以上の場合は4桁年、2文字以下の場合は2桁年となる)
            'y',

            // 月
            'M',

            // 月における日
            'd',

            // 曜日
            'E',

            // 一日における時 (0 - 23)
            'H',

            // 午前 / 午後の時 (0 - 11)
            'K',

            // 分
            'm',

            // 秒
            's',

            // ミリ秒
            'S',

            // RFC 822形式タイムゾーン
            'Z',

                                                                        };


    /**
     * 日付区切りトークン<br>
     * <br>
     * 日付の区切り文字を定義する。<br>
     */
    private static final char[]     SPLIT_TOKENS                        = {

            // ハイフン
            '-',

            // スラッシュ
            '/',

            // ピリオド
            '.',

            // 年
            '年',

            // 月
            '月',

            // 日
            '日',

            // 時
            ':',

            // 半角スペース
            ' ',

                                                                        };

    /**
     * 曜日名一覧<br>
     * <br>
     * JavaのCalendarクラスの曜日名定数値順に配置する
     */
    private static final String[][] WEEK_NAMES_ALL                      = {

            // 英語
            {
            "Sun",
            "Mon",
            "Tue",
            "Wed",
            "Thu",
            "Fri",
            "Sat",
            },

            // 日本語
            {
            "日曜日",
            "月曜日",
            "火曜日",
            "水曜日",
            "木曜日",
            "金曜日",
            "土曜日",
            },

                                                                        };

    /**
     * インスタンス生成防止。
     *
     */
    private DateUtils() {

        // 処理なし

    }

    /**
     * <p>指定日付に対して日付操作種別の値を加算します。<br/>
     * マイナス値を指定した場合は減算されます。</p>
     * 
     * <p>typeには以下の定数を指定可能です。<br/>
     * DateUtils.YEAR<br/>
     * DateUtils.MONTH<br/>
     * DateUtils.DATE<br/>
     * DateUtils.HOUR<br/>
     * DateUtils.HOUR_OF_DAY<br/>
     * DateUtils.MINUTE<br/>
     * DateUtils.SECOND<br/>
     * DateUtils.MILLISECOND</p>
     *
     * @param date  加算先日付データ
     * @param type  加算する種別
     * @param value 加算する値
     * @return 加算後の日付データ
     */
    public static Date addDate(
            final Date date,
            final int type,
            final int value) {

        // 引数が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }


        final Calendar cal = Calendar.getInstance(); // 日付操作用インスタンス

        // 日付データを設定する
        cal.setTime(date);

        // 指定種別で値を加算する
        switch (type) {
		case HOUR_OF_DAY:
			cal.setTime( new Date( cal.getTime().getTime() + ( value * 3600000L ) ) );
			break;
		case HOUR:
			cal.setTime( new Date( cal.getTime().getTime() + ( value * 3600000L ) ) );
			break;
		case MINUTE:
			cal.setTime( new Date( cal.getTime().getTime() + ( value * 60000L ) ) );
			break;
		case SECOND:
			cal.setTime( new Date( cal.getTime().getTime() + ( value * 1000L ) ) );
			break;
		case MILLISECOND:
			cal.setTime( new Date( cal.getTime().getTime() + ( value ) ) );
			break;
      default:
        	cal.set(type, cal.get(type) + value);
        	break;
        }
        
        // 日付データを返す
        return cal.getTime();

    }


    /**
     * <p>指定日付に対して日付操作種別の値を加算します。<br/>
     * マイナス値を指定した場合は減算されます。</p>
     * 
     * <p>typeには以下の定数を指定可能です。<br/>
     * DateUtils.YEAR<br/>
     * DateUtils.MONTH<br/>
     * DateUtils.DATE<br/>
     * DateUtils.HOUR<br/>
     * DateUtils.HOUR_OF_DAY<br/>
     * DateUtils.MINUTE<br/>
     * DateUtils.SECOND<br/>
     * DateUtils.MILLISECOND</p>
     *
     * @param date  加算先日付データ
     * @param type  加算する種別
     * @param value 加算する値
     * @return 加算後の日付データ
     */
    public static long addDate(
            final long date,
            final int type,
            final int value) {

        return addDate(new Date(date), type, value).getTime();

    }


    /**
     * 指定年月の日数を取得します。
     *
     * @param year  指定する年
     * @param month 指定する月 (1 - 12)
     * @return 指定された年月の日数
     * @exception IllegalArgumentException
     *                  月指定が不正な場合に発生します。
     */
    public static int getDateCount(
            final int year,
            final int month) {
    	
    	if (month < 1 || 12 < month) {
    		throw new IllegalArgumentException();
    	}

        final Calendar cal = Calendar.getInstance(); // 日付操作用インスンタンス

        // 指定された年月を設定する
        // (月は日付日数取得用に次月を指定するためそのまま指定する)
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // 指定年月の日数を返す
        return cal.get(Calendar.DATE);

    }

    /**
     * 指定フォーマットで指定日付データを表現した文字列を取得します。<br>
     * <br>
     * ロケールはデフォルトでは(英語）が使用されます<br>
     *
     * @param format    フォーマット文字列
     * @param date      日時
     * @return 指定フォーマットで指定日付データを表現した文字列
     * @exception IllegalArgumentException
     *                  フォーマット文字列が不正な場合に発生します。
     */
    public static String format(
            final String format,
            final Date date) {

        // 引数が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }

        // 指定フォーマットの日付文字列を返す
        return format(format, date.getTime());

    }


    /**
     * 指定フォーマットで指定日付データを表現した文字列を取得します。<br>
     * <br>
     * ロケールはデフォルトでは(英語）が使用されます<br>
     *
     * @param format    フォーマット文字列
     * @param date      日時
     * @return 指定フォーマットで指定日付データを表現した文字列
     * @exception IllegalArgumentException
     *                  フォーマット文字列が不正な場合に発生します。
     */
    public static String format(
            final String format,
            final long date) {

        // 指定フォーマットの日付文字列を返す
        return format(format, date, ENGLISH);

    }


    /**
     * 指定フォーマット、ロケールで指定日付データを表現した文字列を取得します。<br>
     *
     * @param format    フォーマット文字列
     * @param date      日時
     * @param localeType    ロケール(DateUtils.ENGLISH / DateUtils.JAPANESE)
     * @return 指定フォーマットで指定日付データを表現した文字列
     * @exception IllegalArgumentException
     *                  フォーマット文字列が不正な場合に発生します。
    */
    public static String format(
            final String format,
            final Date date,
            final int localeType) {

        // 引数が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }

        // 指定フォーマットの日付文字列を返す
        return format(format, date.getTime(), localeType);

    }


    /**
     * 指定フォーマットで指定日付データを表現した文字列を取得します。<br>
     *
     * @param format    フォーマット文字列
     * @param time      日時
     * @param localeType    ロケール(DateUtils.ENGLISH / DateUtils.JAPANESE)
     * @return 指定フォーマットで指定日付データを表現した文字列
     * @exception IllegalArgumentException
     *                  フォーマット文字列が不正な場合に発生します。
     */
    public static String format(
            final String format,
            final long time,
            final int localeType) {

        // 引数が null の場合は例外
        if ((format == null)) {

            throw new IllegalArgumentException();

        }


        final StringBuffer retBuf = new StringBuffer(); // 返却日付文字列バッファ
        final Calendar cal = Calendar.getInstance(); // 日付算出用カレンダー
        int indexFormat = 0; // フォーマット文字列検索位置


        // 日時を設定する
        cal.setTime(new Date(time));


        // 文字列終端までループ
        while (indexFormat < format.length()) {

            final char nowChar = format.charAt(indexFormat); // 現在位置の文字

            // 日付フォーマットトークン以外の場合
            if (!isDateFormatToken(nowChar)) {

                // 現在の文字を返却日付文字列バッファへ追加する
                retBuf.append(nowChar);

                // 次の文字へ
                indexFormat++;
                continue;

            }


            int tokenCount = 1; // トークン数

            // 同じ日付フォーマットトークンが連続している個数を取得する
            for (int i = indexFormat + 1; i < format.length(); i++, tokenCount++) {

                // 違う文字の場合
                if (format.charAt(i) != nowChar) {

                    // ループ中断
                    break;

                }

            }

            final int digitYearStr = 3; // 文字列表記になる桁数
            final int digitRFC822 = 4; // RFC822タイムゾーンの桁数
            int tempValue; // 作業用数値


            // トークン別処理
            switch (nowChar) {

                // 年
                case 'y':

                    // 桁数が 3 桁以上の場合
                    if (tokenCount >= digitYearStr) {

                        // 返却文字列バッファへ年4桁を追加する
                        retBuf.append(appendPadding(
                                String.valueOf(cal.get(Calendar.YEAR)),
                                tokenCount));
                    } else {

                        // 返却文字列バッファへ年2桁を追加する
                        retBuf.append(appendPadding(
                                String.valueOf(cal.get(Calendar.YEAR)).substring(
                                        2),
                                tokenCount));

                    }

                    break;


                // 月
                case 'M':

                    // 返却文字列バッファへ月の数値表現を追加する
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.MONTH) + 1),
                            tokenCount));


                    break;


                // 月における日
                case 'd':

                    // 返却文字列バッファへ月における日を追加する
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.DAY_OF_MONTH)),
                            tokenCount));
                    break;


                // 曜日
                case 'E':

                    // 返却文字列バッファへ曜日名を追加する
                    retBuf.append(WEEK_NAMES_ALL[localeType][cal.get(Calendar.DAY_OF_WEEK) - 1].substring(
                            0,
                            WEEK_NAMES_ALL[localeType][cal.get(Calendar.DAY_OF_WEEK) - 1].length()));
                    break;


                // 一日における時 (0 - 23)
                case 'H':

                    // 返却文字列バッファへ一日における時 (0 - 23)を追加する
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.HOUR_OF_DAY)),
                            tokenCount));
                    break;


                // 一日における時 (0 - 11)
                case 'K':

                    // 返却文字列バッファへ一日における時 (0 - 11)を追加する
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.HOUR)),
                            tokenCount));
                    break;


                // 分
                case 'm':

                    // 返却文字列バッファへ分を追加する
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.MINUTE)),
                            tokenCount));
                    break;


                // 秒
                case 's':

                    // 返却文字列バッファへ秒を追加する
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.SECOND)),
                            tokenCount));
                    break;


                // ミリ秒
                case 'S':

                    // 返却文字列バッファへミリ秒を追加する
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.MILLISECOND)),
                            tokenCount));
                    break;


                // RFC 822形式タイムゾーン
                case 'Z':

                    // タイムゾーンオフセット値を算出します
                    tempValue = cal.getTimeZone().getRawOffset()
                                / CONVERT_TIMEZONE_RAW_OFFSET_TO_HOUR;

                    // 返却文字列バッファへタイムゾーンオフセットを追加します
                    retBuf.append(appendPadding(
                            String.valueOf(tempValue),
                            digitRFC822,
                            tempValue < 0 ? '-' : '+'));
                    break;


                // エラー
                default:

                    throw new IllegalArgumentException();

            }

            // フォーマット文字列検索位置と入力テキスト検索位置を移動します
            indexFormat += tokenCount;

        }


        // 作成した文字列を返却します
        return retBuf.toString();

    }

    /**
     * 指定フォーマットで指定テキストをパースした日付を取得します。
     *
     * @param format    フォーマット文字列
     * @param text      パースします文字列
     * @return パースした日付
     * @exception IllegalArgumentException
     *                  フォーマット文字列が不正な場合に発生します。
     */
    public static Date parse(
            final String format,
            final String text) {

        // 引数が null の場合は例外
        if ((format == null) || (text == null)) {

            throw new IllegalArgumentException();

        }


        final StringBuffer textBuf = new StringBuffer(text); // パース文字列編集バッファ
        final String editText; // 編集後テキスト
        int index = 0; // 編集用インデックス


        // パース文字列の文字数分検索します
        while (index < textBuf.length()) {

            // 区切り文字以外の場合
            if (!isDateSplitToken(textBuf.charAt(index))) {

                // 次の文字へ
                index++;
                continue;

            }

            // 現在位置の文字を削除します
            textBuf.deleteCharAt(index);

        }


        // 編集後のテキストを取得します
        editText = textBuf.toString();


        // 数値以外の文字列がある場合は失敗
        if (!StringUtils.isDigit(editText)) {

            throw new IllegalArgumentException(
                    "パース対象文字列に数値と区切り文字以外の文字が含まれています [text = " + text + "]");

        }


        final Calendar retCal = Calendar.getInstance(); // 返却日付算出用カレンダー
        int indexFormat = 0; // フォーマット文字列検索位置
        int indexText = 0; // 入力テキスト検索位置


        // カレンダーの日付をクリアします
        clearCalendar(retCal);

        // 文字列終端までループ
        while ((indexFormat < format.length())
               && (indexText < editText.length())) {

            final char nowChar = format.charAt(indexFormat); // 現在位置の文字

            // 日付フォーマットトークン以外の場合
            if (!isDateFormatToken(nowChar)) {

                // 次の文字へ
                indexFormat++;
                continue;

            }


            int tokenCount = 1; // トークン数

            // 同じ日付フォーマットトークンが連続している個数を取得します
            for (int i = indexFormat + 1; i < format.length(); i++, tokenCount++) {

                // 違う文字の場合
                if (format.charAt(i) != nowChar) {

                    // ループ中断
                    break;

                }

            }


            // 算出用定数を設定します
            final int digitYearHalf = 2; // 西暦桁数の半分
            final int yearThousand = 1000; // 西暦切捨用

            int value = 0;
            try {
                // int値を返す
                value = Integer.parseInt(editText.substring(
                        indexText,
                        indexText + tokenCount));

            } catch (final NumberFormatException e) {

                // デフォルト値を設定
                value = 0;

            }

            // トークン別処理
            switch (nowChar) {

                // 年
                case 'y':

                    // 月が西暦桁数の半分以下の場合
                    if (tokenCount <= digitYearHalf) {

                        final Calendar localCal = Calendar.getInstance();
                        final int year;

                        // 西暦を算出します
                        year = (localCal.get(Calendar.YEAR) / yearThousand * yearThousand)
                               + value;

                        // 返却日付に年を設定します
                        retCal.set(Calendar.YEAR, year);

                    } else {

                        // 返却日付に年を設定します
                        retCal.set(Calendar.YEAR, value);

                    }
                    break;


                // 月
                case 'M':

                    // 返却日付に月を設定します
                    retCal.set(Calendar.MONTH, value - 1);
                    break;


                // 月における日
                case 'd':

                    // 返却日付に月における日を設定します
                    retCal.set(Calendar.DAY_OF_MONTH, value);
                    break;


                // 一日における時 (0 - 23)
                case 'H':

                    // 返却日付に一日における時を設定します
                    retCal.set(Calendar.HOUR_OF_DAY, value);
                    break;


                // 一日における時 (0 - 11)
                case 'K':

                    // 返却日付に一日における時を設定します
                    retCal.set(Calendar.HOUR, value);
                    break;


                // 分
                case 'm':

                    // 返却日付に分を設定します
                    retCal.set(Calendar.MINUTE, value);
                    break;


                // 秒
                case 's':

                    // 返却日付に秒を設定します
                    retCal.set(Calendar.SECOND, value);
                    break;


                // ミリ秒
                case 'S':

                    // 返却日付にミリ秒を設定します
                    retCal.set(Calendar.MILLISECOND, value);
                    break;


                // エラー
                default:

                    throw new IllegalArgumentException();

            }

            // フォーマット文字列検索位置と入力テキスト検索位置を移動します
            indexFormat += tokenCount;
            indexText += tokenCount;

        }


        // 日付を返す
        return retCal.getTime();

    }

    /**
     * 指定文字列を元にパディング追加処理を行います。
     *
     * @param str           元となる文字列
     * @param digitCount    表現します桁数
     * @return パディング追加した文字列
     */
    private static String appendPadding(
            final String str,
            final int digitCount) {

        return appendPadding(str, digitCount, (char) 0);

    }


    /**
     * 指定文字列を元にパディング追加処理を行います。
     *
     * @param str           元となる文字列
     * @param digitCount    表現します桁数
     * @param paddingSign   付加します符号文字
     * @return パディング追加した文字列
     */
    private static String appendPadding(
            final String str,
            final int digitCount,
            final char paddingSign) {

        final StringBuffer strBuf = new StringBuffer(str); // 文字列バッファ
        int diffCount = digitCount - str.length(); // 差分を算出


        // 差分が正の値の場合
        if (diffCount > 0) {

            // 差分桁数分ループします
            for (; diffCount > 0; diffCount--) {

                // 先頭に 0 を追加します
                strBuf.insert(0, '0');

            }

        } else {

            // 処理なし

        }


        // 符号を付加します場合
        // かつ追加先文字列バッファの先頭が追加しようとしている符号以外の場合
        if ((paddingSign != 0) && (strBuf.length() > 0)
            && (strBuf.charAt(0) != paddingSign)) {

            // 符号を先頭へ挿入します
            strBuf.insert(0, paddingSign);

        }


        // 編集した文字列を返す
        return strBuf.toString();

    }

    /**
     * カレンダーをクリアします。
     *
     * @param cal クリアしますカレンダー
     */
    private static void clearCalendar(
            final Calendar cal) {

        // 引数が null の場合は例外
        if (cal == null) {

            throw new IllegalArgumentException();

        }


        // クリア値を設定します
        final int clearYear = 1970; // 年
        final int clearMonth = 0; // 月
        final int clearDate = 1; // 日
        final int clearHour = 0; // 時
        final int clearMinute = 0; // 分
        final int clearSecond = 0; // 秒
        final int clearMillisecond = 0; // ミリ秒

        // カレンダーの日付をクリアします
        cal.set(Calendar.YEAR, clearYear);
        cal.set(Calendar.MONTH, clearMonth);
        cal.set(Calendar.DATE, clearDate);
        cal.set(Calendar.HOUR_OF_DAY, clearHour);
        cal.set(Calendar.MINUTE, clearMinute);
        cal.set(Calendar.SECOND, clearSecond);
        cal.set(Calendar.MILLISECOND, clearMillisecond);

    }

    /**
     * 日付フォーマットトークンかどうかを判定します。
     *
     * @param nowChar 比較対象文字
     */
    private static boolean isDateFormatToken(
            final char nowChar) {
        boolean retValue = false;
        for (int i = 0; i < FORMAT_DATE_TIMES.length; i++) {

            if (FORMAT_DATE_TIMES[i] == nowChar) {
                retValue = true;
                break;
            }
        }
        return retValue;
    }

    /**
     * 日付区切りトークンかどうかを判定します。
     *
     * @param nowChar 比較対象文字
     */
    private static boolean isDateSplitToken(
            final char nowChar) {
        boolean retValue = false;
        for (int i = 0; i < SPLIT_TOKENS.length; i++) {

            if (SPLIT_TOKENS[i] == nowChar) {
                retValue = true;
                break;
            }
        }
        return retValue;
    }

}
