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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

/**
 * @author Michael J. Simons
 */
class AesTest {

	private static Neo4j neo4j;

	@BeforeAll
	static void startNeo4j() {

		neo4j = Neo4jBuilders
			.newInProcessBuilder()
			.withDisabledServer() // We don't need the HTTP endpoint
			.withFunction(AesEncrypt.class)
			.withFunction(AesDecrypt.class)
			.build();
	}

	@Test
	void encryptShouldWork() {
		try (var tx = neo4j.defaultDatabaseService().beginTx()) {
			var params = Map.<String, Object>of(
				"value", "Hallo, Neo4j.",
				"passphrase", "your super secret passphrase"
			);

			var result = tx.execute("RETURN aes.encrypt($value, $passphrase) as v", params);
			assertTrue(result.hasNext());
			var record = result.next();
			assertEquals("5E02F9B1B5432DF75126EFFA2DBAB748", record.get("v"));
		}
	}

	@Test
	void decryptShouldWork() {

		try (var tx = neo4j.defaultDatabaseService().beginTx()) {
			var params = Map.<String, Object>of(
				"value", "9D3C5CB0A243E0BF09241A43118CF0DF4AFA810CE7FCF1718CA92E21FFEB1790",
				"passphrase", "your super secret passphrase"
			);

			var result = tx.execute("RETURN aes.decrypt($value, $passphrase) as v", params);
			assertTrue(result.hasNext());
			var record = result.next();
			assertEquals("Hej från Sverige", record.get("v"));
		}
	}

	@AfterAll
	static void stopNeo4j() {

		neo4j.close();
	}
}
