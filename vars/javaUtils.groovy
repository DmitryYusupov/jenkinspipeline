import utils.os.Os
import utils.os.OsUtils

def setJavaHome(String jenkinsJavaName) {
    def os = OsUtils.getOS()

    env.JAVA_HOME = "${tool "" + jenkinsJavaName + ""}"
    if (Os.WINDOWS.equals(os)) {
        env.PATH="%${env.PATH}%;${env.JAVA_HOME}/bin"
    } else {
        //linux
        env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }
}
