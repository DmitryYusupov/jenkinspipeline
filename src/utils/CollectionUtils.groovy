package utils

final class CollectionUtils {
    private CollectionUtils() {

    }

    static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty()
    }

    static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c)
    }
}
