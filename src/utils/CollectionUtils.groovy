package utils

final class CollectionUtils {
    private CollectionUtils() {

    }

    static boolean isEmpty(Collection<?> c) {
        println("AAA " + (c == null))
        println("BBB " + (c == null || c.isEmpty()))
        return c == null || c.isEmpty()
    }

    static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c)
    }
}
