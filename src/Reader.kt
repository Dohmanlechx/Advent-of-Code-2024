object Reader {
    fun example(day: Int, split: String = "\n") =
        lines("/${day.padded()}/example", split)

    fun input(day: Int, split: String = "\n") =
        lines("/${day.padded()}/input", split)

    private fun lines(path: String, split: String) =
        this@Reader::class.java
            .getResource(path)!!
            .readText()
            .split(split)
            .map(String::trim)
            .superTrim()

    private fun Int.padded() =
        toString().padStart(2, '0')

    private fun List<String>.superTrim(): List<String> {
        val trimmedList = mutableListOf<String>()
        val dirt = "  "

        for (line in this) {
            var trimmedLine = line
            while (trimmedLine.contains(dirt)) {
                trimmedLine = trimmedLine.replace(dirt, " ")
            }
            trimmedList.add(trimmedLine)
        }

        return trimmedList
    }
}