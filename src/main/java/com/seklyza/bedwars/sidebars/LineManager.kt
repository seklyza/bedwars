package com.seklyza.bedwars.sidebars

class LineManager private constructor() {
    private val lines = mutableListOf<String>()

    companion object {
        fun builder(): LineManager = LineManager()
    }

    fun add(line: String): LineManager {
        lines.add(line)

        return this
    }

    fun add(lineManager: LineManager) : LineManager {
        for (line in lineManager.lines) {
            add(line)
        }

        return this
    }

    fun newLine(): LineManager {
        add("{{NEW_LINE}}")

        return this
    }

    fun build(): MutableMap<Int, String> {
        val map = mutableMapOf<Int, String>()
        for ((i, line) in lines.withIndex()) {
            var l = line
            if (l == "{{NEW_LINE}}") {
                l = ""
                for (j in 0..i) {
                    l += " "
                }
            }

            map[lines.size - i - 1] = l
        }

        return map
    }
}
