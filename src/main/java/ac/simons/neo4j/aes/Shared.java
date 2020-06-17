/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ac.simons.neo4j.aes;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.spec.SecretKeySpec;

/**
 * @author Michael J. Simons
 */
final class Shared {
	static final String AES = "AES";
	static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

	static SecretKeySpec generateMySQLAESKey(final String key) {
		final byte[] finalKey = new byte[16];
		int i = 0;
		for (byte b : key.getBytes(DEFAULT_CHARSET))
			finalKey[i++ % 16] ^= b;
		return new SecretKeySpec(finalKey, "AES");
	}

	static String toHexString(byte[] bytes) {
		var sb = new StringBuilder(2 * bytes.length);
		for (var b : bytes) {
			sb.append(HEX_DIGITS[(b >> 4) & 0xf]).append(HEX_DIGITS[b & 0xf]);
		}
		return sb.toString();
	}

	public static void main(String ...a) {
		System.out.println(fromHexString("9D3C5CB0A243E0BF09241A43118CF0DF4AFA810CE7FCF1718CA92E21FFEB1790"));
	}

	static byte[] fromHexString(String string) {
		var len = string.length();
		var data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4)
				+ Character.digit(string.charAt(i + 1), 16));
		}
		return data;
	}

	private Shared() {
	}
}
