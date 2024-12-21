package syh

abstract class AbstractAocDay(val year: Int, val day: Int) {

    fun readSingleLineFile(file: String): List<String> {
        return {}.javaClass.classLoader.getResource("year$year/day$day/$file.txt")!!.readText().split("\r\n")
    }

    fun readDoubleLineFile(file: String): List<String> {
        return {}.javaClass.classLoader.getResource("year$year/day$day/$file.txt")!!.readText().split("\r\n\r\n")
    }

    fun getFileNameToWrite(file: String, s: String): String {
        return "src\\test\\resources\\year${year}\\day${day}\\${file}_$s"
    }

    abstract fun doA(file: String): String
    abstract fun doB(file: String): String

}