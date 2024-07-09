sealed interface State {
    data object NotInit : State

    data object NewSession : State
}