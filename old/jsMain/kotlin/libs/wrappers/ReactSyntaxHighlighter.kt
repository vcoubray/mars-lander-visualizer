@file:JsModule("react-syntax-highlighter")
@file:JsNonModule
package libs.wrappers

import react.*

@JsName("default")
external val ReactSyntaxHighlighter: ComponentClass<ReactSyntaxHighlighterProps>

external interface ReactSyntaxHighlighterProps: PropsWithChildren {
    var language: String
    var style: dynamic
}


