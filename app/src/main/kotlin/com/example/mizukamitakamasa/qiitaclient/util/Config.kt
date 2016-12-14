package com.example.mizukamitakamasa.qiitaclient.util

import android.app.Application

/**
 * Created by mizukamitakamasa on 2016/11/13.
 */
class Config {

  fun authUrl(): String {
    return "https://qiita.com/api/v2/oauth/authorize"
  }

  fun clientId(): String {
    return (object : Any() {
      internal var t = 0
      override fun toString(): String {
        val buf = ByteArray(40)
        t = -919088288
        buf[0] = (t.ushr(4)).toByte()
        t = 800621326
        buf[1] = (t.ushr(3)).toByte()
        t = -1328127545
        buf[2] = (t.ushr(9)).toByte()
        t = -860049667
        buf[3] = (t.ushr(22)).toByte()
        t = -420599814
        buf[4] = (t.ushr(21)).toByte()
        t = -1626459362
        buf[5] = (t.ushr(7)).toByte()
        t = -1022119521
        buf[6] = (t.ushr(7)).toByte()
        t = 1663363739
        buf[7] = (t.ushr(20)).toByte()
        t = 459566474
        buf[8] = (t.ushr(23)).toByte()
        t = -590691169
        buf[9] = (t.ushr(9)).toByte()
        t = -856265502
        buf[10] = (t.ushr(12)).toByte()
        t = -657673604
        buf[11] = (t.ushr(7)).toByte()
        t = 456811543
        buf[12] = (t.ushr(8)).toByte()
        t = 2109185084
        buf[13] = (t.ushr(7)).toByte()
        t = -431098605
        buf[14] = (t.ushr(21)).toByte()
        t = -555342091
        buf[15] = (t.ushr(12)).toByte()
        t = 396650927
        buf[16] = (t.ushr(9)).toByte()
        t = 2057165153
        buf[17] = (t.ushr(6)).toByte()
        t = -1761206424
        buf[18] = (t.ushr(12)).toByte()
        t = 1837499437
        buf[19] = (t.ushr(18)).toByte()
        t = -1038038191
        buf[20] = (t.ushr(10)).toByte()
        t = 815355153
        buf[21] = (t.ushr(23)).toByte()
        t = 1606824779
        buf[22] = (t.ushr(12)).toByte()
        t = 1912195488
        buf[23] = (t.ushr(11)).toByte()
        t = -654602138
        buf[24] = (t.ushr(1)).toByte()
        t = 1177751777
        buf[25] = (t.ushr(12)).toByte()
        t = -375779619
        buf[26] = (t.ushr(18)).toByte()
        t = 2134586397
        buf[27] = (t.ushr(11)).toByte()
        t = -349300865
        buf[28] = (t.ushr(19)).toByte()
        t = 1795786234
        buf[29] = (t.ushr(11)).toByte()
        t = 1297013102
        buf[30] = (t.ushr(22)).toByte()
        t = 1147440840
        buf[31] = (t.ushr(16)).toByte()
        t = 359977672
        buf[32] = (t.ushr(1)).toByte()
        t = 1470677530
        buf[33] = (t.ushr(10)).toByte()
        t = 144442886
        buf[34] = (t.ushr(15)).toByte()
        t = 255293848
        buf[35] = (t.ushr(16)).toByte()
        t = 361563796
        buf[36] = (t.ushr(5)).toByte()
        t = -154529516
        buf[37] = (t.ushr(17)).toByte()
        t = 1978767840
        buf[38] = (t.ushr(10)).toByte()
        t = 1126385434
        buf[39] = (t.ushr(5)).toByte()
        return String(buf)
      }
    }.toString())
  }

  fun redirectUrl(): String {
    return (object : Any() {
      internal var t = 0
      override fun toString(): String {
        val buf = ByteArray(29)
        t = -797938933
        buf[0] = (t.ushr(3)).toByte()
        t = -395383604
        buf[1] = (t.ushr(16)).toByte()
        t = 1523063451
        buf[2] = (t.ushr(17)).toByte()
        t = -1688434874
        buf[3] = (t.ushr(14)).toByte()
        t = 1285276162
        buf[4] = (t.ushr(14)).toByte()
        t = -211533101
        buf[5] = (t.ushr(1)).toByte()
        t = -1632724703
        buf[6] = (t.ushr(6)).toByte()
        t = -1032528248
        buf[7] = (t.ushr(17)).toByte()
        t = 1435597031
        buf[8] = (t.ushr(11)).toByte()
        t = -1181053958
        buf[9] = (t.ushr(6)).toByte()
        t = 1131377010
        buf[10] = (t.ushr(8)).toByte()
        t = 1763200380
        buf[11] = (t.ushr(24)).toByte()
        t = -2040891198
        buf[12] = (t.ushr(14)).toByte()
        t = -599442847
        buf[13] = (t.ushr(7)).toByte()
        t = -1113488337
        buf[14] = (t.ushr(10)).toByte()
        t = 813683059
        buf[15] = (t.ushr(3)).toByte()
        t = 1212690844
        buf[16] = (t.ushr(7)).toByte()
        t = 1096533852
        buf[17] = (t.ushr(14)).toByte()
        t = 1832302445
        buf[18] = (t.ushr(24)).toByte()
        t = 1271153556
        buf[19] = (t.ushr(22)).toByte()
        t = 410148917
        buf[20] = (t.ushr(22)).toByte()
        t = -1159272102
        buf[21] = (t.ushr(23)).toByte()
        t = 973314996
        buf[22] = (t.ushr(23)).toByte()
        t = -616134090
        buf[23] = (t.ushr(12)).toByte()
        t = 460270464
        buf[24] = (t.ushr(16)).toByte()
        t = 481165215
        buf[25] = (t.ushr(22)).toByte()
        t = 517101057
        buf[26] = (t.ushr(17)).toByte()
        t = 1866511497
        buf[27] = (t.ushr(21)).toByte()
        t = 1463097698
        buf[28] = (t.ushr(6)).toByte()
        return String(buf)
      }
    }.toString())
  }

  fun scope(): String {
    return (object : Any() {
      internal var t = 0
      override fun toString(): String {
        val buf = ByteArray(22)
        t = -1125782731
        buf[0] = (t.ushr(17)).toByte()
        t = -431183731
        buf[1] = (t.ushr(13)).toByte()
        t = -1284297022
        buf[2] = (t.ushr(1)).toByte()
        t = -1386034833
        buf[3] = (t.ushr(9)).toByte()
        t = 2027511608
        buf[4] = (t.ushr(8)).toByte()
        t = 887246366
        buf[5] = (t.ushr(17)).toByte()
        t = 757177283
        buf[6] = (t.ushr(21)).toByte()
        t = 444513141
        buf[7] = (t.ushr(22)).toByte()
        t = 1560317633
        buf[8] = (t.ushr(22)).toByte()
        t = 1732653197
        buf[9] = (t.ushr(7)).toByte()
        t = 735928525
        buf[10] = (t.ushr(24)).toByte()
        t = 1635244862
        buf[11] = (t.ushr(16)).toByte()
        t = -975692643
        buf[12] = (t.ushr(6)).toByte()
        t = 1518053862
        buf[13] = (t.ushr(22)).toByte()
        t = 1244254374
        buf[14] = (t.ushr(10)).toByte()
        t = 1686937960
        buf[15] = (t.ushr(13)).toByte()
        t = 1026120446
        buf[16] = (t.ushr(3)).toByte()
        t = -608647049
        buf[17] = (t.ushr(15)).toByte()
        t = -351581181
        buf[18] = (t.ushr(11)).toByte()
        t = 584222996
        buf[19] = (t.ushr(17)).toByte()
        t = 914504611
        buf[20] = (t.ushr(3)).toByte()
        t = 522070796
        buf[21] = (t.ushr(3)).toByte()
        return String(buf)
      }
    }.toString())
  }

  fun state(): String {
    return (object : Any() {
      internal var t = 0
      override fun toString(): String {
        val buf = ByteArray(40)
        t = -1160924792
        buf[0] = (t.ushr(2)).toByte()
        t = 2123716804
        buf[1] = (t.ushr(1)).toByte()
        t = -1053019656
        buf[2] = (t.ushr(8)).toByte()
        t = -1685569750
        buf[3] = (t.ushr(23)).toByte()
        t = -447875693
        buf[4] = (t.ushr(14)).toByte()
        t = -355587636
        buf[5] = (t.ushr(14)).toByte()
        t = -424752692
        buf[6] = (t.ushr(21)).toByte()
        t = 763173179
        buf[7] = (t.ushr(6)).toByte()
        t = -419115669
        buf[8] = (t.ushr(21)).toByte()
        t = -1374093770
        buf[9] = (t.ushr(15)).toByte()
        t = -41202603
        buf[10] = (t.ushr(6)).toByte()
        t = -1343468636
        buf[11] = (t.ushr(13)).toByte()
        t = 1930832628
        buf[12] = (t.ushr(19)).toByte()
        t = -688380359
        buf[13] = (t.ushr(5)).toByte()
        t = -584986986
        buf[14] = (t.ushr(11)).toByte()
        t = 2039130072
        buf[15] = (t.ushr(19)).toByte()
        t = -540375435
        buf[16] = (t.ushr(5)).toByte()
        t = 2106874840
        buf[17] = (t.ushr(18)).toByte()
        t = -380554376
        buf[18] = (t.ushr(7)).toByte()
        t = -1415937206
        buf[19] = (t.ushr(15)).toByte()
        t = -1185831591
        buf[20] = (t.ushr(11)).toByte()
        t = -1498105461
        buf[21] = (t.ushr(2)).toByte()
        t = -2070410809
        buf[22] = (t.ushr(15)).toByte()
        t = -356307402
        buf[23] = (t.ushr(17)).toByte()
        t = -980666515
        buf[24] = (t.ushr(8)).toByte()
        t = 293712972
        buf[25] = (t.ushr(11)).toByte()
        t = -1702614825
        buf[26] = (t.ushr(2)).toByte()
        t = 1284373207
        buf[27] = (t.ushr(14)).toByte()
        t = 748310180
        buf[28] = (t.ushr(21)).toByte()
        t = -1644848805
        buf[29] = (t.ushr(6)).toByte()
        t = 1714889296
        buf[30] = (t.ushr(16)).toByte()
        t = -381566482
        buf[31] = (t.ushr(11)).toByte()
        t = -1127643533
        buf[32] = (t.ushr(11)).toByte()
        t = -1444116741
        buf[33] = (t.ushr(13)).toByte()
        t = -1317645162
        buf[34] = (t.ushr(12)).toByte()
        t = -248289470
        buf[35] = (t.ushr(15)).toByte()
        t = -710712843
        buf[36] = (t.ushr(8)).toByte()
        t = 386676130
        buf[37] = (t.ushr(13)).toByte()
        t = 1828296033
        buf[38] = (t.ushr(11)).toByte()
        t = -1373289694
        buf[39] = (t.ushr(3)).toByte()
        return String(buf)
      }
    }.toString())
  }

  fun clientSecret() : String {
    return (object : Any() {
      internal var t = 0
      override fun toString(): String {
        val buf = ByteArray(40)
        t = 2058957780
        buf[0] = (t.ushr(8)).toByte()
        t = 321812354
        buf[1] = (t.ushr(20)).toByte()
        t = 65410484
        buf[2] = (t.ushr(13)).toByte()
        t = 854497972
        buf[3] = (t.ushr(23)).toByte()
        t = -1706619718
        buf[4] = (t.ushr(23)).toByte()
        t = 619106841
        buf[5] = (t.ushr(18)).toByte()
        t = 1808598480
        buf[6] = (t.ushr(14)).toByte()
        t = -630754927
        buf[7] = (t.ushr(17)).toByte()
        t = -965614735
        buf[8] = (t.ushr(21)).toByte()
        t = 805794528
        buf[9] = (t.ushr(24)).toByte()
        t = -1095612513
        buf[10] = (t.ushr(15)).toByte()
        t = 812746437
        buf[11] = (t.ushr(11)).toByte()
        t = 959905050
        buf[12] = (t.ushr(16)).toByte()
        t = -1374100660
        buf[13] = (t.ushr(14)).toByte()
        t = 813891534
        buf[14] = (t.ushr(24)).toByte()
        t = -162940064
        buf[15] = (t.ushr(11)).toByte()
        t = -414443300
        buf[16] = (t.ushr(2)).toByte()
        t = -2134127708
        buf[17] = (t.ushr(17)).toByte()
        t = 371830781
        buf[18] = (t.ushr(11)).toByte()
        t = 718047362
        buf[19] = (t.ushr(17)).toByte()
        t = -1688365
        buf[20] = (t.ushr(13)).toByte()
        t = 855931267
        buf[21] = (t.ushr(20)).toByte()
        t = -1962397304
        buf[22] = (t.ushr(3)).toByte()
        t = 948001901
        buf[23] = (t.ushr(1)).toByte()
        t = -519300821
        buf[24] = (t.ushr(7)).toByte()
        t = -1341213918
        buf[25] = (t.ushr(3)).toByte()
        t = 70291105
        buf[26] = (t.ushr(15)).toByte()
        t = 428675142
        buf[27] = (t.ushr(14)).toByte()
        t = 415360009
        buf[28] = (t.ushr(23)).toByte()
        t = -227676785
        buf[29] = (t.ushr(2)).toByte()
        t = 68939675
        buf[30] = (t.ushr(15)).toByte()
        t = -1491377392
        buf[31] = (t.ushr(8)).toByte()
        t = -435776365
        buf[32] = (t.ushr(13)).toByte()
        t = -1399298353
        buf[33] = (t.ushr(9)).toByte()
        t = -2003246431
        buf[34] = (t.ushr(14)).toByte()
        t = 1632427951
        buf[35] = (t.ushr(14)).toByte()
        t = 401294882
        buf[36] = (t.ushr(5)).toByte()
        t = -356631359
        buf[37] = (t.ushr(2)).toByte()
        t = 1293146005
        buf[38] = (t.ushr(22)).toByte()
        t = 608404010
        buf[39] = (t.ushr(12)).toByte()
        return String(buf)
      }
    }.toString())
  }
}