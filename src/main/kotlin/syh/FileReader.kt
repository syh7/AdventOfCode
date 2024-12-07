package syh

fun readSingleLineFile(fileName: String): List<String> {
    return {}.javaClass.classLoader.getResource(fileName)!!.readText().split("\r\n")
}

fun readDoubleLineFile(fileName: String): List<String> {
    return {}.javaClass.classLoader.getResource(fileName)!!.readText().split("\r\n\r\n")
}
