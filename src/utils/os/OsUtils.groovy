package utils.os

final class OsUtils {

    private OsUtils() {
    }

    static Os getOS() {
        def os = getOsAsString()

        if (isWindows(os)) {
            return Os.WINDOWS
        } else if (isMac(os)) {
            return Os.OSX
        } else if (isUnix(os)) {
            return Os.UNIX
        } else if (isSolaris(os)) {
            return Os.SOLARIS
        } else {
            throw new RuntimeException("Unknown OS '" + getOsAsString() + "'")
        }
    }

    private static boolean isWindows(String osAsStr) {
        return osAsStr.contains("win")
    }

    private static boolean isMac(String osAsStr) {
        return osAsStr.contains("mac")
    }

    private static boolean isUnix(String osAsStr) {
        return (osAsStr.contains("nix") || osAsStr.contains("nux") || osAsStr.contains("aix"))
    }

    private static boolean isSolaris(String osAsStr) {
        return osAsStr.contains("sunos")
    }

    private static String getOsAsString() {
        return System.getProperty("os.name").toLowerCase()
    }
}
