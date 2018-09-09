package com.stehno.difficulty

class CombatantStore(private val combatants: MutableList<Combatant> = mutableListOf()) {

    // FIXME: should this be stored in DB? - not really something needed beyond app use so prob not

    companion object {
        private val xpForCr = mapOf(
            Pair("0", 10), Pair("1/8", 25), Pair("1/4", 50), Pair("1/2", 100),
            Pair("1", 200), Pair("2", 450), Pair("3", 700), Pair("4", 1100),
            Pair("5", 1800), Pair("6", 2300), Pair("7", 2900), Pair("8", 3900),
            Pair("9", 5000), Pair("10", 5900), Pair("11", 7200), Pair("12", 8400),
            Pair("13", 10000), Pair("14", 11500), Pair("15", 11500), Pair("16", 15000),
            Pair("17", 18000), Pair("18", 20000), Pair("19", 22000), Pair("20", 25000),
            Pair("21", 33000), Pair("22", 41000), Pair("23", 50000), Pair("24", 62000),
            Pair("25", 75000), Pair("26", 90000), Pair("27", 105000), Pair("28", 120000),
            Pair("29", 135000), Pair("30", 155000)
        )

        private var multipliers = mapOf(
            Pair(1, 1.0), Pair(2, 1.5), Pair(3, 2.0), Pair(4, 2.0),
            Pair(5, 2.0), Pair(6, 2.0), Pair(7, 2.5), Pair(8, 2.5),
            Pair(9, 2.5), Pair(10, 2.5), Pair(11, 3.0), Pair(12, 3.0),
            Pair(13, 3.0), Pair(14, 3.0), Pair(15, 4.0)
        )
    }

    fun all() = combatants.toList()

    fun count() = combatants.size

    fun add(combatant: Combatant) {
        val index = combatants.indexOfFirst { c -> c.cr == combatant.cr }
        when {
            index >= 0 -> combatants[index] = Combatant(combatants[index].cr, combatants[index].count + combatant.count)
            else -> combatants.add(combatant)
        }
    }

    fun update(combatant: Combatant) {
        val index = combatants.indexOfFirst { c -> c.cr == combatant.cr }
        combatants[index] = combatant
    }

    fun remove(combatant: Combatant) {
        combatants.remove(combatant)
    }

    private fun calculateEffectiveXp(): Int {
        var totalXp = 0
        combatants.forEach { c ->
            totalXp += (xpForCr[c.cr]!! * c.count)
        }

        val multiplier = multipliers[combatants.sumBy { it.count }] ?: 4.0
        return (totalXp * multiplier).toInt()
    }

    fun difficulty(level: Int, xp: Int = calculateEffectiveXp()): Difficulty {
        val diffs = Difficulty.values().toMutableList()
        diffs.reverse()

        val dr = diffs.find { d -> d.xps[level - 1] < xp }
        return when (dr != null) {
            true -> dr!!
            else -> Difficulty.EASY
        }
    }

    operator fun get(index: Int) = combatants[index]
}

enum class Difficulty(val xps: List<Int>) {

    EASY(listOf(100, 200, 300, 500, 1000, 1200, 1400, 1800, 2200, 2400, 3200, 4000, 4400, 5000, 5600, 6400, 8000, 8400, 9600, 11200, 12000)),
    MEDIUM(listOf(200, 400, 600, 1000, 2000, 2400, 3000, 3600, 4400, 4800, 6400, 8000, 8800, 10000, 11200, 12800, 15600, 16800, 19600, 22800, 24000)),
    HARD(listOf(300, 636, 900, 1500, 3000, 3600, 4400, 5600, 6400, 7600, 9600, 12000, 13600, 15200, 17200, 19200, 23600, 25200, 29200, 34400, 36000)),
    DEADLY(listOf(400, 800, 1600, 2000, 4400, 5600, 6800, 8400, 9600, 11200, 14400, 18000, 20400, 22800, 25600, 28800, 35200, 38000, 43600, 50800, 54000))

}