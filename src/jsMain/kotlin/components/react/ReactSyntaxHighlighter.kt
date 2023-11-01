@file:JsModule("react-syntax-highlighter")
@file:JsNonModule
package components.react

import react.*

@JsName("default")
external val ReactSyntaxHighlighter: ComponentClass<ReactSyntaxHighlighterProps>

external interface ReactSyntaxHighlighterProps: PropsWithChildren {
    var language: String
    var style: dynamic
}


