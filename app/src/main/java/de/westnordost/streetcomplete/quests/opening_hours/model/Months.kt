package de.westnordost.streetcomplete.quests.opening_hours.model

import java.text.DateFormatSymbols

class Months {

    private val data = BooleanArray(MONTHS_COUNT)

    val selection: BooleanArray get() = data.copyOf()

    constructor()

    constructor(selection: BooleanArray) {
        var i = 0
        while (i < selection.size && i < data.size) {
            data[i] = selection[i]
            ++i
        }
    }

    fun isSelectionEmpty() = data.all { !it }

    fun toLocalizedString() = toStringUsing(getNames(), ", ", "–")

    fun toStringUsing(names: Array<String>, separator: String, range: String): String {
        return toCircularSections().joinToString(separator) { section ->
            if (section.start == section.end) {
                names[section.start]
            } else {
                names[section.start] + range + names[section.end]
            }
        }
    }

    fun toCircularSections(): List<CircularSection> {
        val result = mutableListOf<CircularSection>()
        var currentStart: Int? = null
        for (i in 0 until MONTHS_COUNT) {
            if (currentStart == null) {
                if (data[i]) currentStart = i
            } else {
                if (!data[i]) {
                    result.add(CircularSection(currentStart, i - 1))
                    currentStart = null
                }
            }
        }
        if (currentStart != null) {
            result.add(CircularSection(currentStart, MONTHS_COUNT-1))
        }

        return MONTHS_NUMBER_SYSTEM.merged(result)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Months) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode() = data.contentHashCode()

    companion object {

        const val MONTHS_COUNT = 12
        val MONTHS_NUMBER_SYSTEM = NumberSystem(0, MONTHS_COUNT-1)

        fun getNames(): Array<String> {
            val symbols = DateFormatSymbols.getInstance()
            val result = symbols.months.copyOf(MONTHS_COUNT)
            return result.requireNoNulls()
        }
    }
}