package pkg;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class MainTest {

    @Test
    void simple() {
        assertDoesNotThrow(() -> Main.main("a", "b", "c"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    void parametrizedValueSource(String arg) {
        assertDoesNotThrow(() -> Main.main(arg));
    }

    @ParameterizedTest
    @MethodSource("args")
    void parametrizedMethodSource(String arg) {
        assertDoesNotThrow(() -> Main.main(arg));
    }

    static Stream<Arguments> args() {
        return Stream.of("a", "b", "c")
            .map(Arguments::of);
    }

}
