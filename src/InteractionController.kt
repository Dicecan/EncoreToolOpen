// Conceptual representation. Not directly compilable.

class InteractionController {

    private var core: LocatorCoreModule? = null

    fun initialize() {
        core = LocatorCoreModule.getInstance()
        
        setupInteractions()
        observe()
    }

    private fun setupInteractions() {
        ui.entryPointButton.setOnClickListener {
            val id = ui.idInput.text
            val margin = ui.marginInput.textAsInt
            core?.startProcess(id, margin)
        }

        ui.positiveButton.setOnClickListener {
            core?.submitFeedback(true)
        }

        ui.negativeButton.setOnClickListener {
            core?.submitFeedback(false)
        }
    }

    private fun observe() {
        core?.stream?.onUpdate { packet ->
            ui.mainOutput.text = packet.query

            val isActive = packet.active
            ui.idInput.isEnabled = !isActive
            ui.marginInput.isEnabled = !isActive
            ui.entryPointButton.text = if (isActive) "Reset" else "Start"
            ui.feedbackControls.isVisible = isActive
        }
    }
}
