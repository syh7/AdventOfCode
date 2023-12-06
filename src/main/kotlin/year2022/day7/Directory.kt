package year2022.day7

data class File(val name: String, val size: Long)
data class Directory(
    val name: String,
    val files: MutableList<File> = mutableListOf(),
    val children: MutableList<Directory> = mutableListOf(),
    val parent: Directory? = null
) {
    override fun toString(): String {
        println("Directory: $name (size=${getSize()})")
        println("files: ")
        files.forEach { println(it) }
        println("Children: ")
        children.forEach { println(it.toString()) }
        return ""
    }

    fun getSize(): Long {
        val filesSize = files.sumOf { it.size }
        val childrenSize = children.sumOf { it.getSize() }
        return filesSize + childrenSize
    }
}

fun readDirectory(lines: List<String>): Directory {
    val root = Directory("/")
    var currentDirectory = root
    for (line in lines.drop(1)) {
        if (line.startsWith("$ cd")) {
            val directoryName = line.split(" ")[2]
            if (directoryName == "..") {
                currentDirectory = currentDirectory.parent!!
            } else {
                val newDirectory = Directory(directoryName, parent = currentDirectory)
                currentDirectory.children.add(newDirectory)
                currentDirectory = newDirectory
            }


        } else if (line.startsWith("$ ls")) {
            // skip
            continue

        } else if (line.startsWith("dir")) {
            // skip
            continue

        } else {
            val (size, name) = line.split(" ")
            val file = File(name, size.toLong())
            currentDirectory.files.add(file)
        }
    }
    return root
}