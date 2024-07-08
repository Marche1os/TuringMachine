class TapeData(
    val data: MutableList<Char>,
) {

    fun modify(position: Int, newSymbol: Char) {
        data[position] = newSymbol
    }
}
