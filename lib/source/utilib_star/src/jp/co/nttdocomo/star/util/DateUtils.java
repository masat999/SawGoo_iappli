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
 *	���t��񑀍샆�[�e�B���e�B�[�N���X�ł��B<BR>
 *  ���t�����Z�ADate���當����A�����񂩂�Date�̑���E�ϊ����s���܂��B<BR>
 *	@since	utilib-1.0
 */
public class DateUtils {

    /**
    * ���t������ : �N
    */
    public static final int         YEAR                                = Calendar.YEAR;

    /**
     * ���t������ : ��
     */
    public static final int         MONTH                               = Calendar.MONTH;

    /**
     * ���t������ : ��
     */
    public static final int         DATE                                = Calendar.DATE;

    /**
     * ���t������ : �� (12���Ԑ�)
     */
    public static final int         HOUR                                = Calendar.HOUR;

    /**
     * ���t������ : �� (24���Ԑ�)
     */
    public static final int         HOUR_OF_DAY                         = Calendar.HOUR_OF_DAY;

    /**
     * ���t������ : ��
     */
    public static final int         MINUTE                              = Calendar.MINUTE;

    /**
     * ���t������ : �b
     */
    public static final int         SECOND                              = Calendar.SECOND;

    /**
     * ���t������ : �~���b
     */
    public static final int         MILLISECOND                         = Calendar.MILLISECOND;

    /**
     * ���P�[�V���� : �p��
     */
    public static final int         ENGLISH                             = 0;

    /**
     * ���P�[�V���� : ���{��
     */
    public static final int         JAPANESE                            = 1;


    /**
     * �ϊ��p�萔 : �~���b �� �b �܂��� �b �� �~���b
     */
    private static final int        CONVERT_MILLIS_TO_SECONDS           = 1000;

    /**
     * �ϊ��p�萔 : �� �� �b �܂��� �b �� ��
     */
    private static final int        CONVERT_SECONDS_TO_MINUTES          = 60;

    /**
     * �ϊ��p�萔 : �� �� �� �܂��� �� �� ��
     */
    private static final int        CONVERT_MINUTES_TO_HOURS            = 60;

    /**
     * �ϊ��p�萔 : �b �� �� �܂��� �� �� �b
     */
    private static final int        CONVERT_SECONDS_TO_HOURS            = CONVERT_MINUTES_TO_HOURS
                                                                          * CONVERT_SECONDS_TO_MINUTES;

    /**
     * �^�C���]�[���ϊ��p�萔
     */
    private static final int        CONVERT_TIMEZONE                    = 100;

    /**
     * �^�C���]�[���I�t�Z�b�g�̎��ԕϊ��p�萔
     * (+0900 �̂悤�ɐ��l�������\�������悤�ϊ�����)
     */
    private static final int        CONVERT_TIMEZONE_RAW_OFFSET_TO_HOUR = CONVERT_SECONDS_TO_HOURS
                                                                          * CONVERT_MILLIS_TO_SECONDS
                                                                          / CONVERT_TIMEZONE;


    /**
     * ���t�t�H�[�}�b�g�g�[�N��
     */
    private static final char[]     FORMAT_DATE_TIMES                   = {

            // �N (3�����ȏ�̏ꍇ��4���N�A2�����ȉ��̏ꍇ��2���N�ƂȂ�)
            'y',

            // ��
            'M',

            // ���ɂ������
            'd',

            // �j��
            'E',

            // ����ɂ����鎞 (0 - 23)
            'H',

            // �ߑO / �ߌ�̎� (0 - 11)
            'K',

            // ��
            'm',

            // �b
            's',

            // �~���b
            'S',

            // RFC 822�`���^�C���]�[��
            'Z',

                                                                        };


    /**
     * ���t��؂�g�[�N��<br>
     * <br>
     * ���t�̋�؂蕶�����`����B<br>
     */
    private static final char[]     SPLIT_TOKENS                        = {

            // �n�C�t��
            '-',

            // �X���b�V��
            '/',

            // �s���I�h
            '.',

            // �N
            '�N',

            // ��
            '��',

            // ��
            '��',

            // ��
            ':',

            // ���p�X�y�[�X
            ' ',

                                                                        };

    /**
     * �j�����ꗗ<br>
     * <br>
     * Java��Calendar�N���X�̗j�����萔�l���ɔz�u����
     */
    private static final String[][] WEEK_NAMES_ALL                      = {

            // �p��
            {
            "Sun",
            "Mon",
            "Tue",
            "Wed",
            "Thu",
            "Fri",
            "Sat",
            },

            // ���{��
            {
            "���j��",
            "���j��",
            "�Ηj��",
            "���j��",
            "�ؗj��",
            "���j��",
            "�y�j��",
            },

                                                                        };

    /**
     * �C���X�^���X�����h�~�B
     *
     */
    private DateUtils() {

        // �����Ȃ�

    }

    /**
     * <p>�w����t�ɑ΂��ē��t�����ʂ̒l�����Z���܂��B<br/>
     * �}�C�i�X�l���w�肵���ꍇ�͌��Z����܂��B</p>
     * 
     * <p>type�ɂ͈ȉ��̒萔���w��\�ł��B<br/>
     * DateUtils.YEAR<br/>
     * DateUtils.MONTH<br/>
     * DateUtils.DATE<br/>
     * DateUtils.HOUR<br/>
     * DateUtils.HOUR_OF_DAY<br/>
     * DateUtils.MINUTE<br/>
     * DateUtils.SECOND<br/>
     * DateUtils.MILLISECOND</p>
     *
     * @param date  ���Z����t�f�[�^
     * @param type  ���Z������
     * @param value ���Z����l
     * @return ���Z��̓��t�f�[�^
     */
    public static Date addDate(
            final Date date,
            final int type,
            final int value) {

        // ������ null �̏ꍇ�͗�O
        if (date == null) {

            throw new IllegalArgumentException();

        }


        final Calendar cal = Calendar.getInstance(); // ���t����p�C���X�^���X

        // ���t�f�[�^��ݒ肷��
        cal.setTime(date);

        // �w���ʂŒl�����Z����
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
        
        // ���t�f�[�^��Ԃ�
        return cal.getTime();

    }


    /**
     * <p>�w����t�ɑ΂��ē��t�����ʂ̒l�����Z���܂��B<br/>
     * �}�C�i�X�l���w�肵���ꍇ�͌��Z����܂��B</p>
     * 
     * <p>type�ɂ͈ȉ��̒萔���w��\�ł��B<br/>
     * DateUtils.YEAR<br/>
     * DateUtils.MONTH<br/>
     * DateUtils.DATE<br/>
     * DateUtils.HOUR<br/>
     * DateUtils.HOUR_OF_DAY<br/>
     * DateUtils.MINUTE<br/>
     * DateUtils.SECOND<br/>
     * DateUtils.MILLISECOND</p>
     *
     * @param date  ���Z����t�f�[�^
     * @param type  ���Z������
     * @param value ���Z����l
     * @return ���Z��̓��t�f�[�^
     */
    public static long addDate(
            final long date,
            final int type,
            final int value) {

        return addDate(new Date(date), type, value).getTime();

    }


    /**
     * �w��N���̓������擾���܂��B
     *
     * @param year  �w�肷��N
     * @param month �w�肷�錎 (1 - 12)
     * @return �w�肳�ꂽ�N���̓���
     * @exception IllegalArgumentException
     *                  ���w�肪�s���ȏꍇ�ɔ������܂��B
     */
    public static int getDateCount(
            final int year,
            final int month) {
    	
    	if (month < 1 || 12 < month) {
    		throw new IllegalArgumentException();
    	}

        final Calendar cal = Calendar.getInstance(); // ���t����p�C���X���^���X

        // �w�肳�ꂽ�N����ݒ肷��
        // (���͓��t�����擾�p�Ɏ������w�肷�邽�߂��̂܂܎w�肷��)
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // �w��N���̓�����Ԃ�
        return cal.get(Calendar.DATE);

    }

    /**
     * �w��t�H�[�}�b�g�Ŏw����t�f�[�^��\��������������擾���܂��B<br>
     * <br>
     * ���P�[���̓f�t�H���g�ł�(�p��j���g�p����܂�<br>
     *
     * @param format    �t�H�[�}�b�g������
     * @param date      ����
     * @return �w��t�H�[�}�b�g�Ŏw����t�f�[�^��\������������
     * @exception IllegalArgumentException
     *                  �t�H�[�}�b�g�����񂪕s���ȏꍇ�ɔ������܂��B
     */
    public static String format(
            final String format,
            final Date date) {

        // ������ null �̏ꍇ�͗�O
        if (date == null) {

            throw new IllegalArgumentException();

        }

        // �w��t�H�[�}�b�g�̓��t�������Ԃ�
        return format(format, date.getTime());

    }


    /**
     * �w��t�H�[�}�b�g�Ŏw����t�f�[�^��\��������������擾���܂��B<br>
     * <br>
     * ���P�[���̓f�t�H���g�ł�(�p��j���g�p����܂�<br>
     *
     * @param format    �t�H�[�}�b�g������
     * @param date      ����
     * @return �w��t�H�[�}�b�g�Ŏw����t�f�[�^��\������������
     * @exception IllegalArgumentException
     *                  �t�H�[�}�b�g�����񂪕s���ȏꍇ�ɔ������܂��B
     */
    public static String format(
            final String format,
            final long date) {

        // �w��t�H�[�}�b�g�̓��t�������Ԃ�
        return format(format, date, ENGLISH);

    }


    /**
     * �w��t�H�[�}�b�g�A���P�[���Ŏw����t�f�[�^��\��������������擾���܂��B<br>
     *
     * @param format    �t�H�[�}�b�g������
     * @param date      ����
     * @param localeType    ���P�[��(DateUtils.ENGLISH / DateUtils.JAPANESE)
     * @return �w��t�H�[�}�b�g�Ŏw����t�f�[�^��\������������
     * @exception IllegalArgumentException
     *                  �t�H�[�}�b�g�����񂪕s���ȏꍇ�ɔ������܂��B
    */
    public static String format(
            final String format,
            final Date date,
            final int localeType) {

        // ������ null �̏ꍇ�͗�O
        if (date == null) {

            throw new IllegalArgumentException();

        }

        // �w��t�H�[�}�b�g�̓��t�������Ԃ�
        return format(format, date.getTime(), localeType);

    }


    /**
     * �w��t�H�[�}�b�g�Ŏw����t�f�[�^��\��������������擾���܂��B<br>
     *
     * @param format    �t�H�[�}�b�g������
     * @param time      ����
     * @param localeType    ���P�[��(DateUtils.ENGLISH / DateUtils.JAPANESE)
     * @return �w��t�H�[�}�b�g�Ŏw����t�f�[�^��\������������
     * @exception IllegalArgumentException
     *                  �t�H�[�}�b�g�����񂪕s���ȏꍇ�ɔ������܂��B
     */
    public static String format(
            final String format,
            final long time,
            final int localeType) {

        // ������ null �̏ꍇ�͗�O
        if ((format == null)) {

            throw new IllegalArgumentException();

        }


        final StringBuffer retBuf = new StringBuffer(); // �ԋp���t������o�b�t�@
        final Calendar cal = Calendar.getInstance(); // ���t�Z�o�p�J�����_�[
        int indexFormat = 0; // �t�H�[�}�b�g�����񌟍��ʒu


        // ������ݒ肷��
        cal.setTime(new Date(time));


        // ������I�[�܂Ń��[�v
        while (indexFormat < format.length()) {

            final char nowChar = format.charAt(indexFormat); // ���݈ʒu�̕���

            // ���t�t�H�[�}�b�g�g�[�N���ȊO�̏ꍇ
            if (!isDateFormatToken(nowChar)) {

                // ���݂̕�����ԋp���t������o�b�t�@�֒ǉ�����
                retBuf.append(nowChar);

                // ���̕�����
                indexFormat++;
                continue;

            }


            int tokenCount = 1; // �g�[�N����

            // �������t�t�H�[�}�b�g�g�[�N�����A�����Ă�������擾����
            for (int i = indexFormat + 1; i < format.length(); i++, tokenCount++) {

                // �Ⴄ�����̏ꍇ
                if (format.charAt(i) != nowChar) {

                    // ���[�v���f
                    break;

                }

            }

            final int digitYearStr = 3; // ������\�L�ɂȂ錅��
            final int digitRFC822 = 4; // RFC822�^�C���]�[���̌���
            int tempValue; // ��Ɨp���l


            // �g�[�N���ʏ���
            switch (nowChar) {

                // �N
                case 'y':

                    // ������ 3 ���ȏ�̏ꍇ
                    if (tokenCount >= digitYearStr) {

                        // �ԋp������o�b�t�@�֔N4����ǉ�����
                        retBuf.append(appendPadding(
                                String.valueOf(cal.get(Calendar.YEAR)),
                                tokenCount));
                    } else {

                        // �ԋp������o�b�t�@�֔N2����ǉ�����
                        retBuf.append(appendPadding(
                                String.valueOf(cal.get(Calendar.YEAR)).substring(
                                        2),
                                tokenCount));

                    }

                    break;


                // ��
                case 'M':

                    // �ԋp������o�b�t�@�֌��̐��l�\����ǉ�����
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.MONTH) + 1),
                            tokenCount));


                    break;


                // ���ɂ������
                case 'd':

                    // �ԋp������o�b�t�@�֌��ɂ��������ǉ�����
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.DAY_OF_MONTH)),
                            tokenCount));
                    break;


                // �j��
                case 'E':

                    // �ԋp������o�b�t�@�֗j������ǉ�����
                    retBuf.append(WEEK_NAMES_ALL[localeType][cal.get(Calendar.DAY_OF_WEEK) - 1].substring(
                            0,
                            WEEK_NAMES_ALL[localeType][cal.get(Calendar.DAY_OF_WEEK) - 1].length()));
                    break;


                // ����ɂ����鎞 (0 - 23)
                case 'H':

                    // �ԋp������o�b�t�@�ֈ���ɂ����鎞 (0 - 23)��ǉ�����
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.HOUR_OF_DAY)),
                            tokenCount));
                    break;


                // ����ɂ����鎞 (0 - 11)
                case 'K':

                    // �ԋp������o�b�t�@�ֈ���ɂ����鎞 (0 - 11)��ǉ�����
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.HOUR)),
                            tokenCount));
                    break;


                // ��
                case 'm':

                    // �ԋp������o�b�t�@�֕���ǉ�����
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.MINUTE)),
                            tokenCount));
                    break;


                // �b
                case 's':

                    // �ԋp������o�b�t�@�֕b��ǉ�����
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.SECOND)),
                            tokenCount));
                    break;


                // �~���b
                case 'S':

                    // �ԋp������o�b�t�@�փ~���b��ǉ�����
                    retBuf.append(appendPadding(
                            String.valueOf(cal.get(Calendar.MILLISECOND)),
                            tokenCount));
                    break;


                // RFC 822�`���^�C���]�[��
                case 'Z':

                    // �^�C���]�[���I�t�Z�b�g�l���Z�o���܂�
                    tempValue = cal.getTimeZone().getRawOffset()
                                / CONVERT_TIMEZONE_RAW_OFFSET_TO_HOUR;

                    // �ԋp������o�b�t�@�փ^�C���]�[���I�t�Z�b�g��ǉ����܂�
                    retBuf.append(appendPadding(
                            String.valueOf(tempValue),
                            digitRFC822,
                            tempValue < 0 ? '-' : '+'));
                    break;


                // �G���[
                default:

                    throw new IllegalArgumentException();

            }

            // �t�H�[�}�b�g�����񌟍��ʒu�Ɠ��̓e�L�X�g�����ʒu���ړ����܂�
            indexFormat += tokenCount;

        }


        // �쐬�����������ԋp���܂�
        return retBuf.toString();

    }

    /**
     * �w��t�H�[�}�b�g�Ŏw��e�L�X�g���p�[�X�������t���擾���܂��B
     *
     * @param format    �t�H�[�}�b�g������
     * @param text      �p�[�X���܂�������
     * @return �p�[�X�������t
     * @exception IllegalArgumentException
     *                  �t�H�[�}�b�g�����񂪕s���ȏꍇ�ɔ������܂��B
     */
    public static Date parse(
            final String format,
            final String text) {

        // ������ null �̏ꍇ�͗�O
        if ((format == null) || (text == null)) {

            throw new IllegalArgumentException();

        }


        final StringBuffer textBuf = new StringBuffer(text); // �p�[�X������ҏW�o�b�t�@
        final String editText; // �ҏW��e�L�X�g
        int index = 0; // �ҏW�p�C���f�b�N�X


        // �p�[�X������̕��������������܂�
        while (index < textBuf.length()) {

            // ��؂蕶���ȊO�̏ꍇ
            if (!isDateSplitToken(textBuf.charAt(index))) {

                // ���̕�����
                index++;
                continue;

            }

            // ���݈ʒu�̕������폜���܂�
            textBuf.deleteCharAt(index);

        }


        // �ҏW��̃e�L�X�g���擾���܂�
        editText = textBuf.toString();


        // ���l�ȊO�̕����񂪂���ꍇ�͎��s
        if (!StringUtils.isDigit(editText)) {

            throw new IllegalArgumentException(
                    "�p�[�X�Ώە�����ɐ��l�Ƌ�؂蕶���ȊO�̕������܂܂�Ă��܂� [text = " + text + "]");

        }


        final Calendar retCal = Calendar.getInstance(); // �ԋp���t�Z�o�p�J�����_�[
        int indexFormat = 0; // �t�H�[�}�b�g�����񌟍��ʒu
        int indexText = 0; // ���̓e�L�X�g�����ʒu


        // �J�����_�[�̓��t���N���A���܂�
        clearCalendar(retCal);

        // ������I�[�܂Ń��[�v
        while ((indexFormat < format.length())
               && (indexText < editText.length())) {

            final char nowChar = format.charAt(indexFormat); // ���݈ʒu�̕���

            // ���t�t�H�[�}�b�g�g�[�N���ȊO�̏ꍇ
            if (!isDateFormatToken(nowChar)) {

                // ���̕�����
                indexFormat++;
                continue;

            }


            int tokenCount = 1; // �g�[�N����

            // �������t�t�H�[�}�b�g�g�[�N�����A�����Ă�������擾���܂�
            for (int i = indexFormat + 1; i < format.length(); i++, tokenCount++) {

                // �Ⴄ�����̏ꍇ
                if (format.charAt(i) != nowChar) {

                    // ���[�v���f
                    break;

                }

            }


            // �Z�o�p�萔��ݒ肵�܂�
            final int digitYearHalf = 2; // ������̔���
            final int yearThousand = 1000; // ����؎̗p

            int value = 0;
            try {
                // int�l��Ԃ�
                value = Integer.parseInt(editText.substring(
                        indexText,
                        indexText + tokenCount));

            } catch (final NumberFormatException e) {

                // �f�t�H���g�l��ݒ�
                value = 0;

            }

            // �g�[�N���ʏ���
            switch (nowChar) {

                // �N
                case 'y':

                    // ����������̔����ȉ��̏ꍇ
                    if (tokenCount <= digitYearHalf) {

                        final Calendar localCal = Calendar.getInstance();
                        final int year;

                        // ������Z�o���܂�
                        year = (localCal.get(Calendar.YEAR) / yearThousand * yearThousand)
                               + value;

                        // �ԋp���t�ɔN��ݒ肵�܂�
                        retCal.set(Calendar.YEAR, year);

                    } else {

                        // �ԋp���t�ɔN��ݒ肵�܂�
                        retCal.set(Calendar.YEAR, value);

                    }
                    break;


                // ��
                case 'M':

                    // �ԋp���t�Ɍ���ݒ肵�܂�
                    retCal.set(Calendar.MONTH, value - 1);
                    break;


                // ���ɂ������
                case 'd':

                    // �ԋp���t�Ɍ��ɂ��������ݒ肵�܂�
                    retCal.set(Calendar.DAY_OF_MONTH, value);
                    break;


                // ����ɂ����鎞 (0 - 23)
                case 'H':

                    // �ԋp���t�Ɉ���ɂ����鎞��ݒ肵�܂�
                    retCal.set(Calendar.HOUR_OF_DAY, value);
                    break;


                // ����ɂ����鎞 (0 - 11)
                case 'K':

                    // �ԋp���t�Ɉ���ɂ����鎞��ݒ肵�܂�
                    retCal.set(Calendar.HOUR, value);
                    break;


                // ��
                case 'm':

                    // �ԋp���t�ɕ���ݒ肵�܂�
                    retCal.set(Calendar.MINUTE, value);
                    break;


                // �b
                case 's':

                    // �ԋp���t�ɕb��ݒ肵�܂�
                    retCal.set(Calendar.SECOND, value);
                    break;


                // �~���b
                case 'S':

                    // �ԋp���t�Ƀ~���b��ݒ肵�܂�
                    retCal.set(Calendar.MILLISECOND, value);
                    break;


                // �G���[
                default:

                    throw new IllegalArgumentException();

            }

            // �t�H�[�}�b�g�����񌟍��ʒu�Ɠ��̓e�L�X�g�����ʒu���ړ����܂�
            indexFormat += tokenCount;
            indexText += tokenCount;

        }


        // ���t��Ԃ�
        return retCal.getTime();

    }

    /**
     * �w�蕶��������Ƀp�f�B���O�ǉ��������s���܂��B
     *
     * @param str           ���ƂȂ镶����
     * @param digitCount    �\�����܂�����
     * @return �p�f�B���O�ǉ�����������
     */
    private static String appendPadding(
            final String str,
            final int digitCount) {

        return appendPadding(str, digitCount, (char) 0);

    }


    /**
     * �w�蕶��������Ƀp�f�B���O�ǉ��������s���܂��B
     *
     * @param str           ���ƂȂ镶����
     * @param digitCount    �\�����܂�����
     * @param paddingSign   �t�����܂���������
     * @return �p�f�B���O�ǉ�����������
     */
    private static String appendPadding(
            final String str,
            final int digitCount,
            final char paddingSign) {

        final StringBuffer strBuf = new StringBuffer(str); // ������o�b�t�@
        int diffCount = digitCount - str.length(); // �������Z�o


        // ���������̒l�̏ꍇ
        if (diffCount > 0) {

            // �������������[�v���܂�
            for (; diffCount > 0; diffCount--) {

                // �擪�� 0 ��ǉ����܂�
                strBuf.insert(0, '0');

            }

        } else {

            // �����Ȃ�

        }


        // ������t�����܂��ꍇ
        // ���ǉ��敶����o�b�t�@�̐擪���ǉ����悤�Ƃ��Ă��镄���ȊO�̏ꍇ
        if ((paddingSign != 0) && (strBuf.length() > 0)
            && (strBuf.charAt(0) != paddingSign)) {

            // ������擪�֑}�����܂�
            strBuf.insert(0, paddingSign);

        }


        // �ҏW�����������Ԃ�
        return strBuf.toString();

    }

    /**
     * �J�����_�[���N���A���܂��B
     *
     * @param cal �N���A���܂��J�����_�[
     */
    private static void clearCalendar(
            final Calendar cal) {

        // ������ null �̏ꍇ�͗�O
        if (cal == null) {

            throw new IllegalArgumentException();

        }


        // �N���A�l��ݒ肵�܂�
        final int clearYear = 1970; // �N
        final int clearMonth = 0; // ��
        final int clearDate = 1; // ��
        final int clearHour = 0; // ��
        final int clearMinute = 0; // ��
        final int clearSecond = 0; // �b
        final int clearMillisecond = 0; // �~���b

        // �J�����_�[�̓��t���N���A���܂�
        cal.set(Calendar.YEAR, clearYear);
        cal.set(Calendar.MONTH, clearMonth);
        cal.set(Calendar.DATE, clearDate);
        cal.set(Calendar.HOUR_OF_DAY, clearHour);
        cal.set(Calendar.MINUTE, clearMinute);
        cal.set(Calendar.SECOND, clearSecond);
        cal.set(Calendar.MILLISECOND, clearMillisecond);

    }

    /**
     * ���t�t�H�[�}�b�g�g�[�N�����ǂ����𔻒肵�܂��B
     *
     * @param nowChar ��r�Ώە���
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
     * ���t��؂�g�[�N�����ǂ����𔻒肵�܂��B
     *
     * @param nowChar ��r�Ώە���
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
