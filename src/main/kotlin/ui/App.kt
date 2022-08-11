package ui

import Benchmark
import config.PUZZLES
import config.defaultSettings
import react.FC
import react.Props
import react.create
import react.router.Navigate
import react.router.Outlet
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import ui.common.Menu
import ui.visualizer.Visualizer

val App = FC<Props> {

    BrowserRouter {
        Menu ()
        Routes {
            Route {
                index = true
                path = "/"
                element = Navigate.create {
                    to = "/visualizer"
                }
            }
            Route {
                index = true
                path = "/visualizer"
                element = Visualizer.create {
                    this.puzzles = PUZZLES
                    this.algoSettings = defaultSettings
                }
            }
            Route {
                path = "/benchmark"
                element = Benchmark.create()
            }
        }
        Outlet()
    }
}