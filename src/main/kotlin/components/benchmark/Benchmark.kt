package components.benchmark

import react.FC
import react.Props
import react.dom.html.ReactHTML.h1

val Benchmark = FC<Props> {

    h1 {
        +"ui.benchmark.getBenchmark"
    }
}