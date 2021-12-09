package client.logger;

import client.config.LoggerConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggerTest {
    static PrintStream old = System.out;
    static ByteArrayOutputStream baos;

    @BeforeAll
    static void initialized() {
        Logger.setConfig(LoggerConfig.getDefaultConfig());
        baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
    }

    @AfterAll
    static void deinitialized() {
        System.out.flush();
        System.setOut(old);
    }

    @Test
    void testOutputLogger() {
        String message = "Bonjour";

        String expected = "\u001B[36mGAME: Bonjour\u001B[00m\n";
        Logger.setLevel(LogLevel.DEBUG);
        Logger.debug(LoggerCategory.GAME, message);
        assertEquals(expected.trim(), baos.toString().trim());

        baos.reset();

        expected = "\u001B[36mNETWORK: Bonjour\u001B[00m\n";
        Logger.debug(LoggerCategory.NETWORK, message);
        assertEquals(expected.trim(), baos.toString().trim());

        baos.reset();

        expected = "\u001B[36mSERVICE: Bonjour\u001B[00m\n";
        Logger.debug(LoggerCategory.SERVICE, message);
        assertEquals(expected.trim(), baos.toString().trim());

        baos.reset();
        Logger.setLevel(LogLevel.INFO);

        expected = "";
        Logger.debug(LoggerCategory.SERVICE, message);
        assertEquals(expected.trim(), baos.toString().trim());

        baos.reset();

        expected = "[33mSERVICE: Bonjour\u001B[00m";
        Logger.warn(LoggerCategory.SERVICE, message);
        assertEquals(expected.trim(), baos.toString().trim());

        baos.reset();

        expected = "[31mNETWORK: Bonjour\u001B[00m";
        Logger.error(LoggerCategory.NETWORK, message);
        assertEquals(expected.trim(), baos.toString().trim());
    }
}
