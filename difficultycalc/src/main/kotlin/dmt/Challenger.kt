package dmt

data class Challenger(val challenge: String, val count: Int) {

    override fun toString(): String {
        return "CR-$challenge x $count"
    }
}