package com.dg.app.api.remote;


import org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * 编码与解码的类，使用RSA公钥加密
 * **/
public class EncodeRSA {
	private static String publicKeyString="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKQ0PEj950V7LTbmjLO7puKIyO"+
			"0+VCTcv7WJW32dtssJU98gUUe+jsWkw8XzUcOTue6Orc2W20MjSoDzCxyvRg+BR8"+
			"TcZmncQJk9KZeFjvWnoKh8OmuiFJPkWRtg0DKkU7Gew7GNvkdiEZqj/+Sq71Hq3v"+
			"GoYEyo1nhOputCcL+QIDAQAB";
//	public static void main(String[] args) {
//		 String publicKeyString="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKQ0PEj950V7LTbmjLO7puKIyO"+
//				 "0+VCTcv7WJW32dtssJU98gUUe+jsWkw8XzUcOTue6Orc2W20MjSoDzCxyvRg+BR8"+
//				 "TcZmncQJk9KZeFjvWnoKh8OmuiFJPkWRtg0DKkU7Gew7GNvkdiEZqj/+Sq71Hq3v"+
//				 "GoYEyo1nhOputCcL+QIDAQAB";
//				 	   JsonObject json=new JsonObject();
//
//				 	   json.addProperty("phone", "010");
//				 	   json.addProperty("password", "123456");
//				 	   json.addProperty("time", System.currentTimeMillis()/1000);
//				 	   String encryptJson=json.toString();
//
//				 	   try {
//				 		encryptJson=encryptByPublicKey(encryptJson, getPublicKey(publicKeyString));
//				 	} catch (NoSuchAlgorithmException e) {
//				 		// TODO Auto-generated catch block
//				 		e.printStackTrace();
//				 	} catch (InvalidKeySpecException e) {
//				 		// TODO Auto-generated catch block
//				 		e.printStackTrace();
//				 	}
//				 	   System.out.println(encryptJson);
//
//				 	org.apache.http.client.fluent.Form form=org.apache.http.client.fluent.Form.form()
//				 			.add("device", "android")
//				 			.add("s",encryptJson);
//				 	Request request=Request.Post("http://120.25.125.233/Api/user/login")
//				 			         .bodyForm(form.build());
//
//				 	org.apache.http.client.fluent.Executor executor=org.apache.http.client.fluent.Executor.newInstance();
//				 	String response;
//				 	try {
//				 		response = executor.execute(request).returnContent().asString();
////				 		response=decodeUnicode(response);
//				 		System.out.println(response+"dfdfd");
//				 	} catch (ClientProtocolException e) {
//				 		// TODO Auto-generated catch block
//				 		e.printStackTrace();
//				 	} catch (IOException e) {
//				 		// TODO Auto-generated catch block
//				 		e.printStackTrace();
//				 	}
//
//
//	}

	public static  String getEncodeRsaStr(String encryptJson)
	{
		String encrypt=encryptByPublicKey(encryptJson, getPublicKey(publicKeyString));
		return  encrypt;

	}
	 public static RSAPublicKey getPublicKey(String publicKeyStr)
	   {
		   byte[] buffer= Base64.decodeBase64(publicKeyStr);
		   KeyFactory keyFactory= null;
		   RSAPublicKey rsaPublicKey=null;
		   try {
			   keyFactory = KeyFactory.getInstance("RSA");
			   X509EncodedKeySpec keySpec=new X509EncodedKeySpec(buffer);
			   rsaPublicKey=(RSAPublicKey)keyFactory.generatePublic(keySpec);
		   } catch (NoSuchAlgorithmException e) {
			   e.printStackTrace();
		   } catch (InvalidKeySpecException e) {
			   e.printStackTrace();
		   }

		   return rsaPublicKey;
	   }
	   public static String encryptByPublicKey(String data,RSAPublicKey publicKey)
	   {
		   byte[] results=null;
		   try {
			   Cipher cipher=Cipher.getInstance("RSA");
			   cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			   results=cipher.doFinal(data.getBytes());
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return Base64.encodeBase64URLSafeString(results);
		   
	   }
	   
	   public static String decodeUnicode(String theString) {    
		   
		          char aChar;    
		     
		         int len = theString.length();    
		     
		         StringBuffer outBuffer = new StringBuffer(len);    
		     
		          for (int x = 0; x < len;) {    
		     
		           aChar = theString.charAt(x++);    
		     
		           if (aChar == '\\') {    
		     
		            aChar = theString.charAt(x++);    
		     
		            if (aChar == 'u') {    
		     
		          // Read the xxxx    
		     
		            int value = 0;    
		     
		             for (int i = 0; i < 4; i++) {    
		     
		              aChar = theString.charAt(x++);    
		     
		             switch (aChar) {    
		     
		              case '0':    
		     
		              case '1':    
		     
		              case '2':    
		     
		              case '3':    
		     
		              case '4':    
		     
		              case '5':    
		     
		             case '6':    
		              case '7':    
		              case '8':    
		              case '9':    
		               value = (value << 4) + aChar - '0';    
		               break;    
		              case 'a':    
		              case 'b':    
		              case 'c':    
		              case 'd':    
		              case 'e':    
		              case 'f':    
		               value = (value << 4) + 10 + aChar - 'a';    
		              break;    
		              case 'A':    
		              case 'B':    
		              case 'C':    
		              case 'D':    
		              case 'E':    
		              case 'F':    
		               value = (value << 4) + 10 + aChar - 'A';    
		               break;    
		              default:    
		               throw new IllegalArgumentException(    
		                 "Malformed   \\uxxxx   encoding.");    
		              }    
		     
		            }    
		             outBuffer.append((char) value);    
		            } else {    
		             if (aChar == 't')    
		              aChar = '\t';    
		             else if (aChar == 'r')    
		              aChar = '\r';    
		     
		             else if (aChar == 'n')    
		     
		              aChar = '\n';    
		     
		             else if (aChar == 'f')    
		     
		              aChar = '\f';    
		     
		             outBuffer.append(aChar);    
		     
		            }    
		     
		           } else   
		     
		           outBuffer.append(aChar);    
		     
		          }    
		     
		          return outBuffer.toString();    
		     
		         }    
	}
