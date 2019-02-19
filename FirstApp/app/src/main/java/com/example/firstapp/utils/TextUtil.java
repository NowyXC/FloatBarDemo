package com.example.firstapp.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 文本操作工具类
 * 方法归纳
 * # 判断是否为空
 * # 判断是否全部有字母和数字组成
 * # 隐藏手机号码中间四位
 * # 判断是否为身份证号码
 * # 判断是否是15位身份证号码
 * # 判断是否是18位身份证号码
 * # 判断车牌号
 * # 判断是否是联系方式(手机号码+固话)
 * # 判断是否是固定电话号码
 * # 判断是否是手机号码
 * # 格式化double成两位小数
 * # 四舍五入保留两位小数
 * # 创建BigDecimal
 * # 获取不为空的文本信息
 * # 距离转化成米
 * # 判断email格式是否正确
 * # String转换到int
 * # 判断是否是邮编
 * # 半角转换为全角
 * # 去除特殊字符或将所有中文标号替换为英文标号
 * # 返回TextView长度
 * # 是否是一个完整的url
 * # 是否是一个完整的图片url（带图片后缀）
 * # 是否银行卡
 * # 根据key,获取URL中相关的value
 * # 隐藏mobile中的4位
 * # 获得字体的缩放密度
 * # 数组个数大于count
 * # 两个对象是否相等，已做非空判断
 * # 去除空格，已做非空判断
 * # 摘取里面第一个不为null的字符串
 * # 替换文本为图片
 * # 压缩字符串到Zip
 * # 解压Zip字符串
 * # 判断是否中文
 */
public class TextUtil {
    private static final java.lang.String TAG =TextUtil.class.getSimpleName();

	//邮箱表达式
	private final static Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

	//手机号表达式
	private final static Pattern phone_pattern = Pattern.compile("^(13|15|18)\\d{9}$");

	//银行卡号表达式
	private final static Pattern bankNo_pattern = Pattern.compile("^[0-9]{16,19}$");

	//座机号码表达式
	private final static Pattern plane_pattern = Pattern.compile("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$");

	//非零表达式
	private final static Pattern notZero_pattern = Pattern.compile("^\\+?[1-9][0-9]*$");

	//数字表达式
	private final static Pattern number_pattern = Pattern.compile("^[0-9]*$");

	//大写字母表达式
	private final static Pattern upChar_pattern = Pattern.compile("^[A-Z]+$");

	//小写字母表达式
	private final static Pattern lowChar_pattern = Pattern.compile("^[a-z]+$");

	//大小写字母表达式
	private final static Pattern letter_pattern = Pattern.compile("^[A-Za-z]+$");

	//中文汉字表达式
	private final static Pattern chinese_pattern = Pattern.compile("^[\u4e00-\u9fa5],{0,}$");

	//条形码表达式
	private final static Pattern onecode_pattern = Pattern.compile("^(([0-9])|([0-9])|([0-9]))\\d{10}$");

	//邮政编码表达式
	private final static Pattern postalcode_pattern = Pattern.compile("([0-9]{3})+.([0-9]{4})+");

	//IP地址表达式
	private final static Pattern ipaddress_pattern = Pattern.compile("[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))");

	//URL地址表达式
	private final static Pattern url_pattern = Pattern.compile("(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?");

	//用户名表达式
	private final static Pattern username_pattern = Pattern.compile("^[A-Za-z0-9_]{1}[A-Za-z0-9_.-]{3,31}");

	//真实姓名表达式
	private final static Pattern realnem_pattern = Pattern.compile("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*");

	//匹配HTML标签,通过下面的表达式可以匹配出HTML中的标签属性。
	private final static Pattern html_patter = Pattern.compile("<\\\\/?\\\\w+((\\\\s+\\\\w+(\\\\s*=\\\\s*(?:\".*?\"|'.*?'|[\\\\^'\">\\\\s]+))?)+\\\\s*|\\\\s*)\\\\/?>");

	//抽取注释,如果你需要移除HMTL中的注释，可以使用如下的表达式。
	private final static Pattern notes_patter = Pattern.compile("<!--(.*?)-->");

	//查找CSS属性,通过下面的表达式，可以搜索到相匹配的CSS属性。
	private final static Pattern css_patter = Pattern.compile("^\\\\s*[a-zA-Z\\\\-]+\\\\s*[:]{1}\\\\s[a-zA-Z0-9\\\\s.#]+[;]{1}");

	//提取页面超链接,提取html中的超链接。
	private final static Pattern hyperlink_patter = Pattern.compile("(<a\\\\s*(?!.*\\\\brel=)[^>]*)(href=\"https?:\\\\/\\\\/)((?!(?:(?:www\\\\.)?'.implode('|(?:www\\\\.)?', $follow_list).'))[^\"]+)\"((?!.*\\\\brel=)[^>]*)(?:[^>]*)>");

	//提取网页图片,假若你想提取网页中所有图片信息，可以利用下面的表达式。
	private final static Pattern image_patter = Pattern.compile("\\\\< *[img][^\\\\\\\\>]*[src] *= *[\\\\\"\\\\']{0,1}([^\\\\\"\\\\'\\\\ >]*)");

	//提取Color Hex Codes,有时需要抽取网页中的颜色代码，可以使用下面的表达式。
	private final static Pattern color_patter = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

	//文件路径及扩展名校验,验证windows下文件路径和扩展名（下面的例子中为.txt文件）
	private final static Pattern route_patter = Pattern.compile("^([a-zA-Z]\\\\:|\\\\\\\\)\\\\\\\\([^\\\\\\\\]+\\\\\\\\)*[^\\\\/:*?\"<>|]+\\\\.txt(l)?$");



	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str != null) {
			str = str.trim();
		}
		return str == null || str.equals("");
	}
	/**
	 * 判断是否为空
	 * @param editText
	 * @return
	 */
	public static boolean isEmpty(EditText editText) {
		return TextUtils.isEmpty(editText.getText().toString());
	}
	/**
	 * 判断是否全部有字母和数字组成
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isLimit(String name) {
		// ^.[a-zA-Z]\w{m,n}$ 由m-n位的字母数字或下划线组成
		Pattern p = Pattern.compile("[a-zA-Z0-9_]*");
		Matcher m = p.matcher(name);
		return m.matches();
	}
	/**
	 * 隐藏手机号码中间四位
	 * @param phone 13711112222
	 * @return 137****2222
	 */
	public static String getPhoneHide(String phone) {
		String phoneHide = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
		return phoneHide;
	}
	/**
	 * 判断是否为身份证号码
	 */
	public static boolean isIDCard(String idCard) {
		if (isIDCard1(idCard) || isIDCard2(idCard)) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 银行卡号，保留最后4位，其他星号替换
	 *
	 * @param cardId 卡号
	 * @return 星号替换的银行卡号
	 */
	public static String cardIdHide(String cardId) {
		return cardId.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1");
	}



	/**
	 * 判断车牌号
	 *
	 * @param carID
	 * @return
	 */
	public static boolean isCarId(String carID) {
		// ^.[a-zA-Z]\w{m,n}$ 由m-n位的字母数字或下划线组成
		Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
		Matcher m = p.matcher(carID);
		return m.matches();
	}

	/**
	 * 判断是否是15位身份证号码
	 *
	 * @return
	 */
	public static boolean isIDCard1(String idCard) {
		// ^.[a-zA-Z]\w{m,n}$ 由m-n位的字母数字或下划线组成
		Pattern p = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
		Matcher m = p.matcher(idCard);
		return m.matches();
	}

	/**
	 * 判断是否是18位身份证号码
	 *
	 * @return
	 */
	public static boolean isIDCard2(String idCard) {
		// ^.[a-zA-Z]\w{m,n}$ 由m-n位的字母数字或下划线组成
		Pattern p = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
		Matcher m = p.matcher(idCard);
		return m.matches();
	}



	/**
	 * 身份证号，中间10位星号替换
	 *
	 * @param id 身份证号
	 * @return 星号替换的身份证号
	 */
	public static String idHide(String id) {
		return id.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1** **** ****$2");
	}

	/**
	 * 判断是否是联系方式(手机号码+固话)
	 * @param mobiles
	 * @return
	 */
	public static boolean isPhoneNumber(String mobiles) {
		if (isMobileNumber(mobiles) || isTelNumber(mobiles)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNumber(String mobiles) {
		Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}


	/**
	 * 判断是否是固定电话号码
	 * @param telPhone
	 * @return
	 */
	public static boolean isTelNumber(String telPhone) {
		Pattern p = Pattern.compile("(\\d{3,4}-)\\d{6,8}");
		Matcher m = p.matcher(telPhone);
		return m.matches();
	}


	// /**
	// * 判断是否为手机号码且不为空
	// */
	// public static boolean isMobileNumberAndNoEmpty(Activity activity, String mobiles) {
	// if (isEmpty(mobiles)) {
	// T.s(activity, "请输入手机号");
	// return false;
	// } else if (!isMobileNumber(mobiles)) {
	// T.s(activity, "请输入正确的手机号码");
	// return false;
	// } else {
	// return true;
	// }
	//
	// }

	/**
	 * 将字符串转移为ASCII码
	 *
	 * @param str 字符串
	 * @return 字符串ASCII码
	 */
	public static String toASCII(String str) {
		StringBuffer strBuf = new StringBuffer();
		byte[] bGBK = str.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return strBuf.toString();
	}


	/**
	 * 将字符串转移为Unicode码
	 * @param str 字符串
	 * @return
	 */
	public static String toUnicode(String str) {
		StringBuffer strBuf = new StringBuffer();
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			strBuf.append("\\u").append(Integer.toHexString(chars[i]));
		}
		return strBuf.toString();
	}


	/**
	 * 将字符串转移为Unicode码
	 * @param chars 字符数组
	 * @return
	 */
	public static String toUnicodeString(char[] chars) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			strBuf.append("\\u").append(Integer.toHexString(chars[i]));
		}
		return strBuf.toString();
	}


	/**
	 * 格式化double成两位小数
	 * @param number  String或者double
	 * @return
	 */
	public static String formatDoubleTo2(Object number){
		try{
			java.text.DecimalFormat   df= new java.text.DecimalFormat("0.00");
			return df.format(number);
		}catch (Exception e){
			Log.e(TAG,"格式化出错：" + e.getMessage());
		}
		return "0.00";
	}


	/**
	 * 创建BigDecimal
	 * @param number
	 * @return
	 */
	public static BigDecimal createBigDecimal(String number){
		try {
			BigDecimal bigDecimal = new BigDecimal(number);
			return bigDecimal;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG,"BigDecimal格式化错误了：" + number);
		}
		return new BigDecimal("0.00");
	}

	public static BigDecimal createBigDecimal(double number){
		try {
			BigDecimal bigDecimal = new BigDecimal(number);
			return bigDecimal;
		} catch (Exception e) {
			Log.e(TAG,"BigDecimal格式化错误了：" + number);
			e.printStackTrace();
		}
		return new BigDecimal("0.00");
	}


	/**
	 * 设置文本
	 * @param editText
	 * @param object
	 */
	public static void setText(EditText editText, Object object){
		editText.setText(object==null?"":object.toString());
	}

	/**
	 * 获取不为空的文本信息
	 * @param object
	 * @param defaultStr
	 * @return
	 */
	public static String getNoNullText(Object object, String defaultStr){
		return object==null? defaultStr:object.toString();
	}

	/**
	 * 从米获取距离
	 * 
	 * @return
	 */
	public static String getdistance(int dis) {
		if (dis < 0)
			return "未知";

		String resu;
		// if (dis>=1000000) {
		// double Kdist = dis/1000;
		// // resu = (int)Kdist+ "k千米";
		// resu = (int)Kdist + "千米";
		// } else
		if (dis >= 1000) {
			double Kdist = dis / 1000.0;
			resu = String.format("%.2f", Kdist) + "千米";// 保留两位小数
		} else {
			resu = dis + "米";// 原始距离
		}

		return resu;
	}

	/**
	 * 判断email格式是否正确
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * String转换到int
	 * 
	 * @param str
	 * @return
	 */
	public static int String2int(String str, int defvalue) {
		int tem = defvalue;
		try {
			tem = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return tem;
	}


	/**
	 * 判断是否是邮编
	 */
	public static boolean isPostcode(String postcode) {

		String str = "^[1-9]\\d{5}(?!\\d)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(postcode);
		return m.matches();
	}

	/**
	 * 半角转换为全角 
	 * @param input    
	 * @return      
	*/
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/** 
	 * 去除特殊字符或将所有中文标号替换为英文标号
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}


	/**
	 * 是否含有特殊符号
	 *
	 * @param str 待验证的字符串
	 * @return 是否含有特殊符号
	 */
	public static boolean hasSpecialCharacter(String str) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 描述：是否只是字母和数字.
	 *
	 * @param str
	 *            指定的字符串
	 * @return 是否只是字母和数字:是为true，否则false
	 */
	public static Boolean isNumberLetter(String str) {
		Boolean isNoLetter = false;
		String expr = "^[A-Za-z0-9]+$";
		if (str.matches(expr)) {
			isNoLetter = true;
		}
		return isNoLetter;
	}


	/**
	 * 是否是纯字母
	 *
	 * @param str 待验证的字符串
	 * @return 是否是纯字母
	 */
	public static boolean isAlphaBetaString(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}

		Pattern p = Pattern.compile("^[a-zA-Z]+$");// 从开头到结尾必须全部为字母或者数字
		Matcher m = p.matcher(str);

		return m.find();
	}




	/**
	 * 描述：是否包含中文.
	 *
	 * @param str
	 *            指定的字符串
	 * @return 是否包含中文:是为true，否则false
	 */
	public static Boolean isContainChinese(String str) {
		Boolean isChinese = false;
		String chinese = "[\u0391-\uFFE5]";
		if (!isEmpty(str)) {
			// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
			for (int i = 0; i < str.length(); i++) {
				// 获取一个字符
				String temp = str.substring(i, i + 1);
				// 判断是否为中文字符
				if (temp.matches(chinese)) {
					isChinese = true;
				} else {

				}
			}
		}
		return isChinese;
	}


	/**
	 * 描述：ip地址转换为10进制数.
	 *
	 * @param ip
	 *            the ip
	 * @return the long
	 */
	public static long ip2int(String ip) {
		ip = ip.replace(".", ",");
		String[] items = ip.split(",");
		return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16
				| Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
	}


	/**
	 * 获取UUID
	 *
	 * @return 32UUID小写字符串
	 */
	public static String gainUUID() {
		String strUUID = UUID.randomUUID().toString();
		strUUID = strUUID.replaceAll("-", "").toLowerCase();
		return strUUID;
	}

	/**
	 * 返回TextView长度
	 * @param tv
	 * @return
	 */
	public static float measureTextLength(TextView tv) {
		Paint paint = new Paint();
		paint.setTextSize(tv.getTextSize());
		return paint.measureText(tv.getText() + "");
	}
	
	/**
	 * 是否是一个完整的url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isWebUrl(String url) {
		if (url == null)
			return false;
		// String pattern =
		// "^((http://)|(https://)|(ftp://)).*?.(html|php|jsp|asp|shtml|xhtml)";
		// return Pattern.matches(pattern, url);
		return Patterns.WEB_URL.matcher(url).matches();
	}

	/**
	 * 是否是一个完整的图片url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isImg(String url) {
		String pattern = "^((http://)|(https://)|(ftp://)).*?.(png|jpg|jpeg|gif|bmp)";

		return Pattern.matches(pattern, url);
	}




	//四舍五入保留两位小数
	public static double getDouble(double sourceData, String sf) {
        DecimalFormat df = new DecimalFormat(sf);
        String str = df.format(sourceData);  
        return Double.parseDouble(str);  
    }


    //是否银行卡
	public static boolean isBankCard(String mobiles) {
		Pattern p = Pattern.compile("^(\\d{16}|\\d{19})$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}


	//根据key,获取URL中相关的value
	public static String getUrlParamValue(String url, String paramName){
		if(url==null) return null;
		int index = url.indexOf(paramName + "=");
		String substring=null;
		if(index>=0){
			substring = url.substring(index);
			int i = substring.indexOf("&");
			if(i>=0){
				substring=substring.substring(0,i);
			}
			substring=substring.substring(substring.indexOf("=")+1);
		}
		return substring;
	}


	/**
	 * 隐藏mobile中的4位
	 * @param mobile
	 * @return
	 */
	public static String formatMobileHide4(String mobile){
		if(TextUtils.isEmpty(mobile)) return "";
		return mobile.substring(0,3)+"****"+mobile.substring(7,11);
	}
	/**
	 * 获得字体的缩放密度
	 *
	 * @param context
	 * @return
	 */
	public static float getScaledDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.scaledDensity;
	}

	/**
	 * *************************************************************
	 */

	public static boolean isEmpty(Collection collection) {
		return null == collection || collection.isEmpty();
	}

	public static boolean isEmpty(Map map) {
		return null == map || map.isEmpty();
	}

	public static boolean isEmpty(Object[] objs) {
		return null == objs || objs.length <= 0;
	}

	public static boolean isEmpty(int[] objs) {
		return null == objs || objs.length <= 0;
	}

	public static boolean isEmpty(CharSequence charSequence) {
		return null == charSequence || charSequence.length() <= 0;
	}

	public static boolean isBlank(CharSequence charSequence) {
		return null == charSequence || charSequence.toString().trim().length() <= 0;
	}


	/**
	 * 数组个数大于count
	 * @param objs
	 * @param count
	 * @return
	 */
	public static boolean isLeast(Object[] objs, int count) {
		return null != objs && objs.length >= count;
	}

	public static boolean isLeast(int[] objs, int count) {
		return null != objs && objs.length >= count;
	}


	/**
	 * 两个对象是否相等，已做非空判断
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEquals(Object a, Object b) {
		return (a == null) ? (b == null) : a.equals(b);
	}


	/**
	 * 去除空格，已做非空判断
	 * @param charSequence
	 * @return
	 */
	public static String trim(CharSequence charSequence) {
		return null == charSequence ? null : charSequence.toString().trim();
	}

	/**
	 * 摘取里面第一个不为null的字符串
	 *
	 * @param options
	 * @return
	 */
	public static String pickFirstNotNull(CharSequence... options) {
		if (isEmpty(options)) {
			return null;
		}
		String result = null;
		for (CharSequence cs : options) {
			if (null != cs) {
				result = cs.toString();
				break;
			}
		}
		return result;
	}

	/**
	 * 摘取里面第一个不为null的字符串
	 *
	 * @param options
	 * @return
	 */
	@SafeVarargs
	public static <T> T pickFirstNotNull(Class<T> clazz, T... options) {
		if (isEmpty(options)) {
			return null;
		}
		T result = null;
		for (T obj : options) {
			if (null != obj) {
				result = obj;
				break;
			}
		}
		return result;
	}



	/**
	 * 替换文本为图片
	 *
	 * @param charSequence
	 * @param regPattern
	 * @param drawable
	 * @return
	 */
	public static SpannableString replaceImageSpan(CharSequence charSequence, String regPattern, Drawable drawable) {
		SpannableString ss = charSequence instanceof SpannableString ? (SpannableString) charSequence : new SpannableString(charSequence);
		try {
			ImageSpan is = new ImageSpan(drawable);
			Pattern pattern = Pattern.compile(regPattern);
			Matcher matcher = pattern.matcher(ss);
			while (matcher.find()) {
				String key = matcher.group();
				ss.setSpan(is, matcher.start(), matcher.start() + key.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
		} catch (Exception ex) {
			Log.e(TAG,ex.getLocalizedMessage());
		}

		return ss;
	}


	/**
	 * 压缩字符串到Zip
	 *
	 * @param str
	 * @return 压缩后字符串
	 * @throws IOException
	 */
	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("ISO-8859-1");
	}

	/**
	 * 解压Zip字符串
	 *
	 * @param str
	 * @return 解压后字符串
	 * @throws IOException
	 */
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayInputStream in = new ByteArrayInputStream(str
				.getBytes("UTF-8"));
		return uncompress(in);
	}

	/**
	 * 解压Zip字符串
	 *
	 * @param inputStream
	 * @return 解压后字符串
	 * @throws IOException
	 */
	public static String uncompress(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream gunzip = new GZIPInputStream(inputStream);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		return out.toString();
	}

	/**
	 * InputStream convert to string
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1; ) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}




	private static int getShort(byte[] data) {
		return (int) ((data[0] << 8) | data[1] & 0xFF);
	}


	/**
	 * 判断是否中文
	 * @param str
	 * @return
     */
	public static boolean isChinese(String str){
		String reg = "[\\u4e00-\\u9fa5]+";//+表示一个或多个中文，
		boolean flag= false;
		if (str.matches(reg)){
			flag = true;
		}
		return flag;
	}

}