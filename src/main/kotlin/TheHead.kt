class TheHead(
    private var head: Int,
    private val tape: TapeData,
    private val currentState: State,
    private val nextState: State,
) {

    fun read(): Char {
        return tape.data[head]
    }

    fun write(symbol: Char) {
        tape.modify(head, symbol)
    }

    fun next() {
        head++
    }

    fun previous() {
        head--
    }
}
