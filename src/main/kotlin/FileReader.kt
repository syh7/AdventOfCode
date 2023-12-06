fun readFile(fileName: String): List<String> {
    return {}.javaClass.classLoader.getResource(fileName)!!.readText().split("\r\n")
}
