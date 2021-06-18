package io.github.hotstu.jsbridge;

import org.junit.Test;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws IOException {
        assertEquals(4, 2 + 2);
        Path path = Paths.get("test.txt");
        System.out.println(path.toAbsolutePath().toString());
        FileChannel open = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
        MappedByteBuffer map = open.map(FileChannel.MapMode.READ_WRITE, 0, 2048);
        map.clear();
        map.asCharBuffer().put("hello,world");
        throw new RuntimeException();
    }
}