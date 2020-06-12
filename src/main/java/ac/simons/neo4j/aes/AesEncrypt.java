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

import javax.crypto.Cipher;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

/**
 * @author Michael J. Simons
 */
public final class AesEncrypt {

	@UserFunction("aes.encrypt")
	@Description("Creates an AES encrypted hex string.")
	public String apply(@Name("value") String value, @Name("passphrase") String passphrase) throws Exception {

		var cipher = Cipher.getInstance(Shared.AES);
		cipher.init(Cipher.ENCRYPT_MODE, Shared.generateMySQLAESKey(passphrase));
		return Shared.toHexString(cipher.doFinal(value.getBytes(Shared.DEFAULT_CHARSET)));
	}
}
