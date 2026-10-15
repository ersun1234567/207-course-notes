import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Additional checks for shared node identity and file-loading edge cases. */
class NumberTriangleStructureTest {

  @TempDir Path directory;

  @Test
  void adjacentParentsShareTheSameChildObject() throws Exception {
    Path file = directory.resolve("shared.txt");
    Files.writeString(file, "1\n2 3\n4 5 6\n");
    NumberTriangle triangle = NumberTriangle.loadTriangle(file.toString());

    // Equal retrieved values alone cannot distinguish shared nodes from copies.
    Field left = NumberTriangle.class.getDeclaredField("left");
    Field right = NumberTriangle.class.getDeclaredField("right");
    left.setAccessible(true);
    right.setAccessible(true);
    Object leftParent = left.get(triangle);
    Object rightParent = right.get(triangle);
    assertSame(right.get(leftParent), left.get(rightParent));
    assertTrue(((NumberTriangle) right.get(leftParent)).isLeaf());
  }

  @Test
  void loadsSingleNodeWithNegativeValue() throws IOException {
    Path file = directory.resolve("single.txt");
    Files.writeString(file, "-12");
    NumberTriangle triangle = NumberTriangle.loadTriangle(file.toString());
    assertEquals(-12, triangle.retrieve(""));
    assertTrue(triangle.isLeaf());
  }

  @Test
  void loadsRowsWithSurroundingAndRepeatedWhitespace() throws IOException {
    Path file = directory.resolve("spaces.txt");
    Files.writeString(file, " 1 \n  -2   3  \n");
    NumberTriangle triangle = NumberTriangle.loadTriangle(file.toString());
    assertEquals(-2, triangle.retrieve("l"));
    assertEquals(3, triangle.retrieve("r"));
  }

  @Test
  void missingFilePropagatesAnIoException() {
    assertThrows(
        IOException.class,
        () -> NumberTriangle.loadTriangle(directory.resolve("missing.txt").toString()));
  }
}
