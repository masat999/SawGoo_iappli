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
/*
 * Ported from the Sun Microsystems FDLIBM C-library.
 * (Freely Distributable Library for Math)
 * ====================================================
 * Copyright (C) 2004 by Sun Microsystems, Inc. All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this
 * software is freely granted, provided that this notice
 * is preserved.
 * ====================================================
 *
 * atan、atan2メソッドにて利用
 */
package jp.co.nttdocomo.star.util;

/**
 *  数値情報操作ユーティリティークラスです。<BR>
 *  逆正接、4 象限表現の逆正接を算出します。<BR>
 *  @since  utilib-1.0
 */
public class MathUtils {

    private static final double zero     = 0.0, one = 1.0, tiny = 1.0e-300,
            huge = 1.0e+300;

    private static final double pi_o_4   = 7.8539816339744827900E-01, /* 0x3FE921FB, 0x54442D18 */
                                         pi_o_2 = 1.5707963267948965580E+00, /* 0x3FF921FB, 0x54442D18 */
                                         pi = 3.1415926535897931160E+00, /* 0x400921FB, 0x54442D18 */
                                         pi_lo = 1.2246467991473531772E-16; /* 0x3CA1A626, 0x33145C07 */

    private static final long   LO_MASK  = 0x00000000ffffffffL;

    private static final int    HI_SHIFT = 32;

    private static final double atanhi[] = {
            4.63647609000806093515e-01, /* atan(0.5)hi 0x3FDDAC67, 0x0561BB4F */
            7.85398163397448278999e-01, /* atan(1.0)hi 0x3FE921FB, 0x54442D18 */
            9.82793723247329054082e-01, /* atan(1.5)hi 0x3FEF730B, 0xD281F69B */
            1.57079632679489655800e+00, /* atan(inf)hi 0x3FF921FB, 0x54442D18 */
                                         };

    private static final double atanlo[] = {
            2.26987774529616870924e-17, /* atan(0.5)lo 0x3C7A2B7F, 0x222F65E2 */
            3.06161699786838301793e-17, /* atan(1.0)lo 0x3C81A626, 0x33145C07 */
            1.39033110312309984516e-17, /* atan(1.5)lo 0x3C700788, 0x7AF0CBBD */
            6.12323399573676603587e-17, /* atan(inf)lo 0x3C91A626, 0x33145C07 */
                                         };

    private static final double aT[]     = {
            3.33333333333329318027e-01, /* 0x3FD55555, 0x5555550D */
            -1.99999999998764832476e-01, /* 0xBFC99999, 0x9998EBC4 */
            1.42857142725034663711e-01, /* 0x3FC24924, 0x920083FF */
            -1.11111104054623557880e-01, /* 0xBFBC71C6, 0xFE231671 */
            9.09088713343650656196e-02, /* 0x3FB745CD, 0xC54C206E */
            -7.69187620504482999495e-02, /* 0xBFB3B0F2, 0xAF749A6D */
            6.66107313738753120669e-02, /* 0x3FB10D66, 0xA0D03D51 */
            -5.83357013379057348645e-02, /* 0xBFADDE2D, 0x52DEFD9A */
            4.97687799461593236017e-02, /* 0x3FA97B4B, 0x24760DEB */
            -3.65315727442169155270e-02, /* 0xBFA2B444, 0x2C6A6C2F */
            1.62858201153657823623e-02, /* 0x3F90AD3A, 0xE322DA11 */
                                         };

    /**
     * インスタンス生成防止。
     */
    private MathUtils() {

        // 処理なし

    }

    /**
     * -Pi/2 から Pi/2 の範囲でアークタンジェントを返します。<br>
     *
     * @param x    適用する値
     * @return xのアークタンジェント
    */
    public static final double atan(
            double x) {
        double w, s1, s2, z;
        int ix, hx, id;

        hx = (int) (Double.doubleToLongBits(x) >>> HI_SHIFT);
        ix = hx & 0x7fffffff;
        if (ix >= 0x44100000) { /* if |x| >= 2^66 */
            if (ix > 0x7ff00000
                || (ix == 0x7ff00000 && ((int) (Double.doubleToLongBits(x) & LO_MASK) != 0)))
                return x + x; /* NaN */
            if (hx > 0)
                return atanhi[3] + atanlo[3];
            else
                return -atanhi[3] - atanlo[3];
        }
        if (ix < 0x3fdc0000) { /* |x| < 0.4375 */
            if (ix < 0x3e200000) { /* |x| < 2^-29 */
                if (huge + x > one)
                    return x; /* raise inexact */
            }
            id = -1;
        } else {
            x = Math.abs(x);
            if (ix < 0x3ff30000) { /* |x| < 1.1875 */
                if (ix < 0x3fe60000) { /* 7/16 <=|x|<11/16 */
                    id = 0;
                    x = (2.0 * x - one) / (2.0 + x);
                } else { /* 11/16<=|x|< 19/16 */
                    id = 1;
                    x = (x - one) / (x + one);
                }
            } else {
                if (ix < 0x40038000) { /* |x| < 2.4375 */
                    id = 2;
                    x = (x - 1.5) / (one + 1.5 * x);
                } else { /* 2.4375 <= |x| < 2^66 */
                    id = 3;
                    x = -1.0 / x;
                }
            }
        }
        /* end of argument reduction */
        z = x * x;
        w = z * z;
        /* break sum from i=0 to 10 aT[i]z**(i+1) into odd and even poly */
        s1 = z
             * (aT[0] + w
                        * (aT[2] + w
                                   * (aT[4] + w
                                              * (aT[6] + w
                                                         * (aT[8] + w * aT[10])))));
        s2 = w * (aT[1] + w * (aT[3] + w * (aT[5] + w * (aT[7] + w * aT[9]))));
        if (id < 0) {
            return x - x * (s1 + s2);
        } else {
            z = atanhi[id] - ((x * (s1 + s2) - atanlo[id]) - x);
            return (hx < 0) ? -z : z;
        }
    }

    /* __ieee754_atan2(y,x)
     * Method :
     *	  1. Reduce y to positive by atan2(y,x)=-atan2(-y,x).
     *	  2. Reduce x to positive by (if x and y are unexceptional):
     *			  ARG (x+iy) = arctan(y/x)		   ... if x > 0,
     *			  ARG (x+iy) = pi - arctan[y/(-x)]   ... if x < 0,
     *
     * Special cases:
     *
     *	  ATAN2((anything), NaN ) is NaN;
     *	  ATAN2(NAN , (anything) ) is NaN;
     *	  ATAN2(+-0, +(anything but NaN)) is +-0  ;
     *	  ATAN2(+-0, -(anything but NaN)) is +-pi ;
     *	  ATAN2(+-(anything but 0 and NaN), 0) is +-pi/2;
     *	  ATAN2(+-(anything but INF and NaN), +INF) is +-0 ;
     *	  ATAN2(+-(anything but INF and NaN), -INF) is +-pi;
     *	  ATAN2(+-INF,+INF ) is +-pi/4 ;
     *	  ATAN2(+-INF,-INF ) is +-3pi/4;
     *	  ATAN2(+-INF, (anything but,0,NaN, and INF)) is +-pi/2;
     *
     * Constants:
     * The hexadecimal values are the intended ones for the following
     * constants. The decimal values may be used, provided that the
     * compiler will convert from decimal to binary accurately enough
     * to produce the hexadecimal values shown.
     */
    /**
     * 直交座標系 (a, b) を極座標系 (r, θ) に変換します。
     * このメソッドは -Pi から Pi の範囲で b/a のアークタンジェントを計算することでθを求めます。 <br>
     *
     * @param x    適用する値
     * @param y    適用する値
     * @return 極座標のθ
    */
    public static final double atan2(
            double y,
            double x) {
        double z;
        int k, m;
        int hx, hy, ix, iy;
        int lx, ly;

        //i0 = (int)((Double.doubleToLongBits(one)) >> (29+HI_SHIFT))^1;
        //i1 = 1-i0;
        hx = (int) (Double.doubleToLongBits(x) >>> HI_SHIFT);
        lx = (int) (Double.doubleToLongBits(x) & LO_MASK);
        hy = (int) (Double.doubleToLongBits(y) >>> HI_SHIFT);
        ly = (int) (Double.doubleToLongBits(y) & LO_MASK);
        ix = hx & 0x7fffffff;
        iy = hy & 0x7fffffff;

        if (((ix | ((lx | -lx) >> 31)) > 0x7ff00000)
            || ((iy | ((ly | -ly) >> 31)) > 0x7ff00000)) { /* x or y is NaN */
            return x + y;
        }

        if ((hx - 0x3ff00000 | lx) == 0) {
            return atan(y); /* x=1.0 */
        }

        m = ((hy >> 31) & 1) | ((hx >> 30) & 2); /* 2*sign(x)+sign(y) */

        /* when y = 0 */
        if ((iy | ly) == 0) {
            switch (m) {
                case 0:
                case 1:
                    return y; /* atan(+-0,+anything)=+-0 */
                case 2:
                    return pi + tiny;/* atan(+0,-anything) = pi */
                case 3:
                    return -pi - tiny;/* atan(-0,-anything) =-pi */
            }
        }
        /* when x = 0 */
        if ((ix | lx) == 0)
            return (hy < 0) ? -pi_o_2 - tiny : pi_o_2 + tiny;

        /* when x is INF */
        if (ix == 0x7ff00000) {
            if (iy == 0x7ff00000) {
                switch (m) {
                    case 0:
                        return pi_o_4 + tiny;/* atan(+INF,+INF) */
                    case 1:
                        return -pi_o_4 - tiny;/* atan(-INF,+INF) */
                    case 2:
                        return 3.0 * pi_o_4 + tiny;/*atan(+INF,-INF)*/
                    case 3:
                        return -3.0 * pi_o_4 - tiny;/*atan(-INF,-INF)*/
                }
            } else {
                switch (m) {
                    case 0:
                        return zero; /* atan(+...,+INF) */
                    case 1:
                        return -zero; /* atan(-...,+INF) */
                    case 2:
                        return pi + tiny; /* atan(+...,-INF) */
                    case 3:
                        return -pi - tiny; /* atan(-...,-INF) */
                }
            }
        }
        /* when y is INF */
        if (iy == 0x7ff00000)
            return (hy < 0) ? -pi_o_2 - tiny : pi_o_2 + tiny;

        /* compute y/x */
        k = (iy - ix) >> 20;
        if (k > 60)
            z = pi_o_2 + 0.5 * pi_lo; /* |y/x| >  2**60 */
        else if (hx < 0 && k < -60)
            z = 0.0; /* |y|/x < -2**60 */
        else
            z = atan(Math.abs(y / x)); /* safe to do y/x */
        switch (m) {
            case 0:
                return z; /* atan(+,+) */
            case 1:
                z = Double.longBitsToDouble(Double.doubleToLongBits(z) ^ 0x80000000); // __HI(z) ^= 0x80000000;
                return z; /* atan(-,+) */
            case 2:
                return pi - (z - pi_lo);/* atan(+,-) */
            default: /* case 3 */
                return (z - pi_lo) - pi;/* atan(-,-) */
        }
    }
}
