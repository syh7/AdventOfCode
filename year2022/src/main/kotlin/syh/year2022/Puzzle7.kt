package syh.year2022

import syh.library.AbstractAocDay


class Puzzle7 : AbstractAocDay(2022, 7) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val root = readDirectory(lines)

        val smallDirectories = findDirectoriesSmallerThanSize(root, 100000L)
        println("Found ${smallDirectories.size} small directories:")
        smallDirectories.forEach { println("${it.name} (${it.getSize()})") }
        val totalSize = smallDirectories.sumOf { it.getSize() }
        println("Total size: $totalSize")
        return totalSize.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val root = readDirectory(lines)

        val maxSize = 70000000L
        val neededSize = 30000000L
        val currentSize = root.getSize()
        val unusedSize = maxSize - currentSize
        val sizeToRemove = neededSize - unusedSize

        println("maxSize: $maxSize")
        println("neededSize: $neededSize")
        println("currentSize: $currentSize")
        println("unusedSize: $unusedSize")
        println("sizeToRemove: $sizeToRemove")

        val applicableDirectories = findDirectoriesBiggerThanSize(root, sizeToRemove)

        applicableDirectories.forEach { println("${it.name} has size ${it.getSize()}") }

        val smallestDirectory = applicableDirectories.minBy { it.getSize() }
        println("smallest directory is ${smallestDirectory.name} with size ${smallestDirectory.getSize()}")
        return smallestDirectory.getSize().toString()
    }

    private fun findDirectoriesBiggerThanSize(root: Directory, maxSize: Long): List<Directory> {
        val dirList = mutableListOf<Directory>()
        for (child in root.children) {
            dirList.addAll(findDirectoriesBiggerThanSize(child, maxSize))
        }
        if (root.getSize() >= maxSize) {
            dirList.add(root)
        }
        return dirList
    }

    private fun findDirectoriesSmallerThanSize(root: Directory, maxSize: Long): List<Directory> {
        val dirList = mutableListOf<Directory>()
        for (child in root.children) {
            dirList.addAll(findDirectoriesSmallerThanSize(child, maxSize))
        }
        if (root.getSize() <= maxSize) {
            dirList.add(root)
        }
        return dirList
    }

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

    private fun readDirectory(lines: List<String>): Directory {
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
}