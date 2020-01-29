
def setJavaHome(String jenkinsJavaName){
    env.JAVA_HOME="${tool "" + jenkinsJavaName + ""}"
    env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

}