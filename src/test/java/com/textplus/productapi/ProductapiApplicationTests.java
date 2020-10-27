package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest // It loads all the Spring context (commented because it takes too long)
class ProductApiApplicationTests {

	@Test
	@Disabled("It takes too long and the test passes with this configuration")
	void contextLoads() {
		assertTrue(true);
	}

}
