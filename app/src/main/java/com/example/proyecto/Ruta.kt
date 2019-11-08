package com.example.proyecto

import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Build.VERSION_CODES.N
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline

class Ruta(val nombre: String, val sentido: String, val id: Int)
{
    var paradas : ArrayList<LatLng>? = null
    var polilinea: String = ""
    constructor( nombre : String, sentido: String, id: Int, paradas : ArrayList<LatLng> , polilinea: String) : this(nombre, sentido, id) {
        this.polilinea= polilinea
        this.paradas=paradas

    }

    companion object
    {
        val ruta1 = Ruta("Ruta 1", "Antihorario", 0, Paradas.getR1(), "spziChdsqR}GwDyDsCqVqRiAcAo@}@We@a@gAW}@aCwQ_@qAq@}A]k@gIsKT[tQqPdCkCrBeDhAeCx@qCP_A`AwI~Dob@T}Aj@cGHyA?a@GwAIu@a@kBc@mAyAiDIYOiABeAFq@nDkOnAcFl@sC`@{CBm@Fe@FmB?cBGqBSwDAq@FeAHc@Ts@nDoHb@s@r@s@j@a@nE}AdHwBv@S|CeA|@o@d@e@TYd@u@Zs@TcAHs@DaBC{@SoAWy@]u@y@iAiF{F{@oAg@aASq@{L}m@Eg@@{AN}@^eA~@cBv@_BPaAF}@?oAGi@WaA[u@g@s@OOw@i@w@WiBWsBMm@Mi@YSSYk@Kg@Ai@Hg@Rk@`@e@PM~@WzFk@h@S\\U`@o@Ro@B_@CeAG[sAaEKUg@y@y@y@o@[{@UeAOsCWw@Q}@c@k@q@Q[a@eA}AqIc@sCGw@q@cEuAoHWiAYiBm@L{Bp@yBcCwAoAaCgCoBhAcIoBkAe@P}@qB{EuC}HMU@WG_@e@c@Te@Fe@v@m@bBcB{CwB[Yg@w@sAaCwBsEOUQMdAgA|AsBh@oAb@k@hAiA`@k@DEZIp@ALGN]M{@?]DWvBqDFO_@a@SK}@SuBu@uBo@MAy@@OFSNKN_CnHYh@GFQDI?UIcBcAqUwLCECw@Wa@a@OY@k@LM?KCmG{CmCwAgB`Gi@tBO\\aB|C_H|KyBdEk@z@_@r@m@n@CPFTLN^FJ?|A]Xx@x@fDbA|Cl@|Az@fCBXVnAQTKVKb@IBGJGd@Er@?z@Bj@J^VXXvBpAzPBr@AvEqBll@k@rMAt@g@lNkBbf@YbAEj@MbEK~ACNGLSPOAKFEFCN@NDH?t@GlB?n@IfAU`Aw@nBGVOBGJ@PMb@Yl@u@`CaA~BaAlCw@pA}@v@gEdCLVDCNBRAd@M`Ac@DGHYBY~@m@p@u@v@uAv@iBpBaGr@iBFEBG?MJOl@}APk@Lg@Fe@FiAJaEJWAOh@FQdDO~A]~B]xAWz@sAzC{@|B]jBC`ABdADh@Hh@J`@d@xA~EpMl@hBXvBDx@?`AWfD?r@@VJv@Lb@lDjJRp@fCvGt@bClAjD~CdH~DhKpAtDV|@XhBzA`Oh@|DbGhk@T|AT`ATr@n@rAtp@r|@x@vA^dATx@N|@dBxNT`A\bAd@fAPXh@n@nAfAjF`EwOjVwP~ViChBoAlBER?FDLJFH@zVk_@vOkV~ObM~C|BbHxD")
        val ruta1Horario = Ruta("Ruta 1 Horario","Horario",1,Paradas.getR1(),"opziCjdsqRcCqA}CgByDsCaOmL{DwC}BoBo@}@We@a@gAW}@cAiH}@mH_@qAq@}A]k@gIsKiHyJe]ed@]i@Ue@Wq@UcAIk@i@{ESeCiA}JgCqV_ByN[iD_@_CYeAe@sAi@mAyFgOs@_CYu@u@iBSm@_AeB{AqDKq@kCkHQ[eA}CESGo@@uACy@Ds@Lu@n@gCj@mCVeB`@}@PIXDTRDJ?ZMv@O\\a@n@QNy@X[D]AWG[UW]}DiKeB_F]{AI_AAe@DyAN{@p@kBfB}Db@sAT}@TkAN}Al@sMb@iNlAgZX_JH{AP_GDg@`@wKNaFNoCtAea@BqCC{Bu@wK}@mIvAsFVQLOJc@Hk@?MCOKMa@Ii@\\]b@QQGMa@sA}AgEcA}Cy@gDYy@[wBEc@?e@xBeE~G}K`B}CN]n@eCzAcFvIjEv@\\RNJ@RCHE^IT@HDNJHTBPCR@NBDzIxEpFpCf@ZpB~@zA|@PPPJHPDRYpDe@vD?XF`@JRJJh@Rj@NPLNTvBrErA`Cf@v@ZXzCvBcBbBw@l@Gd@Ud@d@b@F^AVLTrAlD`AnCpBzEQ|@jAd@bInBnBiA`CfCvAnAxBbCrCy@z@dFt@zDj@zB`@pCFVNPb@rC|ApI`@dAPZj@p@|@b@v@PrCVdANz@Tn@Zx@x@f@x@JTrA`EFZBn@Ct@Sn@a@n@]Ti@R{Fj@_AVQLSTMNGNKZIf@@h@Jf@Xj@RRh@Xl@LrBLhBVv@V`@Vd@`@f@r@Zt@V`AFh@?nAG|@Q`Aw@~A_AbB_@dAO|@AzADf@hBrJrAlG~B~LfCtLHVf@`Az@nAhFzFx@hA\t@Vx@Nv@FrAE`BIr@UbA[r@e@t@UXe@d@}@n@}CdAw@ReHvB}Bv@qAd@k@`@s@r@c@r@yCfGUf@Ur@Ib@GdA@p@RvDFpB?bBGlBGd@Cl@a@zCm@rCiAnEEVwCxLWlAGp@CdANhAHXxAhDb@lA`@jBLvABvAIxAk@bGU|A_Enb@eAfJMn@y@pCiAdCs@lA_AvAeCjCeFtEq@p@}EjE_B|Ag@l@`IlKx@vA^dATx@N|@f@pE|@fHT`A\bAd@fAPXl@r@jAbAjF`EuAzBaMnRwP~ViChBoAlBER@LBFFDLBz@mAfC}DvP_W`MoRtA{BhClBtKtI~C|B~CfB`CnA")
        val ruta2 = Ruta("Ruta2", "Lienzo Charro",2,Paradas.getR2(),"gj`jCl|bqRn@@VE|@iAVULXp@bAd@j@lAhARJh@b@NRFXJPbAzA`FM|@lIZPr@r@xCnDjBlBMsAS_AxAhAj@l@fBzA`@{@FWEq@[c@i@e@e@[aAW[SU[CKE}BKcAKkB}AgAeCFWgC@[g@[wAq@yCkBuDsBQMKOEa@xCaCzAuAj@SRMfB|@|FjDz@h@z@t@PTZr@fAnDXTfAb@KVKb@?^BLFHRJZG~@{@VQLOJc@Hk@C]KMa@Ii@\\]b@QQGMa@sA}AgEcA}Cy@gDYy@[wBEc@?e@xBeE~G}K`B}CN]n@eCzAcFvIjEv@\\RNJ@RCHE^IT@HDNJHTBPCR@NBDzIxEpFpCf@ZpB~@zA|@PPPJHPDRAPKN[L]CYQGIMe@E]EoLBcDCu@Ow@hBKX@h@HtAHjA@^ADCBIAYEc@]kBIiBFyBb@gBTsA?q@SsARIPCp@HL\\Nj@FbCHbAM`@l@LbC\\`@L^BTCZSd@aAdBf@?TVAAsHHaAXkBpA}FZoBdBiJn@kCx@gF@u@Gc@]aEOo@KSa@i@GCoGiFMSWm@GWEg@Ag@Fg@jAaFv@sDHq@@a@EeA@OGaAQc@o@s@a@o@i@wAI_@G_@Ac@Jw@Bk@Ck@oAwGEc@@{ALiE^oH@cBF?BEBuBTqEQCmA?GEEIKs@[g@k@[gBm@o@OM_AFm@f@qAjBwDzAmC^k@xAmBpBwCt@kAjBmD`BwDVq@He@FQpF_NtAcCf@u@bAqAlAqBd@qATmAF{@Bm@AcBCa@?kBBqAFmAVeB`AkFv@aFv@sDLe@nDmIZqBDuAAa@UuBK{G?}@B[d@qBfBwFf@iBf@iDrDeU~CsSn@yDr@uG`@mCxAuI`DdA~Ad@hAb@bD~@t@PLF^sAXyAJeABeBC{AKwAKi@[{Ai@mBsBaJyA{FqE{RmCuK]_CGy@Ce@C{LC{@KwAO}@Aa@H{F@uCOib@EsVTM@WGmN@gHEwB?qC}@ADxG?xQBhFBr@R`C@vA?pLLt_@?hCGfE?hEDx@?dCIvH@n@TlBbB`JfB|GdAzEPEgA}EOFdAzEtC~LbDxM~AbHVfBDh@BlAAjAGfA}@|Du@QcD_AiAc@_Be@iFgBeCs@EdEIhDc@nC{DrPwBvJ]nBGLYbAUlAiA|EaAbF_@zA}@M{@GW@aEbCmAf@o@~IApAVPBXJh@L^`@x@X\\NVr@j@~CdB~@dAb@p@Th@Nh@Lv@BzACp@Gh@e@jBi@pAc@z@]|@}@jCSv@{@tE}AnHg@tCK|@GvBDzB@bDMvAMh@Yz@g@`AoBpCoApBm@jAsCxGeAjCo@b@w@xBk@tAcAtBi@l@Yb@uAbBgAxBo@hAgCbEuClFa@lAQb@OTa@dAVVZTz@f@z@Z~Ad@^FLJJNHZJr@LJZDn@BSlFCbBQzCO|EM`CEtA@zAVjAx@rEFj@?p@KbA@`@R~@Pd@Xn@fAfBLz@GL?VFZB@N|@@VIv@YbB_BjHIl@Ah@Jv@Nb@R\\bA~@tAbAhA`Az@l@X\\JRNb@\\`G?l@QlAQp@WZO\\IlBk@`DeAjFMDYPcAt@qA^S@c@d@m@TcAnBmA~@a@VEHKd@KtAI`@KNa@TMLEROJFbCHbAM`@]l@Wl@GZ?\\ZbBPj@l@vAL`@J|@@b@C`@[nAk@~Ak@QMAy@@UJMJKNmArDq@zBYh@GFQDI?UImC}Aq@[uRaKCECw@Wa@a@OY@k@LM?KCmG{CmCwAaBpFo@dCO\\aB|C_H|KyBdEk@z@_@r@m@n@CPFTLN^FJ?|A]Xx@x@fDbA|Cl@|Az@fCNdAKN]LWIk@c@aA_D[s@QU_@][W{@i@yCeBeAq@eCqASLk@R{AtAyC`CD`@JNPLtDrBxCjBvAp@f@ZAZVfCfAEFhFAvE}AkBs@s@[Q}@mIwCF]_@sAeAo@[OSi@c@SKw@s@{@aAq@cAMYWT}@hAWDo@A")
        val ruta3 = Ruta("Ruta 3", "Zacatecas- Bachilleres Gdpe",3,Paradas.getR3(), "sn~iC~tzpRMEMOEI?SjBsDTk@\\uAJ}@@s@K}@WmAIOs@{DBa@JQtAg@lBcAzBsAtFsC`DkBnFyCPUb@_@JC^ArB[h@CdBQ`A?~Ef@V@fATnBRj@P|DPfEZFDJXDXm@jBSz@c@nAYj@eAvAUb@EV?XFf@i@MkA}@OGMAo@Cg@@kC|@q@XWBeDe@yD_AWPkAlAQJ[JUBkEBF`BbBExKGdGMRCFEPCbAYx@EP@j@LnAl@FFr@Zj@\\Z\\GXo@`AITOt@[z@gDfIyCbHYv@uAdD_@hA]x@Ql@e@~@g@lAu@`C_A~Bj@p@aD~Fe@TkAVx@vIoG\\gDl@u@V{@b@SVGVANDt@UDCPQX_Av@iD~BUJ_@x@M|@C~E@Z\\jA|ChBt@f@`@h@Xn@Lb@XZh@T~Cn@fEtAz@\\}@|@q@|ALFUj@IZCTAd@HrDBTInAM~@WrAw@pCoA~D[z@Mf@M|@_@pEUr@A^_@vAAn@F`@@pABd@mBzIsA~DkAdEIh@E|@Bx@XvANh@~BdFx@vAp@v@r@j@bB|@hBx@t@d@l@d@r@r@x@vARz@FhAAv@LzTEpAKbA[x@QTQLyC~@yBnAe@\\_@R_@Na@Jm@HiEDcAAgCo@[EO?c@Fa@b@OXGb@GrCCZ[~@SVe@^s@TSD{DVoAf@_@Zi@\\cAlAUp@Ah@FRVNZG~@{@VQLOJi@ZO`@a@XKd@K~AK`@GHCZUZI^Qx@}@R_@JU`@qAVo@Ve@Z_@v@m@RIp@K`@CjEAp@Cn@E`@Gd@MrAw@~AkAXOr@[vA]VKNM^s@Ng@Fa@FaABoA?}@ImAEoLBcDCu@E[Qu@O]U_@s@}@c@a@i@_@}@c@oAs@iBu@y@i@i@k@[g@cAoBkAkCe@uAWwABqALeA`CwHz@eD~@kEZ{BD_BXi@jAwCTQl@QXQNg@BWCg@GUwA}BSm@?QdA{DNU\\u@L_@d@sBJaAFiA@s@O{DFe@`@mAXm@Vc@\\]Jk@MK{@]gEuA_Do@i@UY[Mc@Yo@a@i@u@g@}CiB]kAA[B_FL}@^y@TKhD_C~@w@PYBQTEEu@@OFWRWz@c@t@WfDm@nG]u@aI`Be@NIfBgD`AaBKIW]HU|AcE\\s@~@eCVi@~@eCtBgFdCiHxBcF|BkG\\s@@Ym@Mk@]s@[GGoAm@k@MQAy@DcAXQBGDSBeGLyKFcBDGaBjECTCZKPKjAmAVQxD~@dDd@VCp@YjC}@f@A|@DNFjA|@h@LVb@HAYu@CYDc@L[jA{Ab@{@`@mAPu@\\eA\\qAF?LELMBIAWGIGCQ?MDmLm@m@GS@[FeAM{AWuD[kAG]?sCVe@HmBRYHm@n@aHrD{HjEuAr@sAx@cBz@k@T{A^UNbBhIJbA?ZIv@Ur@uAnC{@`@qCd@SI]HIVDVTLJPG^@h@")
        val ruta4 = Ruta("Ruta 4", "N", 4, Paradas.getR4(), "of}iCff_qR}@lJ?`@UvBUdBMn@@^gA|Dt@f@JRnAzEbBDTBl@Jn@Xn@d@Xp@DZp@H\\hAFbCHbAM`@]l@Wl@GZ?\\ZbBPj@l@vAL`@J|@@b@C`@WdAo@hBk@Q_@Ag@@UJMJKNKVsBvGYh@GFQDSCyCcBq@[uRaKCECw@Wa@a@OY@k@LM?KCmG{CPkAA[L{AXuB_A?_AEQhB[nA[t@s@|ByApFO\\aB|C_H|KyBdEk@z@_@r@m@n@CPFTLN^FJ?|A]Xx@x@fDbA|Cl@|Az@fCD`@TfAQTUp@Ah@BLFHRJRCNIv@u@\\WFIJi@ZO`@a@XKnFbAL?b@MRAlEx@`Dd@tJdBQn@k@fAGFrAnBd@j@f@~@z@pAx@xAPz@x@`Gx@bDNfAc@Ts@Vk@Ne@D_A@wASiBa@eBWWUgDwDSQm@[oAl@y@~@Q^Ed@iAzC{@lDy@xBGZ?V@XNl@xC|FZR`CRg@pIhAHf@?~AKT?|@KRGZO|DqCtBcBb@d@h@`@|@d@VFlAN`A?hAQdAa@bCgBz@a@f@QfAUlAQLO`AMXMvAgBVWf@f@Td@l@hBD`@nCsAxAc@?KHOPCbAiALIl@s@b@s@jAiDVmAdGIjB?t@zDj@zBl@a@nAcAvAu@jA_@hCg@`A@pBZ~@XVXbB~@`AV`@Ht@Fl@Ab@Gv@Wb@Uh@e@n@}@Z}@Lw@Bq@A{@Gw@vGNBNNPJ@HAPMBIPAZOZWb@U|JwGLGH?FCFKASGGSAKJ?FO?wGnEaCzAKTKF]Do@EKDi@GaBE_D?s@DLpCGz@Qr@Ub@UZ[Za@X_@PWFc@Da@?{@Ka@K{Bu@MAS@qAo@m@QwDYq@@}Cz@eA`@cAf@_@GUKOSu@eEWiAYiBm@LuDfA}@CIRO|@C`@Kn@WlAkAhDc@r@m@r@MHcAhAQBIN?JyAb@oCrAEa@m@iBUe@g@g@WVwAfBYLk@HUBSEwBXw@ToAl@iBtAy@d@e@Ng@Hs@Ds@C_AO]Mi@_@eA_A[e@gGnEmB|AOJi@T_BJg@?iAI}Ei@|BgHLg@QIWYsBcE[s@Ic@A]BQb@aAPg@t@{Cp@uBt@gATMdA{@~@e@TYBWRPfDvDVTdBVhB`@vAR~@Ad@Ej@Or@Wb@UOgAy@cDy@aGQ{@y@yA{@qAg@_Ae@k@sAoBT]d@aA^{AyO{C}Cs@e@|CwDk@eCg@e@AEC[DOF[VN_A?MGUGGUGKAi@\\]b@QQGMa@sA}AgEcA}Cy@gD[_AYqBEc@?e@xBeE~G}K`B}CN]n@eCzAcFvIjEv@\\RNJ@RCHE^IT@HDNJHTBPCR@NBDzIxEpFpCf@ZpB~@zA|@f@b@J^APKN[L]CYQGIMe@E]EoLBcDCu@Ow@hBKX@h@HtAHjA@^ADCBIAYEc@]kBIiBFyBb@gBNw@Fq@A[SsAKWU]s@o@]Qm@OQAa@e@k@AoA{EKSu@g@fA}DA_@Lo@TeBTwBL_@Jm@Hu@dAmHa@KIl@")
        val ruta5 = Ruta("Ruta 5","N", 5, Paradas.getR5(), "}vziCxlhqRe@aA{@wB}BcFWgAo@gBm@cAoAuCgDiIfDwF{@w@}@cAkAuBKWc@gBODWeCi@qIIsEAeE_EZs@AQE\\uDHmB~@gOHEPsAVaA\\k@TYROAYEOIIQISVg@~@e@tAKp@?NFPUvCk@~JQA{DTkAL@qDoH`@@oCVsBJkBD_BmA\\oCrA?z@Kz@M|CWzBUf@}AwAk@f@u@\\c@NeATuIZi@E]Ie@W][Ua@e@{Ay@eBGYYE]Mi@_@eA_AbCgBvHiFfGwEz@uBZkAD[@u@kACWk@UWc@Ya@yAWm@i@_AlAcAvDoCx@s@rBpCjAd@bB}Hf@qC~@_EFIOUyAiAWI{AsAwDuB{CwB[Yg@w@sAaCwBsEOUQMdAgA|AsBh@oAb@k@hAiA`@k@DEZIp@ALGN]M{@?]DWhB{CTe@_@a@SK}@SuBu@uBo@MAy@@UJMJKNmArDq@zBYh@GFQDI?UIcBcAsBeAsA}EKg@q@gBMUgDiEeBkC_BsBJUU]w@gAiAkAEAO@aB~Ak@?yAJ}BAiCOs@I[Ae@FQAyB{@m@KoCAa@C]OEGuAgAUOMCMFOX[`@eJcD{Bq@m@JS@mACaC[kCg@Wc@e@o@iCkB_B}AcA|@@NLRyAvAY^MHG@UQk@Ke@Au@DoAT{AL{CBkDgAwFqB_@KJEGOAMDqCCwBrCwEdAsAeArAsCvEBvBEpC@LFNnAa@v@[hEaCVEl@CF@b@A`@F\\Ml@GfAQb@AHDlBlB|BfFjAzA`@Lh@e@t@LVRl@RX@`BCjAH~Cj@`CZDFdATj@R\\TpBfAxB|AtBlAIb@@NHPf@j@zBlBhArAvJfIl@^N]n@eCzAcFvIjEv@\\RNJ@RCHE^IT@HDNJHTBPCR@NBDzIxEpFpCf@ZpB~@zA|@PPPJHPDRAPW~Ce@vD?XBRBLJRJJh@Rj@NPLNTvBrErA`Cf@v@JJjDdCvDtBzArAFNpBbB_A~Dg@pCcB|HkAe@sBqCy@r@wDnCmAbAh@~@Vl@`@xAb@XTVVj@jABAt@EZ[jA{@tBgGvE{@M_@RiFnDeDbCZf@b@d@h@`@|@d@VFFNRVZl@x@dC^f@`@\\r@Xb@H|AEjAIdEKdHi@Tg@V{BL}CJ{@?{@nCsAlA]E~AKjBWrBAnCIfDvHWjAMzDUP@ItB]`EMr@[hA]~@i@~@_ArA}AfBuEvFYViAx@gAf@_AZcBb@_LhCc@NMBYAe@FkA\\{@Pw@Vi@VMHa@b@r@`Aj@i@p@g@|@a@fBm@TMPUlOoDbBi@bAe@`Ao@n@k@fGmHl@o@hA}Ap@wAPi@d@uBPDr@@~D[@dEHrEh@pIVdCNEb@fBJVjAtB|@bAz@v@gDvFfDhInAtCl@bAn@fBVfA|BbFz@vBd@`AKD")
        val ruta6= Ruta("Ruta 6", "N",6,Paradas.getR6(), "{muiCz}upRiGjOWb@IFIBUKO?GFkBbEoAdCmBpEOTiA`@oJa@cAAiFTwEH}AJSDKa@[t@QRe@^m@XiElAwAVOuWEmCEs@Y}B[oAs@cEw@DnA`GT`BPbBJzJHzP{Af@iAX}@NmAXa@FcBHuDMKwGuK^G}AE}BAqCQsKGOMG]EgEyAeEqA_FqAyDMNw@LW`@k@IKe@j@GNGPGx@IRORUb@UxAIXKNUDwD?{@HuAh@_CpAy@^[TWn@Mn@OpCc@dFWjA}BxE{@|A}@dAULOKCe@@QH[t@_BlAyCj@oDRcBp@qEd@iCPwATm@XaBNo@HGBIl@eEl@yC?Q~BqMkDiAmBjH[y@CQy@{BSs@q@eB[e@SCkCeC_AcA}@{@c@YISMOc@]c@e@kAeA_BcB}@w@sHuH[a@CK]i@_@y@qAoByBsCw@kAQOuDoFIGgDsEo@y@}@_A}@g@_EkAoBe@q@WeZeIkFsAmKyCgHgBwAc@iIwBa@GqDw@cC[}KuBe@CMDcKgBuGqAgAQcFiAwIuAu@SkFgAjFfAt@RvItAbFhAfAPtGpAbKfBHHlAZjANjEt@fJdBf@Lb@FlEbAvOhEeBtFk@tBcA~CmBpGc@nA]rAyAzE_@tAtLrExDpAF@RAb@LxBx@lA^dCdAh@P`AVdA\\n@Nn@yA~@yA`DhAdDrAjAp@pAl@vCnBd@Tf@@^J`ClA\\FtBOrBl@d@_BnEuFNYL[D[zCgIbCdDx@|@rAlBHDZ`@rHtH|@v@~AbBjAdAb@d@b@\\LNHRb@XvEvEr@l@?Db@h@dAzCz@xBJJZx@o@vDjDz@?Jo@~Ca@vDDZUdAYEOfB@PE`@s@dEq@pESbBk@nDmAxCu@~AWRELCV@ZGBGL@VDHHFF@NAJKFBTCZM^UhA{@vEaE^c@Lo@@q@Gc@YkAKo@Ay@Dk@DuCD]Ha@Na@Vc@X[lAy@lAq@f@Qh@I`F@XCNUL[JgAf@aBD[xDL~EpAdEpAfExAf@HJRPrK@pCD|BF|AtK_@JvGZB`CHzBIlDq@hAYzAg@HxJp@@KyJtB]zBw@vAc@r@i@p@aARE|AKvEIhFUbA@nJ`@hAa@NUlBqEnAeCjBcEFGN?TJHCHGVc@fGgO")
        val ruta7= Ruta("Ruta 7", "CTM- Teleferico", 7, Paradas.getR9(),"kgziCfs_qRqEW}Go@}@Eo@Hc@C@YmCSuEWaGK@yEBgBCQYABrOCnFCz@w@jF[dCG|AJhAXx@Xj@n@r@dAx@vA`Ab@`@RVLVVr@Lv@Bb@YxI@`AHjAZlB|AbHP`APj@t@dB^l@XZ~@r@hAd@xCfAbAn@NL\\f@j@nALbA?n@Ef@Ql@_@z@g@t@{B`CaAz@s@hAYt@W~AGrAB^A@Fj@Tx@r@pAdEbFPl@vAdB|@lA`AvBf@xBZrCNHF@JAPMSaCk@mCk@yA_@u@e@q@iAuAk@o@e@a@IGUIyAsBmAwAm@kASy@Gi@A_@FmAV{A`@{@p@_A|BuBv@o@v@yA`@qADi@@s@Ce@Ga@i@uA[i@_@_@s@c@mFwBa@W}@u@a@m@m@_BYkA?SKu@YaBa@cBWwACY?]KaC@qBTqBZqBA_AEe@Qs@{@mAKIk@V{@LcDJqFBwCDsFNmEFkB@m@AAjJCt@_AfHSf@Y^K@eAZ}@SuBu@uBo@MAy@@UJMJKNmArDq@zBYh@GFQDI?UImC}Aq@[uRaKCECw@Wa@a@OY@k@LM?KCmG{CmCwAaBpFo@dCO\\aB|C_H|KyBdEk@z@_@r@m@n@CPFTLN^FJ?|A]Xx@x@fDbA|Cl@|Az@fCD`@TfAQTKVKb@IBGJGd@Er@?z@Bj@J^VXXvBn@~I`@zEBr@AvEu@fSKTWjA_@bAS`@gArA}BtBqAAq@G}F{H?UOc@UQOES?KFEL?RL^HHPH\\FtDtEzA|BN^ZdADl@?xAIbAQz@u@nCk@bCc@~ASfAKxBWnDY|AUj@e@v@sBjCuDpEaDzE}LrOeBbCoCnDc@Z_@L{@HwA@]VMT?ZBNJPNJVJz@R`@dANn@Hh@JZLX`@d@ZX`@NXDv@?\\EdBm@j@NKT[`@mB~CaBtC{@`BqAnCWfAWtACdCDvA^bBf@vAr@nArAnAbAh@bA`@n@Nt@JhADpBMp@On@Sj@WbKkGx@aAn@iAv@iBvAkElA_DFEBG?MJOl@}A^sAFe@FiAJaEJWASGKGCM@e@GkDcAoDm@q@EqACk@B}AToB^sFv@_@@[DMKKAI@QLGNcDjAg@Lw@Ba@I]S[YOSWy@Ic@Ai@J[FI\\Ux@Yx@g@dAqArCyD`CoCxAeCzBaCj@}@pA_Ep@qAfAcBvIgLj@uALo@Hg@TuDTkCf@cCl@{B`AsEHs@@oAj@u@tAmA^QJDF?LEDEDQb@a@`@IXATEZMV_@z@mCDU@[MUWYk@eAUiALmCb@_NBmEIuBo@aJ}@mIvAsFVQLOJc@Hk@?MCOKMa@Ii@\\]b@QQGMa@sA}AgEcA}Cy@gDYy@[wBEc@?e@xBeE~G}K`B}CN]n@eCzAcFvIjEv@\\RNJ@RCHE^IT@HDNJHTBPCR@NBDzIxEpFpCf@ZpB~@zA|@PPPJHPDRAPW~Ce@vD?XF`@JRJJh@Rj@NdAgA|AsBh@oAb@k@hAiA`@k@DEZIp@ALGN]M{@?]DWhB{Cd@}@b@a@j@eADO|@oHD}@?kIX@hJKbBC|AGjMQDBz@Bb@J^TRPf@lA?`ASlFBhCL`CR`A^nAFnAVlAL^Vf@ZPPj@t@dB^l@XZ~@r@hAd@xCfAbAn@NL\\f@j@nAF\\Dd@?n@Ef@Ql@_@z@g@t@{B`CaAz@s@hAYt@W~AGrAB^A@Fj@Tx@r@pAdEbFPl@vAdB|@lA`AvBf@xBZrCNHF@JAPMSaCk@mCk@yA_@u@e@q@iAuAk@o@e@a@IGUIyAsBmAwAa@u@Sg@Kg@Gi@A_@FmAV{AFOXk@p@_A|BuBv@o@v@yA`@qADi@@s@Ce@Ga@Sk@Ui@[i@_@_@s@c@mFwBaAq@][a@m@m@_BMc@Kg@Ci@a@aCa@cBWwAOyD@qBTqBzAgAxD}BmAiCsAgCiBjAM?aAf@kA}@gAq@wAcAg@k@_@{@Q}@Aw@TmD?KEKr@mEFw@BiB@_J`GJtEVlCRAXb@Bn@I|@D|Gn@rEV")
        val ruta8= Ruta("Ruta 8", "Central-",8,Paradas.getR8(), "y_bjCzk~pRW|GM|F?n@Bh@F`@Lb@h@v@ANtAd@lATP@RFP^JJTJbBXpCPl@PLHnCb@l@Xj@`@hEtE~@jArAdA`BhA|AhBf@^dAfBDBJC`AbAcA|@?HXl@j@jBJN`@TZHX@`BC~ALjCf@`CZDFdATj@R\\TpBfAxB|AtBlAIb@@NHPf@j@zBlBhArAvJfIl@^N]n@eCzAcFvIjEv@\\RNJ@RCHE^IT@HDNJHTBPCR@NBDzIxEpFpCf@ZpB~@zA|@PPPJHPDRAPW~Ce@vD?XF`@JRJJfA^LBdAgA|AsBh@oAb@k@hAiAf@q@ZIp@ALGN]M{@?]DWhB{Cd@}@b@a@j@eADO|@oHD}@?kIX@hJKbBC|AGjMQDBz@Bb@J^TRPf@lA?`ASlFBhCL`CR`A^nAFnAVlAL^Vf@ZPPj@t@dB^l@XZ~@r@hAd@xCfAp@`@`@Z\\f@j@nALbA?n@Ef@Ql@_@z@g@t@{B`CaAz@s@hAYt@W~AGrAB^A@Fj@Tx@r@pAdEbFPl@vAdB|@lAj@jATj@f@xBh@`Ft@AfGLN@BNNPL@NEHIBIPAZOZWb@U|JwGLGH?FCFK?ILKnEiCvLaIJAFG@GTGjCkBdGuD^PrERxBT_@jEQx@OnA_@xEkDlMI^YfC_@|B^}BXgCH_@jDmM^yENoAPy@^kEyBUsES_@QU]]LkBrAcEdCoAz@EHI?GF?BiBfAqDdCcDlBoEvCCPQAEBEF?FO?wGnEaCzAKTKF]Do@EKDi@GaBEcC?_@]WuCWqAS{@Sk@w@cBe@q@uBeCo@i@UIyAsBmAwAm@kASy@Gi@A_@FmAV{A`@{@p@_A|BuBv@o@v@yA`@qADi@@s@Ce@Ga@i@uA[i@_@_@s@c@mFwBa@W}@u@a@m@m@_BYkA?SKu@YaBa@cBWwACY?]KaC@qBTqBZqBA_AEe@Qs@{@mAKIk@V{@LcDJqFBwCDsFNmEFkB@m@AAjJCt@_AfHSf@Y^K@eAZ}@SuBu@uBo@MAy@@OFSNKNmArDq@zBALe@pBAPKN[L]CYQGIMe@E]EoLIMCiA@{@Eq@Iq@EMW[eCmByAc@mAUcAYoAm@EAO@YKuA}@mAeBiAkAEAO@aB~Ak@?yAJ}BAiCOs@I[Ak@FKAyB{@m@KoCAa@C]OEGuAgAUOMCMFOX[`@eJcD{Bq@m@JS@mACaC[kCg@Wc@e@o@iCkBaA}@_BcBcAuBg@g@{AmBaCaBWIw@{@i@gAaCqDc@u@[Ws@]aAQgCIiAK{@OkBO}@OmAYQMaCo@_@Oi@w@Mc@Ga@Ci@?o@L}FXqH")
        val ruta9= Ruta("Ruta 9", "N", 9,Paradas.getR9(), "gpajCts`qRJITCfADrAZt@LzAHxAPV@HC`@e@`@s@r@i@XKbAUr@ArCy@p@LN[Rw@HDh@e@t@LVRl@RX@`BCjAH~Cj@`CZDFdATj@R\\TpBfAxB|AtBlAIb@@NHPf@j@zBlBhArAvJfIl@^aB|C_H|KyBdEk@z@_@r@m@n@CPFTLN^FJ?|A]Xx@x@fDbA|Cl@|Az@fCD`@TfAQTUp@Ah@FRPLD@RCNIv@u@\\WFIJi@ZO`@a@XKnFbAL?j@OJ?n@LZuBDiBEm@?YBSFSz@mBFUd@_@PMRIp@K`@CjEAp@Cn@E`@Gd@MrAw@~AkAXOr@[vA]VKNM^s@Ng@Fa@FaABoA?}@ImAEoLBcDCu@E[Qu@O]U_@s@}@c@a@i@_@}@c@oAs@iBu@y@i@i@k@[g@cAoBkAkCe@uAWwABqALeA`CwHz@eD~@kEZ{BD_BXi@jAwCTQl@QXQNg@BWCg@GUwA}BIGQEK?KBMHUtA_@pEs@`CW|AOf@]n@i@nA[~AGn@EjBOdAI`@Od@w@bB{@~Ag@t@wD~EaAdAKPcAz@sAt@eD|Aa@Zm@^GNYRm@RoCAa@C]OEGuAgAUOMCMFOX[`@eJcD{Bq@m@JS@mACaC[kCg@Wc@e@o@iCkB_B}AcA|@@NLRyAvAY^MHG@UQSEWEe@Ay@Fi@N{BX}CCkDgAwFqBuBi@qC}@CFUNiBbAQNWZKVQp@Ad@BbAZtGNpBJXX\\`@ZXHL@j@?hEg@DMFG`@QBSAg@BKTO@UEY?YDMPK")
        val ruta11= Ruta("Ruta 11 Alberca", "Ruta 11 Alberca", 10, Paradas.getR11(), "g~~iCbjtpRpVtTbIxHTNbCrBtE~DjCpCpEhGp@bA|CbEfAfDpBbHTd@ONWb@}GtOSp@Ml@R@TArPw@dCQxFW~CSbEMlFgBh@dAj@t@dBlA`A^\\P`@\\j@v@`@`AHVJj@@r@[xEK^uAhDm@bBcAnDuAzGITKNOPc@NQBy@QkAMgEFSCWBoAzEkA`EUbAANOBOPCPBJ@XEVo@pBI`@Y~@Sh@Yj@eAvAUb@EV@d@DZVb@ZxABVAh@]Ik@]s@[GGcCzAaBp@UBIAm@HyBDSBcA\\[@wI]yDG?hA^DxGR|BNEVU`@ULkAVc@V[^[f@ELFXCLOLmA~Ax@x@XPlAjAl@d@d@f@y@t@eAnAgA|Ao@p@Sb@MPe@`@e@l@kArDVBZRrBbAtCbB|A`Ax@t@V\\aD~Fe@TkAVcALaACsF]Oj@a@f@[r@_AnD{@`CyB|E_BpCO\\EV_@x@M|@C~E@Z\\jA|ChBt@f@`@h@Xn@Lb@XZh@T~Cn@fEtAz@\\LJiAnA{@rBIZEz@HrDBTInAM~@WrAw@pCoA~D[z@Mf@M|@_@pEUr@A^_@vAAn@F`@@pABd@mBzIsA~DkAdEIh@E|@Bx@XvANh@~BdFx@vAp@v@r@j@bB|@hBx@t@d@l@d@r@r@x@vARz@FhAAv@LrSAlAGdAGh@[x@QTQLyC~@yBnAe@\\_@R_@Na@Jm@HiEDcAAgCo@[EO?c@Fa@b@OXGb@GrCCZ[~@SVe@^s@TSD{DVoAf@_@Zi@\\cAlAUp@Ah@FRVNZG~@{@VQLOJi@ZO`@a@XKd@K~AK`@GHCZUZI^Qx@}@R_@JU`@qAVo@Ve@Z_@v@m@RIp@K`@CjEAp@Cn@E`@Gd@MrAw@~AkAXOr@[vA]VKNM^s@Ng@Fa@FaABoA?}@ImAEoLBcDCu@E[Qu@O]U_@s@}@c@a@i@_@}@c@oAs@iBu@y@i@i@k@[g@cAoBkAkCe@uAWwABqALeA`CwHz@eD~@kEZ{BD_BXi@jAwCTQl@QXQNg@BWCg@GUwA}BSm@?QdA{DNU\\u@L_@d@sBJaAFiA@s@O{DFe@`@mAXm@Vc@\\]Jk@MK{@]gEuA_Do@i@UY[Mc@Yo@a@i@u@g@}CiB]kAA[B_FL}@^y@DWN]~AqCxB}Ez@aC~@oDZs@`@g@HWnFZhA@hAG`Be@JEJM~A}C`AaBw@{@e@a@{DaCkDgB[SQQdAeDd@m@d@a@LQRc@n@q@fA}AdAoAx@u@e@g@m@e@mAkAYQy@y@lA_BNMBMGYDMZg@Z_@b@WjAWTMTa@DW}BOyGS_@E?iAxDFvI\\ZAbA]RCxBEl@IH@TC`Bq@bC{AFFr@Zj@\\Z\\NHBu@Gi@M}@i@sACY@UPi@jA{Ab@{@`@mAPu@\\eA\\qANC|@Ax@If@MtAk@PUb@QROh@Qn@CfBJjD`@^TJRBLA^ELQTYHU?WMQ_@A]GQBqAFmAVeB`AkFv@aFv@sDLe@nDmIZqBDuAAa@UuBUaAi@qAe@k@k@e@aBcAgAe@YSi@m@S[Q_@K]Ic@G{AVuDZqCm@CYNKfA[xEApALdAPr@aAVeDjAmKd@{[bBb@wAtCoGlBuEl@eAXSMSKe@uBcHs@{BWo@OYwAsBwDaFsAkBcBoBeCkCyDeDcDkCmAoAgLiKgGqF}G_G")
        val ruta11_b = Ruta("Ruta 11 Amercias", "Americas",11,Paradas.getR11(),"yx|iCxirpR^wGj`@jPzCtAfA`@DDH@BCfEfBhBv@DJFDxAl@|At@q@xB{@`C]jAo@bBkDfK}C`JMXn@t@`AvAHDZ`@rHtH|@v@~AbBjAdAb@d@b@\\LNHRb@Xy@`DcApEwBvJ]nBGLYbAUlAiA|EaAbF_@zA}@M{@GW@aEbCmAf@oCp@}CrA]L[Fi@HyDHkBJ}@Ek@ICRj@Jh@BdBG@JPXjAhA\\TLNFl@DzARxDgb@vBQCy@fGW~@IPY^a@Xg@TiF~Ae@^S`@eBpE_@t@QRc@\\i@VoFpBuCx@oBx@[XSXYj@Sz@e@zLo@xM?x@@p@VvBbBhIJbACv@EZUr@uAnC{AlCgBjDsApBuBlC{@nAQ\\Ol@A^@t@Jn@|A`Dt@pAZr@LR`@Wt@[QYESJq@Bc@AM{@cBm@sA?I@Ev@c@p@e@FS`@_D~@OhAINwAFY^a@DMBU@c@CMCKW_@Ra@LM\\WRGJ?tHdAZRTVJRSpAe@dEz@fBxB|DdBdDhBaC\\i@NCPBv@d@VBZRrBbAtCbB|A`Ax@t@V\\aD~Fe@TkAVcALaACsF]Oj@a@f@[r@_AnD{@`CyB|E_BpCO\\EV_@x@M|@C~E@Z\\jA|ChBt@f@`@h@Xn@Lb@XZh@T~Cn@fEtAz@\\LJiAnA{@rBIZEz@HrDBTInAM~@WrAw@pCoA~D[z@Mf@M|@_@pEUr@A^_@vAAn@F`@@pABd@mBzIsA~DkAdEIh@E|@Bx@XvANh@~BdFx@vAp@v@r@j@bB|@hBx@t@d@l@d@r@r@x@vARz@FhAAv@LrSAlACj@KbA[x@QTQLyC~@yBnAe@\\_Ab@a@Jm@HiEDcAAgCo@[EO?c@Fa@b@OXGb@GrCCZ[~@SVe@^s@TSD{DVoAf@_@Zi@\\cAlAUp@Ah@FRVNZG~@{@VQLOJi@ZO`@a@XKd@K~AK`@GHCZUZI^Qx@}@R_@JU`@qAVo@Ve@Z_@v@m@f@O~@IjEA`BI`@Gd@MPI`Am@~AkAXOr@[vA]VKNM^s@Ng@Fa@FaABmCImAEoLBcDCu@E[Qu@O]U_@s@}@c@a@i@_@}@c@oAs@iBu@y@i@i@k@[g@cAoBkAkCe@uAWwABqALeA`CwHz@eD~@kEZ{BD_BXi@jAwCTQl@QXQNg@BWCg@GUwA}BSm@?QdA{DNU\\u@L_@d@sBJaAFiA@s@O{DFe@`@mAXm@Vc@\\]Jk@MK{@]gEuA_Do@i@UY[Mc@Yo@a@i@u@g@}CiB]kAA[B_FL}@^y@DWN]~AqCxB}Ez@aC~@oDZs@`@g@HWnFZhA@hAG`Be@JEJM~A}C`AaBw@{@e@a@{DaCkDgB[SQQq@]UGe@GLT]h@iB`CeBeDyB}D{@gBd@eERqAU_@g@_@{Fy@y@KK?SF]VMLS`@KDYBa@C}Bq@I]?ODYCWEGSI]HIVDVTLJPLJ\\PrBj@`@BXCJEV^FXEx@IR[ZGXOvAiAH_ANa@~CGRq@d@w@b@AD?Hl@rAz@bB@LCb@Kp@BNFL_A\\{@aBaCgEIq@?YLgAHUxCyD~AyBXe@x@cB`FaJXw@Dc@@y@Gk@}ByLEe@A{@T}G~@qSP_ATg@Z]f@YvCcAxHwCj@Yr@i@Za@Zi@tAyDTc@`@c@RKdBg@dCy@l@[d@g@Vg@Nk@z@iGhQy@dCQxFW~CSSgEE{AGm@MO]UkAiAQYAKxEMv@MXIbFiBrC{@dDmBj@a@PCT?tBXrDmPf@uC?S\\oBzDiQx@aDc@YISMOc@]c@e@kAeA_BcB}@w@sHuH[a@CK]i@_@y@l@yBlCcHl@mBn@{A~BwG|@oCDK|@u@JKHOD_@Kg@}@e@_DmAIMa@QM?qH_DKOiCeAaBw@gP}G}EyBaDoAaAe@mAe@c@jH")
        val ruta13= Ruta("Ruta 13","n",13,Paradas.getR13(),"{kxiCp{tpRaDtCeE`EfAdAvH`Ip@j@DpD`@~OP`JP~MkQn@a@H}CJcAJi@CoG^}AN{BDiAJQAi@BaEp@m@VoAdA}ErEuAhA]T{@dAuD|Bg@d@aAt@m@^KFUDKC[fAOv@Kn@W`CKVW^iAnAi@`AOd@c@lB}@|EOl@e@nCe@zBy@nFw@lD]hAoExI[`AKr@CnADrBAfAD~BInAIV_@l@gAtAcGzGu@bAaA`AkCbDoDxEa@d@W`@GLOz@gAfHMdB_AAGEEIKs@[g@k@[gBm@o@Ok@Wo@c@i@e@UKe@s@q@q@aD~Fe@TkAVx@vIf@@t@JLNp@b@NFL@HA~@SLLLBHAFLXRJ|A?dAWvCWpAIVGl@CtCEf@gAfEQhAq@hCc@vAc@x@i@t@{@f@_A^iAnA{@rBIZEz@HrDBTInAM~@WrAw@pCoA~D[z@Mf@M|@_@pEUr@A^_@vAAn@F`@@pABd@mBzIsA~DkAdEIh@Ej@BjAXvANh@~BdFx@vAp@v@r@j@bB|@hBx@t@d@l@d@r@r@x@vARz@FhAAv@LzTEpAKbA[x@QTQLyC~@yBnAe@\\_@R_@Na@Jm@HiEDcAAgCo@[EO?c@Fa@b@OXGb@GrCCZ[~@SVe@^s@TSD{DVoAf@_@Zi@\\cAlAUp@Ah@FRPLD@JANE~@{@VQLOJi@ZO`@a@XKd@K~AK`@GHCZUZI^Qx@}@R_@JU`@qAVo@Ve@Z_@v@m@RIp@K`@CjEAp@Cn@E`@Gd@MrAw@~AkAXOr@[vA]VKNM^s@Ng@Fa@FaABoA?}@ImAEoLBcDCu@E[Qu@O]U_@s@}@c@a@i@_@}@c@oAs@iBu@y@i@i@k@[g@cAoBkAkCe@uAWwABqALeA`CwHz@eD~@kEZ{BD_BXi@jAwCTQl@QXQNg@BWCg@GUwA}BSm@?QdA{DNU\\u@L_@d@sBJaAFiA@s@O{DFe@`@mAp@qApAoAJGJQz@g@h@u@b@y@b@wAp@iCPiAfAgEDg@BuCFm@HWVqAVwC?eAK}A@c@EW@UCKGIKEQAMBaBt@}@_@kAOg@Au@aI`Be@NIfBgD`AaBd@f@dA~AVVZT~@h@v@X~Ad@^FHFNRHZJr@FFRF|@DZDR_DRwAFu@ZwBNu@FOXg@vD{EfDyDFMrCoDhBwBtAsAxB}CLUDQFs@@SCW?a@v@PbBj@rFxALc@Jo@dB{GtAaGJe@@UN}@T{@dA}EfBiG^yAOMEMQI}@aAcBaBCK@Kp@mBr@}BDELC|B\\~@HYi@aDuC[OUEK?]Hc@\\cA`BYj@]BMGGQViBb@qBX{@Xc@v@y@h@s@Rg@NgBN}@VcAPaATEx@g@`Au@f@e@tD}Bz@eA\\UtAiA|EsEnAeAl@W`Eq@h@CP@hAKzBE|AOnG_@h@BbAK|CK`@IjQo@WyQm@gWEqDHQoI_J{AsAtDoDdDyC")
        val ruta14 = Ruta("Ruta 14", "Huertas- Utez", 14, Paradas.getR14(), "imtiClxmpR}J_@_BCqBJAl@D\\LF`^tB{ApCwAbCC@GNEf@MZq@hAk@hA{BxDpBhBy@dACHBJrBxCfAhBtBvCzEcFxA~AJPn@rBlElL}@~E]zB_AxE[zBUv@E~@E\\e@lB{ApIEj@gC^wALOCmBP_BTg[vDsG~@iOjBeC`@{BTyJlAgGx@iHx@mARaN`B_IfAkI~@eHbAoEj@oFd@}BVkDd@oF|@QA}B`@cCZ?PwFx@kGt@cBVy@E]QIKKYAi@BQT_@HGJCXAJBLFJJv@|BHh@fB|GdAzEPEEUcDaNh@M~LcBvC]p@Mz@GVDNHFHRf@|CdKdDzIFTBj@Ab@T~JVfGFl@FVVp@lB~DBP?d@s@hIIh@mAtFC`@CxDDnEGxAChBMdCMfEIp@e@fAYh@oDjFe@j@O`@Gx@IRORUb@UxAIXKNUDwD?{@HuAh@_CpAy@^[TKRKZMn@OpCc@dFWjA}BxE{@|A}@dAULIIUCOFGL@VIHSd@mAbFMp@_@vAu@jD[fA[fBW`CKVW^iAnAi@`AOd@c@lB}@|EOl@e@nCe@zBy@nFw@lD]hAoExI[`AKr@CnADrBAfAD~BInAIV_@l@gAtAcGzGu@bAaA`AkCbDoDxEa@d@W`@GLOz@gAfHMdB{@?KGEIKs@GMSYk@[gBm@_AW[OgAw@GLMt@Gv@CtBFbBVlCTn@@JB~@@|ECbEG`BQfB]vBsIf^cCtIa@lA]t@uDhHM\\_AdFo@|CIn@GzBWxAaBpHsA~DkAdEIh@Ej@BjAXvANh@~BdFx@vAp@v@r@j@bB|@hBx@t@d@l@d@r@r@x@vARz@FhAAv@F|L@f@FJBL@lAJ^TVPFXBLHR^@LStBWjB?XF`@JRJJh@Rj@NPLNTvBrErA`Cf@v@JJjDdCvDtBzArAFN`CpBfEnBNXZLNVj@f@fArAnBpCl@`Aj@fAb@jAxAtHz@dFt@zDj@zB`@pCFVNPb@rCVGVStAwAHLzA|@bAb@z@Vn@Bt@E~@QT?f@Pr@f@r@r@`@l@`@tATVf@VnB|A~BhCrCvGLb@h@lCLhB?X[~DKvBUxBCfADnAVdCv@rFNzHHp@X|@aHhAf@rC^jCHvAb@bFEr@VzA|CtObFoAm@wCcFlASkA{AoHW{ADs@c@cFIwA_@kCg@sC`HiAY}@Iq@O{Hw@sFWeCEoABgATyBJwBZ_E?YMiBi@mCMc@sCwG_CiCoB}Ag@WUWa@uAa@m@s@s@s@g@g@QU?_APu@Do@C{@WcAc@{A}@IMuAvAWRWFc@sCGw@q@cEuAoHWiA_@_CyAaH[gAs@wAy@wAqCiDYSU]OOa@q@?MS@[UoEkBOUyAiAWI{AsAwDuBkDeCKKg@w@sAaCwBsEOUQMdAgA|AsBh@oAb@k@hAiA`@k@DEZIp@ALGN]M{@?]DWhB{CTe@_@a@SK}@SuBu@uBo@MAy@@UJMJKNKVsBvGALe@pBAPKN[L]CYQGIMe@E]EoLBcDCu@E[Qu@O]U_@s@}@c@a@i@_@}@c@oAs@iBu@y@i@i@k@[g@cAoBkAkCe@uAWwABqADc@Fa@`CwHz@eD~@kEZ{BD_BFo@`@{BdAgFFS`EuHl@wAh@cBlEwP`@aBf@aC~CcM`@gBV_CJyDBmFEyCEkACQN_AP[VSz@U^WRm@Z}ARq@^FLJJNHZJr@FFRF|@DZDR_DRwAFu@ZwBNu@FOXg@vD{EfDyDFMrCoDhBwBtAsAfBeC^m@DQHgACWAqBIsEBo@Fi@Rq@zEqJp@uBj@yBz@yFb@eCvAeHViBb@qBX{@Xc@v@y@h@s@Rg@NgBZeBJ[PaA~AeHz@gDXi@b@a@Pc@DADGFBH?XIl@]b@[|FaF^c@Lo@@q@Gc@YkAKo@Ay@Dk@DuCD]Ha@Na@Vc@X[lAy@lAq@f@Qh@I`F@XCNUL[JgAf@aBJu@H]LWdAyAz@cAlAiBf@y@d@kAHo@j@qO?{@GaC@aFHm@lAqEj@cGHqAB}@Ai@[{@kBqDMo@Eo@k@uTCQ_@oAaCkGcAyCUDeGnBu@oDgAqElBUNUnFq@f@E\\@`AEvBWTAn@@JDR@\\H|@^b@\\t@lAr@Xf@Bd@I\\SPUR_@N}AZ{@Xk@^Wz@]jIiAlAKpQcCjIcAzAUdHy@nUwC~MkB|C]fTmCtBUtRcCdDc@nDk@RPHRh@t@jAdAn@d@fAp@z@r@n@^`ARyA|Ci@`AyAnBkE`Is@rBgCrE_CzDaNv@eFRsDTSFAQm@Xn@BnHUfCMlHi@zCMLY~B{DfCsEr@sBjEaIxAoBh@aAxA}CaASo@_@{@s@gAq@o@e@u@o@k@o@S[ISEi@@q@xAaI\\qCD_ATw@Z{B~@yE\\{B|@_FmEmLo@sBKQyA_B{EbFuBwCgAiBwBaDBMnFgHgC}B`AeBDMDg@FOBAvAcCzAqCqAIJiCyI]")
        val ruta15= Ruta("Ruta 15","n", 15, Paradas.getR15(), "sqoiC`iwpRi@GgCq@qBQu@AQz@e@xAMR}@l@Uf@iArB_MaJGMAKXmC?_@Ey@CSSg@e@s@eBuAkDaCaCiBiC}B}@k@a@O]GoAMuALqATeBRa@Dm@?g@GyBq@{@Sq@KqKeAmAIiXJyAKmLmAyEGSKqAAaCOoDg@qC[@sABYd@_DpAqGx@oDp@kD{@[e@t@@FAX[rAjAZy@nDqApGe@~CCXArAiEo@kCY{@GmJ_@cBKkAMk@Ow@[cJaEaBq@y@Os@EyCEgROL}BLiDB_BR_FT{I]F]TY\\QJqEfAWHMLU`B?f@g@jF?`BBfAr@bH_Bh@mFrBiA^qCdA{A`@w@Dy@JOHiCKs@KoCXqCTJfDiC\\mD\\yB`@uEp@DVgGkB}Cw@yDMJm@Pa@dAyAz@cAlAiBf@y@Rc@uBk@g@SoM_EqCeA_AdEqAtGkD{@n@wD[y@CQy@{BSs@q@eB[e@SCkCeC_AcA}@{@c@YISMOc@]c@e@kAeA_BcB}@w@sHuH[a@CK]i@_@y@k@}@_DeEw@kAQOuDoFnFuODQ?K|@kCdB}EFKiCeAaBw@yFaCfAoChAkD@Sv@sBfAeDfBaFLsA^yCcDw@yAc@sDoAL{Ah@sLcMwDcBYwBw@FyBEo@Bi@j@sE|CkTL[dB{Mn@_EJ_B@eBGcDW@eBZg@NgAj@yOhKKTwI`GcG|DmT}GQGQSs@h@[LDVn@`IoIvUeKgGk@[MDDNNJfE~BlIbFhAx@@LFHFBLA`A|@|@lAjBtBtKpMdH~IHNZZz@fAd@t@\\x@~@jC`CjELNlDxAfG~Bp^nOzCtAfA`@qCbIQd@QTcBrEuAdE}@xCdBhCp@x@lCvDbBzBjArA`AvAHDZ`@rHtH|@v@t@x@tBnBb@d@b@\\LNHRb@XvEvEr@l@?Db@h@dAzCz@xBJJZx@o@vDjDz@?LDFJ@JI?Q~BqMdC~@nM~Df@RzAb@i@hAoDjFe@j@O`@Gx@IRORUb@Ih@RFf@aBD[nCHh@B|Cv@fGjBEWtEq@xBa@lD]hC]KgDpCUnCYr@J|CNpBOpA[JGzEaBfFqBbBk@w@uHCgA?aBf@kF?g@TaBLMVIpEgAPKX]\\U\\GUzIS~EC~AMhDQpCjMHtGH|@Bp@F`@H~LrFv@Zn@N\\F|BPnLb@nALpG`ABS@sABYd@_DpAqGx@oDp@kD{@[e@t@@FAX[rAjAZy@nDqApGe@~CCXArACRrJhApAHzA?JEN?hEFlLlAxAJhXKlAHpKdAp@Jz@RxBp@f@Fl@?`@EdBSpAUtAMnAL\\F`@N|@j@hC|B`ChBjD`CdBtAd@r@Rf@BRDx@?^YlC@JFL~L`JhAsBTg@|@m@LSd@yAP{@t@@pBPfCp@h@F")
        val ruta16 = Ruta ("Ruta 16", "n",16, Paradas.getR16(), "k{`jCrvqpRt@RvItAbFhAfAPtGpAbKfBHHlAZjANjEt@jNlCtCp@pMlDpEtA`FpArBn@hCn@n@RrKrCzDhAfCl@xCz@hB`@~Aj@pAaELIt@{B~AiErAyDb@yAfAoChAkD@Sv@sBfAeDfBaF`NvGg@jA}@pCsD~J}AtEGJeB|Ek@dBQd@QTcBrEuAdE}@xCdBhCp@x@lCvDbBzBLt@w@|B{@~C_@lCKhATDCbBBdBr@~RZdGPhE^fFA|DG~@c@`Eu@nJAxAD~@Jh@Nb@^t@X\\NVr@j@~CdB~@dAb@p@Th@Nh@Lv@BzACp@Gh@e@jBi@pAc@z@{AhESv@{@tEeA|E_AfFK|@GvBDzB@bDMvAMh@Yz@g@`AoBpCoApBm@jAsCxGiBrEyCfH{AxCaClD_EnFcD|Go@zBw@pDI|A@|@`@~FBbGCbEG`BQfB]vBsIf^cCtIa@lA]t@uDhHM\\_AdFo@|CIn@GzBWxAaBpHsA~DkAdEIh@Ej@BjAXvANh@~BdFx@vAp@v@r@j@bB|@hBx@t@d@l@d@r@r@x@vARz@FhAAv@LzTEpAKbA[x@QTQLyC~@yBnAe@\\_@R_@Na@Jm@HiEDcAAgCo@[EO?c@Fa@b@OXGb@GrCCZ[~@SVe@^s@TSD{DVoAf@_@Zi@\\cAlAUp@Ah@FRVNZG~@{@VQLOJi@ZO`@a@XKd@K~AK`@GHCZUZI^Qx@}@R_@JU`@qAVo@Ve@Z_@v@m@RIp@K`@CjEAp@Cn@E`@Gd@MrAw@~AkAXOr@[vA]VKNM^s@Ng@Fa@FaABoA?}@ImAEoLBcDCu@E[Qu@O]U_@s@}@c@a@i@_@}@c@oAs@iBu@y@i@i@k@[g@cAoBkAkC[{@_@cBAc@B{@LeA`CwHz@eD~@kEZ{BD_BFo@`@{BdAgFFS`EuHl@wAh@cBlEwP`@aBf@aC~CcM`@gBV_CJyDBmFEyCEkACQN_AP[VSz@U^WRm@Z}AZiAgBm@o@OM_AFm@f@qAjBwDzAmC^k@xAmBpBwCt@kAjBmD`BwDVq@He@FQpF_NtAcC|@oAS?jB{Cb@{@\\_AJm@HqACcHFuBX}BtC_PNo@l@uBnC_Hb@eBFaA@w@?c@Em@E_@Sw@m@sA_@k@YYy@k@yBmAiA}@Wc@c@_AQy@Eg@C}@v@mKVyBVwD?mCAeAwBug@BkCHeBPBLaBJu@f@uBrAiDqAoByBsCw@kAQOuDoFnFuODQ?K|@kCdB}EFK|AuErD_K|@qCf@kAaNwGgB`FgAdDw@rBMJq@fBwD|KaDzISr@E^cAzCuBm@uA[kAa@eZeIkFsAmKyCgHgBwAc@iIwBa@GqDw@cC[}KuBe@CMDcKgBuGqAgAQcFiAwIuAu@S")
        val ruta17= Ruta("Ruta 17","N",17,Paradas.getR17(),"e}}iC|unpRqGrEgAp@PRPFlT|GbG}DvIaGRA|@c@l@_@pHaFfBsAt@c@rAe@rAWZCxEMfAI|ESlACbAIp@At@Dj@HhAZb@PvBjANbMDb@o@^ERm@XSPmEjHYh@GT?HJZ`GxJL`@?RK`AcBjVa@lAExAKdBQfA[lAKfAi@xCm@vCtBl@jAV~JtCmAvCYbAe@lAfHhD{DvKbAf@FU`@H|BlAs@bBgBnFo@xAq@xB{@`CJX_FzNiEnLyA|E]tA]bCIx@CrBBdBr@~RZdGPhE^fFA|DG~@c@`Eu@nJAxAD~@Jh@Nb@^t@X\\NVr@j@~CdB~@dAb@p@Th@Nh@Lv@BzACp@Gh@e@jBi@pAc@z@]|@}@jCSv@{@tE}AnHg@tCK|@GvBDzB@bDMvAMh@Yz@g@`AoBpCoApBm@jAsCxGiBrEyCfH{AxCaClD_EnFcD|Go@zBw@pDI|A@|@`@~FBbGCbEG`BQfB]vBsIf^cCtIa@lA]t@uDhHM\\_AdFo@|CIn@GzBWxAaBpHsA~DkAdEIh@Ej@BjAXvANh@~BdFx@vAp@v@r@j@bB|@hBx@t@d@l@d@r@r@x@vARz@FhAAv@LzTEpAKbA[x@QTQLyC~@yBnAe@\\_Ab@a@Jm@HiEDcAAg@D_@Hc@Na@Rm@f@_@f@i@zAMf@e@bAU\\c@Za@NaAPw@@u@Du@Li@Pc@RWPYXm@v@S\\Qb@Oh@GZGt@?x@HfA^|Ch@xH`@zEBr@AvEe@bMkAh^k@rMAt@g@vMIvCc@~Ki@lLSrGYbAEj@MbEK~ACNGLSPOAKFEFCN@NDH?t@GlB?n@IfAU`Aw@nBGVOBGJ@PMb@Yl@u@`CaA~BaAlCw@pA}@v@gEdCyA~@cAj@aA`@HXXIvAs@xCmB|DaCp@u@v@uAv@iBvAkElA_DFEBG?MJOl@}A^sAFe@FiAJaEJWAOh@FQdDO~A]~B]xAWz@sAzC{@|BUjAG^C`ABdADh@Hh@J`@d@xA~EpMl@hBXvBDx@?`AWfD?r@@VJv@Lb@l@dB~BdGRp@fCvGt@bClAjD~CdH~DhKpAtDV|@XhBzA`OD`BLvAn@lGxArMo@Nc@Rc@Vc@\\_@b@m@fAW~@Ih@Gv@C`ANjAdF{ABi@ZwA?m@_@gDMq@n@Or@vGhApLJv@Ll@Z|@l@hAh@h@|S~X|G|IpGvIjGfIpAfBZf@\\n@^dATx@ZtBZxC|@fHT`A\\bAd@fAPXl@r@jAbAjF`ELW{DwCyBkBs@aAWe@m@eBUeAy@cG}@mH_@qAYs@Wi@]k@gIsKiHyJe]ed@o@iBQ{@iBkRk@eE]DqAXc@Rc@Vc@\\_@b@]j@Uf@Qr@Ih@Gv@C`ANjAdF{ABi@ZwA?m@_@gDMq@pAY\\EiCmV_@eEg@{DiA{JOw@Qo@e@oAyA}C_A}B]q@QUuBwFs@_CoA_DSm@_AeB{AqDKq@kCkHQ[eA}CESGo@@uACy@Ds@Lu@n@gCj@mCVeB`@}@JGJARDXX@`@Mv@O\\a@n@QNy@X[D]AWG[UW]}DiKeB_F]{AI_AAe@DyAN{@p@kBfB}Db@sAT}@TkAN}Al@sMb@iNlAgZt@{TDg@p@yRNoCb@wMLmCb@_NBqCC{Bu@wKgAeKBeAFc@VgAP_@Va@X[p@k@p@Wd@KTEpBI~@OZI^Qx@}@R_@JU`@qAVo@Ve@Z_@v@m@RIp@K`@CjEAp@Cn@E`@Gd@MrAw@~AkAXOr@[vA]VKNM^s@Ng@Fa@FaABoA?}@ImAEoLBcDCu@E[Qu@O]U_@s@}@c@a@i@_@}@c@oAs@iBu@y@i@i@k@[g@cAoBkAkCe@uAWwABqADc@Fa@`CwHz@eD~@kEZ{BD_BFo@`@{BdAgFFS`EuHl@wAh@cBlEwP`@aBf@aC~CcM`@gBV_CJyDBmFEyCEkACQN_AP[VSz@U^WRm@Z}AZiAgBm@o@OM_AFm@f@qAjBwDzAmC^k@xAmBpBwCt@kAjBmD`BwDVq@He@FQpF_NtAcC|@oAS?jB{Cb@{@\\_AJm@HqACcHFuBX}BtC_PNo@l@uBnC_Hb@eBFaA@w@?c@Em@E_@Sw@m@sA_@k@YYy@k@yBmAiA}@Wc@c@_AQy@Eg@C}@v@mKVyBVwDAsEwBug@BkCHeBNgBTuAR_AjB{FxFiOxIaW`@]`AmCDINKDI|@aCXo@PWf@_@RIjEs@fB|G~@fE}@ZiJnCqAZIHQFm@L]E{C}A_DmAIMa@QM?qH_DKO|AuErD_K|@qClAyCXcAlAwC_KuCkAWuBm@l@wCh@yCJgAZmAPgAJeBDyA`@mAbBkVJaA?SMa@aGyJK[?IFUXi@lEkHRQbBu@\\Sf@y@vCyFJW}BcBmD_CkAm@aA[cAQo@CkA@gH\\}ELcAJ_BDgAPeAXgAj@yOhKKTwI`GcG|D_UeHnIiG")
        val rutaTyL = Ruta("T.D.G. Tierra y Libertad", "N",18, Paradas.getRTyL(),"wq|iCzhppRiApUeC``@KOeI_Dm@u@kBiDwAsDu@oAWYQ[q@q@}DwF_VcYGc@vCeKd@qBCqAq@mGI]MPqIxU]KsIiFcAi@]Ae@Uk@q@KOISWQYEORPDNENDFP\\`@LNj@`@VJLBFJd@\\fAj@`ItEhBfAnAx@BF@PDNGp@_@xLIlBCBrAxAlEfFxD~EZLj@HJCLFbCtAnDzBuAxEkEtNmBbGoFvQqBlG}ClKKTBHrAj@vAb@bMrEPBh@NfDjAdE~AbEjAbAwBj@}@bA`@zAd@tClAlDhBnCdBp@Zr@Ht@Z`Bz@VBbAGj@GnBf@J@f@yAnFbB`Cl@HDCl@@lAM`@uBjFaArCy@jBkAfCKp@[x@kBtE_CpFyDhIcA~BDFb@NXC`@KzAl@ZTHRE`@On@kAtCTDt@FrA@bCK|CStC_AnBw@RCVJNPH\\BVQhCIbDHn@R`A`@v@nA~ArBpAtAp@dAbB`@d@PXNd@HzA?l@Ej@Kv@c@tA}A~DQV_@Rc@nAkAxEaCxLWlAE^Dj@CtCBjGKl@Uz@[z@uAvB}@jAc@v@qBzCSh@Gr@_@fAm@zA}FjN]j@_@b@aAzAs@t@}@dBa@r@Sn@{BrDyCnFgB`EOt@MlDPtDP~ALXAlADxDIrI[jCY~AmGbXcDdMUbAYbAo@tAaAlBo@`AcAhBYv@UbAUvAm@rCWnCAv@I`A]nAwAxGwBxGc@|AKdAA`AJ|@Nx@x@hBtCrFr@jAjBbAx@`@p@`@z@`@~@f@h@d@`@l@b@z@VhAJ|BHnUIpAOdAc@v@s@ZmCz@e@^aBbAi@Xq@TkAHoE@{AH_AXmAlAg@t@Oh@q@lBW^e@^WL_@JaANy@B}@Hw@Le@To@`@g@j@c@j@Yv@YhAK~ALvAPzABn@L|@VvDp@fIBzCCxBY`HWvHg@pMOzFy@rQStFKtB]~LaAxUOrFUh@QbFMlBUl@QDu@IaCs@mCg@mBU{ACcADgHlA_CXs@F[G_@FIRDb@mFrIeD|Gs@vBUfBEnABbBVfAl@tBv@pA`BxAt@d@nAb@~B\\n@BfAGlB_@hAe@tAaA|EuC`Aq@lAwAf@IVC`@RT^H\\^h@n@fBz@nCzAvD\\d@VEHg@kEgLYi@QqAHgCt@sC`BmDt@eC^eBVkCRiDJ}CLcBPy@b@iDZ}Fb@oLl@C`DDpCNtAD|AVPENM|AiC@@vAjNNdCj@tFHtAZbDxDy@pA]vGsAl@YbB_Ah@e@J[?W[uB_@aB_Aq@gAWcAE_@Na@\\c@l@}F`GCRMJcBb@]DEMU{B_BiQa@cDEoAHgCH{AOcAmAcBgB}BaAwAq@mAa@aA]s@gBqEWqAGw@LwETiC?q@NqAQWm@Kq@CpAs^H_DFqFo@eKaAqKKiBH}@Ny@n@oAh@o@bAe@dA[fAI`@?bAK~@_@r@w@Zg@|@aCf@eAr@u@b@UdDU|DA|@Mt@OpDcCvAm@n@Qt@Wh@}@Xs@JoAAaHGuDCgIE_BMo@u@}A{@gAcA{@{@[oB{@eCwA{@qA{B{Eq@eBSy@CwCRmA|BiHf@cBbAkETwANkALyCPy@\\oBr@sCp@yArBcDjAgCdAeD~DeP`DwNfAeE^mBXgBHuADyHCuA@yAM}BPk@X_@tAk@Xe@RiA^uAH_@kBu@c@KKQCw@Jq@xBcFv@mAr@mA|GwJtB_EnDaJdByEfBcEj@iA`DkEdAsBl@uALoA@mB?}FDeA`@_Ed@gBtAoI~@gEbDmIf@gCAeAK}BYoAm@yAiBaB}@i@{@a@nAyEjAeGl@kCz@gE{@OgAKk@H}A~@e@`@oCdAsBj@}DxA_BVoBD{BPgACaAIl@wA`@gAFm@CYYWmBw@g@Rc@IMIdDeHrEiKbCiGR{@xCiHdCwG?ESIy@WBU`@cA_@Q_H{BaCq@oCHa@OoBcAiAOoAu@aBgA}DmBiDuAiCy@OR{AxCsA[}Ai@gIwC}Ae@c@[{FmBcG_CkA]FYtGeTxAeFlDcL`Li_@MOw@xBgHmEk@Gm@MQOa@o@wKiMHuA\\uJFuCJYB]MS]GuHqEwHmEg@Gi@[y@iAUGWDQKHKVDz@fA|CrBfKbGVm@dEiLtC~@ShA}@rCuAhFc@hABRn@Rb@l@`RtTnCnDlEnFv@fAdBhEdCvEPPtGfCzAp@L_A|Cwf@`@mKCY@a@GG")
        val rutaTdG = Ruta("T.D.D Guadalupe/Zacatecas", "N",19, Paradas.getRTdG(),"ic~iCtm`qRVr@n@zApB`E~@nAtA~@dGfD`@X|A|BT~@DbAHvVMfC_@fAUZ{C`AkDrBg@Vo@Rq@JqHJq@Jw@\\q@n@[l@Yn@]nAUh@_@h@a@^kBb@{BHo@Ls@Vm@`@i@f@e@r@Yj@Wz@MhA?|@d@|DtAvQErGCnA_@pI{@xW_AvUaCdp@S~DU`@SbGGnAERGNMJEDU@MPAJFX@r@ObFQ|@YbAaG|N[~@g@bAk@r@mAx@eI|EsAf@w@PeAJeA?_BSmBo@_@UgBcBa@u@e@eAg@eCCqA?sAP{A`@qA`FaKxByDdAuALIJ?HEFKDOAQEKKGQGK@GFGFER?JBPAHc@h@a@n@oD`GeAlBkB|Dq@~CC`CF|AZvAj@bBr@jApAlA~Ax@v@Vf@JvAN~@?tAM`Ba@h@UdIaF|@m@h@m@V]l@UP@RBRPPVXp@TX~D`LP`@JJHBNCNUE_@gEwK]q@Q}A@mANkAh@iBxBeFr@_CRyARaCh@cLXs@N{@H}@^_Gh@aQIoCQw@fA_[x@mSjBNYbCQzBG`CDv@NJHAJO?MEIQKCUJyCh@eF}BQ^iJd@qOVwFDmFMoDi@}HcA_KAq@F_AVeAPa@hA{Aj@]vA]|BMv@Kz@]r@s@h@cA`@uA`@_Ad@m@hAs@lASlFAxAI~@UlEwCp@YhBe@f@u@b@}ADeB?yEKoL?}BCu@SeAWk@qAgBeBkA[KuBgAi@QwA{@k@u@sAeC{AmD_@iAO{@?e@@u@LcAtAgErAyEvAeHVwAHqBFo@hBmItDuGpAyCxCyLhDaOxAyFtA}FToCHkC@mGIuFHo@N]XWx@S`@YFMv@aDHg@y@[{Ac@Ky@Hs@h@uAvC}FlHkKfCyErB{En@uBnBaFrB}EdAiBtCgEt@oAf@uAPuABwCAqELiCTaBbCiNl@qCbAkCvBeFZmB@aBUgCImGh@k@~@AdH]~AEtDOy@bBUTGh@BLKJERDPHFX?l@GjAs@fHgGRw@@q@o@mDDkBJyCJm@Ri@h@q@pA{@hB_Az@OlF?\\_@J]B_Af@yA\\oBtCiD`C{DwBk@g@SwGoBaIsC\\wA\\aBjE{P_C{@XiAZ_BJmAByAEkBIcAq@cDwFqUeHc[xNkBbDo@`De@lCU`@HjDc@n@FhARz@b@hBpCp@zAbJjc@rAfF`BvHrGrYrC`N~EjVd@nB\\hDFxEVv_@r@DG_G\\ACs@[@AoDS}WWwC}A{HSmBGmAoAyHeGuYkG{Xy@oEaBiHm@oB}@yBa@qAoBoJS_CIaE@yAN_BEk@Sm@[[]OwANa@CkA?oD^sAP{@d@cBXuD`@{Cb@yB\\cCZyMtA[Ec@SO[Ce@DUL[RG\\GXBPRZl@f@~AVPR@TEfNiBzB|JeOnEbFjTxBzIhAdFTpAFfBCrCaAxEDH`Cx@mEbQq@bDILm@xCSn@a@pBYxBIHCL@H]vAgDo@e@fC{@lFs@tDw@tEe@~Ak@jAcAbBqCjC}B|AsCjBmB`AiCn@c@PQLCh@ZLb@`@h@t@\\p@RP`@|@F`@BxACv@Ip@a@|Am@vA_@t@c@jAm@`@Ux@u@lCsArGo@fDc@lBO~@Ab@BpLEn@I`@y@|B_AnAa@d@yBbDeAlBO^Gp@w@~BkA~ByEnLm@p@sBlCiCzEyBnDiCvEMZYx@y@nBSv@G|@EdCHxBNz@N^J|@@tNGhBQhBu@dEkHlZcDdMq@~AsDlGY|@mBxJCrAInAgBdIc@dBmAhDu@zCSxA@jATrA")
        public fun getName() :String
        {
            lateinit var nombre_ruta:String
            when (MainActivity.ruta) {
                0 -> {
                    //Ruta 1
                    nombre_ruta = "Ruta 1"
                }
                1 -> {
                    //Ruta
                    nombre_ruta = "Ruta 1"
                }
                2 -> {
                    //Ruta 3
                    nombre_ruta = "Ruta "
                }
                3 -> {
                    //Ruta 4
                    nombre_ruta = "Ruta 3"
                }
                4 -> {
                    //Ruta 5
                    nombre_ruta = "Ruta 4"
                }
                5 -> {
                    nombre_ruta = "Ruta 5"
                }
                6 -> {
                    nombre_ruta = "Ruta 6"
                }
                7 -> {
                    nombre_ruta = "Ruta 7"
                }
                8 -> {
                    nombre_ruta = "Ruta 8"
                }
                9 -> {
                    nombre_ruta = "Ruta 9"
                }
                10 -> {
                    nombre_ruta = "Ruta 11 Americas"
                }
                11 -> {
                    nombre_ruta = "Ruta 11 Alberca"
                }
                12 -> {
                    nombre_ruta = "Ruta 13"
                }
                13 -> {
                    nombre_ruta = "Ruta 14"
                }
                14 -> {
                    nombre_ruta = "Ruta 15"
                }
                15 -> {
                    nombre_ruta = "Ruta 16"
                }
                16 -> {
                    nombre_ruta = "Ruta 17"
                }
                17 -> {
                    nombre_ruta = "Transportes de Guadalupe - Guadalupe/Zacatecas"
                }
                18 -> {
                    nombre_ruta = "Transportes de Guadalupe - Tierra y Libertad"
                }

            }
            return nombre_ruta
        }
        public fun getNameFile(): String {
            var nameFile: String = ""
            when (MainActivity.ruta) {
                0 -> {
                    //Ruta 1 Antihorario
                    nameFile = "ruta_1.txt"
                }
                1 -> {
                    //RUTA 1 Horario
                    nameFile = "ruta_1Horario.txt"

                }
                2 -> {
                    nameFile = "ruta_2.txt"
                }
                3 -> {
                    nameFile = "ruta_3.txt"
                }
                4 -> {
                    nameFile = "ruta_4.txt"

                }
                5 -> {
                    nameFile = "ruta_5.txt"
                }
                6 -> {
                    //RUTA 6
                    nameFile = "ruta_6.txt"
                }
                7 -> {
                    nameFile = "ruta_7.txt"
                }
                8 -> {
                    nameFile = "ruta_8.txt"
                }
                9 -> {
                    nameFile = "ruta_9.txt"
                }
                10 -> {
                    nameFile = "ruta_11.txt"
                }
                11 -> {
                    //RUTA 11B
                    nameFile = "ruta_11b.txt"

                }
                12 -> {
                    nameFile = "ruta_13.txt"
                }
                13 -> {
                    nameFile = "ruta_14.txt"
                }
                14 -> {
                    nameFile = "ruta_15.txt"
                }
                15 -> {
                    nameFile = "ruta_16.txt"
                }
                16 -> {
                    nameFile = "ruta_17.txt"
                }
                17 -> {
                    nameFile = "ruta_TdG.txt"
                }
                18 -> {
                    nameFile = "ruta_TyL.txt"
                }

            }
            return nameFile
        }
    }


}

