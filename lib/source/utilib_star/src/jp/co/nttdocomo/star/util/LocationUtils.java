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
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * distanceBetweenメソッドにて利用
 */
package jp.co.nttdocomo.star.util;

/**
 *  位置情報操作ユーティリティークラスです。<BR>
 *  2地点の緯度経度から距離と方位角を算出します。<BR>
 *  @since  utilib-1.0
 */
public class LocationUtils {

    /**
     * インスタンス生成防止。
     */
    private LocationUtils() {

        // 処理なし

    }

    /**
     * 2点間の距離と、その最短ルートの最初と最後の方位を計算します。<BR>
     * 2点間の距離については、距離が短い方を返します。単位はメートルです。<BR>
     * 距離と方位は、WGS84楕円体を用いて計算します。<BR>
     * @param startLatitude  加算先日付データ
     * @param startLongitude  加算する種別
     * @param endLatitude 加算する値
     * @param endLongitude 加算する値
     * @param results 計算された距離はresult[0]に格納されます。<BR>
     *      もし、results配列の要素数が2以上だった場合、最初の方位角がresults[1]に格納されます。<BR>
     *      もし、results配列の要素数が3以上だった場合、最後の方位角がresults[2]に格納されます。<BR>
     * @exception IllegalArgumentException
     *                  フォーマット文字列が不正な場合に発生します。
     *
     */
    public static void distanceBetween(
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude,
            float[] results) {
        // Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
        // using the "Inverse Formula" (section 4)

        if (results == null || results.length < 1) {
            throw new IllegalArgumentException(
                    "results is null or has length < 1");
        }

        int MAXITERS = 20;
        // Convert lat/long to radians
        startLatitude *= Math.PI / 180.0;
        endLatitude *= Math.PI / 180.0;
        startLongitude *= Math.PI / 180.0;
        endLongitude *= Math.PI / 180.0;

        double a = 6378137.0; // WGS84 major axis
        double b = 6356752.3142; // WGS84 semi-major axis
        double f = (a - b) / a;
        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

        double L = endLongitude - startLongitude;
        double A = 0.0;
        double U1 = MathUtils.atan((1.0 - f) * Math.tan(startLatitude));
        double U2 = MathUtils.atan((1.0 - f) * Math.tan(endLatitude));

        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;

        double sigma = 0.0;
        double deltaSigma = 0.0;
        double cosSqAlpha = 0.0;
        double cos2SM = 0.0;
        double cosSigma = 0.0;
        double sinSigma = 0.0;
        double cosLambda = 0.0;
        double sinLambda = 0.0;

        double lambda = L; // initial guess
        for (int iter = 0; iter < MAXITERS; iter++) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
            sinSigma = Math.sqrt(sinSqSigma);
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
            sigma = MathUtils.atan2(sinSigma, cosSigma); // (16)
            double sinAlpha = (sinSigma == 0) ? 0.0 : cosU1cosU2 * sinLambda
                                                      / sinSigma; // (17)
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SM = (cosSqAlpha == 0) ? 0.0 : cosSigma - 2.0 * sinU1sinU2
                                               / cosSqAlpha; // (18)

            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
            A = 1
                + (uSquared / 16384.0)
                * // (3)
                (4096.0 + uSquared
                          * (-768 + uSquared * (320.0 - 175.0 * uSquared)));
            double B = (uSquared / 1024.0) * // (4)
                       (256.0 + uSquared
                                * (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
            double C = (f / 16.0) * cosSqAlpha
                       * (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B
                         * sinSigma
                         * // (6)
                         (cos2SM + (B / 4.0)
                                   * (cosSigma * (-1.0 + 2.0 * cos2SMSq) - (B / 6.0)
                                                                           * cos2SM
                                                                           * (-3.0 + 4.0
                                                                                     * sinSigma
                                                                                     * sinSigma)
                                                                           * (-3.0 + 4.0 * cos2SMSq)));

            lambda = L
                     + (1.0 - C)
                     * f
                     * sinAlpha
                     * (sigma + C
                                * sinSigma
                                * (cos2SM + C * cosSigma
                                            * (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.abs(delta) < 1.0e-12) {
                break;
            }
        }

        float distance = (float) (b * A * (sigma - deltaSigma));
        results[0] = distance;
        if (results.length > 1) {
            float initialBearing = (float) MathUtils.atan2(
                    cosU2 * sinLambda,
                    cosU1 * sinU2 - sinU1 * cosU2 * cosLambda);
            initialBearing *= 180.0 / Math.PI;
            results[1] = initialBearing;
            if (results.length > 2) {
                float finalBearing = (float) MathUtils.atan2(
                        cosU1 * sinLambda,
                        -sinU1 * cosU2 + cosU1 * sinU2 * cosLambda);
                finalBearing *= 180.0 / Math.PI;
                results[2] = finalBearing;
            }
        }
    }
}
