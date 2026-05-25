package fr.vco.genetic.algorithm.visualizer

class WasmJsPlatform : Platform {
    override val name: String = "Web with JS/Wasm"
}

actual fun getPlatform(): Platform = WasmJsPlatform()