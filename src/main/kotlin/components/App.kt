package components

import AlgoSettings
import Benchmark
import PUZZLES
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import react.router.dom.Link


val App = FC<Props> {

    val defaultSettings = AlgoSettings(
        chromosomeSize = 180,
        populationSize = 80,
        mutationProbability = 0.02,
        elitismPercent = 0.2,
        puzzle = PUZZLES.first(),
        100.0,
        30.0,
        50.0,
        10.0,
        110.0,
        0.9
    )



    BrowserRouter {
        div {
            ReactHTML.nav {
                Link {
                    +"Visualizer"
                    to = "/visualizer"
                }
                Link {
                    +"Benchmark"
                    to = "/benchmark"
                }
            }
        }
        Routes {
            Route {
                index = true
                path = "/visualizer"
                element =  Visualizer.create {
                    this.puzzles = PUZZLES
                    this.algoSettings = defaultSettings
                }

            }
            Route {
                path = "/benchmark"
                element = Benchmark.create()
            }

        }
    }

}