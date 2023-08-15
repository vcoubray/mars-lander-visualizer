package components.form


import LimitType
import components.form.models.FormField
import components.form.models.SettingsFormProps
import react.FC
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.summary
import web.html.InputType


val GlobalSettingsForm = FC<SettingsFormProps> { props ->


    details {
        summary {
            +"Genetic settings"
        }
        label {
            +"Limit type"
            select {
                LimitType.entries.forEach {
                    option {
                        label = it.label
                        value = it
                    }
                }
                onChange = { event ->
                    props.settingsValues["limitType"] = event.target.value
                }
            }
        }

        FormInputList{
            fields = listOf(
                FormField("limitValue", "Limit value", InputType.number),
                FormField("populationSize", "Population Size", InputType.number),
                FormField("chromosomeSize", "Chromosome Size", InputType.number),
                FormField("mutationProbability", "Mutation probability (0 to 1)", InputType.number),
                FormField("elitismPercent", "Elitism (0 to 1)", InputType.number),
            )
            values = props.settingsValues
        }
    }
}