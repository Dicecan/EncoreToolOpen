// Conceptual representation. Not directly compilable.

class LocatorCoreModule private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: LocatorCoreModule? = null

        fun getInstance(): LocatorCoreModule {
            return INSTANCE ?: synchronized(this) {
                val instance = LocatorCoreModule()
                INSTANCE = instance
                instance
            }
        }
    }

    object Stages {
        const val IDLE = 0
        const val INIT = 1
        const val PIVOT_A = 2
        const val PIVOT_B = 3
        const val SEARCH_A = 4
        const val SEARCH_B = 5
        const val DONE = 6
    }

    private val _stream = // A reactive stream
    val stream = _stream.asReadOnly()

    fun startProcess(id: String, margin: Int) {
        if (id.isBlank()) return
        val initialState = StatePacket(
            target = id,
            stage = Stages.INIT,
            tolerance = margin,
            active = true
        )
        _update(initialState)
        _composeNext(initialState)
    }

    fun resetProcess() {
        _update(StatePacket(stage = Stages.IDLE))
    }

    fun submitFeedback(isPositive: Boolean) {
        val current = _stream.current
        if (!current.active) return

        when (current.stage) {
            Stages.INIT -> _onInit(isPositive)
            Stages.PIVOT_A -> _onPivotA(isPositive)
            Stages.PIVOT_B -> _onPivotB(isPositive)
            else -> _onSearch(isPositive)
        }
    }

    private fun _onInit(isPositive: Boolean) {
        if (isPositive) {
            _update(stage = Stages.PIVOT_A)
            _composeNext(_stream.current)
        } else {
            _terminate("Init check failed")
        }
    }

    private fun _onPivotA(isPositive: Boolean) {
        val range = if (isPositive) 0L..300000L else -300000L..-1L
        _update(stage = Stages.PIVOT_B, temp = range)
        _composeNext(_stream.current)
    }

    private fun _onPivotB(isPositive: Boolean) {
        val boundsA = _stream.current.temp as LongRange
        val boundsB = if (isPositive) 0L..300000L else -300000L..-1L
        _update(
            stage = Stages.SEARCH_A,
            axis = "A",
            bound1 = boundsA.first,
            bound2 = boundsA.last,
            temp = boundsB
        )
        _composeNext(_stream.current)
    }

    private fun _onSearch(isPositive: Boolean) {
        val state = _stream.current
        val mid = state.bound1 + (state.bound2 - state.bound1) / 2
        val newBounds = if (isPositive) state.bound1..mid else (mid + 1)..state.bound2
        _update(bound1 = newBounds.first, bound2 = newBounds.last)
        _check()
    }

    private fun _check() {
        val state = _stream.current
        if (state.bound1 == state.bound2) {
            if (state.axis == "A") {
                val finalA = state.bound1
                val boundsB = state.temp as LongRange
                _update(
                    finalA = finalA,
                    stage = Stages.SEARCH_B,
                    axis = "B",
                    bound1 = boundsB.first,
                    bound2 = boundsB.last
                )
                _composeNext(_stream.current)
            } else {
                val finalB = state.bound1
                _terminate("Process complete: (${state.finalA}, $finalB)", isFinal = true)
            }
        } else {
            _composeNext(state)
        }
    }

    private fun _composeNext(state: StatePacket) {
        val query = _buildQuery(state)
        _update(query = query)
    }

    private fun _buildQuery(state: StatePacket): String {
        // Query string generation logic
        return "..."
    }

    private fun _terminate(msg: String, isFinal: Boolean = false) {
        val finalStage = if (isFinal) Stages.DONE else Stages.IDLE
        _update(
            active = false,
            stage = finalStage,
            query = if (isFinal) msg else ""
        )
    }

    private fun _update( /*...*/ ) {
        // State update logic
    }
}