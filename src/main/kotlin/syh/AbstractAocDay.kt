package syh

abstract class AbstractAocDay(val year: Int, val day: Int) {

    fun readSingleLineFile(file: String): List<String> {
        return {}.javaClass.classLoader.getResource("year$year/day$day/$file.txt")!!.readText().split("\r\n")
    }

    fun readDoubleLineFile(file: String): List<String> {
        return {}.javaClass.classLoader.getResource("year$year/day$day/$file.txt")!!.readText().split("\r\n\r\n")
    }

    abstract fun doA(file: String): String
    abstract fun doB(file: String): String

}